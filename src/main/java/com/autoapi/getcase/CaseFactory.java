package com.autoapi.getcase;

import com.autoapi.getcase.yaml.ParseApiConfig.ParseApiConfig;
import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.ApiConfig;

import static com.autoapi.keywords.FileKeyWords.YAML;

public class CaseFactory {
    private static ApiConfig apiConfig;
    /**
     *
     * @param tag 测试用例的标签，"yaml","json","excel"等
     * @return
     * @throws Exception
     */
    public static ApiConfig getCase(String tag) throws Exception {
        if (apiConfig == null){
            synchronized (CaseFactory.class){
                if (apiConfig == null){
                    if (YAML.equals(tag)){
                        ParseApiConfig parseApiConfig = new ParseApiConfig(FileKeyWords.CASEBASEPATH);
                        apiConfig = parseApiConfig.getApiConfig();
                    }
                }
            }
        }
        return apiConfig;
    }

}
