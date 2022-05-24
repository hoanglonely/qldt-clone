package com.mb.lab.banks.utils.storage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.CollectionUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.event.DeliveryMode;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.Transfer.TransferState;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.internal.MultipleFileTransfer;
import com.amazonaws.services.s3.transfer.internal.MultipleFileTransferMonitor;
import com.amazonaws.services.s3.transfer.internal.MultipleFileUploadImpl;
import com.amazonaws.services.s3.transfer.internal.TransferProgressUpdatingListener;
import com.amazonaws.services.s3.transfer.internal.TransferStateChangeListener;
import com.amazonaws.services.s3.transfer.internal.UploadImpl;
import com.mb.lab.banks.utils.storage.StorageProperties.S3;

public class S3StorageProvider implements StorageProvider, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private S3 s3Properties;
    private AWSCredentialsProvider credentials;
    
    private AmazonS3 amazonS3;
    private AwsUrlCreator urlCreator;
    private TransferManager transferManager;

    public S3StorageProvider(S3 s3Properties) {
        this.s3Properties = s3Properties;
        this.setup();
    }
    
    private void setup() {
        this.credentials = new AWSStaticCredentialsProvider(s3Properties.getCredentials());
        
        // Build s3 client
        ClientConfiguration config = new ClientConfiguration();
        
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentials)
                ;
        
        if (this.s3Properties.getEndpoint().isUseCustomEndpoint()) {
            builder = builder.withEndpointConfiguration(
                    new EndpointConfiguration(
                            this.s3Properties.getEndpoint().getCustomEndpoint(),
                            this.s3Properties.getEndpoint().getRegion()));
            config.setProtocol(this.s3Properties.isUseHttps() ? Protocol.HTTPS : Protocol.HTTP);
            config.setSignerOverride(this.s3Properties.getEndpoint().getSignatureVersion().getValue());
            
            builder.setPathStyleAccessEnabled(this.s3Properties.getEndpoint().isUsePathStyleAccess());
        } else {
            builder = builder.withRegion(this.s3Properties.getEndpoint().getRegion());
        }
        
        builder = builder.withClientConfiguration(config);
        this.amazonS3 = builder.build();
        
        // Build URL Creator
        this.urlCreator = AwsUrlCreator.buildAwsUrlCreator(this.s3Properties, this.credentials, this.amazonS3);
        
        this.transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
    }

    @Override
    public String store(File photo, String path, String filename) throws IOException {
        try {
            uploadFileList(Collections.singletonList(photo), path, Collections.singletonList(filename)).waitForCompletion();
            return filename;
        } catch (AmazonClientException | InterruptedException e) {
            logger.error("Fail to upload photo to S3", e);
            return null;
        }
    }

    @Override
    public List<String> store(List<File> photos, String path, List<String> filenames) throws IOException {
        try {
            uploadFileList(photos, path, filenames).waitForCompletion();
            return filenames;
        } catch (AmazonClientException | InterruptedException e) {
            logger.error("Fail to upload photo to S3", e);
            return null;
        }
    }

    @Override
    public void remove(String path, String filename) {
        try {
            amazonS3.deleteObject(s3Properties.getBucket(), path + filename);
        } catch (AmazonClientException e) {
            logger.error("Fail to remove photo from S3", e);
        }
    }

    @Override
    public void remove(String path, List<String> filenames) {
        if (CollectionUtils.isEmpty(filenames)) {
            return;
        }

        String[] keys = new String[filenames.size()];

        for (int i = 0; i < filenames.size(); i++) {
            keys[i] = path + filenames.get(i);
        }

        DeleteObjectsRequest request = new DeleteObjectsRequest(s3Properties.getBucket());
        request.withKeys(keys);

        try {
            amazonS3.deleteObjects(request);
        } catch (AmazonClientException e) {
            logger.error("Fail to remove photos from S3", e);
        }
    }

    @Override
    public String getUrl(String path, String filename) {
        return urlCreator.getUrl(path + filename);
    }

    @Override
    public File getFile(String path, String filename) {
        try {
            File tempFile = File.createTempFile(filename, ".temp");
            amazonS3.getObject(new GetObjectRequest(s3Properties.getBucket(), path + filename), tempFile);
            return tempFile;
        } catch (IOException e) {
            logger.error("Cannot create temp file", e);
            throw new UnsupportedOperationException("Cannot create temp file", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.transferManager.shutdownNow();
    }

    private MultipleFileUpload uploadFileList(List<File> files, String path, List<String> filenames) {
        /* This is the hook for adding additional progress listeners */
        ProgressListenerChain additionalListeners = new ProgressListenerChain();
        TransferProgress progress = new TransferProgress();
        /*
         * Bind additional progress listeners to this MultipleFileTransferProgressUpdatingListener to receive ByteTransferred events from each single-file
         * upload implementation.
         */
        ProgressListener listener = new MultipleFileTransferProgressUpdatingListener(progress, additionalListeners);

        List<UploadImpl> uploads = new LinkedList<UploadImpl>();
        MultipleFileUploadImpl multipleFileUpload = new MultipleFileUploadImpl("Uploading etc", progress,
                additionalListeners, path, s3Properties.getBucket(), uploads);
        multipleFileUpload.setMonitor(new MultipleFileTransferMonitor(multipleFileUpload, uploads));
        final CountDownLatch latch = new CountDownLatch(1);
        MultipleFileTransferStateChangeListener transferListener = new MultipleFileTransferStateChangeListener(latch, multipleFileUpload);
        if (files == null || files.isEmpty()) {
            multipleFileUpload.setState(TransferState.Completed);
        } else {
            long totalSize = 0;
            for (int i = 0; i < files.size(); i++) {
                File f = files.get(i);

                // Check, if file, since only files can be uploaded.
                if (f.isFile()) {
                    totalSize += f.length();

                    ObjectMetadata metadata = new ObjectMetadata();
                    AccessControlList acl = new AccessControlList();

                    if (s3Properties.isGrantPublicPermission()) {
                        metadata.setCacheControl("max-age=2592000,public");
                        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
                    }

                    // All the single-file uploads share the same
                    // MultipleFileTransferProgressUpdatingListener and
                    // MultipleFileTransferStateChangeListener
                    PutObjectRequest putRequest = new PutObjectRequest(s3Properties.getBucket(), path + filenames.get(i), f)
                            .withMetadata(metadata)
                            .withAccessControlList(acl)
                            .<PutObjectRequest> withGeneralProgressListener(listener);

                    UploadImpl upload = (UploadImpl) transferManager.upload(putRequest);
                    upload.addStateChangeListener(transferListener);

                    uploads.add(upload);
                }
            }
            progress.setTotalBytesToTransfer(totalSize);
        }

        // Notify all state changes waiting for the uploads to all be queued
        // to wake up and continue
        latch.countDown();
        return multipleFileUpload;
    }

    public static class MultipleFileTransferProgressUpdatingListener extends TransferProgressUpdatingListener implements DeliveryMode {
        private final ProgressListenerChain progressListenerChain;

        public MultipleFileTransferProgressUpdatingListener(TransferProgress transferProgress, ProgressListenerChain progressListenerChain) {
            super(transferProgress);
            this.progressListenerChain = progressListenerChain;
        }

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            super.progressChanged(progressEvent);
            progressListenerChain.progressChanged(progressEvent);
        }

        @Override
        public boolean isSyncCallSafe() {
            return progressListenerChain == null || progressListenerChain.isSyncCallSafe();
        }
    }

    public static class MultipleFileTransferStateChangeListener implements TransferStateChangeListener {
        private final CountDownLatch latch;
        private final MultipleFileTransfer<?> multipleFileTransfer;

        public MultipleFileTransferStateChangeListener(CountDownLatch latch, MultipleFileTransfer<?> multipleFileTransfer) {
            this.latch = latch;
            this.multipleFileTransfer = multipleFileTransfer;
        }

        @Override
        public void transferStateChanged(Transfer upload, TransferState state) {
            // There's a race here: we can't start monitoring the state of
            // individual transfers until we have added all the transfers to the
            // list, or we may incorrectly report completion.
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new SdkClientException("Couldn't wait for all downloads to be queued");
            }

            synchronized (multipleFileTransfer) {
                if (multipleFileTransfer.getState() == state || multipleFileTransfer.isDone())
                    return;

                /*
                 * If we're not already in a terminal state, allow a transition to a non-waiting state. Mark completed if this download is completed and the
                 * monitor says all of the rest are as well.
                 */
                if (state == TransferState.InProgress) {
                    multipleFileTransfer.setState(state);
                } else if (multipleFileTransfer.getMonitor().isDone()) {
                    multipleFileTransfer.collateFinalState();
                } else {
                    multipleFileTransfer.setState(TransferState.InProgress);
                }
            }
        }
    }

}
