package com.plefs.gpr.scripts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.plefs.gpr.backend.Application;
import com.plefs.gpr.backend.Backend;
import com.plefs.gpr.backend.BrowserInitialization;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

public class US_245178 {
	Logger logger = Logger.getLogger(US_245178.class);
	static String dbUrl = "jdbc:oracle:thin:@ldap://oid.inf.fedex.com:3060/PLEFS_AB_RO_SVC1_L3,cn=OracleContext,dc=ute,dc=fedex,dc=com";
	static String username = "PLEFS_AB_RO_APP";
	static String password = "Apwd4abinit";

	/**********************************************************************************************
	 * Description: Created on :
	 *********************************************************************************************/
	@Test
	public void TC_01() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_01";
		String result = null;
		boolean status = true;
		logger.info("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_Lasttimecomparetblrecord, 120);
				Thread.sleep(80000);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_successmsg, 20);
				Thread.sleep(80000);
				String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
				Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
				Thread.sleep(4000);
				Application.findButtonAndClick(XpathRepository.by_newcreatedextract);
				Application.findButtonAndClick(XpathRepository.by_4218extract);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_CompareSelectedButton);
				Thread.sleep(40000);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_LastCheckbox, 30);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(80000);
				int NewPolicyOfferingidcount = BrowserInitialization.driver.findElements(By.xpath("//policy-report-view//table//td[7]")).size();
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = con.createStatement();
				for (int i = 0; i < NewPolicyOfferingidcount; i++) {
					int j = i + 1;
					String value = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[7])[" + j + "]")).getText();
					String GuiValue = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[8])[" + j + "]")).getText();
					// String NewPolicyID=NewPolicyOfferingid.get(0).getText();
					// logger.info(+i+ "before:"+value);
					if (!value.equals(null) && (value.length() == 12)) {
						// logger.info(value);
						String Str_QueryGroupName = "select om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om where o.OFFERING_TYPE_CD = 'BS'   and o.LFCL_STATUS_CD = 'AC' and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate)) and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD   and om.LFCL_STATUS_CD = 'AC' and om.INCL_ORIG_CNTRY_CD is null and o.ENTERPRISE_PRODUCT_ID ='"
								+ value
								+ "' and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD in ('epic', 'o2e') and CASE WHEN om.ALTERNATE_DESC = '03' THEN CASE WHEN (om.INCL_ORIG_CNTRY_CD = om.INCL_DEST_CNTRY_CD and o.DOM_INTL_CD = 'D' and o.ENTERPRISE_PRODUCT_ID = 'EP1000000003') THEN 1 WHEN (om.INCL_ORIG_CNTRY_CD in ('CA', 'PR', 'US') and om.INCL_ORIG_CNTRY_CD <> om.INCL_DEST_CNTRY_CD and o.DOM_INTL_CD = 'I' and o.ENTERPRISE_PRODUCT_ID = 'EP1000000004') THEN 1 END WHEN om.ALTERNATE_DESC = '80' THEN CASE WHEN (o.DOM_INTL_CD = 'D' and o.ENTERPRISE_PRODUCT_ID = 'EP1000000027') THEN 1 WHEN (o.DOM_INTL_CD = 'I') THEN 0 END WHEN (om.ALTERNATE_DESC <> '03' and om.ALTERNATE_DESC <> '80' and o.OFFERING_SEQ_NBR = om.OFFERING_NBR) THEN 1END = 1 order by o.ORGANIZATION_CD asc, o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
						ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
						while (rs1.next()) {
							result = rs1.getString(1);
							// logger.info(result);
							if (result.equals(GuiValue)) {
								// logger.info("Passed for record:" +
								// value);
							} else {
								status = false;
							}
						}
					}
				}
				if (status == true) {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
					Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
					Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("PASS", testCaseName);
					// logger.info("Test Case Passed");
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
					// logger.info("Test Case Failed");
				}
			}

			else {
				try {
					throw new SkipException(testCaseName + " is Skipped");
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Created on :
	 *********************************************************************************************/
	@Test
	public void TC_02() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_02";
		String result = null;
		boolean status = true;
		logger.info("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 120);
				/*
				 * String Policygrid = DataObject.getVariable("DropDownMenu",
				 * testCaseName); Select policygrid_dropdown = new
				 * Select(BrowserInitialization.driver.findElement(
				 * XpathRepository.by_dropdownPG));
				 * policygrid_dropdown.selectByVisibleText(Policygrid);
				 * BrowserInitialization.driver.findElement(XpathRepository.
				 * by_extractbtn).click();
				 * //BackendFunctionality.waitForElement(BrowserInitialization.
				 * driver, XpathRepository.by_successmsg, 20);
				 * Thread.sleep(20000); String LimitCount =
				 * BrowserInitialization.driver.findElement(XpathRepository.
				 * by_records).getText();
				 * BrowserInitialization.driver.findElement(XpathRepository.
				 * by_ExtractLimittextbox).clear();
				 * BrowserInitialization.driver.findElement(XpathRepository.
				 * by_ExtractLimittextbox).sendKeys(LimitCount);
				 * ApplicationFunctionality.findButtonAndClick(XpathRepository.
				 * by_ExtractRefreshbutton); Thread.sleep(6000);
				 * ApplicationFunctionality.findButtonAndClick(XpathRepository.
				 * by_newcreatedextract);
				 * ApplicationFunctionality.findButtonAndClick(XpathRepository.
				 * by_4218extract);
				 * ApplicationFunctionality.findButtonAndClick(XpathRepository.
				 * by_CompareSelectedButton);
				 */
				Thread.sleep(20000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_LastCheckbox, 30);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				Application.findButtonAndClick(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(50000);
				int OldPolicyOfferingidcount = BrowserInitialization.driver.findElements(By.xpath("//policy-report-view//table//td[8]")).size();
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = con.createStatement();
				for (int i = 0; i < OldPolicyOfferingidcount; i++) {
					int j = i + 1;
					String value = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[9])[" + j + "]")).getText();
					String GuiValue = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[10])[" + j + "]")).getText();
					// String NewPolicyID=NewPolicyOfferingid.get(0).getText();
					// logger.info(+i+ "begore:"+value);
					if (!value.equals(null) && (value.length() == 12)) {
						// logger.info(value);
						String Str_QueryGroupName = "select om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om where o.OFFERING_TYPE_CD = 'BS'   and o.LFCL_STATUS_CD = 'AC' and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate)) and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD   and om.LFCL_STATUS_CD = 'AC' and om.INCL_ORIG_CNTRY_CD is null and o.ENTERPRISE_PRODUCT_ID ='"
								+ value
								+ "' and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD in ('epic', 'o2e') and CASE WHEN om.ALTERNATE_DESC = '03' THEN CASE WHEN (om.INCL_ORIG_CNTRY_CD = om.INCL_DEST_CNTRY_CD and o.DOM_INTL_CD = 'D' and o.ENTERPRISE_PRODUCT_ID = 'EP1000000003') THEN 1 WHEN (om.INCL_ORIG_CNTRY_CD in ('CA', 'PR', 'US') and om.INCL_ORIG_CNTRY_CD <> om.INCL_DEST_CNTRY_CD and o.DOM_INTL_CD = 'I' and o.ENTERPRISE_PRODUCT_ID = 'EP1000000004') THEN 1 END WHEN om.ALTERNATE_DESC = '80' THEN CASE WHEN (o.DOM_INTL_CD = 'D' and o.ENTERPRISE_PRODUCT_ID = 'EP1000000027') THEN 1 WHEN (o.DOM_INTL_CD = 'I') THEN 0 END WHEN (om.ALTERNATE_DESC <> '03' and om.ALTERNATE_DESC <> '80' and o.OFFERING_SEQ_NBR = om.OFFERING_NBR) THEN 1END = 1 order by o.ORGANIZATION_CD asc, o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
						ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
						while (rs1.next()) {
							result = rs1.getString(1);
							if (result.equals(GuiValue)) {
								// logger.info("Passed for record:" +
								// value);
							} else {
								status = false;
							}
						}
					}
				}
				if (status == true) {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
					Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
					Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("PASS", testCaseName);
					// logger.info("Test Case Passed");
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
					// logger.info("Test Case Failed");
				}
			}

			else {
				try {
					throw new SkipException(testCaseName + " is Skipped");
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		}
	}

	@Parameters({ "US_Name" })
	@BeforeTest
	public void beforeTest(String US_Name) throws InterruptedException, IOException {
		TestConfiguration.beforeTest(US_Name);
	}

	@AfterTest
	public void afterTest() throws Exception {
		TestConfiguration.afterTest();
	}

	@BeforeSuite
	public void beforeSuite() throws IOException {
		TestConfiguration.beforeSuite();
	}

	@AfterSuite
	public void afterSuite() throws Exception {
		TestConfiguration.afterSuite();
	}

	

}
