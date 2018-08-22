//全局变量：json文件中的节点
let projects = "projects";
let modules = "modules";
let apis = "apis";
let cases = "cases";
let name = "name";
let run = "run";
let result = "result";
let total = "total";
let success = "success";
let fail = "fail";


window.onload = function () {
    let data = getJsonString("./data.json");
    let obj = JSON.parse(data);
    //获取项目列表
    let projectList = obj[projects];
    //生成项目级信息
    for (let projectName in projectList){
        let project = projectList[projectName];
        if (project[run] === true) {
            addElement(document.getElementById("project"), "div", projectName,projectName + " total:"+project[total] + " success:" + project[success],true,0);
            //生成相应的存放模块的div
            let moduleParentId = projectName + "." + modules;
            addElement(document.getElementById("module"),"div",moduleParentId,null  ,false,0);
            //获取模块列表
            let moduleList = project[modules];
            //生成模块级信息
            for (let moduleName in moduleList) {
                let module = moduleList[moduleName];
                if (module[run] === true) {
                    let moduleId = projectName + "." + moduleName;
                    addElement(document.getElementById(moduleParentId), "div", moduleId,moduleId + " total:"+module[total] + " success:" + module[success],true,0);
                    //获取api列表
                    let apiList = module[apis];
                    //生成api信息
                    for (let apiName in apiList) {
                        let api = apiList[apiName];
                        if (api[run] === true) {
                            let apiId = moduleName + "." + apiName;
                            addElement(document.getElementById(moduleId), "div", apiId,apiId ,true,1);
                            // style  text-inde
                            //获取case列表
                            let caseList = api[cases];
                            //生成case信息
                            for (let caseName in caseList) {
                                let testCase = caseList[caseName];
                                if (testCase[run] === true) {
                                    let caseId = apiName + "." + caseName;
                                    addElement(document.getElementById(apiId), "div", caseId,caseId + "   " + testCase[result],true,2);
                                    //生成casedetail的div
                                    let detailId = caseId + ".detail";
                                    addElement(document.getElementById("detail"),"div",detailId,detailId + "   " + testCase[result],false,0);
                                    //给detail增加更多信息
                                    addElement(document.getElementById(detailId),"div",detailId+".request","request: " + JSON.stringify(testCase["var"]["request"]),true);
                                    addElement(document.getElementById(detailId),"div",detailId+".response","response: " + JSON.stringify(testCase["var"]["response"]),true);
                                    addElement(document.getElementById(detailId),"div",detailId+".result","result: " + testCase["result"],true);
                                    //给用例绑定点击事件
                                    document.getElementById(caseId).addEventListener("click",function () {
                                        setDisplay("detail",detailId);
                                    },false);
                                }
                            }

                        }
                    }
                }
            }
            //给项目绑定事件，将相应的模块div设置成显示，其他的模块设置成不显示
            document.getElementById(projectName).addEventListener("click",function () {
                setDisplay("module",moduleParentId);
            },false);

        }
    }
}

/**
 * 给指定的元素增加element
 * @param element 指定的元素，父节点
 * @param type 要增加的element的元素的类型
 * @param id 要增加的元素的id,以及innerText
 * @param isDisplay 是否隐藏
 * @param n 缩进几个字符
 */
function addElement(element,type,id,text,isDisplay,n) {
    let number = 2*n + "em";
    let e1 = document.createElement(type);
    e1.setAttribute("id",id);
    e1.innerText = text;
    if (isDisplay === true){
        e1.setAttribute("style","display:block;text-indent:"+number);
    } else {
        e1.setAttribute("style","display:none;text-indent:"+number);
    }
    element.appendChild(e1);
}

/**
 * @param parentName 要控制parentName下的节点
 * @param targetName targetName节点显示，其他隐藏
 */
function setDisplay(parentName,targetName) {
    let parentNode = document.getElementById(parentName);
    //获取所有子节点
    let sonNodeList = parentNode.children;
    for (let e = 0; e < sonNodeList.length; e++) {
        if (sonNodeList[e].nodeName == "DIV"){
            if (sonNodeList[e]["id"] == targetName){
                sonNodeList[e].setAttribute("style","display:block");
            } else {
                sonNodeList[e].setAttribute("style","display:none");
            }
        }
    }
}

function getJsonString(url) {
    let res = "";
    let request = new XMLHttpRequest();
    request.open( "GET", url ,false);
    request.onreadystatechange = function(){
        if( request.readyState !== 4 ) return;
        if( request.status === 200 ){
            res = request.responseText;
        }
    }
    request.send(null);
    return res;
}
