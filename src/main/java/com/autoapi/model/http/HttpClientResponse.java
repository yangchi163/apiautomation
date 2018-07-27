package com.autoapi.model.http;

import java.util.Map;

public class HttpClientResponse {
    private String statusCode;
    private Map<String,String> headers;
    private String body;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpClientResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
