package org.enums.exception;

public class XapiClientException extends RuntimeException {
    public XapiClientException(String msg) { super(msg); }
    public XapiClientException(String msg, Throwable cause) { super(msg, cause); }
}