package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
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
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;

public class US_258911 {
	Logger logger = Logger.getLogger(US_258911.class);

	@Test
	public void TC_01() throws Exception {
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 100);
					Thread.sleep(10000);
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					Thread.sleep(5000);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 100);
					Thread.sleep(5000);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_dateCreated, 15);

						String date = BrowserInitialization.driver.findElement(XpathRepository.by_dateCreated).getText();
						Thread.sleep(5000);
						Boolean Date = Application.isValidDate(date);

						if (Date.equals(true)) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
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

	@Test
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 100);
					Thread.sleep(10000);
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					Thread.sleep(10000);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 100);
					Thread.sleep(5000);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_dateCreated, 100);
						Thread.sleep(5000);
						String date = BrowserInitialization.driver.findElement(XpathRepository.by_dateCreated).getText();

						Boolean Date = Application.isValidDate(date);
						if (Date.equals(true)) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
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

	@Test
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(15000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 100);
					Thread.sleep(10000);
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					Thread.sleep(10000);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 100);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_dateCreated, 100);
						Thread.sleep(5000);
						boolean Sort = Application.isElementPresent(XpathRepository.by_timeCreatedSort);

						if (Sort == false) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
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

	@Test
	public void TC_04() throws Exception {
		String testCaseName = "TC_04";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(15000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 100);
					Thread.sleep(5000);
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					Thread.sleep(5000);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 100);
					Thread.sleep(5000);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_dateCreated, 100);
						Thread.sleep(5000);
						boolean Sort = Application.isElementPresent(XpathRepository.by_timeCreatedSortR);
						if (Sort == false) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
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
