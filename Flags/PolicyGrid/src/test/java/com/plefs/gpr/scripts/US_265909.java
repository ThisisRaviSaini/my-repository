package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
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

public class US_265909 {
	Logger logger = Logger.getLogger(US_265909.class);

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
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					BrowserInitialization.driver.navigate().refresh();
					Application.waitForCursor();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 50);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
						Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
						policygrid_dropdown.selectByVisibleText(Policygrid);

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_extractbtn, 50);
						Thread.sleep(5000);
						BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
						Application.waitForCursor();
						Thread.sleep(45000);

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_statusC, 50);
						String Policygrid2 = DataObject.getVariable("DropDownMenu", testCaseName);
						Select policygrid_dropdown2 = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
						policygrid_dropdown.selectByVisibleText(Policygrid);
						BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
						Application.waitForCursor();
						Thread.sleep(25000);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_statusC, 50);
						BrowserInitialization.driver.findElement(XpathRepository.by_select1).click();
						Thread.sleep(5000);
						BrowserInitialization.driver.findElement(XpathRepository.by_select2).click();
						BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button).click();
						Application.waitForCursor();
						Thread.sleep(5000);

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_successmsg, 40);
						Thread.sleep(25000);
						BrowserInitialization.driver.findElement(XpathRepository.by_selectReportA).click();
						BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedBttn).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 80);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_Approve_Button).isEnabled()) {
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
