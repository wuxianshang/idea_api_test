package com.lemon.day06_19.test;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.day06_19.apidefinition.ApiCall;
import com.lemon.day06_19.common.BaseTest;
import com.lemon.day06_19.pojo.CaseData;
import com.lemon.day06_19.util.ExcelUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class LoginTest extends BaseTest {

    //登录接口
    @Test
    public void test_login_sucess() {
        //1、准备测试数据
        String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //2、直接调用登录的接口请求
        Response res = ApiCall.login(jsonData);

        int statusCode = res.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String nickName = res.jsonPath().get("nickName");
        Assert.assertEquals(nickName, "waiwai");
    }

//    @DataProvider
//    public Object[] getLoginDatas(){
//        Object[] data={"{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}",
//                "{\"principal\":\"\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}",
//                "{\"principal\":\"waiwai\",\"credentials\":\"\",\"appType\":3,\"loginType\":0}",
//                "{\"principal\":\"lemon111\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}"
//                };
//        return data;
//    }

//    @Test(dataProvider = "getLoginDatas")
//    public void test_login_seccess(String caseData){
//        Response res= ApiCall.login(caseData);
//    }




    @Test(dataProvider = "getLoginDatasFromExcel")
    public void test_login_from_excel(CaseData caseData){
        Response res= ApiCall.login(caseData.getInputParams());
        //断言
        String asserDates=caseData.getAssertResponse();
        assertResponse(asserDates,res);
}


    @DataProvider
    public Object[] getLoginDatasFromExcel(){
//       //读取Excel，Easypoi
//        ImportParams importParams=new ImportParams();
//        importParams.setStartSheetIndex(0);
//        //读取的文件路径 src/main/resources/testData.xlsx
//        List<Object> datas = ExcelImportUtil.importExcel(new File("src/main/resources/testData.xlsx"), CaseData.class, importParams);
//        //集合转一维数组
        return ExcelUtil.readExcel(0).toArray();
    }
}
