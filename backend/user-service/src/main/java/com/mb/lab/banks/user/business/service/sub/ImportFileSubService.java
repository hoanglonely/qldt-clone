package com.mb.lab.banks.user.business.service.sub;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.mb.lab.banks.utils.common.StringUtils;
import com.mb.lab.banks.utils.storage.StorageProvider;

@Service
public class ImportFileSubService {

	private static final String RELATIVE_PATH = "public/import/file/";

	@Autowired
	private StorageProvider storageProvider;

	/**
	 * @return filePath
	 */
	public String storeFile(File file, String prefix, String surfix) throws IOException {
		Assert.notNull(file, "file cannot be null");

		Calendar calendar = Calendar.getInstance();
		StringBuilder filename = new StringBuilder();
		filename.append(calendar.get(Calendar.YEAR)).append("/");
		filename.append(calendar.get(Calendar.MONTH) + 1).append("/");
		filename.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/");

		if (!StringUtils.isEmpty(prefix)) {
			filename.append(prefix);
		}
		filename.append(UUID.randomUUID().toString());
		if (!StringUtils.isEmpty(surfix)) {
			filename.append(surfix);
		}

		filename.append(".xlsx");

		return storageProvider.store(file, RELATIVE_PATH, filename.toString());
	}

	public String getUrl(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return null;
		}

		return storageProvider.getUrl(RELATIVE_PATH, filePath);
	}

	public File getFile(String filePath) {
		return storageProvider.getFile(RELATIVE_PATH, filePath);
	}

}
