package com.autoapi.domain;

import com.autoapi.httprequest.HttpClientUtil;
import com.autoapi.model.BaseModel;
import com.autoapi.model.CaseModel;
import com.autoapi.model.SqlModel;
import com.autoapi.model.UrlModel;
import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import keywords.DoSql;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunUtil {

    /**
     * @param sqlModel
     * @param baseModel sqlmodel所处的节点
     * @throws IOException
     * @throws NoSuchMethodException
     */
    public void runSql(InputStream inputStream,SqlModel sqlModel, BaseModel baseModel) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //从sqlmodel中取出值
        String conn = sqlModel.getConn();
        String sqlName = sqlModel.getSql();
        Map params = sqlModel.getParams();
        String output = sqlModel.getOutput();
        //初始化SqlSessionFactory，获取代理执行类
        String resource = "mybatis/mabatis-config.xml";
        inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory ;
        if (conn != null){
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,conn);
        } else {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        SqlSession session = sqlSessionFactory.openSession();
        DoSql mapper = session.getMapper(DoSql.class);
        Class clazz = mapper.getClass();
        //获取执行方法
        Method m = clazz.getMethod(sqlName,Map.class);
        if (output != null){
            baseModel.getVar().put(output,m.invoke(mapper,params));
        }
        session.close();
    }

    /**
     * 执行api
     * @param caseModel
     * @param baseModel casemodel输出的东西存放的节点
     * @throws Exception
     */
    public void runApi(CaseModel caseModel,BaseModel baseModel) throws Exception {
        HttpClientRequest request = getRequest(caseModel);
        HttpClientUtil clientUtil = new HttpClientUtil();
        HttpClientResponse response = clientUtil.doRequest(request);
        if (caseModel.getOutput() != null){
            baseModel.getVar().put(caseModel.getOutput(),response);
        }
    }
    /**
     * 将casemodel转化成HttpClientRequest，此时变量已替换完成
     * @param caseModel
     * @return
     */
    private HttpClientRequest getRequest(CaseModel caseModel) throws URISyntaxException {
        HttpClientRequest request = new HttpClientRequest();
        String url = getUrl(caseModel.getRequest().getUrlModel());
        request.setMethod(caseModel.getRequest().getMethod());
        request.setUrl(url);
        request.setHeaders(caseModel.getRequest().getHeaders());
        request.setBody(caseModel.getRequest().getBody());
        return request;
    }

    /**
     * 将urlmodel解析成url
     * @param urlModel
     * @return
     * @throws URISyntaxException
     */
    private String getUrl(UrlModel urlModel) throws URISyntaxException {
        //拼接url
        String schema = urlModel.getSchema();
        String host = urlModel.getHost();
        int port = urlModel.getPort();
        String version = urlModel.getVersion();
        String path = urlModel.getPath();
        if (version != null){
            path = version + "/" + path;
        }
        URIBuilder uriBuilder = new URIBuilder().setScheme(schema).setHost(host).setPort(port)
                .setPath(path);
        //组装params
        Map params = urlModel.getParams();
        if (params != null){
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            for (Object k : params.keySet()){
                NameValuePair pair = new BasicNameValuePair((String) k,(String) params.get(k));
                pairList.add(pair);
            }
            uriBuilder.setParameters(pairList);
        }

        String url = uriBuilder.build().toString();
        return url;
    }
}
