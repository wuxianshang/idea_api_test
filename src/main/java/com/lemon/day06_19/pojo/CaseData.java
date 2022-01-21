package com.lemon.day06_19.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class CaseData {
    @Excel(name = "接口入参")
    private String inputParams;

    @Excel(name = "用例编号")
    private int caseId;

    @Excel(name = "用例标题")
    private String castTitle;

    @Excel(name = "响应断言")
    private String assertResponse;

    @Excel(name="数据库断言")
    private String asserDB;

    public String getAssertResponse() {
        return assertResponse;
    }

    public void setAssertResponse(String assertResponse) {
        this.assertResponse = assertResponse;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getCastTitle() {
        return castTitle;
    }

    public void setCastTitle(String castTitle) {
        this.castTitle = castTitle;
    }

    public String getAsserDB() {
        return asserDB;
    }

    public void setAsserDB(String asserDB) {
        this.asserDB = asserDB;
    }

    public CaseData() {
    }

    public CaseData(String inputParams, int caseId, String castTitle, String assertResponse, String asserDB) {
        this.inputParams = inputParams;
        this.caseId = caseId;
        this.castTitle = castTitle;
        this.assertResponse = assertResponse;
        this.asserDB = asserDB;
    }

    @Override
    public String toString() {
        return "CaseData{" +
                "inputParams='" + inputParams + '\'' +
                ", caseId=" + caseId +
                ", castTitle='" + castTitle + '\'' +
                ", assertResponse='" + assertResponse + '\'' +
                ", asserDB='" + asserDB + '\'' +
                '}';
    }
}
