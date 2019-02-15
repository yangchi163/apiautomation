package com.autoapi.httprequest;

import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import static com.autoapi.keywords.RequestKeyWords.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private HttpClientResponse sendRequest(HttpUriRequest httpRequest, HttpClientRequest request) throws UnsupportedEncodingException {
        //初始化response，用来接收返回结果
        HttpClientResponse httpClientResponse = new HttpClientResponse();
        if (request.getHeaders() != null) {
            for (String key : request.getHeaders().keySet()) {
                httpRequest.setHeader(key, request.getHeaders().get(key));
            }
        }
        if (request.getBody() != null && httpRequest instanceof HttpEntityEnclosingRequestBase) {
            HttpEntity entity = getEntity(getContentType(httpRequest),request);
            ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(entity);
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
            String body = EntityUtils.toString(entity,UTF8);
            httpClientResponse.setBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return httpClientResponse;
    }

    /**
     * 判断请求的传参方式，是表单还是json
     * @param httpRequest 一个具体的请求（包含请求头）
     * @return
     */
    private String getContentType(HttpUriRequest httpRequest){
        String headerName = "Content-Type";
        String res = FORM;
        if (httpRequest.containsHeader(headerName)){
            Header header = httpRequest.getFirstHeader(headerName);
            String v = header.getValue();
            if (v.contains("application/json")){
                res = JSON;
            }
        }
        return res;
    }

    /**
     * 获取请求的entity
     * @param postType 传参方式
     * @param request
     * @return
     */
    private HttpEntity getEntity(String postType,HttpClientRequest request) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        Map body = request.getBody();
        HttpEntity entity ;
        if (postType.equals(JSON)){
            entity = new StringEntity(gson.toJson(body),UTF8);
        } else {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Object key : body.keySet()){
                params.add(new BasicNameValuePair((String) key, (String) body.get(key)));
            }
            entity = new UrlEncodedFormEntity(params, UTF8);
        }
        return entity;
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
