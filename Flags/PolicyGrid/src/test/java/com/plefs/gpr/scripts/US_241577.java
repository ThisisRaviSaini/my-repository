package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
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

public class US_241577 {
	public static String exceptionMsg = "Exception in the Code";
	Logger logger = Logger.getLogger(US_241577.class);

	// ********************************************************************************************//
	// Description:Verify that the User is able to validate all columns are
	// aligned properly on Policy Grid Extract Monthly Processes screen.
	// Created by : Ravi Saini
	// Created on : 17-April-2018
	// Last updated by : Ravi Saini
	// Last updated : 17-April-2018
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

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(Keys.ENTER);
						Thread.sleep(2000);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_TimeCreatedDataCompTab, 120);
						String limit = DataObject.getVariable("LimitField", testCaseName);
						String limitS = limit.substring(0, 4);
						int limitI = Integer.parseInt(limitS);
						String extractId = DataObject.getVariable("Extract ID", testCaseName).substring(0, 4);
						for (int p = 1; p <= limitI; p++) {
							String extractedExtractId = BrowserInitialization.driver
									.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
							if (extractedExtractId.equals(extractId)) {
								BrowserInitialization.driver.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]")).click();
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedBttn).click();
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								break;
							}
						}
						int noOfRows = BrowserInitialization.driver.findElements(By.xpath("//tr")).size();
						String width = DataObject.getVariable("Width", testCaseName).substring(0, 4);
						String height = DataObject.getVariable("Height", testCaseName).substring(0, 3);
						String height2ndRow = DataObject.getVariable("2nd Row Height", testCaseName).substring(0, 2);
						for (int i = 1; i <= noOfRows; i++) {

							Dimension dimension = BrowserInitialization.driver.findElement(By.xpath("//tr[" + i + "]")).getSize();
							if (dimension.getWidth() == Integer.parseInt(width))
								temp++;
							if (dimension.getHeight() == Integer.parseInt(height))
								temp2++;
							if (dimension.getHeight() == Integer.parseInt(height2ndRow))
								temp2++;

						}
						if (temp == noOfRows && temp2 == noOfRows) {
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
						Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Assert.fail();

					}
				} else {
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Assert.fail();

				}
			} else {
				try {
					throw new SkipException(testCaseName + " is Skipped");
				} catch (SkipException e) {
					Backend.displaySkipException(e);

				}
			}
		} catch (

		Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);

		} finally {
			// BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description:Verify that the User is able to validate all columns are
	// aligned properly on Policy Grid Extract Monthly Processes screen.
	// Created by : Ravi Saini
	// Created on : 17-April-2018
	// Last updated by : Ravi Saini
	// Last updated : 17-April-2018
	// *********************************************************************************************//
	@Test(enabled = false)
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
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
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 20);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						Thread.sleep(2000);
						BrowserInitialization.driver.findElement(XpathRepository.by_RefreshbttnCompareTab).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_TimeCreatedDataCompTab, 15);
						String limit = DataObject.getVariable("LimitField", testCaseName);
						String limitS = limit.substring(0, 3);
						int limitI = Integer.parseInt(limitS);
						String extractId = DataObject.getVariable("Extract ID", testCaseName).substring(0, 4);
						for (int p = 1; p <= limitI; p++) {
							String extractedExtractId = BrowserInitialization.driver
									.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
							if (extractedExtractId.equals(extractId)) {
								BrowserInitialization.driver.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]")).click();
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 15);
								BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedBttn).click();
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 40);
								break;
							}
						}
						int noOfRows = BrowserInitialization.driver.findElements(By.xpath("//tr")).size();
						for (int i = 2; i <= noOfRows; i++) {
							for (int j = 1; j <= 52; j += 2) {
								if (j == 7 || j == 8 || j == 9 || j == 10 || j == 11 || j == 12 || j == 13 || j == 14) {
									continue;
								} else {
									String newValue = null;
									String oldValue = null;

									String header = BrowserInitialization.driver.findElement(By.xpath("//tr[" + i + "]//th[" + j + "]")).getText();

									if (header.contains("New")) {
										newValue = BrowserInitialization.driver.findElement(By.xpath("//tr[" + (i + 1) + "]//td[" + j + "]")).getText();
									}
									if (header.contains("Old")) {
										oldValue = BrowserInitialization.driver.findElement(By.xpath("//tr[" + (i + 1) + "]//td[" + j + "]")).getText();
									}
									if (newValue.equals(oldValue)) {

									} else {
										String newColorValue = BrowserInitialization.driver.findElement(By.xpath("//tr[" + i + "]//td[" + j + "]")).getAttribute("style");
										String oldColorValue = BrowserInitialization.driver.findElement(By.xpath("//tr[" + i + "]//td[" + (j + 1) + "]")).getAttribute("style");
										if (newColorValue.equals(oldColorValue)) {

										}
									}

								}
							}
						}

						if (temp == noOfRows && temp2 == noOfRows) {
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
						Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Assert.fail();

					}
				} else {
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
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