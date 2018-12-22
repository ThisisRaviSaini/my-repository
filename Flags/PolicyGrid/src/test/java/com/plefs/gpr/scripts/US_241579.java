package com.plefs.gpr.scripts;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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
import com.plefs.gpr.backend.Copier;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

public class US_241579 {
	Logger logger = Logger.getLogger(US_241579.class);
	// ********************************************************************************************//
	// Description: Verify that Compare Selected button is enabled when user
	// select 2 checkboxes of same policy grid
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void US_241579_TC_01() throws Exception {
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						Thread.sleep(2000);
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(Keys.ENTER);
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
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_ReturnButton).click();

								break;
							}
						}
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);

						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
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
