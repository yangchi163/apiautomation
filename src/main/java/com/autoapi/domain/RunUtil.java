package com.autoapi.domain;

import com.autoapi.httprequest.HttpClientUtil;
import com.autoapi.model.*;
import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import com.autoapi.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import keywords.DoFunction;
import keywords.DoSql;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;
import static com.autoapi.util.CommonUtil.*;
public class RunUtil {
    private ApiConfig apiConfig;

    public RunUtil(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }


    /**
     *
     * @param fixtureModel 整个setup或者teardown
     * @param baseModel fixturemodel所处的节点
     * @param inputStream mybatis需要
     * @param varPath fixturemodel所处的路径
     * @throws Exception
     */
    public void runFixture(FixtureModel fixtureModel,BaseModel baseModel,InputStream inputStream,String[] varPath) throws Exception {
        if (fixtureModel != null){
            List fixtureList = fixtureModel.getFixture();
            for (Object fixture: fixtureList){
                if (fixture instanceof SqlModel){
                    runSql(inputStream, (SqlModel) fixture,baseModel,varPath);
                }
                if (fixture instanceof FunctionModel){
                    runFunction((FunctionModel) fixture,baseModel,varPath);
                }
                if (fixture instanceof CaseModel){
                    runApi((CaseModel) fixture,baseModel,varPath,false);
                }
            }

        }
    }

    /**
     * 执行api
     * @param caseModel
     * @param baseModel casemodel输出的东西存放的节点
     * @param saveRequest 是否保存request,response到var
     * @throws Exception
     */
    public void runApi(CaseModel caseModel,BaseModel baseModel,String[] varPath,boolean saveRequest) throws Exception {
        //执行前替换变量
        replaceVar(caseModel,varPath);
        HttpClientRequest request = getRequest(caseModel);
        HttpClientUtil clientUtil = new HttpClientUtil();
        HttpClientResponse response = clientUtil.doRequest(request);
        if (caseModel.getOutput() != null){
            baseModel.getVar().put(caseModel.getOutput(),response);
        }
        //如果是测试用例，将请求和响应保存到var中
        if (saveRequest){
            //解析response中的body
            String body = (String) response.getBody();
            JsonParser parser = new JsonParser();
            response.setBody(parser.parse(body));
            //保存request,response（供使用）
            baseModel.getVar().put(REQUEST,request);
            baseModel.getVar().put(RESPONSE,response);
            //将request,response保存到config,方便出错了检查问题
            caseModel.setHttpClientRequest(request);
            caseModel.setHttpClientResponse(response);

        }
    }

    /**
     *
     * @param inputStream mybatis需要
     * @param sqlModel sql信息
     * @param baseModel sqlmodel所处的节点，回写变量
     * @param varPath sqlmodel所处的路径，用来替换变量
     * @return
     */
    public Object runSql(InputStream inputStream,SqlModel sqlModel, BaseModel baseModel,String[] varPath) {
        //执行前替换变量
        replaceVar(sqlModel,varPath);
        //返回的结果
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
            //获取执行方法,可能没有参数
            Method m;
            if (params != null){
                m = clazz.getMethod(sqlName,Map.class);
                //判断方法是否有返回值,执行方法，并将返回结果赋值给result
                if (m.getReturnType().getName().equals("void")){
                    m.invoke(mapper,params);
                }else {
                    result = m.invoke(mapper,params);
                }
            }else {
                m = clazz.getMethod(sqlName);
                //判断方法是否有返回值,执行方法，并将返回结果赋值给result
                if (m.getReturnType().getName().equals("void")){
                    m.invoke(mapper);
                }else {
                    result = m.invoke(mapper);
                }
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
    private void runFunction(FunctionModel functionModel, BaseModel baseModel,String[] varPath) throws Exception {
        //执行前替换变量
        replaceVar(functionModel,varPath);
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
            //找到对应的方法,反射调用传入数组，相当于调用可变参数方法
            if (functionName.equals(method.getName())){
                if (method.getReturnType().getName().equals("void")){
                    method.invoke(doFunction,param);
                }else {
                    res = method.invoke(doFunction,param);
                }
                if (output != null){
                    baseModel.getVar().put(output,res);
                }
                break;
            }
        }
    }

    /**
     * 更新测试执行结果
     * @param result
     * @param caseModel
     * @param apiModel
     * @param moduleModel
     * @param projectModel
     */
    public void countResult(boolean result,CaseModel caseModel,ApiModel apiModel,ModuleModel moduleModel,ProjectModel projectModel){
        if (result){
            caseModel.setSuccess();
            apiModel.setSuccess();
            moduleModel.setSuccess();
            projectModel.setSuccess();
        }else {
            caseModel.setFail();
            apiModel.setFail();
            moduleModel.setFail();
            projectModel.setFail();
        }
        caseModel.setTotal();
        apiModel.setTotal();
        moduleModel.setTotal();
        projectModel.setTotal();
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
        if (params != null && params.size() > 0){
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

    /**
     *只有当值是string（${varname}）时，才会去替换,替换后的结果是object(可能是对象，基本数据)
     * @param o 需要替换的对象
     * @param varPath 当前变量的路径
     */
    public void replaceVar(Object o,String[] varPath){
        if (o != null){
            if (o instanceof Map){
                Map m = (Map) o;
                for (Object keyMap : m.keySet()){
                    if (m.get(keyMap) instanceof String){
                        String v = (String) m.get(keyMap);
                        m.put(keyMap,getReplaceString(v,varPath));
                    } else {
                        replaceVar(m.get(keyMap),varPath);
                    }
                }
            } else if (o instanceof List){
                //记录list的index
                int i = 0;
                List l = (List) o;
                for (Object ol: l){
                    if (ol instanceof String){
                        l.set(i,getReplaceString((String) ol,varPath));
                    } else {
                        replaceVar(ol,varPath);
                    }
                    i = i + 1;
                }
            } else if (o instanceof BaseModel){
                BaseModel baseModel = (BaseModel) o;
                //处理var
                replaceVar(baseModel.getVar(),varPath);
                //处理fixturemodel.setup
                replaceVar(baseModel.getSetup(),varPath);
                //处理fixturemodel.teardown
                replaceVar(baseModel.getTeardown(),varPath);
                if (o instanceof CaseModel){
                    CaseModel caseModel = (CaseModel) o;
                    //处理requstmodel
                    replaceVar(caseModel.getRequest(),varPath);
                    //处理assert
                    replaceVar(caseModel.getAsserts(),varPath);
                }
            } else if (o instanceof FixtureModel){
                FixtureModel fixtureModel = (FixtureModel) o;
                //fixture是个list
                replaceVar(fixtureModel.getFixture(),varPath);
            } else if (o instanceof RequestModel){
                RequestModel requestModel = (RequestModel) o;
                //解析urlmodel
                replaceVar(requestModel.getUrlModel(),varPath);
                //解析headers
                replaceVar(requestModel.getHeaders(),varPath);
                //解析body
                replaceVar(requestModel.getBody(),varPath);
            } else if (o instanceof UrlModel){
                UrlModel urlModel = (UrlModel) o;
                //解析params
                replaceVar(urlModel.getParams(),varPath);
                //解析path
                replaceVar(urlModel.getPath(),varPath);
            } else if (o instanceof SqlModel){
                SqlModel sqlModel = (SqlModel) o;
                //解析sql中参数变量
                replaceVar(sqlModel.getParams(),varPath);
            } else if (o instanceof FunctionModel){
                FunctionModel functionModel = (FunctionModel) o;
                //解析function中的变量
                replaceVar(functionModel.getParams(),varPath);
            } else {
                //数字类的不做处理
            }
        }
    }

    /**
     * @param src 待替换的string
     * @param varPath 当前变量所处的路径：project-module-api-case : 没有包含varname
     * @return
     */
    private Object getReplaceString(String src,String[] varPath){
        Object res = src;
        int beginIndex = 0;
        while (true){
            //s是需要替换掉的部分,s=${aaaa}
            String s = CommonUtil.getFirstString(src,beginIndex);
            if (s != null){
                //varname 是待替换的变量名
                String varName = s.substring(2,s.length()-1);
                String[] realVarName = {varName};
                String[] realVarPath = CommonUtil.mergeArray(varPath,realVarName);
                Object strForReplace = getReplaceValue(realVarPath);
                //1.防止var中拿到的值是null，就不替换,不是null才替换
                //2.var中拿到的值如果不是string,就直接给
                if(strForReplace != null){
                    if (strForReplace instanceof String){
                        //是string,src有可能是/${varName1}/${varName2}/***
                        src = src.replace(s, (String)strForReplace);
                        res = src;
                    }else {
                        //strForReplace不是string证明:src只是一个单独的${varName}
                        //判断是否需要从对象中取值
                        if(CommonUtil.isJsonVariable(src)){
                            String jsonpath = src.replace(CommonUtil.getFirstString(src,0),"$");
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(strForReplace);
                            res = CommonUtil.getFromJsonByjsonPath(jsonString,jsonpath);
                        }else {
                            res = strForReplace;
                        }
                    }
                }
                //计算下一个需替换值的位置
                beginIndex = src.indexOf(s) + s.length();
            } else {
                break;
            }

        }
        return res;
    }


    /**
     * 获得请求数据中变量对应的值:包含var，以及其他部分
     * @param varPath 变量对应的路径数组 project-module-api-case-varname
     * @return
     */
    private Object getReplaceValue(String[] varPath){
        Object res = null;
        String varName = varPath[varPath.length - 1];
        ProjectModel projectModel = null;
        ModuleModel moduleModel = null;
        ApiModel apiModel = null;
        CaseModel caseModel;
        if (varPath.length > 1){
            projectModel = this.apiConfig.getProjects().get(varPath[0]);
            if (projectModel.getVar().containsKey(varName)){
                Object temp = projectModel.getVar().get(varName);
                if (!CommonUtil.isVariable(temp)){
                    res = temp;
                }

            }
        }
        if (varPath.length > 2){
            moduleModel = projectModel.getModules().get(varPath[1]);
            if (moduleModel.getVar().containsKey(varName)){
                Object temp = moduleModel.getVar().get(varName);
                if (!CommonUtil.isVariable(temp)){
                    res = temp;
                }
            }
        }
        if (varPath.length > 3){
            apiModel = moduleModel.getApis().get(varPath[2]);
            if (apiModel.getVar().containsKey(varName)){
                Object temp = apiModel.getVar().get(varName);
                if (!CommonUtil.isVariable(temp)){
                    res = temp;
                }
            }
        }
        if (varPath.length > 4){
            caseModel = apiModel.getCases().get(varPath[3]);
            if (caseModel.getVar().containsKey(varName)){
                Object temp = caseModel.getVar().get(varName);
                if (!CommonUtil.isVariable(temp)){
                    res = temp;
                }
            }
        }
        return res;
    }

}
