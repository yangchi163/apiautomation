package com.autoapi.model;

import java.util.Map;

public class RequestBaseModel {
    public UrlModel urlModel;
    public String method = "";
    public Map<String,String> headers;


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

    @Override
    public String toString() {
        return "RequestBaseModel{" +
                "urlModel=" + urlModel +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                '}';
    }
}
