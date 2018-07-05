import com.autoapi.parse.ParseBase;
import com.autoapi.util.YamlUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
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
        ParseBase parseBase = new ParseBase("/Users/chiyang/IdeaProjects/apiautomation/src/main/resources/projectA/api.yaml");
        parseBase.getApis();
    }
}
