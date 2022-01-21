package com.lemon.self_testing;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class A_easy {

    @Test
    public void loginCase() {
        given().
                header("Content-Type", "application/json; charset=utf-8").
                body("{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}").
        when().
                post("http://mall.lemonban.com:8107/login").
        then().
                log().all();
    }


    @Test
    public void searchTest(){
        given().
                header("Content-Type", "application/json; charset=utf-8").
                body("prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12").
                body("\"access_token\": \"d9f59ed1-af58-49f0-9389-edc8161afa29\",\n" +
                        "    \"token_type\": \"bearer\",").
        when().
                get("http://mall.lemonban.com:8107/search/searchProdPage").
        then().
                log().all();
    }


    @Test
    public void productInfoTest(){
        given().
                header("Content-Type", "application/json; charset=utf-8").
        when().
                get("http://mall.lemonban.com:8107/prod/prodInfo?prodId=134").
        then().
                log().all();
    }


    @Test
    public void addCartTest(){
        given().
                header("Content-Type", "application/json; charset=utf-8").

                body("{\"basketId\":0,\"count\":1,\"prodId\":\"134\",\"shopId\":1,\"skuId\":466}").
        when().
                get("http://mall.lemonban.com:8107/p/shopCart/changeItem").
        then().
              log().all();
    }

}
