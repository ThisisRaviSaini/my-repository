package com.plefs.gpr.scripts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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

public class US_265911 {
	Logger logger = Logger.getLogger(US_265911.class);
	/**********************************************************************************************
	 * Description: Verify that the user is able to view report status as "A" in
	 * FILE_STORAGE table if user approves report by clicking on Deny button.
	 * Created on : 04/07/2018
	 *********************************************************************************************/
	@Test
	public void TC_01() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_01";
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 100);
				Thread.sleep(10000);
				String Policygrid = DataObject.getVariable("Extract", "TC_01");
				// logger.info(Policygrid);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				Thread.sleep(50000);
				String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
				Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
				Thread.sleep(10000);
				for (int k = 1; k < 3; k++) {
					BrowserInitialization.driver.findElement(By.xpath(
							"(//span[text()='Approval Status']/../../../..//div[text()='C']" + "/../..//div[text()='"+Policygrid+"']/../..//input[@type='checkbox'])[" + k + "]"))
							.click();
				}
				BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectButton).click();
				Thread.sleep(10000);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				//BackendFunctionality.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 20);
				Thread.sleep(40000);
				String Extract = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTypeID).getText();
				// logger.info(Extract);
				Application.findButtonAndClick(XpathRepository.by_LastCheckbox);
				Thread.sleep(10000);
				//Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedReport_button, 20);
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedReport_button).click();
				//BackendFunctionality.waitForElement(BrowserInitialization.driver, XpathRepository.by_ReportTable, 20);
				Thread.sleep(30000);
				Application.findButtonAndClick(XpathRepository.by_ApproveBtn);
				Thread.sleep(10000);
				Application.findButtonAndClick(XpathRepository.by_ReturnButton);
				Thread.sleep(80000);
				String dbUrl = "jdbc:oracle:thin:@ldap://oid.inf.fedex.com:3060/PLEFS_AB_RO_SVC1_L3,cn=OracleContext,dc=ute,dc=fedex,dc=com";
				String username = "PLEFS_AB_RO_APP";
				String password = "Apwd4abinit";
				String result = null;
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					Connection con = DriverManager.getConnection(dbUrl, username, password);
					Statement stmt = con.createStatement();
					String Str_QueryGroupName = "select STATUS_CD from FILE_STORAGE where FILE_STORAGE_SEQ_NBR=" + Extract + "";
					ResultSet rs1 = stmt.executeQuery(Str_QueryGroupName);
					while (rs1.next()) {
						result = rs1.getString(1);
						// logger.info(result);
					}
				} catch (ClassNotFoundException e) {
					System.err.println(e.getMessage());
				}
				if (result.contentEquals("A")) {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
					Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
					Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("PASS", testCaseName);
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
				}
			} else {
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
