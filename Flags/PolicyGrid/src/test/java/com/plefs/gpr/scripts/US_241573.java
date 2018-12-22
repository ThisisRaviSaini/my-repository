package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class US_241573 {
	public static String exceptionMsg = "Exception occured in the script";
	Logger logger = Logger.getLogger(US_241573.class);

	// ********************************************************************************************//
	// Description: Verify that Compare Selected button is enabled when user
	// select 2 checkboxes of same policy grid
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_01() throws Exception {
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab,
							120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader)
							.getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton)
									.isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(15000);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver,
										XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage,
										45);
								if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage)
										.getText().contains(DataObject.getVariable("SuccessMessage", testCaseName))) {
									js = (JavascriptExecutor) BrowserInitialization.driver;
									js.executeScript("window.scrollBy(0,150)");
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
								Backend.displayTestCaseStatus("Compare Selected button is disabled ", "FAIL");
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ",
										screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract",
									screenshotPath);
							Assert.fail();
						}
					}

					else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc,
								"Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
		} catch (

		Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		} finally {
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Compare Selected button is enabled when user
	// selects two Extract Type of OFFERING_DIMENSION_CONSTRAINTS policy grid in
	// Extract table
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab,
							120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader)
							.getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton)
									.isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(15000);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver,
										XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage,
										45);
								if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage)
										.getText().contains(DataObject.getVariable("SuccessMessage", testCaseName))) {
									js = (JavascriptExecutor) BrowserInitialization.driver;
									js.executeScript("window.scrollBy(0,150)");
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
								Backend.displayTestCaseStatus("Compare Selected button is disabled ", "FAIL");
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ",
										screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract",
									screenshotPath);
							Assert.fail();
						}
					}

					else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc,
								"Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
		} catch (

		Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		} finally {
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Compare Selected button is enabled when user
	// selects two Extract Type of OFFERING_CONSTRAINTS policy grid in Extract
	// table
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(enabled = false)
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab,
							120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader)
							.getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton)
									.isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(15000);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver,
										XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage,
										45);
								if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage)
										.getText().contains(DataObject.getVariable("SuccessMessage", testCaseName))) {
									js = (JavascriptExecutor) BrowserInitialization.driver;
									js.executeScript("window.scrollBy(0,150)");
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
								Backend.displayTestCaseStatus("Compare Selected button is disabled ", "FAIL");
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ",
										screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract",
									screenshotPath);
							Assert.fail();
						}
					}

					else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc,
								"Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
		} catch (

		Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		} finally {
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
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
