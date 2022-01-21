package com.lemon.day06_19.apidefinition;

import com.lemon.day06_19.common.GlobalConfig;
import com.lemon.day06_19.util.Environment;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCall {

    /**
     * 接口请求通用的方法封装
     *
     * @param method      请求方法（get/post/put/delete...）
     * @param rul         接口请求地址
     * @param headersMap  请求头，存到Map结构中
     * @param inputParams 请求参数
     * @return 接口结果
     */
    public static Response request(String method, String rul, Map headersMap, String inputParams) {
        //把所有的接口日志（请求+响应）重定向到本地指定文件汇总
        //每个接口请求的日志单独的保存到每一个文件中（重定向）
        String logFilePath = null;
        if (!GlobalConfig.IS_DEBUG) {
            PrintStream fileOutPutSteam = null;
            PrintStream fileOutPutStream = null;
            String lofFileDir = "target/log/";
            File file = new File(lofFileDir);
            if (!file.exists()) {
                file.mkdir();
            }
            logFilePath = lofFileDir + "test_" + System.currentTimeMillis() + ".log";
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        //对接口的入参进行替换
        //1、接口入参做参数化替换
        inputParams = Environment.replaceParams(inputParams);
        //2、接口请求头参数化替换
        headersMap = Environment.replaceParams(headersMap);
        //3、接口请求地址参数化替换
        rul = Environment.replaceParams(rul);

        Response res = null;
        //指定项目base url
        RestAssured.baseURI=GlobalConfig.url;
        if ("get".equalsIgnoreCase(method)) {
            res = given().log().all().headers(headersMap).
                    when().get(rul + "?" + inputParams).
                    then().log().all().extract().response();
        } else if ("post".equalsIgnoreCase(method)) {
            res = given().log().all().headers(headersMap).body(inputParams).
                    when().post(rul).
                    then().log().all().extract().response();
        } else if ("put".equalsIgnoreCase(method)) {
            res = given().log().all().headers(headersMap).body(inputParams).
                    when().put(rul).
                    then().log().all().extract().response();
        }
        //添加日志信息到Allure
        if (!GlobalConfig.IS_DEBUG) {
            Allure.addAttachment("接口的请求/相应信息", "附件内容XX");
            try {
                Allure.addAttachment("接口的请求/相应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 登录接口请求定义
     *
     * @param inputParams 传入的接口入参
     * @return {"principal":"waiwai","credentials":"lemon123456","appType":3,"loginType":0}
     */
    @Test
    public static Response login(String inputParams) {
//        Map headMap=new HashMap();
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
        return request("post", "http://mall.lemonban.com:8107/login", headMap, inputParams);
    }

    /**
     * 搜索商品接口请求定义
     *
     * @param inputParams 接口请求入参
     *                    prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12
     * @return 返回响应信息
     */
    @Test
    public static Response searcProduct(String inputParams) {
        Map headMap = new HashMap();
        headMap.put("Content-Type", "application/json");
        return request("get", "http://mall.lemonban.com:8107/search/searchProdPage", headMap, inputParams);
    }

    /**
     * 商品信息接口请求定义
     *
     * @param prodId 商品ID
     * @return 响应结束
     */
    @Test
    public static Response productInfo(int prodId) {
        Map headMap = new HashMap();
        headMap.put("Content-Type", "application/json");
        return request("get", "http://mall.lemonban.com:8107/prod/prodInfo", headMap, "prodId=" + prodId);

    }

    /**
     * 添加购物车接口请求
     *
     * @param inputParams 请求入参
     *                    {"basketId":0,"count":1,"prodId":"99","shopId":1,"skuId":433}
     * @param token       鉴权token值，从登录接口返回
     * @return 相应数据
     */
    @Test
    public static Response addShopCart(String inputParams, String token) {
        Map headMap = new HashMap();
        headMap.put("Content-Type", "application/json");
        headMap.put("Authorization", "bearer" + token);
        return request("post", "http://mall.lemonban.com:8107/p/shopCart/changeItem", headMap, inputParams);
    }


    /**
     * 注册验证码发送接口请求
     *
     * @param inputParams 请求参数
     *                    {"mobile":"13323234543"}
     * @return 响应数据
     */
    public static Response sendRegisterSms(String inputParams) {
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
        return request("put", "http://mall.lemonban.com:8107/user/sendRegisterSms", headMap, inputParams);
    }

    /**
     * 校验注册验证码接口请求
     *
     * @param inputParams 请求入参
     *                    {"mobile":"13323234543","validCode":"815435"}
     * @return
     */
    public static Response checkRegisterSms(String inputParams) {
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
        return request("put", "http://mall.lemonban.com:8107/user/checkRegisterSms", headMap, inputParams);
    }

    /**
     * 注册接口请求
     *
     * @param inputParams 请求入参
     *                    {"appType":3,"checkRegisterSmsFlag":"3cbc5a0c45564c91915388db268d6559",
     *                    "mobile":"13323234543","userName":"lemontester02",
     *                    "password":"123456","registerOrBind":1,"validateType":1}
     * @return
     */
    public static Response register(String inputParams) {
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
        return request("put", "/user/registerOrBindUser", headMap, inputParams);
    }

    /**
     * 确认订单接口定义
     * @param inputParams 接口入参
     * {"addrId":0,"orderItem":{"prodId":412,"skuId":766,"prodCount":1,"shopId":1},"couponIds":[],"isScorePay":0,"userChangeCoupon":0,"userUseScore":0,"uuid":"c3b16d57-6683-4ad2-8bc6-7aeee5e79936"}
     * @param token
     * @return
     */
    public static Response confirmOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/confirm",headMap,inputParams);
    }

    /**
     * 提交订单接口定义
     * @param inputParams 接口入参
     * {"orderShopParam":[{"remarks":"","shopId":1}],"uuid":"c3b16d57-6683-4ad2-8bc6-7aeee5e79936"}
     *                    [{remarks: "", shopId: 1}], uuid: "b66a4503-0684-4b0f-8789-ca038a690137"}
     * @param token
     * @return
     */
    public static Response submitOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/submit",headMap,inputParams);
    }

    /**
     * 支付下单接口定义
     * @param inputParams 接口入参
     * {"payType":3,"orderNumbers":"1481249684885606400"}
     * @param token
     * @return
     */
    public static Response placeOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/pay",headMap,inputParams);
    }

    /**
     * 模拟支付回调的接口
     * @param inputParams 接口入参
     *{
     *    "payNo":1470015941797744640, #商城支付订单号
     *    "bizPayNo":XXXX, #微信方的订单号
     *    "isPaySuccess":true,#true成功，false失败
     * }
     * @param token
     * @return
     */
    public static Response mockPay(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/notice/pay/3",headMap,inputParams);
    }


    /**
     * erp项目的登录请求
     * @param inputParams
     * @return
     */
    public static Response erpLogin(String inputParams){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/x-www-form-urlencoded");
        return request("POST","/user/login ",headMap,inputParams);
    }


    /**
     * 前程贷项目的登录接口请求
     * @param inputParams 请求参数
     * @return
     */
    public static Response futureloanLogin(String inputParams){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("X-Lemonban-Media-Type","lemonban.v3");
        return request("POST","/futureloan/member/login",headMap,inputParams);
    }

    /**
     * 前程贷项目的充值接口请求
     * @param inputParams 接口请求入参
     * {
     *     "member_id": XXX,
     *     "amount": 10000.0,
     *     "timestamp": XXX,
     *     "sign": XXX
     * }
     * @param token
     * @return
     */
    public static Response futureloanRecharge(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("X-Lemonban-Media-Type","lemonban.v3");
        headMap.put("Authorization","Bearer "+token);
        return request("POST","/futureloan/member/recharge",headMap,inputParams);
    }
}

