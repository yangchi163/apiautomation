import com.autoapi.domain.ApiRun;
import com.autoapi.domain.GenetateTestCase;
import com.autoapi.domain.asserts.AssertsRun;
import com.autoapi.genetator.CodeGenerator;
import com.autoapi.genetator.element.*;
import org.testng.annotations.*;

import java.io.File;
import java.util.*;

public class Practice {
    @Test
    public void prac() {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.setPackageName("com.yc.test");
        codeGenerator.setClassName("TestCode");
        codeGenerator.setSuperClassName("YamlUtil");
        String imports = "java.util.List";
        codeGenerator.addRelyOn(imports);
        ElementField f1 = new ElementField();
        f1.setPermission(ElementPermission.PUBLIC);
        f1.setDataType(DataType.STRING);
        f1.setName("fieldName");
        codeGenerator.addField(f1);
        ElementMethod m1 = new ElementMethod();
        m1.setPermission(ElementPermission.PUBLIC);
        m1.setReturnType(DataType.VOID);
        m1.setName("say");
        String body = "\t\tSystem.out.println();";
        m1.setBody(body);
        ElementAnnotation annotation = new ElementAnnotation();
        annotation.setName("Test");
        m1.addAnnotation(annotation);
        codeGenerator.addMethod(m1);
        codeGenerator.generateFile();
    }

    @Test
    public void prac2() throws Exception {

        GenetateTestCase genetateTestCase = new GenetateTestCase();
        genetateTestCase.generateTestCase();


    }


}
