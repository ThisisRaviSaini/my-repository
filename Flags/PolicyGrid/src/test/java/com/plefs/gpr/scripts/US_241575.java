package com.plefs.gpr.scripts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.ITestContext;
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
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.uftexecution;

public class US_241575 {
	Logger logger = Logger.getLogger(US_241575.class);

	/**********************************************************************************************
	 * Description: Verify the user is able to see ViewSelectReport button as
	 * enabled when selects any row from Comparison Table Created on :
	 * 03/23/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 1)
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
				testCaseDesc = "Verify ViewSelectReport button enabled";
				passTestCaseDesc = "User is able to view ViewSelectButton as enable when selects any row from Comparision Table:";
				failTestCaseDesc = "User is not able to view ViewSelectButton as enable when selects any row from Comparision Table:";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 80);
				Application.ScrollByVisibleElement(XpathRepository.by_LastCheckbox);
				BrowserInitialization.driver.findElement(XpathRepository.by_LastCheckbox).click();
				Thread.sleep(5000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedReport_button).isEnabled()) {
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to navigate to Report page when
	 * clicks on ViewSelectReport button Created on : 03/23/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 2)
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
			if (testRunnable) {
				testCaseDesc = "Navigated to Report Screen";
				passTestCaseDesc = "User is able to navigate to Report Screen when user selects any row from Comparision Table and clicks on ViewSelectReport button:";
				failTestCaseDesc = "User is not able to navigate to Report Screen when user selects any row from Comparision Table and clicks on ViewSelectReport button:";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 80);
				Application.ScrollByVisibleElement(XpathRepository.by_ViewSelectedReport_button);
				BrowserInitialization.driver.findElement(XpathRepository.by_LastCheckbox).click();
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedReport_button).click();
				Thread.sleep(5000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_ReportTitle).isDisplayed()) {
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to see ViewSelectReport button as
	 * disabled when selects no row from Comparison Table Created on :
	 * 03/23/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 3)
	public void TC_03() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_03";
		logger.info("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = "ViewSelectReport button is disabled";
				passTestCaseDesc = "User is able to view ViewSelectButton as disabled when user doesn't select any row from Comparision Table:";
				failTestCaseDesc = "User is not able to view ViewSelectButton as disabled when user doesn't select any row from Comparision Table:";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 80);
				JavascriptExecutor js = ((JavascriptExecutor) BrowserInitialization.driver);
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				BrowserInitialization.driver.findElement(XpathRepository.by_LastCheckbox).click();
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				BrowserInitialization.driver.findElement(XpathRepository.by_LastCheckbox).click();
				Thread.sleep(5000);
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
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to see ViewSelectReport button as
	 * disabled by default when selects no row from Comparison Table Created on
	 * : 03/23/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 4)
	public void TC_04() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_04";
		logger.info("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = "ViewSelectReport button is disabled";
				passTestCaseDesc = "User is able to view ViewSelectReport as disabled by default when user navigates to GPR Extract Screen:";
				failTestCaseDesc = "User is not able to view ViewSelectReport as disabled by default when user navigates to GPR Extract Screen:";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 80);
				JavascriptExecutor js = ((JavascriptExecutor) BrowserInitialization.driver);
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				Thread.sleep(5000);
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
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	@Parameters({ "US_Name" })
	@BeforeTest
	public void beforeTest(String US_Name,final ITestContext testContext) throws InterruptedException, IOException {
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
