package com.mb.lab.banks.auth.endpoint.mobile;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import com.mb.lab.banks.utils.exception.mobile.MobileApiException;
import com.mb.lab.banks.utils.rest.mobile.MobileApiResponse;

@ControllerAdvice(basePackageClasses = { MobileUserInfoEndpoint.class })
public class MobileApiExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> defaultErrorHandler(HttpServletRequest req, Exception e) {
        logger.error("Unexpected error", e);
        return new ResponseEntity<>(MobileApiResponse.INTERNAL_SERVER_ERROR, HttpStatus.OK);
    }

    @ExceptionHandler(value = JsonMappingException.class)
    public ResponseEntity<?> jsonErrorHandler(HttpServletRequest req, JsonMappingException e) {
        logger.debug("JSON mapping error", e);
        return new ResponseEntity<>(MobileApiResponse.INVALID_REQUEST, HttpStatus.OK);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotSupportErrorHandler(HttpServletRequest req, Exception e) {
        logger.debug("MethodNotSupportedException error", e);
        return new ResponseEntity<>(MobileApiResponse.INVALID_REQUEST, HttpStatus.OK);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class })
    public ResponseEntity<?> illegalErrorHandler(HttpServletRequest req, Exception e) {
        logger.debug("IllegalArgument error", e);
        return new ResponseEntity<>(MobileApiResponse.INVALID_REQUEST, HttpStatus.OK);
    }

    @ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<?> mediaTypeNotSupportErrorHandler(HttpServletRequest req, Exception e) {
        logger.debug("HttpMediaTypeNotSupported error", e);
        return new ResponseEntity<>(MobileApiResponse.INVALID_REQUEST, HttpStatus.OK);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(HttpServletRequest req, IllegalArgumentException e) {
        logger.debug("IllegalArgumentException error", e);
        return new ResponseEntity<>(MobileApiResponse.INVALID_PARAM, HttpStatus.OK);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<?> businessErrorHandler(HttpServletRequest req, BusinessException e) {
        logger.debug("BusinessException error", e);
        return new ResponseEntity<>(MobileApiResponse.INVALID_PARAM, HttpStatus.OK);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<?> notFoundErrorHandler(HttpServletRequest req, NoHandlerFoundException e) {
        logger.debug("ResourceNotFoundException error", e);
        return new ResponseEntity<>(MobileApiResponse.NOT_FOUND, HttpStatus.OK);
    }

    @ExceptionHandler(value = MobileApiException.class)
    public ResponseEntity<?> businessErrorHandler(HttpServletRequest req, MobileApiException e) {
        logger.debug("MobileApiException error", e);
        MobileApiResponse response = new MobileApiResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
