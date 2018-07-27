package com.autoapi.model.http;

import java.util.Map;

public class HttpClientRequest {
    private String url;
    private String method;
    private Map<String,String> headers;
    private Map body;

    public HttpClientRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        return "HttpClientRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
