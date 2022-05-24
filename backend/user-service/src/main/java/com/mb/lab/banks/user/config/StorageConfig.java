package com.mb.lab.banks.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.lab.banks.utils.storage.FileStorageProvider;
import com.mb.lab.banks.utils.storage.S3StorageProvider;
import com.mb.lab.banks.utils.storage.StorageProperties;
import com.mb.lab.banks.utils.storage.StorageProvider;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {

	@Configuration
	@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "file", matchIfMissing = true)
	public static class FilePhotoStorageConfig {

		@Autowired
		private StorageProperties storageProperties;

		@Bean
		public StorageProvider storageProvider() {
			return new FileStorageProvider(storageProperties.getFile().getRootPath(),
					storageProperties.getFile().getRootUrl());
		}

	}

	@Configuration
	@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "s3", matchIfMissing = false)
	public static class S3PhotoStorageConfig {

		@Autowired
		private StorageProperties storageProperties;

		@Bean
		public StorageProvider storageProvider() {
			return new S3StorageProvider(storageProperties.getS3());
		}

	}

}
