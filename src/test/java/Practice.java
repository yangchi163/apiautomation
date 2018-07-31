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
import javafx.beans.binding.ObjectExpression;
import org.testng.annotations.Test;

import java.net.URL;
import java.text.Format;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Practice {
    @Test
    public void prac() {
        Map map = YamlUtil.read(
                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projects\\projectA\\project.yaml");
        System.out.println(map);
        Map map2 = (Map) map.get("var");
        System.out.println(map2.get("a").getClass());




    }
    @Test
    public void prac2() throws Exception {
        ParseBase parseBase = new ParseBase(
                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projects\\projectA\\用户\\登录.yaml");
        System.out.println(parseBase.getApiBaseModel());
        ParseApi parseApi = new ParseApi("projectA","moduleA","获取验证码.yaml");


    }

    @Test
    public void prac4() throws Exception {
        ParseDirecotory parseDirecotory = new ParseDirecotory();
        List<String[]> l = parseDirecotory.getCasePath();
//        System.out.println(l);
//        for (String[] s : l){
//            for (String v : s){
//                System.out.print(v+ "        ");
//            }
//            System.out.println("****");
//        }
        ParseApiConfig parseApiConfig = new ParseApiConfig(l);
        System.out.println(parseApiConfig.getApiConfig());
    }
    @Test
    public void prac3() throws Exception {
        ApiRun apiRun = new ApiRun();
        apiRun.run("projectA","用户","登录","合法手机号");
//        UrlModel urlModel = new UrlModel();
//        urlModel.setSchema("http");
//        urlModel.setHost("www.baidu.com");
//        urlModel.setPort(1234);
//        urlModel.setVersion("v1");
//        urlModel.setPath("login/password");
//        Map map = new HashMap();
//        map.put("name","xiaoqiang");
//        map.put("age","9");
//        urlModel.setParams(map);
//        System.out.println(new ApiRun().getUrl(urlModel));
    }

    @Test
    public void prac5() throws Exception {
        HttpClientUtil clientUtil = new HttpClientUtil();
        HttpClientRequest request = new HttpClientRequest();
        Map headers = new HashMap();
        headers.put("Content-Type","application/json");
        Map body = new HashMap();
        body.put("accountName","16666666662");
        body.put("password","qqq123");
        request.setUrl("http://t.jufandev.com:33084/mp/user/login/password");
        request.setHeaders(headers);
        request.setBody(body);
        request.setMethod("posT");
        HttpClientResponse response = clientUtil.doRequest(request);
        System.out.println(request);
        System.out.println(response);
    }

    @Test
    public void prac6() throws Exception {
        String s = "abd{ss}{ff}";
        System.out.println(CommonUtil.getFirstString(s));
    }

}
