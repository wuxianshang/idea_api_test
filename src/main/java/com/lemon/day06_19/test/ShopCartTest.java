package com.lemon.day06_19.test;

import com.lemon.day06_19.apidefinition.ApiCall;
import com.lemon.day06_19.pojo.CaseData;
import com.lemon.day06_19.service.BusinessFlow;
import com.lemon.day06_19.util.Environment;
import com.lemon.day06_19.common.BaseTest;
import com.lemon.day06_19.util.ExcelUtil;
import com.lemon.day06_19.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ShopCartTest extends BaseTest {

    //添加购物车用例
    @Test
    public void test_add_shopcart_sucess() {
        Response infoRes = BusinessFlow.login_search_info();
        //提起skuId
        int skuId = infoRes.jsonPath().get("skuList[0].skuId");
        Environment.savaToEnvironment("skuId",skuId);

        //2-4、添加购物车
        String shopCartData = "{\"basketId\":0,\"count\":1,\"prodId\":\"#prodId#\",\"shopId\":1,\"skuId\":#skuId#}";

        Response shopCartRes = ApiCall.addShopCart(shopCartData, "#token#");

        //3、断言
        Assert.assertEquals(shopCartRes.getStatusCode(), 200);
        //4、数据库断言
        String assertSql="SELECT COUNT(*) from tz_basket where user_id = (SELECT user_id FROM tz_user WHERE user_name='lemontester');";
        //根据购物车记录条数
        Assert.assertEquals((long) JDBCUtils.querySingleData(assertSql),1);
    }

    @Test(dataProvider = "getShopCartDatas")
    public void test_shopcart(CaseData caseData){
        //测试数据
        //1、token
        Response loginRes = ApiCall.login("{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}");
        String token = loginRes.jsonPath().get("access_token");
        String inputParams = caseData.getInputParams();
        Response addShopCartRes = ApiCall.addShopCart(inputParams,token);
        //断言
        assertResponse(caseData.getAssertResponse(),addShopCartRes);
        //数据库断言
//        assertResponse(caseData.getAssertResponse(),addShopCartRes);
        asserDB(caseData.getAsserDB());
    }

    @DataProvider
    public Object[] getShopCartDatas(){
        return ExcelUtil.readExcel(2).toArray();
    }


}
