package com.lemon.day06_19.util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.day06_19.pojo.CaseData;

import java.io.File;
import java.util.List;

public class ExcelUtil {
    public static final String EXCEL_FILE_PATH="src\\main\\resources\\caseData02.xlsx";

    /**
     * 读取外部Excel文件中的数据
     * @param sheetNum Sheet的编号
     */
    public static List<CaseData> readExcel(int sheetNum){
        ImportParams importParams=new ImportParams();
        importParams.setStartSheetIndex(sheetNum);
        //读取的文件路径 src/main/resources/testData.xlsx
        List<CaseData> datas = ExcelImportUtil.importExcel(new File(EXCEL_FILE_PATH),
                CaseData.class, importParams);
            return datas;
    }

}
