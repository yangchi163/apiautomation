import com.autoapi.util.YamlUtil;
import org.testng.annotations.Test;

import java.util.Map;

public class Practice {
    @Test
    public void prac() {
        Map map = YamlUtil.read("/Users/chiyang/IdeaProjects/apiautomation/src/main/resources/api.yaml");
        System.out.println(map);

    }
}
