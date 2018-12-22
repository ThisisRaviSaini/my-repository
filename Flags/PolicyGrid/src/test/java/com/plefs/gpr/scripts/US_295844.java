package com.plefs.gpr.scripts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
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
import com.plefs.gpr.backend.Copier;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

public class US_295844 {
	Logger logger = Logger.getLogger(US_245178.class);

	/*
	 * static String dbUrl =
	 * "jdbc:oracle:thin:@ldap://oid.inf.fedex.com:3060/PLEFS_AB_RO_SVC1_L3,cn=OracleContext,dc=ute,dc=fedex,dc=com";
	 * static String username = "PLEFS_AB_RO_APP"; static String password =
	 * "Apwd4abinit";
	 */

	/**********************************************************************************************
	 * Description: Verify that user should be able to see the EPIC ID(New) is
	 * matching with the Offering ID(New Policy Grid) under Packaging ID Column
	 * in Policy Grid Extract Monthly Processes screen and as well as database.
	 * Created on : 03/05/2018 Created By: Arpan
	 * 
	 * @throws Exception
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 150);
				Application.createExtract(testCaseName);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 150);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(80000);
				int NewPolicyOfferingidcount = BrowserInitialization.driver.findElements(By.xpath("//policy-report-view//table//td[7]")).size();
				String dbUrl = Backend.getProperty("dbUrl");
				String username = Backend.getProperty("dbusername");
				String password = Backend.getProperty("dbpassword");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = con.createStatement();
				for (int i = 0; i < NewPolicyOfferingidcount; i++) {
					int j = i + 1;
					String value = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[11])[" + j + "]")).getText();
					String GuiValue = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[12])[" + j + "]")).getText();
					// logger.info(+i+ "before:"+value);
					if (!value.equals(null) && (value.length() == 12)) {
						// logger.info(value);
						String Str_QueryGroupName = "select om.ALTERNATE_DESC as EPIC_CD from OFFERING o,OFFERING_MAPPING om where o.OFFERING_TYPE_CD = 'BPT'"
								+ " and o.LFCL_STATUS_CD = 'AC' and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))and "
								+ " o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC' and ((om.EXPIRATION_DT is not NULL AND "
								+ " (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL)  and om.MAP_TYPE_CD  = 'epic' and o.offering_seq_nbr = om.offering_nbr and o.ENTERPRISE_PRODUCT_ID='"
								+ value + "'";
						ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
						while (rs1.next()) {
							result = rs1.getString(1);
							// logger.info(result);
							if (result.equals(GuiValue)) {
								// logger.info("Passed for record:" + value);

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
					// logger.info("Test Case Passed");
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify that user should be able to see the EPIC ID(Old) is
	 * matching with the Offering ID(Old Policy Grid) under PackagingID Column
	 * in Policy Grid Extract Monthly Processes screen and as well as database.
	 * Created on : 03/05/2018 Created By: Arpan
	 * 
	 * @throws Exception
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
				Thread.sleep(100000);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(80000);
				int NewPolicyOfferingidcount = BrowserInitialization.driver.findElements(By.xpath("//policy-report-view//table//td[7]")).size();
				String dbUrl = Backend.getProperty("dbUrl");
				String username = Backend.getProperty("dbusername");
				String password = Backend.getProperty("dbpassword");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = con.createStatement();
				for (int i = 0; i < NewPolicyOfferingidcount; i++) {
					int j = i + 1;
					String value = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[13])[" + j + "]")).getText();
					String GuiValue = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[14])[" + j + "]")).getText();
					// logger.info(+i+ "before:"+value);
					if (!value.equals(null) && (value.length() == 12)) {
						// logger.info(value);
						String Str_QueryGroupName = "select om.ALTERNATE_DESC as EPIC_CD from OFFERING o,OFFERING_MAPPING om where o.OFFERING_TYPE_CD = 'BPT'"
								+ " and o.LFCL_STATUS_CD = 'AC' and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))and "
								+ " o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC' and ((om.EXPIRATION_DT is not NULL AND "
								+ " (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL)  and om.MAP_TYPE_CD  = 'epic' and o.offering_seq_nbr = om.offering_nbr and o.ENTERPRISE_PRODUCT_ID='"
								+ value + "'";
						ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
						while (rs1.next()) {
							result = rs1.getString(1);
							// logger.info(result);
							if (result.equals(GuiValue)) {
								// logger.info("Passed for record:" + value);

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
					// logger.info("Test Case Passed");
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
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
	 * Description: Verify that user should be able to see the EPIC ID(New) is
	 * matching with the Offering ID(New Policy Grid) under ServiceID Column in
	 * Policy Grid Extract Monthly Processes screen and as well as database.
	 * Created on : 03/05/2018 Created By: Arpan
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test
	public void TC_03() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_03";
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
				Thread.sleep(100000);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(80000);
				int NewPolicyOfferingidcount = BrowserInitialization.driver.findElements(By.xpath("//policy-report-view//table//td[7]")).size();
				String dbUrl = Backend.getProperty("dbUrl");
				String username = Backend.getProperty("dbusername");
				String password = Backend.getProperty("dbpassword");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = con.createStatement();
				for (int i = 0; i < NewPolicyOfferingidcount; i++) {
					int j = i + 1;
					String value = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[15])[" + j + "]")).getText();
					String GuiValue = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[16])[" + j + "]")).getText();
					// logger.info(+i + "before:" + value);
					if (!value.equals(null) && (value.length() == 12)) {
						// logger.info(value);
						String Str_QueryGroupName = "select om.ALTERNATE_DESC as EPIC_CD  from OFFERING o, OFFERING_MAPPING om where o.OFFERING_TYPE_CD = 'BSO'  "
								+ " and o.LFCL_STATUS_CD = 'AC' and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate)) "
								+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC' and ((om.EXPIRATION_DT is not NULL AND "
								+ " (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL)  and om.MAP_TYPE_CD  = 'epic' and"
								+ " o.offering_seq_nbr = om.offering_nbr and o.ENTERPRISE_PRODUCT_ID='" + value + "'";
						ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
						while (rs1.next()) {
							result = rs1.getString(1);
							// logger.info(result);
							if (result.equals(GuiValue)) {
								// logger.info("Passed for record:" + value);

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
					// logger.info("Test Case Passed");
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
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
	 * Description: Verify that user should be able to see the EPIC ID(Old) is
	 * matching with the Offering ID(Old Policy Grid) under ServiceID Column in
	 * Policy Grid Extract Monthly Processes screen and as well as database.
	 * Created on : 03/05/2018 Created By: Arpan
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test
	public void TC_04() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_04";
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
				Thread.sleep(100000);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Thread.sleep(2000);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(2000);
				Application.findButtonAndClick(XpathRepository.by_ViewSelectedReport_button);
				Thread.sleep(80000);
				int NewPolicyOfferingidcount = BrowserInitialization.driver.findElements(By.xpath("//policy-report-view//table//td[7]")).size();
				String dbUrl = Backend.getProperty("dbUrl");
				String username = Backend.getProperty("dbusername");
				String password = Backend.getProperty("dbpassword");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = con.createStatement();
				for (int i = 0; i < NewPolicyOfferingidcount; i++) {
					int j = i + 1;
					String value = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[17])[" + j + "]")).getText();
					String GuiValue = BrowserInitialization.driver.findElement(By.xpath("(//policy-report-view//table//td[18])[" + j + "]")).getText();
					// logger.info(+i+ "before:"+value);
					if (!value.equals(null) && (value.length() == 12)) {
						// logger.info(value);
						String Str_QueryGroupName = "select om.ALTERNATE_DESC as EPIC_CD  from OFFERING o, OFFERING_MAPPING om where o.OFFERING_TYPE_CD = 'BSO'  "
								+ " and o.LFCL_STATUS_CD = 'AC' and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate)) "
								+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC' and ((om.EXPIRATION_DT is not NULL AND "
								+ " (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL)  and om.MAP_TYPE_CD  = 'epic' and"
								+ " o.offering_seq_nbr = om.offering_nbr and o.ENTERPRISE_PRODUCT_ID='" + value + "'";
						ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
						while (rs1.next()) {
							result = rs1.getString(1);
							// logger.info(result);
							if (result.equals(GuiValue)) {
								// logger.info("Passed for record:" + value);

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
					// logger.info("Test Case Passed");
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
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
