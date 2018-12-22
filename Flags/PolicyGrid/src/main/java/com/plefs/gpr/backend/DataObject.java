package com.plefs.gpr.backend;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**********************************************************************************************
 * Description:This method will store the data of excel. Created on : 01/29/2018
 *********************************************************************************************/

public class DataObject {
	public String key;
	public static HashMap<String, LinkedHashMap<String, String>> ExcelObject = new LinkedHashMap<String, LinkedHashMap<String, String>>();

	public static HashMap<String, LinkedHashMap<String, String>> getExcelObject() {
		return ExcelObject;
	}

	public void setExcelObject(HashMap<String, LinkedHashMap<String, String>> excelObject) {
		ExcelObject = excelObject;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static String getVariable(String ColumnName, String testcase) {

		String value = DataObject.getExcelObject().get(testcase).get(ColumnName).toString();
		return value;
	}
	

	
	
	
	
}
