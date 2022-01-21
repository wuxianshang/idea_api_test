package com.lemon.day06_19.test;

import com.lemon.day06_19.apidefinition.ApiCall;
import com.lemon.day06_19.common.BaseTest;
import com.lemon.day06_19.util.Environment;
import com.lemon.day06_19.util.JDBCUtils;
import com.lemon.day06_19.util.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    @Test
    public void test_register_success() {
        String randomPhone = RandomDataUtil.getUnregisterPhone();
        String randomName = RandomDataUtil.getUnregisterPhone();
        Environment.savaToEnvironment("randomPhone", randomPhone);
        Environment.savaToEnvironment("randomName", randomName);

        //1、发起验证码接口
        String data01 = "{\"mobile\":\"#randomPhone#\"}";
        ApiCall.sendRegisterSms(data01);

        //2、校验验证码接口
        //关键问题：验证码该怎么获取？？？查询数据库表tz_sms_log-->Java代码查询数据库
        String sql = "SELECT mobile_code from tz_sms_log where id = (SELECT MAX(id) FROM tz_sms_log);";
        String code = (String) JDBCUtils.querySingleData(sql);
        //将验证码保存到环境变量中
        Environment.savaToEnvironment("code", code);
        String data02 = "{\"mobile\":\"#randomPhone#\",\"validCode\":\"#code#\"}";
        Response checkRes = ApiCall.checkRegisterSms(data02);
        //拿到接口响应纯文本类型的数据
        String checkSms = checkRes.body().asString();
        //将验证码校验字符串保存到环境变量中
        Environment.savaToEnvironment("checkSms", checkSms);

        //3、注册接口请求
        String data03 = "{\"appType\":3,\"checkRegisterSmsFlag\":\"#checkSms#\"," +
                "\"mobile\":\"#randomPhone#\",\"userName\":\"#randomName#\"," +
                "\"password\":\"123456\",\"registerOrBind\":1,\"validateType\":1}";
        Response registerRes = ApiCall.register(data03);

        //4、响应断言
        Assert.assertEquals(registerRes.getStatusCode(), 200);
        Assert.assertEquals(registerRes.jsonPath().get("nickName"), randomName);

        //5、数据库断言
        String assertSql = "SELECT COUNT(*) FROM tz_user WHERE user_mobile='#randomPhone#';";
        long actual = (long) JDBCUtils.querySingleData(assertSql);
        Assert.assertEquals(actual, 1);


    }
}
