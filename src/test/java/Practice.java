import com.autoapi.parse.ParseApiConfig.ParseApi;
import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseBase;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
import com.autoapi.util.CommonUtil;
import com.autoapi.util.YamlUtil;
import javafx.beans.binding.ObjectExpression;
import org.testng.annotations.Test;

import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Practice {
    @Test
    public void prac() {
//        Map map = YamlUtil.read(
//                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projects\\projectA\\moduleA\\获取验证码.yaml");
//        System.out.println(map);
        ParseDirecotory parseDirecotory = new ParseDirecotory();
        List<String[]> l = parseDirecotory.getCasePath();
        for (String[] s : l ){
            for (String v : s){
                System.out.print(v + "    ");
            }
            System.out.println("");
        }
    }

    @Test
    public void prac2() throws Exception {
//        ParseBase parseBase = new ParseBase(
//                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projects\\projectA\\api.yaml");
//        System.out.println(parseBase.getApiBaseModel());
//        ParseApi parseApi = new ParseApi("projectA","moduleA","获取验证码.yaml");


    }

    @Test
    public void prac4() throws Exception {
        ParseDirecotory parseDirecotory = new ParseDirecotory();
        List<String[]> l = parseDirecotory.getCasePath();
        ParseApiConfig parseApiConfig = new ParseApiConfig(l);
        System.out.println(parseApiConfig.getApiConfig());
    }
    @Test
    public void prac3() throws Exception {
        Map p = new HashMap();
        Map d = new HashMap();
        p.put("dog",d);
        d.put("name","xiaoqiang");
        d.put("age",10);
        p.put("name","zhangsna");
        System.out.println(p);
        Map p2 = new HashMap();
        Map d2 = new HashMap();
        d2.put("age",5);
        d2.put("sex","female");
        p2.put("dog",d2);
        p2.put("sex","male");
        p2.put("name","lisi");
        System.out.println(p2);
        Map res = CommonUtil.mergeMap(p,p2);
        System.out.println(res);
    }
}
