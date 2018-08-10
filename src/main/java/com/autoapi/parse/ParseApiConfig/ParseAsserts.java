package com.autoapi.parse.ParseApiConfig;

import com.autoapi.model.asserts.AssertModel;
import com.autoapi.model.SqlModel;
import com.autoapi.model.asserts.AssertNodeModel;
import com.autoapi.model.asserts.ResponseModel;
import com.autoapi.parse.ParseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

public class ParseAsserts {
    private List<AssertModel> asserts;

    public List<AssertModel> getAsserts(Map caseDetailFromConfig) {
        asserts = parseAsserts(caseDetailFromConfig);
        return asserts;
    }

    public void setAsserts(List<AssertModel> asserts) {
        this.asserts = asserts;
    }

    /**
     *获得assert的list
     * @param caseDetailFromConfig case的详细信息
     * @return
     */
    private List parseAsserts(Map caseDetailFromConfig){
        if (caseDetailFromConfig == null){
            return null;
        }
        List res = new ArrayList();
        if (caseDetailFromConfig.containsKey(ASSERTS)){
            List resFromConfig = (List) caseDetailFromConfig.get(ASSERTS);
            for (Object o:resFromConfig){
                //o是一个assertmodel，即一个验证点，包含一组expect-actual
                AssertModel assertModel = new AssertModel();
                Map m = (Map) o;
                if (m.containsKey(EXPECT)){
                    Map expect = (Map) m.get(EXPECT);
                    assertModel.setExpect(getAssertNodeModel(expect));
                }
                if (m.containsKey(ACTUAL)){
                    Map actual = (Map) m.get(ACTUAL);
                    assertModel.setActual(getAssertNodeModel(actual));
                }
                res.add(assertModel);
            }
        }
        return res;
    }

    /**
     * expect或actual:只能有json,response,sql中的一个
     * expexct:  (不包含expect:只包含下面的节点)
     *     json:
     *          k: v
     *     response: $.body.data
     *     sql:
     *          sqlname:
     * @param assertNodeDetailFromConfig 一个节点
     * @return
     */
    private AssertNodeModel getAssertNodeModel(Map assertNodeDetailFromConfig){
        if (assertNodeDetailFromConfig == null){
            return null;
        }
        AssertNodeModel assertNodeModel = new AssertNodeModel();
        if (assertNodeDetailFromConfig.containsKey(JSON)){
            Map j = (Map) assertNodeDetailFromConfig.get(JSON);
            assertNodeModel.setAssertNode(j);
        }else if(assertNodeDetailFromConfig.containsKey(RESPONSE)){
            ResponseModel responseModel = new ResponseModel();
            responseModel.setPath((String) assertNodeDetailFromConfig.get(RESPONSE));
            assertNodeModel.setAssertNode(responseModel);
        }else if (assertNodeDetailFromConfig.containsKey(SQL)){
            Map s = (Map) assertNodeDetailFromConfig.get(SQL);
            SqlModel sqlModel = ParseUtil.getSqlModel(s);
            assertNodeModel.setAssertNode(sqlModel);
        }else {
            //暂时不存在的情形
        }
        return assertNodeModel;
    }


}
