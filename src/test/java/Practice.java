import com.autoapi.parse.ParseBase;
import com.autoapi.parse.ParseDirecotory;
import com.autoapi.util.YamlUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Practice {
    @Test
    public void prac() {
        Map map = YamlUtil.read("/Users/chiyang/IdeaProjects/apiautomation/src/main/resources/api.yaml");
        Map map1 = (Map) map.get("base");
        System.out.println(map1.get("url"));


    }

    @Test
    public void prac2() throws Exception {
        ParseBase parseBase = new ParseBase(
                "C:\\Users\\yangchi\\IdeaProjects\\apiautomation\\src\\main\\resources\\projectA\\api.yaml");
        System.out.println(parseBase.getApis());
    }

    @Test
    public void prac3() {
        Map m = new HashMap();
        m.put("a",null);
        m.put("b",null);
        System.out.println(m);
    }
}
