package com.autoapi.parse;

import com.autoapi.model.*;
import com.autoapi.model.asserts.AssertModel;
import com.autoapi.parse.ParseApiConfig.ParseAsserts;
import com.autoapi.parse.ParseApiConfig.ParseFixture;
import com.autoapi.util.CommonUtil;
import org.apache.http.util.Asserts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.autoapi.keywords.RequestKeyWords.*;

public class ParseUtil {
    /**
     * 将测试用例中的数据，与api.yaml中的数据合并成测试请求
     * @param caseDetail  测试用例文件中的数据
     * @param requestModelFromBase api.yaml文件中对应的接口数据
     * @return
     */
    public static CaseModel getCaseModel(Map caseDetail, RequestModel requestModelFromBase) throws Exception {
        CaseModel caseModel = new CaseModel("");
        //组装request.urlmodel
        caseModel.getRequest().setUrlModel(requestModelFromBase.getUrlModel());
        //组装request.method
        caseModel.getRequest().setMethod(requestModelFromBase.getMethod());
        //组装request.headers,params,body
        Map<String,String> headersFromCase = new HashMap<String, String>();
        Map paramsFromCase = new HashMap();
        Map bodyFromCase = new HashMap();
        if (caseDetail != null){
            //获取case中的信息
            if (caseDetail.containsKey(HEADERS)){
                if (caseDetail.get(HEADERS) != null){
                    headersFromCase = (Map<String, String>) caseDetail.get(HEADERS);
                }
            }
            if (caseDetail.containsKey(PARAMS)){
                if (caseDetail.get(PARAMS) != null){
                    paramsFromCase = (Map) caseDetail.get(PARAMS);
                }
            }
            if (caseDetail.containsKey(BODY)){
                if (caseDetail.get(BODY) != null){
                    bodyFromCase = (Map) caseDetail.get(BODY);
                }
            }
            //设置case级var
            if (caseDetail.containsKey(VAR)){
                if (caseDetail.get(VAR) != null){
                    caseModel.setVar((Map) caseDetail.get(VAR));
                }
            }
            //设置setup
            if (caseDetail.containsKey(SETUP)){
                if (caseDetail.get(SETUP) != null){
                    ParseFixture parseFixture = new ParseFixture();
                    FixtureModel fixtureModel = parseFixture.getFixtureModel(caseDetail,SETUP);
                    caseModel.setSetup(fixtureModel);
                }
            }
            //设置teardown
            if (caseDetail.containsKey(TEARDOWN)){
                if (caseDetail.get(TEARDOWN) != null){
                    ParseFixture parseFixture = new ParseFixture();
                    FixtureModel fixtureModel = parseFixture.getFixtureModel(caseDetail,TEARDOWN);
                    caseModel.setTeardown(fixtureModel);
                }
            }
            //设置output
            if (caseDetail.containsKey(OUTPUT)){
                if (caseDetail.get(OUTPUT) != null){
                    caseModel.setOutput((String) caseDetail.get(OUTPUT));
                }
            }
            //设置assert
            if (caseDetail.containsKey(ASSERTS)){
                if (caseDetail.get(ASSERTS) != null){
                    ParseAsserts parseAsserts = new ParseAsserts();
                    List<AssertModel> asserts = parseAsserts.getAsserts(caseDetail);
                    caseModel.setAsserts(asserts);
                }
            }
        }
        //将case中的信息和base中的信息合并
        Map headers = CommonUtil.mergeMap(requestModelFromBase.getHeaders(),headersFromCase);
        caseModel.getRequest().setHeaders(headers);
        Map params = CommonUtil.mergeMap(caseModel.getRequest().getUrlModel().getParams(),paramsFromCase);
        caseModel.getRequest().getUrlModel().setParams(params);
        Map body = CommonUtil.mergeMap(requestModelFromBase.getBody(),bodyFromCase);
        caseModel.getRequest().setBody(body);
        return caseModel;
    }

    /**
     * sqlname:
     *    params:
     *       a: s
     *       b: ss
     *    conn: ss
     *    output: varName
     *一个sqlMap中只有一个sqlmodel
     * @param sqlMapFromConfig
     * @return
     */
    public static SqlModel getSqlModel(Map sqlMapFromConfig) {
        if (sqlMapFromConfig == null) {
            return null;
        }
        SqlModel sqlModel = new SqlModel();
        for (Object sqlName : sqlMapFromConfig.keySet()) {
            //设置sqlname
            String sqlNames = (String) sqlName;
            sqlModel.setSqlName(sqlNames);
            Map sqlDetailFromConfig = (Map) sqlMapFromConfig.get(sqlName);
            //设置conn
            if (sqlDetailFromConfig.containsKey(CONN)) {
                if (sqlDetailFromConfig.get(CONN) != null){
                    sqlModel.setConn((String) sqlDetailFromConfig.get(CONN));
                }
            }
            //设置params
            if (sqlDetailFromConfig.containsKey(PARAMS)) {
                if (sqlDetailFromConfig.get(PARAMS) != null){
                    sqlModel.setParams((Map) sqlDetailFromConfig.get(PARAMS));
                }
            }
            //设置output
            if (sqlDetailFromConfig.containsKey(OUTPUT)) {
                if (sqlDetailFromConfig.get(OUTPUT) != null){
                    sqlModel.setOutput((String) sqlDetailFromConfig.get(OUTPUT));
                }
            }
        }
        return sqlModel;
    }

    /**
     * 一个functionMapFromConfig中只有一个functionmodel
     * @param functionMapFromConfig
     * @return
     */
    public static FunctionModel getFunctionModel(Map functionMapFromConfig) {
        if (functionMapFromConfig == null) {
            return null;
        }
        FunctionModel functionModel = new FunctionModel();
        for (Object keyName:functionMapFromConfig.keySet()){
            //设置functionName
            String functionName = (String) keyName;
            functionModel.setFunctionName(functionName);

            Map functionDetailFromConfig = (Map) functionMapFromConfig.get(keyName);
            //设置params
            if (functionDetailFromConfig.containsKey(PARAMS)){
                if (functionDetailFromConfig.get(PARAMS) != null){
                    functionModel.setParams((Map) functionDetailFromConfig.get(PARAMS));
                }
            }
            //设置output
            if (functionDetailFromConfig.containsKey(OUTPUT)){
                if (functionDetailFromConfig.get(OUTPUT) != null){
                    functionModel.setOutput((String) functionDetailFromConfig.get(OUTPUT));
                }
            }
        }
        return functionModel;
    }
}
