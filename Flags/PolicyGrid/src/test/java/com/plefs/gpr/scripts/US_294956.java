package com.plefs.gpr.scripts;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.Select;
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
import com.plefs.gpr.backend.Copier;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;

public class US_294956 {
	Logger logger = Logger.getLogger(US_288245.class);

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

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Thread.sleep(15000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 100);
					Thread.sleep(15000);
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					Thread.sleep(15000);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 60);
					Thread.sleep(15000);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						Thread.sleep(15000);
						String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
						Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
						policygrid_dropdown.selectByVisibleText(Policygrid);
						BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
						Thread.sleep(30000);
						Backend.waitForElementE(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 40);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_successmsg, 20);
						Thread.sleep(15000);
						String Policygrid2 = DataObject.getVariable("DropDownMenu", testCaseName);
						Select policygrid_dropdown2 = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
						policygrid_dropdown.selectByVisibleText(Policygrid);
						Thread.sleep(1000);
						BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
						Thread.sleep(5000);
						Thread.sleep(5000);
						Backend.waitForElementE(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 40);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_successmsg, 20);
						Thread.sleep(150000);
						BrowserInitialization.driver.findElement(XpathRepository.by_select1).click();
						BrowserInitialization.driver.findElement(XpathRepository.by_select2).click();
						BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button).click();
						BrowserInitialization.driver.quit();

						BrowserInitialization.intilaize();
						Backend.login();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 300);
						BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
						Thread.sleep(30000);

						String Stat2 = BrowserInitialization.driver.findElement(XpathRepository.by_statusIPC).getText();

						if (Stat2.equals("C")) {
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
