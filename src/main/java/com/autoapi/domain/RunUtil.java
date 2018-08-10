package com.autoapi.domain;

import com.autoapi.httprequest.HttpClientUtil;
import com.autoapi.model.*;
import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import keywords.DoFunction;
import keywords.DoSql;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;
public class RunUtil {

    /**
     * @param sqlModel
     * @param baseModel sqlmodel所处的节点
     * @throws IOException
     * @throws NoSuchMethodException
     */
    public Object runSql(InputStream inputStream,SqlModel sqlModel, BaseModel baseModel) {
        Object result = null;
        //从sqlmodel中取出值
        String conn = sqlModel.getConn();
        String sqlName = sqlModel.getSqlName();
        Map params = sqlModel.getParams();
        String output = sqlModel.getOutput();
        //初始化SqlSessionFactory，获取代理执行类
        SqlSessionFactory sqlSessionFactory ;
        if (conn != null){
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,conn);
        } else {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DoSql mapper = session.getMapper(DoSql.class);
            Class clazz = mapper.getClass();
            //获取执行方法
            Method m = clazz.getMethod(sqlName,Map.class);
            //判断方法是否有返回值,执行方法，并将返回结果赋值给result
            if (m.getReturnType().getName().equals("void")){
                m.invoke(mapper,params);
            }else {
                result = m.invoke(mapper,params);
            }
            //判断是否需要将result放到变量中
            if (output != null){
                baseModel.getVar().put(output,result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    /**
     *
     * @param functionModel
     * @param baseModel
     * @throws Exception
     */
    public void runFunction(FunctionModel functionModel, BaseModel baseModel) throws Exception {
        Object res = null;
        //获取方法的相关信息
        String functionName = functionModel.getFunctionName();
        Map params = functionModel.getParams();
        String output = functionModel.getOutput();
        //将params中的值存放到数组中，有序
        Object[] param = null;
        if (params != null){
            param = new Object[params.size()];
            //计数器
            int i = 0;
            for (Object paramsKey:params.keySet()){
                param[i] = params.get(paramsKey);
                i++;
            }
        }
        //利用反射执行方法
        DoFunction doFunction = new DoFunction();
        Class clazz = doFunction.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods){
            //找到对应的方法
            if (functionName.equals(method.getName())){
                if (method.getReturnType().getName().equals("void")){
                    method.invoke(doFunction,param);
                }else {
                    res = method.invoke(doFunction,param);
                }
                if (output != null){
                    baseModel.getVar().put(output,res);
                    System.out.println(baseModel.getVar());
                }
                break;
            }
        }
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
        System.out.println(request);
        HttpClientResponse response = clientUtil.doRequest(request);
        System.out.println(response);
        if (caseModel.getOutput() != null){
            baseModel.getVar().put(caseModel.getOutput(),response);
        }
        //如果是测试用例，将请求和响应保存到var中
        if (baseModel instanceof CaseModel){
            baseModel.getVar().put(REQUEST,caseModel.getRequest());
            baseModel.getVar().put(RESPONSE,response);
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
