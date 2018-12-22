package com.plefs.gpr.scripts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
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
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

public class US_267977 {
	Logger logger = Logger.getLogger(US_267977.class);

	/**********************************************************************************************
	 * Description: Verify the user is able to view "View Report" button is
	 * disabled while a Compare report is In Progress of generating. Created on
	 * : 04/03/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 2)
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
				testCaseDesc = "Verify ViewSelectReport button is disabled while a Compare report is In Progress of generating.";
				passTestCaseDesc = "User is able to view 'View Report' button is disabled while a Compare report is In Progress of generating";
				failTestCaseDesc = "User is not able to view 'View Report' button is disabled while a Compare report is In Progress of generating:";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 120);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				String ApprovalStatus = DataObject.getVariable("ApprovalStatus", testCaseName);
				Thread.sleep(50000);

				for (int i = 1; i < 3; i++) {
					BrowserInitialization.driver.findElement(By.xpath("(//span[text()='Approval Status']/../../../..//div[text()='" + ApprovalStatus + "']/../.." + "//div[text()='" + Policygrid
							+ "']/../..//input[@type='checkbox'])[" + i + "]")).click();
				}

				BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectButton).click();

				String chk = BrowserInitialization.driver.findElement(By.xpath("//body")).getAttribute("style");
				//logger.info(chk);

				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);

				if (!BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedReport_button).isEnabled()) {
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to view "View Report" button is
	 * enabled once a Compare report has completed generating/processing.
	 * Created on : 04/03/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 1)
	public void TC_02() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_02";
		logger.info("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			// logger.info(testRunnable);
			if (testRunnable) {
				testCaseDesc = "Verify ViewSelectReport button is enabled while a Compare report has completed generating/processing";
				passTestCaseDesc = "User is able to view 'View Report' button is enabled while a Compare report has completed generating/processing";
				failTestCaseDesc = "User is not able to view 'View Report' button is enabled while a Compare report has completed generating/processing";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 120);
				//BrowserInitialization.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				Thread.sleep(50000);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				//BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				Thread.sleep(5000);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				Thread.sleep(100000);

				BrowserInitialization.driver.findElement(XpathRepository.by_FirstComparisonRecord).click();

				String chk = BrowserInitialization.driver.findElement(By.xpath("//body")).getAttribute("style");
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);

				if (chk.equals("cursor: default;") && BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedReport_button).isEnabled()) {
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
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
