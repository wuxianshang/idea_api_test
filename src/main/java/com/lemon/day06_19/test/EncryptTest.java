package com.lemon.day06_19.test;

import com.lemon.day06_19.apidefinition.ApiCall;
import com.lemon.day06_19.util.Environment;
import com.lemon.encryption.MD5Util;
import com.lemon.encryption.RSAManager;
import io.restassured.response.Response;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;
import org.testng.annotations.Test;


public class EncryptTest {

    @Test
    public void test_md5(){
        String data="123456";
        //找开发沟通加密方式，要加密jar
        String encryptdata = MD5Util.stringMD5(data);
        ApiCall.erpLogin("loginame=admin&password="+encryptdata);
    }

    @Test
    public void test_rsa(){
        //前程贷该项目没有在登录接口限制加密处理，而是其他的接口
        String data01="{\n" +
                "    \"mobile_phone\": \"13329334510\",\n" +
                "    \"pwd\": \"12345678\"\n" +
                "}";
        Response res=ApiCall.futureloanLogin(data01);
        String token = res.jsonPath().get("data.token_info.token");
        int memberId=res.jsonPath().get("data.id");
        //充值接口请求
        String sign=getsign(token);
        long timestamp = System.currentTimeMillis() / 10000;
        Environment.savaToEnvironment("member_id",memberId);
        Environment.savaToEnvironment("timestamp",timestamp);
        Environment.savaToEnvironment("sign",sign);

        String data02="{\n" +
                "    \"member_id\": #member_id#,\n" +
                "    \"amount\": 10000.0,\n" +
                "    \"timestamp\": #timestamp#,\n" +
                "    \"sign\": \"#sign#\"\n" +
                "}";
        ApiCall.futureloanRecharge(data02,token);

    }


    public static String getsign(String token) {
        //获取秒级时间戳
        long timestamp = System.currentTimeMillis() / 10000;
//        String token="eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjgxMDUyOCwiZXhwIjoxNjQyNDkwNzA4fQ.nzq4DTkyWTv4XH2l0f1h-MxQLHFVFdUhunFFH08J3cfGloXptWx3TYpfDV6X3PWuI9II93-p8Esq1flWP6d1vA";
        //取token的前面50位
        String subStr = token.substring(0, 50);
        //拼接时间戳
        String newStr=subStr+timestamp;
        //使用导入的加密jar来进行rsa加密
        String singn=null;
        try {
             singn=RSAManager.encryptWithBase64(newStr);
            System.out.println(RSAManager.encryptWithBase64(newStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return singn;

    }

}
