import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseBase;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
import com.autoapi.util.YamlUtil;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class Practice {
    @Test
    public void prac() {
        Map map = YamlUtil.read(
                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projects\\projectA\\moduleA\\获取验证码.yaml");
        System.out.println(map);


    }

    @Test
    public void prac2() throws Exception {
        ParseBase parseBase = new ParseBase(
                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projectA\\api.yaml");
        System.out.println(parseBase.getApis());
    }

    @Test
    public void prac3() throws Exception {
        ParseDirecotory p = new ParseDirecotory();
        List<String[]> l = p.listFiles();
//        for (String[] str : l){
//            for (String s: str){
//                System.out.print(s+"  ");
//            }
//            System.out.println(" ");
//        }
        ParseApiConfig parseApiConfig = new ParseApiConfig(l);
        System.out.println(parseApiConfig.getApiConfig());
    }
}
