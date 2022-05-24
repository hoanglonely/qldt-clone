package com.mb.lab.banks.auth.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mb.lab.banks.utils.rest.Envelope;
import com.mb.lab.banks.utils.rest.RestError;

@Controller
public class ErrorPageController implements ErrorController {
    
    private final static String ERROR_PATH = "/error";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * Supports the HTML Error View
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(WebRequest request) {
        return new ModelAndView("error", getErrorAttributes(request, false));
    }

    /**
     * Supports other formats like JSON, XML
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<?> error(WebRequest request) {
        Map<String, Object> errorAttrs = getErrorAttributes(request, false);
        String error = (String) errorAttrs.get("error");
        String message = (String) errorAttrs.get("messsage");
        Integer status = (Integer) errorAttrs.get("status");
        if (status != null && status < 500) {
            HttpStatus httpStatus = getStatus(request);
            RestError errorData = new RestError(error, status, message);
            Envelope response = new Envelope(errorData);
            return new ResponseEntity<Envelope>(response, httpStatus);
        } else {
            RestError errorData = new RestError(error, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
            Envelope response = new Envelope(errorData);
            return new ResponseEntity<Envelope>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private Map<String, Object> getErrorAttributes(WebRequest request, boolean includeStackTrace) {
        return this.errorAttributes.getErrorAttributes(request, includeStackTrace);
    }

    private HttpStatus getStatus(WebRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unknow HTTP status code " + statusCode, ex);
                }
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
