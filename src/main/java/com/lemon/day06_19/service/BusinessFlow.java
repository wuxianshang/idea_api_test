package com.lemon.day06_19.service;

import com.lemon.day06_19.apidefinition.ApiCall;
import com.lemon.day06_19.util.Environment;
import io.restassured.response.Response;

public class BusinessFlow {
    /**
     * 登录->搜索->商品信息场景组合接口调用
     * @return 商品信息接口的返回数据
     */
    public static Response login_search_info(){
        //1、登录接口
        String jsonData="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        Response loginRes = ApiCall.login(jsonData);

        //提取token
        String token = loginRes.jsonPath().get("access_token");
        //保存到环境变量中
        Environment.savaToEnvironment("token",token);

        //2、搜索商品
        String searchData="prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response searchRes = ApiCall.searcProduct(searchData);
        int prodId = searchRes.jsonPath().get("records[0].prodId");
        Environment.savaToEnvironment("prodId",prodId);

        //3、商品信息
        Response infoRes = ApiCall.productInfo(prodId);
        return infoRes;



    }
}
