package com.lemon.day06_19.test;

import com.lemon.day06_19.apidefinition.ApiCall;
import com.lemon.day06_19.common.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


public class ProductSearchTest extends BaseTest {
    //搜索冰箱
    @Test
    public void test_ProductSearchTest_sucess() {
        //1、准备测试数据
        String data = "冰箱";
        //2、发起接口请求
        String inputParam="prodName="+data+"&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response res = ApiCall.searcProduct(inputParam);
        //3、断言
        //响应状态码
        int status = res.getStatusCode();
        Assert.assertEquals(status,200);
        // 根据prodName是否包含"冰箱"
        String prodName = res.jsonPath().get("records[0].prodName");
        Assert.assertTrue(prodName.contains(data));

    }
}
