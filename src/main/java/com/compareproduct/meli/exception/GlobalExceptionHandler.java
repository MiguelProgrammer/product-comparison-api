package com.compareproduct.meli.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        log.error("EXCEPTION_PRODUCT_NOT_FOUND path={} error={}", 
                request.getDescription(false), ex.getMessage());
        return buildErrorResponse("Product not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchResourceFoundException(
            NoSuchResourceFoundException ex, WebRequest request) {
        log.error("EXCEPTION_RESOURCE_NOT_FOUND path={} error={}", 
                request.getDescription(false), ex.getMessage());
        return buildErrorResponse("Product not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadResourceRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadResourceRequestException(
            BadResourceRequestException ex, WebRequest request) {
        log.error("EXCEPTION_BAD_REQUEST path={} error={}", 
                request.getDescription(false), ex.getMessage());
        return buildErrorResponse("Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        log.error("EXCEPTION_ILLEGAL_ARGUMENT path={} error={}", 
                request.getDescription(false), ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error("EXCEPTION_INVALID_PARAMETER path={} parameter={} value={} error={}", 
                request.getDescription(false), ex.getName(), ex.getValue(), ex.getMessage());
        String message = String.format("Invalid parameter '%s': %s", ex.getName(), ex.getValue());
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("EXCEPTION_VALIDATION_ERROR path={} error={}", 
                request.getDescription(false), ex.getMessage());
        return buildErrorResponse("Validation failed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        log.error("EXCEPTION_MESSAGE_NOT_READABLE path={} error={}", 
                request.getDescription(false), ex.getMessage());
        return buildErrorResponse("Invalid request body", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, WebRequest request) {
        log.error("EXCEPTION_UNSUPPORTED_MEDIA_TYPE path={} contentType={} error={}", 
                request.getDescription(false), ex.getContentType(), ex.getMessage());
        return buildErrorResponse("Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {
        log.error("EXCEPTION_INTERNAL_ERROR path={} error={}", 
                request.getDescription(false), ex.getMessage(), ex);
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        
        return new ResponseEntity<>(errorDetails, status);
    }
}