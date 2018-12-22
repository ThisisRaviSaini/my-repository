package com.plefs.gpr.scripts;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.plefs.gpr.backend.Backend;
import com.plefs.gpr.backend.BrowserInitialization;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;

public class US_267976 {
	Logger logger = Logger.getLogger(US_267976.class);
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

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 50);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
						Thread.sleep(2000);
						BrowserInitialization.driver.findElement(XpathRepository.by_RefreshbttnCompareTab).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_TimeCreatedDataCompTab, 15);

						String limit = DataObject.getVariable("LimitField", testCaseName);
						String limitS = limit.substring(0, 4);

						int x = Integer.parseInt(limitS);

						for (int i = 1; i <= x; i++) {
							String policyGridName = BrowserInitialization.driver
									.findElement(By.xpath("(//h4[text()='Comparison Reports']/../../..//span[text()='New Extract Type']/../../../..//td[4])[" + i + "]//div")).getText();
							if (policyGridName.equals(DataObject.getVariable("Policy Grid", testCaseName))) {
								BrowserInitialization.driver.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (i + 1) + "]")).click();
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 15);
								BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedBttn).click();

								Thread.sleep(10000);

								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 40);

								List<WebElement> before_tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader_count);
								int noOfHeaders = before_tabHeaders.size();

								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								List<WebElement> after_tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader_count);
								int noOfHeaders2 = after_tabHeaders.size();

								if (noOfHeaders == noOfHeaders2) {

									temp++;
								}

								if (temp == 1) {

									screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
									Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
									Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
								} else {
									screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
									Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
									Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
									Assert.fail();
								}
								break;

							}
						}

					} else {
						Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
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
