package com.autoapi.httprequest;

import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import static com.autoapi.keywords.RequestKeyWords.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtil {
    private CloseableHttpClient client;

    public HttpClientResponse doRequest(HttpClientRequest request) throws Exception {
        init();
        HttpUriRequest httpRequest ;
        String method = request.getMethod();
        if (PUT.equalsIgnoreCase(method)){
            httpRequest = new HttpPut(request.getUrl());
        }else if (POST.equalsIgnoreCase(method)){
            httpRequest = new HttpPost(request.getUrl());
        }else if (DELETE.equalsIgnoreCase(method)){
            httpRequest = new HttpDelete(request.getUrl());
        }else if (GET.equalsIgnoreCase(method)){
            httpRequest = new HttpGet(request.getUrl());
        }else {
            throw new Exception("不支持的请求方式");
        }
        return sendRequest(httpRequest,request);

    }

    private void init() {
        client = HttpClientBuilder.create().build();
        //System.out.println("client初始化成功");
    }

    private HttpClientResponse sendRequest(HttpUriRequest httpRequest, HttpClientRequest request) {
        //初始化response，用来接收返回结果
        HttpClientResponse httpClientResponse = new HttpClientResponse();
        Gson gson = new Gson();
        if (request.getHeaders() != null) {
            for (String key : request.getHeaders().keySet()) {
                httpRequest.setHeader(key, request.getHeaders().get(key));
            }
        }
        if (request.getBody() != null && httpRequest instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(gson.toJson(request.getBody()),"utf-8"));
        }
        try {
            HttpResponse httpResponse = client.execute(httpRequest);
            //获得状态码
            httpClientResponse.setStatusCode(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            //获得headers
            Header[] headers = httpResponse.getAllHeaders();
            Map<String,String> headerMap = new HashMap<String, String>();
            for (Header header : headers){
                headerMap.put(header.getName(),header.getValue());
            }
            httpClientResponse.setHeaders(headerMap);
            //获得body
            HttpEntity entity = httpResponse.getEntity();
            String body = EntityUtils.toString(entity,"utf-8");
            httpClientResponse.setBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return httpClientResponse;
    }

    private void close() {
        try {
            client.close();
            //System.out.println("client已关闭");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("client关闭失败");
        }
    }
}
