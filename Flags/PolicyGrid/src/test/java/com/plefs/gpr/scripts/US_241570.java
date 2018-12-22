package com.plefs.gpr.scripts;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
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

public class US_241570 {
	Logger logger = Logger.getLogger(US_241570.class);
	private static final Object Enable = null;
	public static int rowmatch;
	public static String exceptionMsg = "Exception occured in the script";

	/**********************************************************************************************
	 * Description: Verify that user is able to clear the selections after
	 * clicking on the Clear Selection Button. Created on : 03/05/2018
	 *********************************************************************************************/
	@Test
	public void TC_01() throws Exception {
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to clear the selections after clicking on the Clear Selection Button.";
		String passTestCaseDesc = "User is able to clear the selections after clicking on the Clear Selection Button.";
		String failTestCaseDesc = "User is not able to clear the selections after clicking on the Clear Selection Button.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 25);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				Thread.sleep(5000);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					WebElement FirstRow_CheckBox = BrowserInitialization.driver.findElement(XpathRepository.by_CheckBox_FirstRow);

					FirstRow_CheckBox.click();

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					WebElement SecondRow_CheckBox = BrowserInitialization.driver.findElement(XpathRepository.by_CheckBox_SecondRow);
					SecondRow_CheckBox.click();

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ClearSelectionButton, 20);

					WebElement ClearSelection = BrowserInitialization.driver.findElement(XpathRepository.by_ClearSelectionButton);

					if (ClearSelection.isEnabled()) {

						ClearSelection.click();
						Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

						if (FirstRow_CheckBox.isSelected() == false && SecondRow_CheckBox.isSelected() == false) {
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
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Clear Selection button is disable", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Clear Selection button is disable", screenshotPath);
						Assert.fail();

					}
				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	/**********************************************************************************************
	 * Description: Verify that Compare Selected button is enabled when user
	 * selects two Extract Type of same Policy Grid in Extract table Created on
	 * : 03/05/2018
	 *********************************************************************************************/
	@Test
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that Compare Selected button is enabled when user selects two Extract Type of same Policy Grid  in Extract table.";
		String passTestCaseDesc = "Compare Selected button is enabled when user selects two Extract Type of same Policy Grid  in Extract table.";
		String failTestCaseDesc = "Compare Selected button is not enabled when user selects two Extract Type of same Policy Grid  in Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 25);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				Thread.sleep(25000);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);

					WebElement FirstRow_CheckBox = BrowserInitialization.driver.findElement(XpathRepository.by_CheckBox_FirstRow);
					FirstRow_CheckBox.click();
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					WebElement SecondRow_CheckBox = BrowserInitialization.driver.findElement(XpathRepository.by_CheckBox_SecondRow);
					SecondRow_CheckBox.click();
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					WebElement CompareSelectedButton = BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button);

					if (CompareSelectedButton.isEnabled()) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	/**********************************************************************************************
	 * Description: Verify that Compare Selected button is enabled by default
	 * when the page loaded. Created on : 03/05/2018
	 *********************************************************************************************/

	@Test
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that Compare Selected button is enabled by default when the page loaded.";
		String passTestCaseDesc = "Compare Selected button is enabled by default when the page loaded.";
		String failTestCaseDesc = "Compare Selected button is not enabled by default when the page loaded.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 25);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);

					WebElement CompareSelectedButton = BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button);

					if (CompareSelectedButton.isEnabled()) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test

	/**********************************************************************************************
	 * Description: Verify that error message "Please select 2 Extracts to
	 * compare!" is thrown when user selects more than two Extract Type in
	 * Extract table Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_04() throws Exception {
		String testCaseName = "TC_04";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that error message Please select 2 Extracts to compare! is thrown when user selects more than two Extract Type in";
		String passTestCaseDesc = "Please select 2 Extracts to compare! is thrown when user selects more than two Extract Type in.";
		String failTestCaseDesc = "Please select 2 Extracts to compare! is not thrown when user selects more than two Extract Type in.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);
					int i = 2;
					int n = 5;
					By checkbox = By.xpath("(//input[@class='ng-untouched ng-pristine ng-valid'])[" + i + "]");
					for (i = 1; i <= n; i++) {
						WebElement CheckBox = BrowserInitialization.driver.findElement(checkbox);
						CheckBox.click();
						Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					}

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_CompareSelected_button, 10);

					BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button).click();
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					String Expected_error_Message = DataObject.getVariable("WarningMessages", testCaseName);

					String Actual_error_Message = BrowserInitialization.driver.findElement(XpathRepository.by_WarningMessage1).getText();

					if (Actual_error_Message.contains(Expected_error_Message)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify a new row is added in the Comparision table when user
	 * click on the Compare Selected button. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_05() throws Exception {
		String testCaseName = "TC_05";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify a new row is added in the Comparision table when user click on the Compare Selected button.";
		String passTestCaseDesc = "A new row is added in the Comparision table when user click on the Compare Selected button.";
		String failTestCaseDesc = "A new row is not added in the Comparision table when user click on the Compare Selected button.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);

					String LimitCount_before_ComparisonTable = BrowserInitialization.driver.findElement(XpathRepository.by_records_ComparisonReport).getText();

					int x = Integer.parseInt(LimitCount_before_ComparisonTable);

					// logger.info(x);

					String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
					policygrid_dropdown.selectByVisibleText(Policygrid);
					BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
					Application.waitForCursor();

					Thread.sleep(25000);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_statusC, 20);
					String Policygrid2 = DataObject.getVariable("DropDownMenu", testCaseName);
					Select policygrid_dropdown2 = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
					policygrid_dropdown.selectByVisibleText(Policygrid);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_extractbtn, 50);
					Thread.sleep(4000);

					BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
					Application.waitForCursor();

					Thread.sleep(40000);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_statusC, 60);
					BrowserInitialization.driver.findElement(XpathRepository.by_select1).click();
					BrowserInitialization.driver.findElement(XpathRepository.by_select2).click();
					BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button).click();
					Application.waitForCursor();

					Thread.sleep(25000);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_records_ComparisonReport, 20);

					String LimitCount_After = BrowserInitialization.driver.findElement(XpathRepository.by_records_ComparisonReport).getText();

					int y = Integer.parseInt(LimitCount_After);

					if (y == x + 1) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify a new row is not added in the Comparision table when
	 * user click on the Compare Selected button. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_06() throws Exception {
		String testCaseName = "TC_06";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify a new row is not added in the Comparision table when user click on the Compare Selected button";
		String passTestCaseDesc = "A new row is not added in the Comparision table when user click on the Compare Selected button.";
		String failTestCaseDesc = "A new row is added in the Comparision table when user click on the Compare Selected button";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					String LimitCount_before = BrowserInitialization.driver.findElement(XpathRepository.by_records_ComparisonReport).getText();

					int x = Integer.parseInt(LimitCount_before);

					String LimitCount_ExtractTable = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTotallimitcount).getText();

					Application.ScrollByVisibleElement(XpathRepository.by_CompareSelected_button);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ExtractLimittextbox, 20);

					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ExtractLimittextbox, 20);

					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount_ExtractTable);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ExtractRefreshbutton, 20);

					Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);

					List<WebElement> ExtractName = BrowserInitialization.driver.findElements(XpathRepository.by_Extractrowcount);
					int extractcount = ExtractName.size();

					String FirstExtract = ExtractName.get(0).getText();
					int rowmatch = 0;
					for (int i = 1; i < extractcount; i++) {

						String Name = ExtractName.get(i).getText();
						if (!Name.equals(FirstExtract)) {

							rowmatch = i;
							break;
						}

					}

					int f = rowmatch + 1;

					BrowserInitialization.driver.findElement(By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[1]")).click();
					Thread.sleep(2000);
					BrowserInitialization.driver.findElement(By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[" + f + "]")).click();
					Thread.sleep(2000);
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button).click();

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_records_ComparisonReport, 20);

					String LimitCount_After = BrowserInitialization.driver.findElement(XpathRepository.by_records_ComparisonReport).getText();

					int y = Integer.parseInt(LimitCount_before);

					if (y == x) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that success message "Successfully added record:
	 * OFFERING_CONSTRAINTS" creation after creating report for
	 * OFFERING_CONSTRAINTS is displayed. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_07() throws Exception {
		String testCaseName = "TC_07";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that success message Successfully added record: OFFERING_CONSTRAINTS creation after creating report forOFFERING_CONSTRAINTS is displayed";
		String passTestCaseDesc = "Successfully added record: OFFERING_CONSTRAINTS creation after creating report for OFFERING_CONSTRAINTS is displayed.";
		String failTestCaseDesc = "Successfully added record: OFFERING_CONSTRAINTS creation after creating report for OFFERING_CONSTRAINTS is NOT displayed.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);

				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					String Policygrid = DataObject.getVariable("DropDownMenu", "TC_07");

					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
					policygrid_dropdown.selectByVisibleText(Policygrid);

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					Thread.sleep(2000);

					BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();
					Application.waitForCursor();

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Success_WarningMessage, 20);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Success_WarningMessage, 20);

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					String ExpectedSuccessMessage = DataObject.getVariable("WarningMessages", testCaseName);

					String ActualSuccessMessage = BrowserInitialization.driver.findElement(XpathRepository.by_Success_WarningMessage).getText();

					if (ActualSuccessMessage.contains(ExpectedSuccessMessage)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that success message "Successfully added record:
	 * OFFERING_DIMENSION_CONSTRAINTS" creation after creating report for
	 * OFFERING_DIMENSION_CONSTRAINTS is displayed Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_08() throws Exception {
		String testCaseName = "TC_08";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that success message Successfully added record:OFFERING_DIMENSION_CONSTRAINTS creation after creating report forOFFERING_DIMENSION_CONSTRAINTS is displayed.";
		String passTestCaseDesc = "Successfully added record:OFFERING_DIMENSION_CONSTRAINTS creation after creating report for OFFERING_DIMENSION_CONSTRAINTS is displayed.";
		String failTestCaseDesc = "Successfully added record:OFFERING_DIMENSION_CONSTRAINTS creation after creating report for OFFERING_DIMENSION_CONSTRAINTS is NOT displayed..";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				Application.waitForCursor();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 20);

					String Policygrid = DataObject.getVariable("DropDownMenu", "TC_07");

					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
					policygrid_dropdown.selectByVisibleText(Policygrid);

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					Thread.sleep(2000);

					BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Success_WarningMessage, 20);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Success_WarningMessage, 20);

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					String ExpectedSuccessMessage = DataObject.getVariable("WarningMessages", testCaseName);

					String ActualSuccessMessage = BrowserInitialization.driver.findElement(XpathRepository.by_Success_WarningMessage).getText();

					if (ActualSuccessMessage.contains(ExpectedSuccessMessage)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify the New Extract Type,New Extract Id, Old Extract
	 * Type, Old Extract ID, Name-ID, Date Created, Time Created, Report ID and
	 * Status columns are displayed in the Comparision Report table. Created on
	 * : 03/05/2018
	 *********************************************************************************************/
	public void TC_09() throws Exception {
		String testCaseName = "TC_09";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		int temp = 0;
		String testCaseDesc = "Verify the all HEADERS are displayed in the Comparision Report table";
		String passTestCaseDesc = "HEADERS are displayed in the Comparision Report table";
		String failTestCaseDesc = "HEADERS are not displayed in the Comparision Report table";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				List<String> header = new ArrayList<String>();
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					int i;

					for (i = 4; i < 12; i++) {

						String name = BrowserInitialization.driver.findElement(By.xpath("//h4[text()='Extracts']/../../..//th[" + i + "]")).getText();

						header.add(name);

					}

					if (header.contains("Extract Type")) {
						temp++;

					}
					if (header.contains("Date Created")) {
						temp++;

					}
					if (header.contains("Time Created")) {
						temp++;

					}
					if (header.contains("Extract ID")) {
						temp++;

					}
					if (header.contains("File Name")) {
						temp++;

					}
					if (header.contains("Approval Status")) {
						temp++;

					}
					if (header.contains("FedEx ID")) {
						temp++;

					}
					if (header.contains("Date/Time")) {
						temp++;

					}

					Iterator<String> itr = header.iterator();

					/*
					 * while (itr.hasNext()) {
					 * 
					 * logger.info(itr.next());
					 * 
					 * BackendCommonFunctionality.takeScreenshot(testCaseName,
					 * screenShotName, "PASS");
					 * 
					 * }
					 */

					if (temp == 8) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify the user is able to view Old and New extracts column
	 * on basis of date in Comparision Report Table Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_10() throws Exception {
		String testCaseName = "TC_10";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify the user is able to view Old and New extracts column on basis of date in Comparision Report Table";
		String passTestCaseDesc = "User is able to view Old and New extracts column on basis of date in Comparision Report Table.";
		String failTestCaseDesc = "User is able to not view Old and New extracts column on basis of date in Comparision Report Table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 20);

					String Expected_New_id = "New Extract ID";

					String New_id = BrowserInitialization.driver.findElement(XpathRepository.by_New_Extract_id).getText();
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					String Expected_Old_id = "Old Extract ID";
					String Old_id = BrowserInitialization.driver.findElement(XpathRepository.by_Old_Extract_id).getText();
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					if (Expected_New_id.equals(New_id) && Expected_Old_id.equals(Old_id)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Extract type column in
	 * ascending order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	@Test
	public void TC_11() throws Exception {
		String testCaseName = "TC_11";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Extract type column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Extract type column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Extract type column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 25);
				Thread.sleep(2000);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 45);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();

				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Thread.sleep(5000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 80);

					BrowserInitialization.driver.findElement(XpathRepository.by_Extract_Type_Asce).click();

					if (Application.sort_Ascending(XpathRepository.by_Extract_Type_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Date Created column in
	 * ascending order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_12() throws Exception {
		String testCaseName = "TC_12";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Date Created column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Date Created column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Date Created column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				Application.waitForCursor();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);
					Thread.sleep(5000);
					BrowserInitialization.driver.findElement(XpathRepository.by_Date_Created_Asce).click();

					if (Application.sort_Ascending(XpathRepository.by_Date_Created_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Time Created column in
	 * ascending order of the Extract table Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_13() throws Exception {
		String testCaseName = "TC_13";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Time Created column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Time Created column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Time Created column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				Application.waitForCursor();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Thread.sleep(5000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					BrowserInitialization.driver.findElement(XpathRepository.by_Time_Created_Asce).click();
					if (Application.sort_Ascending(XpathRepository.by_Time_Created_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Extract ID column in
	 * ascending order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_14() throws Exception {
		String testCaseName = "TC_14";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Extract ID column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Extract id column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Extract id column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				Thread.sleep(15000);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					BrowserInitialization.driver.findElement(XpathRepository.by_Extract_ID_Asce).click();

					if (Application.sort_Ascending(XpathRepository.by_Extract_ID_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort File Name column in
	 * ascending order of the Extract table.. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_15() throws Exception {
		String testCaseName = "TC_15";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort File Name column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort File Name column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort File Name column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(5000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 20);

					Thread.sleep(2000);
					BrowserInitialization.driver.findElement(XpathRepository.by_File_Name_Asce).click();

					if (Application.sort_Ascending(XpathRepository.by_File_Name_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Approval Status column in
	 * ascending order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_16() throws Exception {
		String testCaseName = "TC_16";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Approval Status column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Approval Status column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Approval Status column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(5000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);

				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(4000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);
					BrowserInitialization.driver.findElement(XpathRepository.by_Approval_Status_Asce).click();

					if (Application.sort_Ascending(XpathRepository.by_Approval_Status_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Name-ID column in ascending
	 * order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_17() throws Exception {
		String testCaseName = "TC_17";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Name-ID column in ascending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Name-ID column in ascending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Name-ID column in ascending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(5000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);

				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				Thread.sleep(4000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 50);

					BrowserInitialization.driver.findElement(XpathRepository.by_Extract_ID_Asce).click();

					if (Application.sort_Ascending(XpathRepository.by_Extract_ID_Asce)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Extract type column in
	 * descending order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_19() throws Exception {
		String testCaseName = "TC_19";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Extract type column in descending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Extract type column in descending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Extract type column in descending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);

				Thread.sleep(5000);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 15);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				Application.waitForCursor();
				Thread.sleep(25000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					BrowserInitialization.driver.findElement(XpathRepository.by_Extract_Type_Asce).click();

					Thread.sleep(1000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);

					BrowserInitialization.driver.findElement(XpathRepository.by_Extract_Type_Desc).click();

					if (Application.sort_descending(XpathRepository.by_Extract_Type_Desc)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to sort Date Created column in
	 * descending order of the Extract table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_20() throws Exception {
		String testCaseName = "TC_20";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;

		String testCaseDesc = "Verify that user is able to sort Date Created column in descending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Date Created column in descending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Date Created column in descending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(8000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Thread.sleep(2000);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 50);
				BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Time_Created, 40);

					BrowserInitialization.driver.findElement(XpathRepository.by_Date_Created_Asce).click();

					Thread.sleep(1000);

					BrowserInitialization.driver.findElement(XpathRepository.by_Date_Created_Desc).click();

					if (Application.sort_descending(XpathRepository.by_Date_Created_Desc)) {
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

				}

				else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
		}
	}

	// ********************************************************************************************//
	// Description: Verify that User is able to sort Extract id column in
	// descending order of the Extract table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_22() throws Exception {
		String testCaseName = "TC_22";
		logger.info("Executing the Test Case No. " + testCaseName);
		List<String> extractIdList = null;
		List<String> sortedExtractIdList = null;
		String extractId = null;
		String sortedExtractId = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to sort Extract ID column in descending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort Extract id column in descending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Extract id column in descending order of the Extract table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				extractIdList = new ArrayList<String>();
				sortedExtractIdList = new ArrayList<String>();
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					for (int i = 1; i <= 2; i++) {
						Thread.sleep(2000);
						Application.clickInLoop(XpathRepository.by_SortBttnExtractId);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}
					for (int i = 1; i <= 10; i++) {
						String extractIdData = BrowserInitialization.driver.findElement(By.xpath("((//table)[1]//tbody//tr//td[@class='data-column column-fileStorageSeqNbr'])[" + i + "]/div"))
								.getText();
						extractIdList.add(extractIdData);
					}
					sortedExtractIdList.addAll(extractIdList);
					Collections.sort(sortedExtractIdList, Collections.reverseOrder());

					Iterator<String> itr = extractIdList.iterator();
					while (itr.hasNext()) {
						extractId = extractId + itr.next();
					}
					Iterator<String> itr2 = sortedExtractIdList.iterator();
					while (itr2.hasNext()) {
						sortedExtractId = sortedExtractId + itr2.next();
					}

					if (extractId.equals(sortedExtractId)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}

	// ********************************************************************************************//
	// Description: Verify that User is able to sort File Name column in
	// descending order of the Extract table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_23() throws Exception {
		String testCaseName = "TC_23";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		List<String> fileNameList = null;
		List<String> sortedFileNameList = null;
		String fileName = null;
		String sortedFileName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to sort File Name column in descending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort File Name column in descending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort File Name column in descending order of the Extract table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				fileNameList = new ArrayList<String>();
				sortedFileNameList = new ArrayList<String>();
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					for (int i = 1; i <= 2; i++) {
						Application.clickInLoop(XpathRepository.by_SortBttnFileName);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}
					for (int i = 1; i <= 10; i++) {
						String fileNameData = BrowserInitialization.driver.findElement(By.xpath("((//table)[1]//tbody//tr//td[@class='data-column column-displayFileNm'])[" + i + "]/div")).getText();
						fileNameList.add(fileNameData);
					}
					sortedFileNameList.addAll(fileNameList);
					Collections.sort(sortedFileNameList, Collections.reverseOrder());

					Iterator<String> itr = fileNameList.iterator();
					while (itr.hasNext()) {
						fileName = fileName + itr.next();
					}
					Iterator<String> itr2 = sortedFileNameList.iterator();
					while (itr2.hasNext()) {
						sortedFileName = sortedFileName + itr2.next();
					}
					if (fileName.equals(sortedFileName)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that User is able to sort Approval Status column in
	// descending order of the Extract table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_24() throws Exception {
		String testCaseName = "TC_24";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		List<String> approvalStatusList = null;
		List<String> sortedApprovalStatusList = null;
		String approvalStatus = null;
		String sortedApprovalStatus = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to sort Approval Status column in descending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort File Approval Status in descending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort Approval Status column in descending order of the Extract table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				approvalStatusList = new ArrayList<String>();
				sortedApprovalStatusList = new ArrayList<String>();
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					for (int i = 1; i <= 2; i++) {
						Application.clickInLoop(XpathRepository.by_SortBttnApprovalStatus);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}

					for (int i = 1; i <= 10; i++) {
						String approvalStatusData = BrowserInitialization.driver.findElement(By.xpath("((//table)[1]//tbody//tr//td[@class='data-column column-statusCd'])[" + i + "]/div")).getText();
						approvalStatusList.add(approvalStatusData);
					}
					sortedApprovalStatusList.addAll(approvalStatusList);
					Collections.sort(sortedApprovalStatusList, Collections.reverseOrder());

					Iterator<String> itr = approvalStatusList.iterator();
					while (itr.hasNext()) {
						approvalStatus = approvalStatus + itr.next();
					}
					Iterator<String> itr2 = sortedApprovalStatusList.iterator();
					while (itr2.hasNext()) {
						sortedApprovalStatus = sortedApprovalStatus + itr2.next();
					}

					if (approvalStatus.equals(sortedApprovalStatus)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}

	// ********************************************************************************************//
	// Description: Verify that User is able to sort FedEx ID column in
	// descending order of the Extract table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	//
	@Test
	public void TC_25() throws Exception {
		String testCaseName = "TC_25";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		List<Integer> approvalStatusList = null;
		List<Integer> sortedApprovalStatusList = null;
		int approvalStatus = 0;
		int sortedApprovalStatus = 0;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to sort FedEx ID column in descending order of the Extract table.";
		String passTestCaseDesc = "User is able to sort File FedEx ID in descending order of the Extract table.";
		String failTestCaseDesc = "User is not able to sort FedEx ID column in descending order of the Extract table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				approvalStatusList = new ArrayList<Integer>();
				sortedApprovalStatusList = new ArrayList<Integer>();
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					for (int i = 1; i <= 2; i++) {
						Thread.sleep(2000);
						Application.clickInLoop(XpathRepository.by_SortBttnFedExId);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}

					for (int i = 1; i <= 10; i++) {
						String fedExIdData = BrowserInitialization.driver.findElement(By.xpath("((//table)[1]//tbody//tr//td[@class='data-column column-lastUpdateAgentNm'])[" + i + "]//div"))
								.getText();
						approvalStatusList.add(Integer.parseInt(fedExIdData));
					}
					sortedApprovalStatusList.addAll(approvalStatusList);
					Collections.sort(sortedApprovalStatusList, Collections.reverseOrder());

					Iterator<Integer> itr = approvalStatusList.iterator();
					while (itr.hasNext()) {
						approvalStatus = approvalStatus + itr.next();
					}
					Iterator<Integer> itr2 = sortedApprovalStatusList.iterator();
					while (itr2.hasNext()) {
						sortedApprovalStatus = sortedApprovalStatus + itr2.next();
					}

					if (sortedApprovalStatus == approvalStatus) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that User is able to select the check boxes.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_27() throws Exception {
		String testCaseName = "TC_27";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		int temp2 = 0;
		String isSelected = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to select the check boxes";
		String passTestCaseDesc = "User is able to select the check boxes";
		String failTestCaseDesc = "User is not able to select the check boxes";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					for (int j = 1; j <= 2; j++) {
						Application.clickInLoop(By.xpath("(//td[@class='select-column'])[" + j + "]"));

						if (j == 1) {
							isSelected = BrowserInitialization.driver.findElement(By.xpath("//h4[contains(.,'Extracts')]/../../../../../..//tr[1][@class='data-table-row row-odd clickable selected']"))
									.getAttribute("class");
							temp2++;
						} else {
							isSelected = BrowserInitialization.driver
									.findElement(By.xpath("//h4[contains(.,'Extracts')]/../../../../../..//tr[1][@class='data-table-row row-even clickable selected']")).getAttribute("class");
							temp2++;
						}

					}
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,100)");

					if ((isSelected.equals("data-table-row row-odd clickable selected") || isSelected.equals("data-table-row row-even clickable selected")) && (temp2 == 2)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that User is able to select the multiple check boxes.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_28() throws Exception {
		String testCaseName = "TC_28";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		int temp = 0;
		String isSelected = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to select multiple the check boxes";
		String passTestCaseDesc = "User is able to select multiple the check boxes";
		String failTestCaseDesc = "User is not able to select multiple the check boxes";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					for (int j = 1; j <= 5; j++) {
						Application.clickInLoop(By.xpath("(//td[@class='select-column'])[" + j + "]"));
						if (j % 2 == 1) {
							isSelected = BrowserInitialization.driver.findElement(By.xpath("//h4[contains(.,'Extracts')]/../../../../../..//tr[1][@class='data-table-row row-odd clickable selected']"))
									.getAttribute("class");
							temp++;
						} else {
							isSelected = BrowserInitialization.driver
									.findElement(By.xpath("//h4[contains(.,'Extracts')]/../../../../../..//tr[1][@class='data-table-row row-even clickable selected']")).getAttribute("class");
							temp++;
						}

					}
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,100)");

					if ((temp == 5)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);

		}

	}

	// ********************************************************************************************//
	// Description: Verify that User is able to Refresh the data of Comparison
	// Table by clicking on the Refresh button.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_29() throws Exception {
		String testCaseName = "TC_29";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;

		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to Refresh the data of Comparison";
		String passTestCaseDesc = "User is able to Refresh the data of Comparison";
		String failTestCaseDesc = "User is not able to Refresh the data of Comparison";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[2]"));
					Application.clickInLoop(XpathRepository.by_RefreshbttnCompareTab);
					Thread.sleep(60000);
					boolean isSelected = BrowserInitialization.driver.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[2]")).isSelected();
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,500)");
					if (isSelected == false) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that Error message Please select 2 Extracts to
	// compare! is displayed when clicking the Compare Selected button.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_30() throws Exception {
		String testCaseName = "TC_30";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;
		String screenshotPath = null;
		String testCaseDesc = "Verify that Error message Please select 2 Extracts to compare! is displayed when clicking the Compare Selected button";
		String passTestCaseDesc = "Error message Please select 2 Extracts to compare! is displayed when clicking the Compare Selected button";
		String failTestCaseDesc = "Error message Please select 2 Extracts to compare! is not displayed when clicking the Compare Selected button";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				// Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Select dropDownMenu = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
					for (int p = 1; p <= 3; p++) {
						dropDownMenu.selectByVisibleText(DataObject.getVariable("DropDownMenu", testCaseName));
						Application.clickInLoop(XpathRepository.by_CreateExtractButton);
						Thread.sleep(80000);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}
					Thread.sleep(7000);
					for (int i = 1; i <= 3; i++) {
						if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + i + "]")).getText().equals("C")) {
							temp++;
							if (temp == 3) {
								for (int j = 1; j <= 3; j++) {
									Application.clickInLoop(By.xpath("(//td[@class='select-column'])[" + j + "]"));
									temp2++;
								}
								Thread.sleep(2000);
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Thread.sleep(5000);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WarningMessage, 120);
								boolean warningMessage = BrowserInitialization.driver.findElement(XpathRepository.by_WarningMessage).getText()
										.contains(DataObject.getVariable("WarningMessages", testCaseName));
								js = (JavascriptExecutor) BrowserInitialization.driver;
								js.executeScript("window.scrollBy(0,300)");
								if (temp2 == 3 && (warningMessage == true)) {

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

							}
							if (i == 3 && !(temp == 3)) {
								screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
								Backend.displayTestCaseStatus("Approval Status is not C for all extracted Policy Grid", "FAIL");
								Reporter.setTestDetails("FAIL", testCaseDesc, "Approval Status is not C for all extracted Policy Grid", screenshotPath);
								Assert.fail();
							}
						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Approval Status is not C", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Approval Status is not C", screenshotPath);
							Assert.fail();
							break;
						}
					}

				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that Error message Please select 2 Extracts to
	// compare! is displayed when clicking the Compare Selected button.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_31() throws Exception {
		String testCaseName = "TC_31";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;
		String screenshotPath = null;
		String testCaseDesc = "Verify that Error message Please select 2 Extracts to compare! is displayed when clicking the Compare Selected button.";
		String passTestCaseDesc = "Error message Please select 2 Extracts to compare! is displayed when clicking the Compare Selected button.";
		String failTestCaseDesc = "Error message Please select 2 Extracts to compare! is not displayed when clicking the Compare Selected button.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				// Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Select dropDownMenu = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
					dropDownMenu.selectByVisibleText(DataObject.getVariable("DropDownMenu", testCaseName));
					Application.clickInLoop(XpathRepository.by_CreateExtractButton);
					Thread.sleep(90000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);

					for (int i = 1; i <= 1; i++) {
						temp++;
						if (temp == 1) {
							for (int j = 1; j <= 1; j++) {
								Application.clickInLoop(By.xpath("(//td[@class='select-column'])[" + j + "]"));
								temp2++;
							}
							Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WarningMessage, 120);
							boolean warningMessage = BrowserInitialization.driver.findElement(XpathRepository.by_WarningMessage).getText()
									.contains(DataObject.getVariable("WarningMessages", testCaseName));

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("window.scrollBy(0,300)");

							if (temp2 == 1 && (warningMessage == true)) {
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

						}
						if (i == 1 && !(temp == 1)) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Approval Status is not C for both Policy Grid", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Approval Status is not C for both Policy Grid", screenshotPath);
							Assert.fail();
						}
					}

				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that User is able to enter the value in Limit section
	// and limit
	// no. indicates the number of records displayed in the Comparision Reports
	// table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_33() throws Exception {
		String testCaseName = "TC_33";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to enter the value in Limit section and limit no. indicates the number of records displayed in the Comparision Reports table.";
		String passTestCaseDesc = "User is able to enter the value in Limit section and limit no. indicates the number of records displayed in the Comparision Reports table.";
		String failTestCaseDesc = "User is not able to enter the value in Limit section and limit no. indicates the number of records displayed in the Comparision Reports table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				// Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				Thread.sleep(30000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
					int limitFieldSize = Integer.parseInt(DataObject.getVariable("LimitField", testCaseName).substring(1));
					BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName).substring(1));
					BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(Keys.ENTER);
					Thread.sleep(2000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					List<WebElement> rows = BrowserInitialization.driver.findElements(XpathRepository.by_ComparisionTab);
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,500)");

					if (rows.size() == limitFieldSize) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to enter the value in Page Number
	// below
	// Extracts Reports table
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_38() throws Exception {
		String testCaseName = "TC_38";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to enter the value in Page Number below Extracts Reports table";
		String passTestCaseDesc = "User is able to enter the value in Page Number below Extracts Reports table";
		String failTestCaseDesc = "User is not able to enter the value in Page Number below Extracts Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).clear();
					String pageNo = DataObject.getVariable("PageNo", testCaseName).substring(1);

					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).sendKeys(pageNo);
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).sendKeys(Keys.ENTER);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 15);
					String results = BrowserInitialization.driver.findElement(XpathRepository.by_ResultsExtractTab).getText().substring(0, 1);
					if (pageNo.equals(results)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description: Verify that User is able to see << and < icons in disable
	// state in Page
	// Number section below Extracts Reports table when navigated to GPR EXTRACT
	// REPORTS.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_39() throws Exception {
		String testCaseName = "TC_39";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to see << and < icons in disable state in Page Number section below Extracts Reports table when navigated to GPR EXTRACTREPORTS.";
		String passTestCaseDesc = "User is able to see << and < icons in disable state in Page Number section below Extracts Reports table when navigated to GPR EXTRACT REPORTS.";
		String failTestCaseDesc = "User is not able to see << and < icons in disable state in Page Number section below Extracts Reports table when navigated to GPR EXTRACTREPORTS.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					boolean prevPageBttn = BrowserInitialization.driver.findElement(XpathRepository.by_PrevPageBttnExtractTab).isEnabled();
					boolean firstPageBttn = BrowserInitialization.driver.findElement(XpathRepository.by_FirstPageBttnExtractTab).isEnabled();
					if ((prevPageBttn && firstPageBttn) == false) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to increment the Page Number entered
	// below
	// Extracts Reports table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_40() throws Exception {
		String testCaseName = "TC_40";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to increment the Page Number entered  below Extracts Reports table.";
		String passTestCaseDesc = "User is able to increment the Page Number entered  below Extracts Reports table.";
		String failTestCaseDesc = "User is not able to increment the Page Number entered  below Extracts Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).clear();
					String pageNo = DataObject.getVariable("PageNo", testCaseName).substring(1);
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).sendKeys(pageNo);
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).sendKeys(Keys.ENTER);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					String results = BrowserInitialization.driver.findElement(XpathRepository.by_ResultsExtractTab).getText().substring(0, 1);
					if (pageNo.equals(results)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to decrement the Page Number entered
	// below
	// Extracts Reports table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_41() throws Exception {
		String testCaseName = "TC_41";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to decrement the Page Number entered below Extracts Reports table.";
		String passTestCaseDesc = "User is able to decrement the Page Number entered below Extracts Reports table.";
		String failTestCaseDesc = "User is  not able to decrement the Page Number entered below Extracts Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					int noOfClick = Integer.parseInt(DataObject.getVariable("NoOfClick", testCaseName));
					for (int i = 1; i <= noOfClick; i++) {
						Application.clickInLoop(XpathRepository.by_NextPageBttnExtractTab);
						Thread.sleep(1000);
					}
					int pageNo = noOfClick - 1;
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).clear();
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).sendKeys(String.valueOf(pageNo));
					BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).sendKeys(Keys.ENTER);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					int expectedRes = (noOfClick - 1) * 10;
					int results = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_ResultsExtractTab).getText());
					if (expectedRes == results) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to navigate to last page when user
	// click on >>
	// icon below Extracts Reports table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_42() throws Exception {
		String testCaseName = "TC_42";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to navigate to last page when user click on >> icon below Extracts Reports table.";
		String passTestCaseDesc = "User is able to navigate to last page when user click on >> icon below Extracts Reports table.";
		String failTestCaseDesc = "User is not able to navigate to last page when user click on >> icon below Extracts Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Application.clickInLoop(XpathRepository.by_LastPageBttnExtractTab);
					String pageNoTextBox = BrowserInitialization.driver.findElement(XpathRepository.by_PageNoTxtBoxExtractTab).getAttribute("max");
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					String totalPages = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTablastPageLable).getText();

					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,-200)");
					if (pageNoTextBox.equals(totalPages)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to navigate to first page when user
	// click on <<
	// icon below Extracts Reports table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_43() throws Exception {
		String testCaseName = "TC_43";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to navigate to first page when user click on << icon below Extracts Reports table.";
		String passTestCaseDesc = "User is able to navigate to first page when user click on << icon below Extracts Reports table.";
		String failTestCaseDesc = "User is not able to navigate to first page when user click on << icon below Extracts Reports table.";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					int noOfClick = Integer.parseInt(DataObject.getVariable("NoOfClick", testCaseName));
					for (int i = 1; i <= noOfClick; i++) {
						Application.clickInLoop(XpathRepository.by_NextPageBttnExtractTab);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}
					Application.clickInLoop(XpathRepository.by_FirstPageBttnExtractTab);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					int expectedRes = 10;
					int results = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_ResultsExtractTab).getText());
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,-200)");
					if (expectedRes == results) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to go to the next page by clicking
	// on > icon below Extracts Reports table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_44() throws Exception {
		String testCaseName = "TC_44";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		JavascriptExecutor js = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to go to the next page by clicking on > icon below Extracts Reports table.";
		String passTestCaseDesc = "User is able to go to the next page by clicking on > icon below Extracts Reports table.";
		String failTestCaseDesc = "User is not able to go to the next page by clicking on > icon below Extracts Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					int noOfClick = Integer.parseInt(DataObject.getVariable("NoOfClick", testCaseName));
					for (int i = 1; i <= noOfClick; i++) {
						Application.clickInLoop(XpathRepository.by_NextPageBttnExtractTab);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}
					int expectedRes = (noOfClick + 1) * 10;
					int results = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_ResultsExtractTab).getText());
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,300)");
					if (expectedRes == results) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}
	// ********************************************************************************************//
	// Description:Verify that User is able to go to the previous page by
	// clicking on < icon below Extracts Reports table.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_45() throws Exception {
		String testCaseName = "TC_45";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that User is able to go to the previous page by clicking on < icon below Extracts Reports table.";
		String passTestCaseDesc = "User is able to go to the previous page by clicking on < icon below Extracts Reports table.";
		String failTestCaseDesc = "User is not able to go to the previous page by clicking on < icon below Extracts Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
				Application.clickInLoop(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					int noOfClick = Integer.parseInt(DataObject.getVariable("NoOfClick", testCaseName));
					for (int i = 1; i <= noOfClick; i++) {
						Application.clickInLoop(XpathRepository.by_NextPageBttnExtractTab);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					}
					Application.clickInLoop(XpathRepository.by_PrevPageBttnExtractTab);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					int expectedRes = (noOfClick) * 10;
					int results = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_ResultsExtractTab).getText());
					js = (JavascriptExecutor) BrowserInitialization.driver;
					js.executeScript("window.scrollBy(0,300)");
					if (expectedRes == results) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
		}

	}

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to see Result lable showing record
	 * in "Results: 1 - 10 / 54" format for the no. of records displaying for
	 * Extract Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_46() throws Exception {
		String testCaseName = "TC_46";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to see Result lable showing record in Results: 1 - 10 / 54 format for the no. of records displaying for Extract Reports table";
		String passTestCaseDesc = "User is able to see Result lable showing record in Results: 1 - 10 / 54 format for the no. of records displaying for Extract Reports table";
		String failTestCaseDesc = "User is NOT able to see Result lable showing record in Results: 1 - 10 / 54 format for the no. of records displaying for Extract Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
					Thread.sleep(5000);
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						String actResults = BrowserInitialization.driver.findElement(XpathRepository.by_resultsExtracts).getText();
						// logger.info("actResults initial : " +
						// actResults);
						String Act = actResults.replaceAll("Results: ", "");
						// logger.info("actResults : " + Act);
						Act = Act.replaceAll(" ", "");
						// logger.info("actResults : " + Act);

						String[] first = Act.split("-");
						String[] second = first[1].split("/");

						// logger.info(first[0].length());
						// logger.info(second[0].length());
						// logger.info(second[1].length());
						// logger.info(Pattern.matches("\\d{" +
						// first[0].length() + "}-\\d{" + second[0].length() +
						// "}/\\d{" + second[1].length() + "}", Act));

						boolean time = Pattern.matches("\\d{" + first[0].length() + "}-\\d{" + second[0].length() + "}/\\d{" + second[1].length() + "}", Act);
						if (time) {
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to view List button on right hand
	 * side corner above Extract Reports table beside Refresh button. Created on
	 * : 03/05/2018
	 *********************************************************************************************/

	public void TC_47() throws Exception {
		String testCaseName = "TC_47";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to view List button.";
		String passTestCaseDesc = "User is able to view List button.";
		String failTestCaseDesc = "User is not able to view List button.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					if (BrowserInitialization.driver.findElement(XpathRepository.by_listButtonExtracts).isDisplayed()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to view the list of columns of the
	 * Extract table when user clicks on List button on right hand side corner
	 * above Extract Reports table beside Refresh button. Created on :
	 * 03/05/2018
	 *********************************************************************************************/

	public void TC_48() throws Exception {
		String testCaseName = "TC_48";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to view the list of columns of the Extract table";
		String passTestCaseDesc = "User is able to view the list of columns of the Extract table";
		String failTestCaseDesc = "User is not able to view the list of columns of the Extract table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
					BrowserInitialization.driver.findElement(XpathRepository.by_listButtonExtracts).click();
					String listColumns = BrowserInitialization.driver.findElement(XpathRepository.by_listButtonClickedExtracts).getText();
					// logger.info(listColumns);
					String expectedColumns = "select, Extract Type, Date Created, Time Created, Extract ID, File Name, Approval Status, FedEx ID,Date/Time";
					// logger.info(listColumns);

					if (expectedColumns.equals(expectedColumns)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to select the required columns to
	 * be viewed in the Comparison Reports table from the List button. Created
	 * on : 03/05/2018
	 *********************************************************************************************/

	public void TC_49() throws Exception {
		String testCaseName = "TC_49";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to select the required columns in the Comparison Reports table from the List button";
		String passTestCaseDesc = "User is able to select the required columns in the Comparison Reports table from the List button";
		String failTestCaseDesc = "User is not able to select the required columns in the Comparison Reports table from the List button";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listButtonExtracts, 60);
					BrowserInitialization.driver.findElement(XpathRepository.by_listButtonExtracts).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_unselectExtractType, 60);
					BrowserInitialization.driver.findElement(XpathRepository.by_unselectExtractType).click();

					boolean unselect = BrowserInitialization.driver.findElement(XpathRepository.by_extractTypeHeader).isDisplayed();
					// logger.info(unselect);

					if (!unselect) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that time of creation of extract is displayed in the
	 * "Time Created" column of the Extract Table.
	 * 
	 * Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_50() throws Exception {
		String testCaseName = "TC_50";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that time of creation of extract is displayed in the Time Created column of the Extract Table.";
		String passTestCaseDesc = "Time of creation of extract is displayed in the Time Created column of the Extract Table";
		String failTestCaseDesc = "Time of creation of extract is not displayed in the Time Created column of the Extract Table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 60);

					String Policygrid = DataObject.getVariable("DropDownMenu", "TC_50");
					// logger.info(Policygrid);

					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
					policygrid_dropdown.selectByVisibleText(Policygrid);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
					BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_successmsg, 60);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 60);
					String ActualTime = BrowserInitialization.driver.findElement(XpathRepository.by_timeCreated).getText();

					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
					String expectedTime = sdf.format(new Date());
					Date expectedTimeDateFormat = sdf.parse(expectedTime);
					// logger.info("i" + expectedTimeDateFormat);
					Date actualTimeDateFormat = sdf.parse(ActualTime);

					// logger.info(sdf.format(actualTimeDateFormat));

					if (actualTimeDateFormat.equals(expectedTimeDateFormat)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that date of creation of extract is displayed in the
	 * "Date/Time" column of the Extract Table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_51() throws Exception {
		String testCaseName = "TC_51";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that date of creation of extract is displayed in the Date/Time column of the Extract Table.";
		String passTestCaseDesc = "Date of creation of extract is displayed in the Date/Time column of the Extract Table";
		String failTestCaseDesc = "Date of creation of extract is not displayed in the Date/Time column of the Extract Table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listButtonExtracts, 60);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_dateCreated, 60);
					int prv_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
					// logger.info("Record Count " + prv_record_count);
					int exp_record_count = prv_record_count + 1;

					String Policygrid = DataObject.getVariable("DropDownMenu", "TC_51");
					// logger.info(Policygrid);

					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
					policygrid_dropdown.selectByVisibleText(Policygrid);

					BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_successmsg, 60);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 60);
					String ActualDate = BrowserInitialization.driver.findElement(XpathRepository.by_dateCreated).getText();
					String expectedDate = Application.system_date();
					if (expectedDate.equals(ActualDate)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()
	/**********************************************************************************************
	 * Description: Verify that user is able to sort New Extract type column in
	 * ascending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_52() throws Exception {
		String testCaseName = "TC_52";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort New Extract type column in ascending order of the Comparison Reports table";
		String passTestCaseDesc = "User is able to sort New Extract type column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = "User is not able to sort New Extract type column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(20000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();

					if (Application.sort_Ascending(XpathRepository.by_NewExtractTypeList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort New Extract ID column in
	 * ascending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_53() throws Exception {
		String testCaseName = "TC_53";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to sort New Extract ID column in ascending order of the Comparison Reports table";
		String passTestCaseDesc = "User is able to sort New Extract ID column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = "User is not able to sort New Extract ID column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					BrowserInitialization.driver.findElement(XpathRepository.by_sortNewExtractID).click();

					if (Application.sort_Ascending(XpathRepository.by_NewExtractIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Old Extract type column in
	 * ascending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_54() throws Exception {
		String testCaseName = "TC_54";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to sort Old Extract type column in ascending order of the Comparison Reports table";
		String passTestCaseDesc = "User is able to sort Old Extract type column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = "User is not able to sort Old Extract type column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();

					if (Application.sort_Ascending(XpathRepository.by_OldExtractTypeList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Old Extract Id column in
	 * ascending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_55() throws Exception {
		String testCaseName = "TC_55";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to sort Old Extract Id column in ascending order of the Comparison Reports table.";
		String passTestCaseDesc = "User is able to sort Old Extract Id column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = "User is not able to sort Old Extract Id column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					BrowserInitialization.driver.findElement(XpathRepository.by_sortOldExtractID).click();

					if (Application.sort_Ascending(XpathRepository.by_OldExtractIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Name-ID column in ascending
	 * order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_56() throws Exception {
		String testCaseName = "TC_56";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort Name-ID column in ascending order of the Comparison Reports table";
		String passTestCaseDesc = "User is able to sort Name-ID column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = "User is not able to sort Name-ID column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					BrowserInitialization.driver.findElement(XpathRepository.by_sortFedExID).click();

					if (Application.sort_Ascending(XpathRepository.by_FedExIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Report ID column in
	 * ascending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_59() throws Exception {
		String testCaseName = "TC_59";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort Report ID column in ascending order of the Comparison Reports table";
		String passTestCaseDesc = "User is able to sort Report ID column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = "User is not able to sort Report ID column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					BrowserInitialization.driver.findElement(XpathRepository.by_sortReportID).click();
					Thread.sleep(2000);

					if (Application.sort_Ascending(XpathRepository.by_ReportIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Status column in ascending
	 * order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_60() throws Exception {
		String testCaseName = "TC_60";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort Status column in ascending order of the Comparison Reports table";
		String passTestCaseDesc = " User is able to sort Status column in ascending order of the Comparison Reports table";
		String failTestCaseDesc = " User is not able to sort Status column in ascending order of the Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					BrowserInitialization.driver.findElement(XpathRepository.by_sortStatus).click();

					if (Application.sort_Ascending(XpathRepository.by_StatusList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
	// Descending Sorting

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort New Extract type column in
	 * descending order of the Comparision Reports table. Created on :
	 * 03/05/2018
	 *********************************************************************************************/

	public void TC_61() throws Exception {
		String testCaseName = "TC_61";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort New Extract type column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort New Extract type column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort New Extract type column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();

					if (Application.sort_descending(XpathRepository.by_NewExtractTypeList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort New Extract ID column in
	 * descending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_62() throws Exception {
		String testCaseName = "TC_62";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort New Extract ID column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort New Extract ID column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort New Extract ID column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortNewExtractID).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortNewExtractID).click();
					Thread.sleep(1000);

					if (Application.sort_descending(XpathRepository.by_NewExtractIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Old Extract type column in
	 * descending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_63() throws Exception {
		String testCaseName = "TC_63";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to sort Old Extract type column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort Old Extract type column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort Old Extract typ column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();
					Thread.sleep(1000);

					if (Application.sort_descending(XpathRepository.by_OldExtractTypeList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Old Extract Id column in
	 * descending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_64() throws Exception {
		String testCaseName = "TC_64";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to sort Old Extract Id column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort Old Extract Id column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort Old Extract Id column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortOldExtractID).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortOldExtractID).click();
					Thread.sleep(1000);

					if (Application.sort_descending(XpathRepository.by_OldExtractIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Fedex-ID column in
	 * descending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_65() throws Exception {
		String testCaseName = "TC_65";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to sort Fedex-ID column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort Fedex-ID column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort Fedex-ID column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortFedExID).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortFedExID).click();
					Thread.sleep(1000);

					if (Application.sort_descending(XpathRepository.by_FedExIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Report ID column in
	 * descending order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_68() throws Exception {
		String testCaseName = "TC_68";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to sort Report ID column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort Report ID column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort Report ID column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortReportID).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortReportID).click();

					if (Application.sort_descending(XpathRepository.by_ReportIDList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify that user is able to sort Status column in descending
	 * order of the Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_69() throws Exception {
		String testCaseName = "TC_69";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to sort Status column in descending order of the Comparision Reports table";
		String passTestCaseDesc = "User is able to sort Status column in descending order of the Comparision Reports table";
		String failTestCaseDesc = "User is not able to sort Status column in descending order of the Comparision Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortStatus).click();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_sortStatus).click();

					if (Application.sort_descending(XpathRepository.by_StatusList)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test()

	/**********************************************************************************************
	 * Description: Verify the New Extracts Type and Old Extract Type are sorted
	 * simultaneously. Created on : 03/05/2018
	 *********************************************************************************************/

	public void TC_70() throws Exception {
		String testCaseName = "TC_70";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify the New Extracts Type and Old Extract Type are sorted simultaneously";
		String passTestCaseDesc = "New Extracts Type and Old Extract Type are sorted simultaneously";
		String failTestCaseDesc = "New Extracts Type and Old Extract Type are not sorted simultaneously";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,600)", "");

					Thread.sleep(1000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_timeCreated, 120);

					BrowserInitialization.driver.findElement(XpathRepository.by_sortExtractType).click();
					Thread.sleep(1000);

					ArrayList<WebElement> List_GetAtoZ = (ArrayList<WebElement>) BrowserInitialization.driver.findElements(XpathRepository.by_NewExtractTypeList);

					ArrayList<String> Str_StoreAtoZ = new ArrayList<String>();
					for (WebElement ExtractAtoZ : List_GetAtoZ)

					{

						Str_StoreAtoZ.add(ExtractAtoZ.getText());
					}
					ArrayList<WebElement> List_GetAtoZOld = (ArrayList<WebElement>) BrowserInitialization.driver.findElements(XpathRepository.by_OldExtractTypeList);

					ArrayList<String> Str_StoreAtoZOld = new ArrayList<String>();
					for (WebElement ExtractAtoZOld : List_GetAtoZOld)

					{

						Str_StoreAtoZOld.add(ExtractAtoZOld.getText());
					}
					int counterB = 1;
					for (int i = 1; i < Str_StoreAtoZ.size(); i++) {

						// Compare the results
						if (Str_StoreAtoZ.get(i).equals(Str_StoreAtoZOld.get(i)))

						{

							counterB++;

						} else

						{
							Boolean flag = false;

							Assert.assertEquals(Enable, flag);
						}

					}

					if (counterB == Str_StoreAtoZ.size()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to view Refresh button for
	 * Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_71() throws Exception {
		String testCaseName = "TC_71";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to view Refresh button for Comparison Reports table";
		String passTestCaseDesc = "User is able to view Refresh button for Comparison Reports table";
		String failTestCaseDesc = "User is not able to view Refresh button for Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Application.ScrollByVisibleElement(XpathRepository.by_Refresh_button);

					BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_Refresh_button).isDisplayed()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to Refresh the data of Comparison
	 * Reports table Table by clicking on the Refresh button. Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_72() throws Exception {
		String testCaseName = "TC_72";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to Refresh the data of Comparison Reports table Table by clicking on the Refresh button";
		String passTestCaseDesc = "User is able to Refresh the data of Comparison Reports table Table by clicking on the Refresh button";
		String failTestCaseDesc = "User is not able to Refresh the data of Comparison Reports table Table by clicking on the Refresh button";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				BrowserInitialization.driver.navigate().refresh();
				// BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 150);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Thread.sleep(2000);
					Application.ScrollByVisibleElement(XpathRepository.by_ComparisionsTitle);
					Thread.sleep(5000);
					List<WebElement> RowCount = BrowserInitialization.driver.findElements(XpathRepository.by_rowcount);
					int prevcount = RowCount.size();
					BrowserInitialization.driver.findElement(XpathRepository.by_Limittextbox).clear();
					BrowserInitialization.driver.findElement(XpathRepository.by_Limittextbox).sendKeys("15");
					BrowserInitialization.driver.findElement(XpathRepository.by_Refresh_button).click();
					Thread.sleep(5000);
					List<WebElement> RowCount2 = BrowserInitialization.driver.findElements(XpathRepository.by_rowcount);
					int curcount = RowCount2.size();
					if (curcount != prevcount) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that the user's Name and EID is displayed in the top
	 * right corner on the Policy Grid Monthly Processes screen Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_73() throws Exception {
		String testCaseName = "TC_73";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that the user's Name and EID is displayed in the top right corner on the Policy Grid Monthly Processes screen ";
		String passTestCaseDesc = "User's Name and EID is displayed in the top right corner on the Policy Grid Monthly Processes screen";
		String failTestCaseDesc = "User's Name and EID is not displayed in the top right corner on the Policy Grid Monthly Processes screen";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_Screenname).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Thread.sleep(2000);

					String username = Backend.getProperty("AdminUser");
					// logger.info(username);

					String LoggedinUser = BrowserInitialization.driver.findElement(XpathRepository.by_loggedinuser).getText();

					String ActualUser = LoggedinUser.substring(12, 19);
					// logger.info(ActualUser);

					if (ActualUser.equals(username)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to enter the value in Limit section
	 * and limit no. indicates the number of records displayed in the Extracts
	 * table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_74() throws Exception {
		String testCaseName = "TC_74";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to enter the value in Limit section and limit no. indicates the number of records displayed in the Extracts table ";
		String passTestCaseDesc = "User is able to enter the value in Limit section and limit no. indicates the number of records displayed in the Extracts table ";
		String failTestCaseDesc = "User is not able to enter the value in Limit section and limit no. indicates the number of records displayed in the Extracts table ";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					String Limit = DataObject.getVariable("Limit", "TC_74");
					// logger.info(Limit);

					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(Limit);
					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractRefreshbutton).click();

					Thread.sleep(3000);

					String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_Extractlimitcount).getText();
					// logger.info(LimitCount);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_74");

					if (LimitCount.equals(Limit)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to enter the value in Page Number
	 * below Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_77() throws Exception {
		String testCaseName = "TC_77";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to enter the value in Page Number below Comparison Reports table ";
		String passTestCaseDesc = "User is able to enter the value in Page Number below Comparison Reports table ";
		String failTestCaseDesc = "User is not able to enter the value in Page Number below Comparison Reports table ";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 100);
				BrowserInitialization.driver.navigate().refresh();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					Application.ScrollByVisibleElement(XpathRepository.by_ComparisionsTitle);
					Thread.sleep(1000);

					js = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js).executeScript("window.scrollBy(0,1000)", "");
					Thread.sleep(1000);

					String Limit = DataObject.getVariable("Limit", "TC_77");
					// logger.info(Limit);

					Application.findButtonAndClick(XpathRepository.by_ComparisionPageNumber);
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_ComparisionPageNumber).clear();
					Thread.sleep(1000);
					BrowserInitialization.driver.findElement(XpathRepository.by_ComparisionPageNumber).sendKeys(Limit);
					Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
					Thread.sleep(3000);
					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");
					Thread.sleep(50000);

					String RecCount = BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionrecordcount).getText();
					// logger.info("Record Count:"+RecCount);
					String PageCount = RecCount.replace("0", "");
					// logger.info("Updated COunt"+PageCount);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_77");

					if (PageCount.equals(Limit)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to see "<<" and "<" icons in
	 * disable state in Page Number section below Comparison Reports table when
	 * navigated to GPR EXTRACT REPORTS.
	 *********************************************************************************************/
	public void TC_78() throws Exception {
		String testCaseName = "TC_78";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to see << and < icons in disable state in Page Number section below Comparison Reports table when navigated to GPR EXTRACT REPORTS.";
		String passTestCaseDesc = "User is able to see << and < icons in disable state in Page Number section below Comparison Reports table when navigated to GPR EXTRACT REPORTS. ";
		String failTestCaseDesc = "User is not able to see << and < icons in disable state in Page Number section below Comparison Reports table when navigated to GPR EXTRACT REPORTS.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Thread.sleep(5000);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_78");

					if (!BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionfirstpgbutton).isEnabled()
							&& !BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionprevpgbutton).isEnabled()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to navigate to last page when user
	 * click on ">>" icon below Comparison Reports table. Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_81() throws Exception {
		String testCaseName = "TC_81";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to navigate to last page when user click on >> icon below Comparison Reports table";
		String passTestCaseDesc = "User is able to navigate to last page when user click on >> icon below Comparison Reports table. ";
		String failTestCaseDesc = "User is not able to navigate to last page when user click on >> icon below Comparison Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Thread.sleep(5000);

					Application.findButtonAndClick(XpathRepository.by_Comparisionlastpgbutton);
					Thread.sleep(1000);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_81");

					if (!BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionlastpgbutton).isEnabled()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to navigate to first page when user
	 * click on "<<" icon below Comparison Reports table. Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_82() throws Exception {
		String testCaseName = "TC_82";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to navigate to first page when user click on << icon below Comparison Reports table";
		String passTestCaseDesc = "User is able to navigate to first page when user click on << icon below Comparison Reports table. ";
		String failTestCaseDesc = "User is not able to navigate to first page when user click on << icon below Comparison Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Thread.sleep(5000);

					Application.findButtonAndClick(XpathRepository.by_Comparisionnextpgbutton);
					Thread.sleep(1000);

					Application.findButtonAndClick(XpathRepository.by_Comparisionfirstpgbutton);
					Thread.sleep(1000);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_82");

					if (!BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionfirstpgbutton).isEnabled()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to go to the next page by clicking
	 * on ">" icon below Comparison Reports table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_83() throws Exception {
		String testCaseName = "TC_83";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to go to the next page by clicking on > icon below Comparison Reports table.";
		String passTestCaseDesc = "User is able to go to the next page by clicking on > icon below Comparison Reports table. ";
		String failTestCaseDesc = "User is not able to go to the next page by clicking on > icon below Comparison Reports table.";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Thread.sleep(5000);

					String RecCount = BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionrecordcount).getText();
					String PrevPageCount = RecCount.substring(0, 1);
					// logger.info(PrevPageCount);

					Application.findButtonAndClick(XpathRepository.by_Comparisionnextpgbutton);
					Thread.sleep(1000);

					String RecordCount = BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionrecordcount).getText();
					String CurrPageCount = RecordCount.substring(0, 1);
					// logger.info(CurrPageCount);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_83");

					if (PrevPageCount != CurrPageCount) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to go to the previous page by
	 * clicking on "<" icon below Comparison Reports table. Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_84() throws Exception {
		String testCaseName = "TC_84";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that user is able to go to the previous page by clicking on < icon below Comparison Reports table.";
		String passTestCaseDesc = "User is able to go to the previous page by clicking on < icon below Comparison Reports table. ";
		String failTestCaseDesc = "User is not able to go to the previous page by clicking on < icon below Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Thread.sleep(10000);

					Application.findButtonAndClick(XpathRepository.by_Comparisionnextpgbutton);
					Thread.sleep(1000);

					String RecCount = BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionrecordcount).getText();
					int PrevPageCount = Integer.parseInt(RecCount.substring(0, 1));
					// logger.info(PrevPageCount);

					Application.findButtonAndClick(XpathRepository.by_Comparisionprevpgbutton);
					Thread.sleep(1000);

					String RecordCount = BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionrecordcount).getText();

					int CurrPageCount = Integer.parseInt(RecordCount.substring(0, 1));
					// logger.info(CurrPageCount);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_84");

					if (PrevPageCount > CurrPageCount) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to see Result lable showing record
	 * in "Results: 1 - 10 / 54" format for the no. of records displaying for
	 * Comparison Reports table Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_85() throws Exception {
		String testCaseName = "TC_85";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to see Result lable showing record in Results: 1 - 10 / 54 format for the no. of records displaying for Comparison Reports table";
		String passTestCaseDesc = "User is able to see Result lable showing record in Results: 1 - 10 / 54 format for the no. of records displaying for Comparison Reports table ";
		String failTestCaseDesc = "User is not able to see Result lable showing record in Results: 1 - 10 / 54 format for the no. of records displaying for Comparison Reports table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					String actResults = BrowserInitialization.driver.findElement(XpathRepository.by_resultsComparision).getText();

					// logger.info("actResults initial : " + actResults);
					String Act = actResults.replaceAll("Results: ", "");
					// logger.info("actResults : " + Act);
					Act = Act.replaceAll(" ", "");
					// logger.info("actResults : " + Act);

					String[] first = Act.split("-");
					String[] second = first[1].split("/");

					// logger.info(first[0].length());
					// logger.info(second[0].length());
					// logger.info(second[1].length());
					// logger.info(Pattern.matches("\\d{" +
					// first[0].length() + "}-\\d{" + second[0].length() +
					// "}/\\d{" + second[1].length() + "}", Act));

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_85");

					if (Pattern.matches("\\d{" + first[0].length() + "}-\\d{" + second[0].length() + "}/\\d{" + second[1].length() + "}", Act)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify If more than 10 results (by default)user can click on
	 * next page arrow or last page arrow Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_86() throws Exception {
		String testCaseName = "TC_86";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify If more than 10 results (by default)user can click on next page arrow or last page arrow";
		String passTestCaseDesc = "If more than 10 results (by default)user can click on next page arrow or last page arrow";
		String failTestCaseDesc = "If more than 10 results (by default)user can not click on next page arrow or last page arrow";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ComparisionLimitCount, 20);

					String ComparisionRecord = BrowserInitialization.driver.findElement(XpathRepository.by_ComparisionLimitCount).getText();
					int Records = Integer.parseInt(ComparisionRecord);
					// logger.info(Records);

					String RecCount = BrowserInitialization.driver.findElement(XpathRepository.by_Comparisionrecordcount).getText();
					int PrevPageCount = Integer.parseInt(RecCount.substring(0, 1));
					// logger.info(PrevPageCount);

					int exprecord = 10;

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_86");

					if (Records > exprecord) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to view List button containing the
	 * list of columns in the table of Extacts table on right hand side corner
	 * above Extracts table beside Refresh button. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_87() throws Exception {
		String testCaseName = "TC_87";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "  Verify that user is able to view List button containing the list of columns in the table of Extacts table on right hand side corner above Extracts table beside Refresh button";
		String passTestCaseDesc = "User is able to view List button containing the list of columns in the table of Extacts table on right hand side corner above Extracts table beside Refresh button";
		String failTestCaseDesc = "User is not able to view List button containing the list of columns in the table of Extacts table on right hand side corner above Extracts table beside Refresh button";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ComparisionLimitCount, 20);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_87");

					if (BrowserInitialization.driver.findElement(XpathRepository.by_listButtonReports).isDisplayed()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to click on List button containing
	 * the list of columns in the table of Extracts table on right hand side
	 * corner above Extracts table beside Refresh button. Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_88() throws Exception {
		String testCaseName = "TC_88";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to click on List button containing the list of columns in the table of Extracts table on right hand sidecorner above Extracts table beside Refresh button";
		String passTestCaseDesc = "User is able to click on List button containing the list of columns in the table of Extracts table on right hand sidecorner above Extracts table beside Refresh button";
		String failTestCaseDesc = "User is not able to click on List button containing the list of columns in the table of Extracts table on right hand sidecorner above Extracts table beside Refresh button";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ComparisionLimitCount, 20);

					BrowserInitialization.driver.findElement(XpathRepository.by_listButtonReports).click();
					String listColumns = BrowserInitialization.driver.findElement(XpathRepository.by_listButtonClicked).getText();
					// logger.info(listColumns);
					String expectedColumns = "select, Extract Type, Date Created, Time Created, Extract ID, File Name, Approval Status, FedEx ID,Date/Time";
					// logger.info(listColumns);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_88");

					if (expectedColumns.equals(expectedColumns)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that user is able to select the required columns to
	 * be viewed in the Extracts table from the List button. Created on :
	 * 03/05/2018
	 *********************************************************************************************/
	public void TC_89() throws Exception {
		String testCaseName = "TC_89";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to select the required columns to be viewed in the Extracts table from the List butto";
		String passTestCaseDesc = "User is able to select the required columns to be viewed in the Extracts table from the List butto";
		String failTestCaseDesc = "User is not able to select the required columns to be viewed in the Extracts table from the List butto";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,1000)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ComparisionLimitCount, 20);

					BrowserInitialization.driver.findElement(XpathRepository.by_listButtonReports).click();

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_unselectNewExtracttype, 20);
					BrowserInitialization.driver.findElement(XpathRepository.by_unselectNewExtracttype).click();

					Thread.sleep(2000);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_89");

					if (!BrowserInitialization.driver.findElement(XpathRepository.by_NewExtracttypeheader).isDisplayed()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that time of creation of extract is displayed in the
	 * "Time Created" (in XXXX format) column of the Comparision Reports Table.
	 * Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_90() throws Exception {
		String testCaseName = "TC_90";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that user is able to select the required columns to be viewed in the Extracts table from the List butto";
		String passTestCaseDesc = "User is able to select the required columns to be viewed in the Extracts table from the List butto";
		String failTestCaseDesc = "User is not able to select the required columns to be viewed in the Extracts table from the List butto";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,500)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_firsttimeComparision, 20);

					String Time = BrowserInitialization.driver.findElement(XpathRepository.by_firsttimeComparision).getText();
					String Formatedtime = Time.substring(0, 7);
					// logger.info(Formatedtime);

					String pattern = "\\d(\\d)?:\\d(\\d)?\\s\\D{2}";

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_90");
					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_89");

					if (Pattern.matches(pattern, Formatedtime)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that date of creation of extract is displayed in the
	 * "Date/Time" (DDMMMYYYY) column of the Comparison Reports Table. Created
	 * on : 03/05/2018
	 *********************************************************************************************/
	public void TC_91() throws Exception {
		String testCaseName = "TC_91";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that date of creation of extract is displayed in the Date/Time (DDMMMYYYY) column of the Comparison Reports Table";
		String passTestCaseDesc = "Date of creation of extract is displayed in the Date/Time (DDMMMYYYY) column of the Comparison Reports Table";
		String failTestCaseDesc = "Date of creation of extract is not displayed in the Date/Time (DDMMMYYYY) column of the Comparison Reports Table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				// BrowserInitialization.driver.navigate().refresh();
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,500)", "");

					Thread.sleep(2000);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_firstdateComparision, 20);

					String Date = BrowserInitialization.driver.findElement(XpathRepository.by_firstdateComparision).getText();
					// logger.info(Date);

					String pattern = "^((0[0-9]||1[0-2])/[0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$";

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_91");
					if (Pattern.matches(pattern, Date)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that clear selections button is disabled after the
	 * page loded in Extract Table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_92() throws Exception {
		String testCaseName = "TC_92";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = " Verify that clear selections button is disabled after the page loded in Extract Table";
		String passTestCaseDesc = "clear selections button is disabled after the page loded in Extract Table";
		String failTestCaseDesc = "clear selections button is not disabled after the page loded in Extract Table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,500)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Clearselectionbutton, 20);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_92");

					if (!BrowserInitialization.driver.findElement(XpathRepository.by_Clearselectionbutton).isEnabled()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that clear selections button is enabled when the user
	 * select altleast one checkbox in Extract Table. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_93() throws Exception {
		String testCaseName = "TC_93";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that clear selections button is enabled when the user select altleast one checkbox in Extract Table";
		String passTestCaseDesc = "clear selections button is enabled when the user select altleast one checkbox in Extract Table";
		String failTestCaseDesc = " clear selections button is not enabled when the user select altleast one checkbox in Extract Table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Application.findButtonAndClick(XpathRepository.by_Selectfirstcheckbox);

					JavascriptExecutor js2 = (JavascriptExecutor) BrowserInitialization.driver;
					((JavascriptExecutor) js2).executeScript("window.scrollBy(0,500)", "");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Clearselectionbutton, 20);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_93");

					if (BrowserInitialization.driver.findElement(XpathRepository.by_Clearselectionbutton).isEnabled()) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that error message"Selected Extracts are already
	 * compared!" is thrown when user selects Extract Type which are already
	 * compared. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_95() throws Exception {
		String testCaseName = "TC_95";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that clear selections button is enabled when the user select altleast one checkbox in Extract Table";
		String passTestCaseDesc = "clear selections button is enabled when the user select altleast one checkbox in Extract Table";
		String failTestCaseDesc = " clear selections button is not enabled when the user select altleast one checkbox in Extract Table";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 60);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTotallimitcount).getText();
					// logger.info(LimitCount);

					Application.ScrollByVisibleElement(XpathRepository.by_Comaprebutton);

					Thread.sleep(3000);

					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
					Thread.sleep(3000);
					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
					Thread.sleep(3000);

					Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);

					Thread.sleep(5000);

					String ExtractID1 = BrowserInitialization.driver.findElement(XpathRepository.by_FirstNewExtractID).getText();
					String ExtractID2 = BrowserInitialization.driver.findElement(XpathRepository.by_FirstOldExtractID).getText();

					List<WebElement> ExtractID = BrowserInitialization.driver.findElements(XpathRepository.by_ExtractIDCount);
					int ExtractIDcount = ExtractID.size();
					// logger.info(ExtractIDcount);

					int ExtractCol1 = 0;
					int ExtractCol2 = 0;

					for (int j = 0; j < ExtractIDcount; j++) {
						if (ExtractID.get(j).getText().equals(ExtractID1)) {
							ExtractCol1 = j + 1;
							break;
						}
					}

					for (int k = 0; k < ExtractIDcount; k++) {
						if (ExtractID.get(k).getText().equals(ExtractID2)) {
							ExtractCol2 = k + 1;
							break;
						}
					}

					BrowserInitialization.driver.findElement(By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[" + ExtractCol1 + "]")).click();
					Thread.sleep(2000);
					BrowserInitialization.driver.findElement(By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[" + ExtractCol2 + "]")).click();
					Thread.sleep(2000);

					Application.ScrollByVisibleElement(XpathRepository.by_ExtractLimittextbox);

					Thread.sleep(3000);

					Application.findButtonAndClick(XpathRepository.by_Comaprebutton);

					Thread.sleep(5000);

					// logger.info("Button clicked");

					String Msg = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractAlreadyComparedMsg).getText();
					// logger.info(Msg);

					String ExpectedMsg = "Selected Extracts are already compared!";
					// logger.info(ExpectedMsg);

					String file_name = Backend.makeDirectory("TC_95");

					if (Msg.contains(ExpectedMsg)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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

	@Test
	/**********************************************************************************************
	 * Description: Verify that error message"Selected Extracts are already
	 * compared!" is thrown when user selects Extract Type which are already
	 * compared. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_96() throws Exception {
		String testCaseName = "TC_96";
		logger.info("Executing the Test Case No. " + testCaseName);
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that error message Selected Extracts are already compared is thrown when user selects Extract Type which are already compared";
		String passTestCaseDesc = "Selected Extracts are already compared! is thrown when user selects Extract Type which are already compared";
		String failTestCaseDesc = " Selected Extracts are already compared! is not thrown when user selects Extract Type which are already compared";

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.navigate().refresh();

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 100);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					// Thread.sleep(5000);

					// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
					// ObjectRepository.by_ExtractTotallimitcount, 10);

					String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTotallimitcount).getText();
					// logger.info(LimitCount);

					Application.ScrollByVisibleElement(XpathRepository.by_Comaprebutton);

					Thread.sleep(3000);

					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
					Thread.sleep(3000);
					BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
					Thread.sleep(3000);

					Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);

					Thread.sleep(5000);

					List<WebElement> ExtractName = BrowserInitialization.driver.findElements(XpathRepository.by_Extractrowcount);
					int extractcount = ExtractName.size();
					// logger.info(extractcount);

					String FirstExtract = ExtractName.get(0).getText();
					int rowmatch = 0;

					for (int i = 1; i < extractcount; i++) {
						String Name = ExtractName.get(i).getText();
						if (!Name.equals(FirstExtract)) {
							rowmatch = i;
							break;
						}

					}

					// logger.info(rowmatch);
					int f = rowmatch + 1;

					BrowserInitialization.driver.findElement(By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[1]")).click();
					Thread.sleep(2000);
					BrowserInitialization.driver.findElement(By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[" + f + "]")).click();
					Thread.sleep(2000);

					Application.ScrollByVisibleElement(XpathRepository.by_ExtractLimittextbox);

					Thread.sleep(3000);

					Application.findButtonAndClick(XpathRepository.by_Comaprebutton);

					Thread.sleep(5000);

					// logger.info("Button clicked");

					String Msg = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractAlreadyComparedMsg).getText();
					// logger.info(Msg);

					String ExpectedMsg = "Select Extracts of same Type!";
					// logger.info(ExpectedMsg);

					// String file_name =
					// BackendCommonFunctionality.makeDirectory("TC_96");

					if (Msg.contains(ExpectedMsg)) {
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
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
