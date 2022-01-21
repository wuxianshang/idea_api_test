package com.lemon.day06_19.test;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MockTest {
    @Test
    public void test_moco(){
        String inputParams = "{\n" +
                "    \"phone\":\"13323234545\",\n" +
                "    \"pwd\":\"123456\"\n" +
                "}";
        Map<String,Object> map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("X-Lemonban-Media-Type","lemonban.v1");
        given().
                log().all().
                headers(map).
                body(inputParams).
        when().
                post("http://127.0.0.1:9999/pay").
        then().
                log().all();
    }
}
