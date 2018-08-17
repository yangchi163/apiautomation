package com.autoapi.domain.asserts;

import com.autoapi.domain.RunUtil;
import com.autoapi.model.ApiConfig;
import com.autoapi.model.BaseModel;
import com.autoapi.model.SqlModel;
import com.autoapi.model.asserts.AssertModel;
import com.autoapi.model.asserts.AssertNodeModel;
import com.autoapi.model.asserts.ResponseModel;
import com.autoapi.model.http.HttpClientResponse;
import com.autoapi.util.CommonUtil;
import com.google.gson.Gson;

import static com.autoapi.keywords.RequestKeyWords.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class AssertsRun {

    /**
     * 执行casemodel整个asserts模块
     * @param assertModels
     * @param baseModel
     * @param inputStream
     * @return
     */
    public boolean runAsserts(List<AssertModel> assertModels,BaseModel baseModel,InputStream inputStream,
                              ApiConfig apiConfig,String[] varpath){
        if (assertModels == null){
            return true;
        }
        for (AssertModel assertModel:assertModels){
            Object actual = getAssertNodeResult(assertModel.getActual(),baseModel,inputStream,apiConfig,varpath);
            Object expect = getAssertNodeResult(assertModel.getExpect(),baseModel,inputStream,apiConfig,varpath);
            if(compare(actual,expect) == false){
                return false;
            }
        }
        return true;
    }

    /**
     * 执行assert部分，并将最终结果给到assertnode
     * @param assertNodeModel
     * @param baseModel
     * @param inputStream
     * @return
     */
    private Object getAssertNodeResult(AssertNodeModel assertNodeModel, BaseModel baseModel, InputStream inputStream,
                                       ApiConfig apiConfig, String[] varpath){
        Object res = null;
        Object assertNode = assertNodeModel.getAssertNode();
        if (assertNode instanceof Map){
            res = assertNode;
        }
        if (assertNode instanceof ResponseModel){
            res = getResponseNode((HttpClientResponse) baseModel.getVar().get(RESPONSE),(ResponseModel) assertNode);
        }
        if (assertNode instanceof SqlModel){
            RunUtil runUtil = new RunUtil(apiConfig);
            res = runUtil.runSql(inputStream, (SqlModel) assertNode,baseModel,varpath);
        }
        return res;
    }

    /**
     * 解析responseModel:从response中，根据jsonpath获取对应值
     * @param httpClientResponse
     * @param responseModel
     * @return
     */
    private Object getResponseNode(HttpClientResponse httpClientResponse,ResponseModel responseModel){
        String path = responseModel.getPath();
        Gson gson = new Gson();
        String jsonString = gson.toJson(httpClientResponse);
        return CommonUtil.getFromJsonByjsonPath(jsonString,path);
    }

    /**
     * 比较2个对象是否相同
     * @param actual 实际结果
     * @param expect 预期结果
     * @return
     */
    private boolean compare(Object actual,Object expect){
        //2个都是string
        if (actual instanceof String && expect instanceof String){
            return actual.equals(expect);
        }
        //2个都是map,expect中的key-value在actual中都有，但是actual中的值可能多于expect
        //expect中的值不能是对象
        if (actual instanceof Map && expect instanceof Map){
            return compareMap((Map) actual,(Map) expect);
        }
        //2个都是list,1.验证数量要相等；2.list中的元素是对象 3.ecpect对象的属性可以少于actual,但是必须存在
        if (actual instanceof List && expect instanceof List){
            return compareList((List<Map>) actual,(List<Map>) expect);
        }
        return false;
    }

    /**
     * 验证expect中的 k-v 在actual中都存在（expect内容较少）
     * @param actual
     * @param expect
     * @return
     */
    public boolean compareMap(Map actual,Map expect){
        for (Object keyExpect:expect.keySet()){
            if (!actual.containsKey(keyExpect)){
                return false;
            }
            if (!actual.get(keyExpect).equals(expect.get(keyExpect))){
                return false;
            }
        }
        return true;
    }

    /**
     * 2个都是list<map>：
     * 1.验证数量要相等
     * 2.list中的元素是对象
     * 3.ecpect对象的属性可以少于actual,但是必须存在；
     * 4.map中都是普通的 k-v,v不能是对象
     * 5.map中的对象需要能唯一确定一条记录
     * @param actual
     * @param expect
     * @return
     */
    public boolean compareList(List<Map> actual,List<Map> expect){
        //验证size
        if (actual.size() != expect.size()){
            return false;
        }
        //验证expect中的map在actual中都存在
        for (Map mapExpect:expect){
            boolean flag = false;
            for (Map mapActual:actual){
                //一旦找到匹配的map,将mapExpect从expect中去除，将flag变成true,跳出本次循环
                if (compareMap(mapActual,mapExpect) == true){
                    actual.remove(mapActual);
                    flag = true;
                    break;
                }
            }
            //一轮循环结束后,flag为false表明在actual中找不到对应的map,直接2个list判断不一致
            if (flag == false){
                return flag;
            }
        }
        return true;
    }
}
