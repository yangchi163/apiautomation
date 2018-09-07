import com.autoapi.domain.ApiRun;
import com.autoapi.domain.asserts.AssertsRun;
import com.autoapi.httprequest.HttpClientUtil;
import com.autoapi.model.UrlModel;
import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import com.autoapi.parse.ParseApiConfig.ParseApi;
import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseBase;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
import com.autoapi.util.CommonUtil;
import com.autoapi.util.YamlUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.beans.binding.ObjectExpression;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.io.File;
import java.net.URL;
import java.text.Format;
import java.util.*;

public class Practice {
    @Test
    public void prac() {
        Map a = new HashMap();
        Map aa = new HashMap();
        Map b = new HashMap();
        Map bb = new HashMap();
        a.put("k","hello");
        aa.put("k","hello");
        aa.put("k2","world");
        b.put("k1","k1");
        bb.put("k1","k1");
        bb.put("k2","k2");
        List l = new ArrayList();
        List ll = new ArrayList();

        l.add(a);
        ll.add(aa);
        l.add(b);
        ll.add(bb);

        AssertsRun assertsRun = new AssertsRun();
        boolean res = assertsRun.compareList(ll,l);
        System.out.println(res);


    }

    @Test
    public void prac2() throws Exception {
        ApiRun apiRun = new ApiRun();
        apiRun.run("projectA","用户","登录");
    }
    @Test
    public void prac3() throws Exception {

    }

}
