package com.mb.lab.banks.user.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;
import com.mb.lab.banks.utils.rest.Envelope;
import com.mb.lab.banks.utils.rest.RestError;

/**
 * @author thanh
 */
@ControllerAdvice
public class DefaultExceptionHandler {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> defaultErrorHandler(HttpServletRequest req, Exception e) {
		logger.error("Unknow error", e);
		RestError error = new RestError("unknown", 500, "Unknown error occurs");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	public ResponseEntity<?> notFoundErrorHandler(HttpServletRequest req, NoHandlerFoundException e) {
		logger.debug("ResourceNotFoundException error", e);
		RestError error = new RestError("ResourceNotFoundException", 404, "Requested resource not found");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<?> illegalArgumentExceptionHandler(HttpServletRequest req, IllegalArgumentException e) {
		logger.debug("IllegalArgumentException error", e);
		RestError error = new RestError("IllegalArgumentException", 400, BusinessExceptionCode.INVALID_PARAM);
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<?> businessErrorHandler(HttpServletRequest req, BusinessException e) {
		logger.debug("BusinessException error", e);
		RestError error = new RestError("BusinessException", 400, e.getCode());
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = JsonMappingException.class)
	public ResponseEntity<?> jsonErrorHandler(HttpServletRequest req, JsonMappingException e) {
		logger.debug("JSON mapping error", e);
		RestError error = new RestError("IllegalArgumentException", 400, "Invalid request parameter");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> methodNotSupportErrorHandler(HttpServletRequest req, Exception e) {
		logger.debug("MethodNotSupportedException error", e);
		RestError error = new RestError("MethodNotSupportedException", 400, "Method not supported");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
			HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
	public ResponseEntity<?> illegalErrorHandler(HttpServletRequest req, Exception e) {
		logger.debug("IllegalArgument error", e);
		RestError error = new RestError("IllegalArgumentException", 400, "Invalid request parameter");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
	public ResponseEntity<?> mediaTypeNotSupportErrorHandler(HttpServletRequest req, Exception e) {
		logger.debug("HttpMediaTypeNotSupported error", e);
		RestError error = new RestError("MediaTypeNotSupportedException", 400, "MediaType not supported");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> accessDeniedErrorHandler(HttpServletRequest req, AccessDeniedException e) {
		logger.debug("AccessDeniedException error", e);
		RestError error = new RestError("AccessDeniedException", 401, "Access denied");
		Envelope response = new Envelope(error);
		return new ResponseEntity<Envelope>(response, HttpStatus.UNAUTHORIZED);
	}

}
