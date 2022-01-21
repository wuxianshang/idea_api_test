package com.lemon.day06_19.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment {
    //设计一个Map结构 类型于Postman的环境变量区域
   static public Map<String,Object> envMap=new HashMap<String, Object>();
    /**
     * 向环境变量中存储对应的键值对
     * @param varName
     * @param varValue
     */
   static public void savaToEnvironment(String varName,Object varValue){
       Environment.envMap.put(varName,varValue);
   }

    /**
     * 从环境变量区域中取得对应的值
     * @param varName
     * @return
     */
    static public Object getEnvironment(String varName){
       return Environment.envMap.get(varName);
    }

    /**
     * 字符类型的 参数化替换
     * @param inputParam
     * @return
     */
    public static String replaceParams(String inputParam) {
        String regex = "#(.+?)#";
        //编译得到Pattern模式对象
        Pattern pattern = Pattern.compile(regex);
        //通过pattern的matcher匹配，得到匹配器
        Matcher matcher = pattern.matcher(inputParam);
        //循环在原始的字符串中来找符合正则表达式对应的字符串
        while (matcher.find()) {
            //matcher.group(0)表示整个匹配到的字符串
            String wholeStr = matcher.group(0);

            //matcher.group(1) 分组的第一个结果  #XX# 里的XX
            String subStr = matcher.group(1);
            //替换#XX#
            inputParam = inputParam.replace(wholeStr, Environment.getEnvironment(subStr) + "");
        }
        return inputParam;
    }


    /**
     * 字符类型的 参数化替换
     * @param headersMap
     * @return
     */
    public static Map replaceParams(Map headersMap) {
        //一、把Map转成字符串
        String datas = JSONObject.toJSONString(headersMap);

        String regex = "#(.+?)#";
        //编译得到Pattern模式对象
//        Pattern pattern = Pattern.compile(regex);
//        //通过pattern的matcher匹配，得到匹配器
//        Matcher matcher = pattern.matcher(datas);
//        //循环在原始的字符串中来找符合正则表达式对应的字符串
//        while (matcher.find()) {
//            //matcher.group(0)表示整个匹配到的字符串
//            String wholeStr = matcher.group(0);
//
//            //matcher.group(1) 分组的第一个结果  #XX# 里的XX
//            String subStr = matcher.group(1);
//            //替换#XX#
//            datas = datas.replace(wholeStr, Environment.getEnvironment(subStr) + "");
//        }
        datas=replaceParams(datas);
        //二、 把字符串再转成Map
        Map map = JSONObject.parseObject(datas);
        return map;
    }


    public static void main(String[] args) {
        String inputParam = "{\"basketId\":0,\"count\":1,\"prodId\":\"#prodId#\",\"shopId\":1,\"skuId\":#skuId#}";
        //{"basketId":0,"count":1,"prodId":"#prodId#","shopId":1,"skuId":#skuId#}
        //1、将对应的值保存到环境变量中
        Environment.savaToEnvironment("prodId", 101);
        Environment.savaToEnvironment("skuId", 203);
        //2、replaceParams方法完成替换
        System.out.println("Params方法完成替换:  "+replaceParams(inputParam));
    }


}
