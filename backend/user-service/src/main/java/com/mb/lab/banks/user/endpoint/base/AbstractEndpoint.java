package com.mb.lab.banks.user.endpoint.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.mb.lab.banks.user.business.dto.base.FileDownloadDto;
import com.mb.lab.banks.user.util.security.SecurityContextHelper;
import com.mb.lab.banks.user.util.security.UserLogin;
import com.mb.lab.banks.utils.rest.Envelope;

public abstract class AbstractEndpoint {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected final UserLogin getUserLogin() {
		return SecurityContextHelper.getCurrentUser();
	}

	protected final ResponseEntity<?> getResponseEntity(Object data) {
		return new Envelope(data).toResponseEntity(HttpStatus.OK);
	}

	protected final ResponseEntity<InputStreamResource> getFileDownloadResponseEntity(FileDownloadDto fileDownloadDto) {
		InputStreamResource response;
		try {
			response = new InputStreamResource(new FileInputStream(fileDownloadDto.getFile()));
		} catch (FileNotFoundException e) {
			logger.error("file not found", e);
			throw new UnsupportedOperationException(e);
		}

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.set("Content-Disposition", "attachment; filename=\"" + fileDownloadDto.getFileName() + "\"");

		return new ResponseEntity<>(response, header, HttpStatus.OK);
	}

	protected final ResponseEntity<InputStreamResource> getFilePdfResponseEntity(FileDownloadDto fileDownloadDto) {
		InputStreamResource response;
		try {
			response = new InputStreamResource(new FileInputStream(fileDownloadDto.getFile()));
		} catch (FileNotFoundException e) {
			logger.error("file not found", e);
			throw new UnsupportedOperationException(e);
		}

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_PDF);
		header.set("Content-Disposition", "inline; filename=\"" + fileDownloadDto.getFileName() + "\"");

		return new ResponseEntity<>(response, header, HttpStatus.OK);
	}

}
