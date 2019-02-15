import com.autoapi.domain.GenerateXml;
import com.autoapi.domain.GenetateTestCase;
import org.testng.annotations.Test;

public class Practice {
    /**
     * 生成测试用例
     * @throws Exception
     */
    @Test
    public void genetateTestCase() throws Exception {
        GenetateTestCase genetateTestCase = new GenetateTestCase();
        genetateTestCase.generateTestCase();
    }

    /**
     * 生成testng.xml
     * @throws Exception
     */
    @Test
    public void generateXml() throws Exception {
        GenerateXml generateXml = new GenerateXml();
        generateXml.generateXml();

        //生成报告:allure serve allure-results

    }

}
