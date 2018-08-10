import com.autoapi.domain.ApiRun;
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
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.beans.binding.ObjectExpression;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.net.URL;
import java.text.Format;
import java.util.*;

public class Practice {
    @Test
    public void prac() {
        String s = "ab${c}${e}f";
        String res = CommonUtil.getFirstString(s,0);
        System.out.println(res);
        System.out.println(s.indexOf(res));
        String ress = CommonUtil.getFirstString(s,s.indexOf(res)+res.length());
        System.out.println(ress);


    }

    @Test
    public void prac2() throws Exception {
        ApiRun apiRun = new ApiRun();
        apiRun.run("projectA","用户","登录");
    }

}
