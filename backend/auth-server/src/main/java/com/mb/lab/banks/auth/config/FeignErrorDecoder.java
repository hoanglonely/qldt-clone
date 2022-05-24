package com.mb.lab.banks.auth.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.mb.lab.banks.utils.exception.BusinessException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ErrorDecoder errorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() != 400) {
            return errorDecoder.decode(methodKey, response);
        }

        Reader reader = null;

        try {
            reader = response.body().asReader();
            String result = CharStreams.toString(reader);

            Map<String, Object> envelope = objectMapper.readValue(result, Map.class);
            Map<String, Object> meta = (Map<String, Object>) envelope.get("meta");
            
            BusinessException exception = new BusinessException(meta.get("error_message").toString());
            return exception;
        } catch (IOException e) {
            logger.warn("Fail to decode error response to BusinessException", e);
            return errorDecoder.decode(methodKey, response);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
