package com.lemon.day06_19.common;

import com.alibaba.fastjson.JSONObject;
import com.lemon.day06_19.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;
import java.util.Set;

public class BaseTest {
    /**
     * 通用的响应断言方法
     *
     * @param asserDates Excel中的断言设计数据（Json格式设计）
     * @param res        接口响应结果
     */
    public void assertResponse(String asserDates, Response res) {
        //判空处理，当从Excel读取的响应数据为空的，表示不需要断言
        if(null!=asserDates){
        // json字符串转成java的Map
            Map<String, Object> map = JSONObject.parseObject(asserDates);
        // 遍历map
        Set<Map.Entry<String, Object>> datas = map.entrySet();
        for (Map.Entry<String, Object> keyValus : datas) {
            String key = keyValus.getKey();
            Object value = keyValus.getValue();
            if ("statuscode".equals(key)) {
                int statuscode = res.getStatusCode();
                System.out.println("断言效验状态码，期望值：" + value + "  实际值：" + statuscode);
                Assert.assertEquals(statuscode, value);
            } else {
                Object actualValue = res.jsonPath().get(key);
                System.out.println("断言响应体字段，期望值：" + value + "  实际值：" + actualValue);
                System.out.println("断言响应体字段，期望值类型：" + value.getClass() + "  实际值类型：" + actualValue.getClass());

                Assert.assertEquals(actualValue, value);
            }
        }

        }
    }


    public void asserDB(String asserDB){
        //把原始的json数据转成Map
        Map<String,Object> map=JSONObject.parseObject(asserDB);
        Set<Map.Entry<String,Object>> datas=map.entrySet();
        for(Map.Entry<String,Object> keyValue:datas){
            //map里面的key就是我们的查询sql语句
            Object actualValue = JDBCUtils.querySingleData(keyValue.getKey());
            //map里面的value就是我们的期望值
            Assert.assertEquals(actualValue.toString(),keyValue.getValue().toString());
        }

    }


}
