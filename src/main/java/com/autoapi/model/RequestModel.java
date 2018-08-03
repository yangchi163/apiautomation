package com.autoapi.model;

import java.util.Map;

public class RequestModel {
    private String name;
    private UrlModel urlModel;
    private String method = "";
    private Map<String,String> headers;
    private Map body;

    public RequestModel(String name) {
        this.name = name;
    }

    public UrlModel getUrlModel() {
        return urlModel;
    }

    public void setUrlModel(UrlModel urlModel) {
        this.urlModel = urlModel;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map getBody() {
        return body;
    }

    public void setBody(Map body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "urlModel=" + urlModel +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
