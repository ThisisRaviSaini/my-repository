package com.plefs.gpr.scripts;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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

public class US_255818 {
	Logger logger = Logger.getLogger(US_255818.class);
	private static final Object Enable = null;
	public static int rowmatch;
	public static String exceptionMsg = "Exception in the Code";

	// ********************************************************************************************//
	// Description: Verify the user is able to view List button for "selection
	// of Columns to be displayed" on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test
	public void TC_01() throws Exception {
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to view List button for selection of Columns to be displayed on View Report Screen";
		String passTestCaseDesc = "User is able to view List button for selection of Columns to be displayed on View Report Screen";
		String failTestCaseDesc = "User is not able to view List button for selection of Columns to be displayed on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								break;
							}
						}

						if (BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).isDisplayed()) {
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify the user is able to click on List button for
	// "selection of Columns to be displayed" and view column name dsiplayed
	// with checkboxes for selection on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to click on List button for selection of Columns to be displayed  and view column name dsiplayed with checkboxes for selection on View Report Screen";
		String passTestCaseDesc = "User is able to click on List button for selection of Columns to be displayed  and view column name dsiplayed with checkboxes for selection on View Report Screen";
		String failTestCaseDesc = "User is not able to click on List button for selection of Columns to be displayed  and view column name dsiplayed with checkboxes for selection on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								break;
							}
						}

						if (BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).isEnabled()) {
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify that new and old subheader is unselected after
	 * clicking on the ID Header View Report Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		logger.info("Executing the Test Case No. " + testCaseName);
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify that new and old subheader is unselected after clicking on the ID Header View Report Screen";
		String passTestCaseDesc = "New and old subheader is unselected after clicking on the ID Header View Report Screen";
		String failTestCaseDesc = "New and old subheader is selected after clicking on the ID Header View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(5000);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(Keys.ENTER);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_TimeCreatedDataCompTab, 120);
						Thread.sleep(1000);
						String limit = DataObject.getVariable("LimitField", testCaseName);
						String limitS = limit.substring(0, 4);
						int limitI = Integer.parseInt(limitS);
						String extractId = DataObject.getVariable("Extract ID", testCaseName).substring(0, 4);
						for (int p = 1; p <= limitI; p++) {
							String extractedExtractId = BrowserInitialization.driver
									.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
							if (extractedExtractId.equals(extractId)) {
								BrowserInitialization.driver.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]")).click();
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ID, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_ID).click();
								break;
							}
						}

						String check = "false";
						if (BrowserInitialization.driver.findElement(XpathRepository.by_ID_Old_1).getAttribute("aria-checked").contains(check)
								&& BrowserInitialization.driver.findElement(XpathRepository.by_ID_New_1).getAttribute("aria-checked").contains(check)) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify the user is able to Select or Unselect Header ID and
	// view columns as selected on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test
	public void TC_04() throws Exception {
		String testCaseName = "TC_04";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header ID and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("ID")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_ID).click();

						Thread.sleep(1000);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Thread.sleep(1000);

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("ID")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Thread.sleep(1000);

						BrowserInitialization.driver.findElement(XpathRepository.by_ID).click();
						Thread.sleep(1000);

						if (BrowserInitialization.driver.findElement(XpathRepository.by_ID_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for ID and view columns as selected on View Report Screen Created
	 * on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_05() throws Exception {
		String testCaseName = "TC_05";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for ID and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);
							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);
							int noofsubheader_before = subHeaders_before.size();
							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ID, 120);
							BrowserInitialization.driver.findElement(XpathRepository.by_ID).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_ID_Old).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);
							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);
							int noofsubheader_after = subHeaders_after.size();
							if (noofsubheader_after == (noofsubheader_before - 1)) {
								temp++;
							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ID, 120);
							BrowserInitialization.driver.findElement(XpathRepository.by_ID_Arrow).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_ID_New).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_ID_Old).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							if (noofsubheader_after == (noofsubheader_before - 1)) {
								temp++;
							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Id and view columns as selected on View Report Screen Created
	 * on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_06() throws Exception {
		String testCaseName = "TC_06";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for ID and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ID, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_ID).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_ID_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ID, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_ID_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_ID_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_ID_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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

			Exception e)

			{
				Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);

			} finally

			{
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}

		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Country
	 * Code " and view columns as selected on View Report Screen Created on :
	 * 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_07() throws Exception {
		String testCaseName = "TC_07";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		JavascriptExecutor js = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header Country Code and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header Country Code and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header Country Code and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}

						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("COUNTRY CODE")) {
								temp++;
								break;
							}
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("COUNTRY CODE")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_COUNTRY_CODE, 40);

						BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Country Code and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_08() throws Exception {
		String testCaseName = "TC_08";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for COUNTRY CODE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR COUNTRY CODE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for COUNTRY CODE and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_COUNTRY_CODE, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_COUNTRY_CODE_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Country Code and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_09() throws Exception {
		String testCaseName = "TC_09";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for COUNTRY CODE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR COUNTRY CODE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for COUNTRY CODE and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);

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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_COUNTRY_CODE, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_COUNTRY_CODE_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_COUNTRY_CODE_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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

			Exception e)

			{
				Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);

			} finally

			{
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}

		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "State
	 * Province Code " and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_10() throws Exception {
		String testCaseName = "TC_10";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header State Province Code and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header State Province Code and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header State Province Code and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys("100");

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);

						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("STATE OR PROVINCE CODE")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE).click();
						;

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("STATE OR PROVINCE CODE")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_STATE_OR_PROVINCE_CODE, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for State Province Code and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_11() throws Exception {
		String testCaseName = "TC_11";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for STATE OR PROVINCE CODE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR STATE OR PROVINCE CODE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for STATE OR PROVINCE CODE and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_STATE_OR_PROVINCE_CODE, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_STATE_OR_PROVINCE_CODE_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for State Province Code and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_12() throws Exception {
		String testCaseName = "TC_12";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for STATE OR PROVINCE CODE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR STATE OR PROVINCE CODE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for STATE OR PROVINCE CODE and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_STATE_OR_PROVINCE_CODE, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_STATE_OR_PROVINCE_CODE_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_STATE_OR_PROVINCE_CODE_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	// ********************************************************************************************//
	// Description: Verify the user is able to Select or Unselect Header
	// "Service id " and view columns as selected on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_13() throws Exception {
		String testCaseName = "TC_13";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header Service id and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header Service id and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header Service id and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);

						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("SERVICE ID")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("SERVICE ID")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID, 40);
						BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "Old
	 * Epic" for Country Code and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_14() throws Exception {
		String testCaseName = "TC_14";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old Epic for SERVICE ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old Epic foR SERVICE ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old Epic for SERVICE ID and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
							Thread.sleep(4000);

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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Epic).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Epic).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Policy_Grid).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "Old
	 * Policey Grid" for Country Code and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_15() throws Exception {
		String testCaseName = "TC_15";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old Policy Grid for SERVICE ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old Policy Grid foR SERVICE ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old Policy Grid for SERVICE ID and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 20);

					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);

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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Policy_Grid).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "New
	 * Epic" for Country Code and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_16() throws Exception {
		String testCaseName = "TC_16";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New EPIC for SERVICE ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New EPIC foR SERVICE ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New EPIC for SERVICE ID and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Epic).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "New
	 * Policey Grid" for Country Code and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_17() throws Exception {
		String testCaseName = "TC_17";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New Policy Grid for SERVICE ID and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New Policy Grid foR SERVICE ID and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New Policy Grid for SERVICE ID and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);

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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Policy_Grid).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Arrow).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_ID_Old_Epic, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_Old_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_ID_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Service
	 * Option ids " and view columns as selected on View Report Screen Created
	 * on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_18() throws Exception {
		String testCaseName = "TC_18";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header  Service Option ids   and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header  Service Option ids  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header  Service Option ids  and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys("100");
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_HeaderDiffTable, 20);
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("SERVICE OPTION IDS")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("SERVICE OPTION IDS")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "Old
	 * Epic" for Service Option ids and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_19() throws Exception {
		String testCaseName = "TC_19";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old Epic for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old Epic foR SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old Epic for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);

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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Epic).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Epic).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Policy_Grid).click();
							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "Old
	 * Policey Grid" for Service Option ids and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_20() throws Exception {
		String testCaseName = "TC_20";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old Policy Grid for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old Policy Grid foR SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old Policy Grid for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);

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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Policy_Grid).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header "New
	 * Epic" for Service Option ids and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_21() throws Exception {
		String testCaseName = "TC_21";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New EPIC for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New EPIC foR SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New EPIC for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Epic).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New Policey Grid" for Service Option ids and view columns as selected on
	 * View Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_22() throws Exception {
		String testCaseName = "TC_22";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New Policy Grid for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New Policy Grid foR SERVICE OPTION IDS and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New Policy Grid for SERVICE OPTION IDS and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Policy_Grid).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Arrow).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SERVICE_OPTION_IDS_Old_Epic, 40);

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_Old_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Epic).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SERVICE_OPTION_IDS_New_Policy_Grid).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 3)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	// ********************************************************************************************//
	// Description: Verify the user is able to Select or Unselect Header
	// "IS_Domestic" and view columns as selected on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// **********************/***********************************************************************//
	@Test
	public void TC_23() throws Exception {
		String testCaseName = "TC_23";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header  IS_Domestic  and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header  IS_Domestic  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header  IS_Domestic  and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_HeaderDiffTable, 20);
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("IS DOMESTIC")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("IS DOMESTIC")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_IS_DOMESTIC, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for IS_Domestic and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_24() throws Exception {
		String testCaseName = "TC_24";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for IS DOMESTIC and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR IS DOMESTIC and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for IS DOMESTIC and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_IS_DOMESTIC, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_IS_DOMESTIC_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for IS_Domestic and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_25() throws Exception {
		String testCaseName = "TC_25";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for IS DOMESTIC and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR IS DOMESTIC and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for IS DOMESTIC and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_IS_DOMESTIC, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_IS_DOMESTIC_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_IS_DOMESTIC_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	// ********************************************************************************************//
	// Description: Verify the user is able to Select or Unselect Header "SHIP
	// DATE" and view columns as selected on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_26() throws Exception {
		String testCaseName = "TC_26";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header SHIP DATE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header SHIP DATE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header SHIP DATE and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys("100");
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("SHIP DATE")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("SHIP DATE")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SHIP_DATE, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Effective,Expiration Date and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_27() throws Exception {
		String testCaseName = "TC_27";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for SHIP DATE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR SHIP DATE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for SHIP DATE and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SHIP_DATE, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SHIP_DATE_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Effective,Expiration Date and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_28() throws Exception {
		String testCaseName = "TC_28";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for SHIP DATE and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR SHIP DATE and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for SHIP DATE and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SHIP_DATE, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SHIP_DATE_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_SHIP_DATE_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header
	 * "Permission " and view columns as selected on View Report Screen Created
	 * on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_29() throws Exception {
		String testCaseName = "TC_29";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header Permission and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header Permission and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header Permission and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys("100");
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("Permission")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_Permission).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("Permission")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Permission, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_Permission).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_Permission_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Permission and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_30() throws Exception {
		String testCaseName = "TC_30";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for Permission and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR Permission and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for Permission and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Permission, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Permission_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Permission and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_31() throws Exception {
		String testCaseName = "TC_31";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;

		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for Permission and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR Permission and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for Permission and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Permission, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Permission_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_Permission_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	// ********************************************************************************************//
	// Description: Verify the user is able to Select or Unselect Header
	// "Min Length in" and view columns as selected on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_32() throws Exception {
		String testCaseName = "TC_32";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header  Min Length in   and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header  Min Length in  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header  Min Length in  and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN LENGTH IN")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN LENGTH IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Length in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_33() throws Exception {
		String testCaseName = "TC_33";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MIN Length in and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MIN Length in and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MIN Length in and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_Arrow).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN_New, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Length in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_34() throws Exception {
		String testCaseName = "TC_34";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MIN Length in and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MIN Length in and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MIN Length in and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_Arrow).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_IN_Old, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	// ********************************************************************************************//
	// Description: Verify the user is able to Select or Unselect Header
	// "Max Length in" and view columns as selected on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// *********************************************************************************************//
	@Test
	public void TC_35() throws Exception {
		String testCaseName = "TC_35";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header Max Length in and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header Max Length in  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header Max Length in and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
						Thread.sleep(4000);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX LENGTH IN")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN).click();
						;

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX LENGTH IN")) {
								temp--;
							}

						}
						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_IN, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Length in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_36() throws Exception {
		String testCaseName = "TC_36";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for Max Length in and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR Max Length in and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for Max Length in and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Length in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_37() throws Exception {
		String testCaseName = "TC_37";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for Max Length in and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR Max Length in and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for Max Length in and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Min
	 * Height in " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_38() throws Exception {
		String testCaseName = "TC_38";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header Min Height in and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header Min Height in and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header Min Height in and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);

						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN HEIGHT IN")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN HEIGHT IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_IN, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Height in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_39() throws Exception {
		String testCaseName = "TC_39";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MIN HEIGHT IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MIN HEIGHT IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MIN HEIGHT IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Min Height in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_40() throws Exception {
		String testCaseName = "TC_40";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MIN HEIGHT IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MIN HEIGHT IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MIN HEIGHT IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_URSAUVDSKfedExOnsiteLink, 30);

					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Max
	 * Height in " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_41() throws Exception {
		String testCaseName = "TC_41";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX HEIGHT IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MAX HEIGHT IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MAX HEIGHT IN and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX HEIGHT IN")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX HEIGHT IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_IN, 20);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Height in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_42() throws Exception {
		String testCaseName = "TC_42";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MAX HEIGHT IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MAX HEIGHT IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MAX HEIGHT IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Height in and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_43() throws Exception {
		String testCaseName = "TC_43";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MAX HEIGHT IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MAX HEIGHT IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MAX HEIGHT IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Min
	 * Width in " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_44() throws Exception {
		String testCaseName = "TC_44";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MIN WIDTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MIN WIDTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MIN WIDTH IN and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN WIDTH IN")) {
								temp++;
								break;
							}
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN).click();

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);

						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN WIDTH IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Width in and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_45() throws Exception {
		String testCaseName = "TC_45";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MIN WIDTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MIN WIDTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MIN WIDTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Min Width in and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_46() throws Exception {
		String testCaseName = "TC_46";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header NEW for MIN WIDTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header NEW foR MIN WIDTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header NEW for MIN WIDTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header "Max
	 * Width in " and view columns as selected on View Report Screenn Created on
	 * : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_47() throws Exception {
		String testCaseName = "TC_47";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX WIDTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MAX WIDTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MAX WIDTH IN and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX WIDTH IN")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN).click();
						;
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX WIDTH IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_IN, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_CheckBox).click();
						;

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Width in and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_48() throws Exception {
		String testCaseName = "TC_48";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MAX WIDTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MAX WIDTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MAX WIDTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Width in and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_49() throws Exception {
		String testCaseName = "TC_49";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MAX WIDTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MAX WIDTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MAX WIDTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header "Min
	 * Length plus Girth in " and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_50() throws Exception {
		String testCaseName = "TC_50";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN LENGTH PLUS GIRTH IN")) {
								temp++;
								break;
							}
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN LENGTH PLUS GIRTH IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN, 40);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Length plus Girth in and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_51() throws Exception {
		String testCaseName = "TC_51";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Min Length plus Girth in and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_52() throws Exception {
		String testCaseName = "TC_52";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MIN LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Max
	 * Length plus Girth in " and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_53() throws Exception {
		String testCaseName = "TC_53";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX LENGTH PLUS GIRTH IN")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX LENGTH PLUS GIRTH IN")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Length plus Girth in and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_54() throws Exception {
		String testCaseName = "TC_54";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Length plus Girth in and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_55() throws Exception {
		String testCaseName = "TC_55";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header NEW for MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header NEW foR MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header NEW for MAX LENGTH PLUS GIRTH IN and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_IN_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header "Min
	 * Length Cm " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_56() throws Exception {
		String testCaseName = "TC_56";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MIN LENGTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header  MIN LENGTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header  MIN LENGTH CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN LENGTH CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM).click();
						Thread.sleep(1000);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Thread.sleep(1000);

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN LENGTH CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Length Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_57() throws Exception {
		String testCaseName = "TC_57";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MIN LENGTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MIN LENGTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MIN LENGTH CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Min Length Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_58() throws Exception {
		String testCaseName = "TC_58";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header NEW for MIN LENGTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header NEW foR MIN LENGTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header NEW for MIN LENGTH CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header "Max
	 * Length Cm " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_59() throws Exception {
		String testCaseName = "TC_59";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX LENGTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MAX LENGTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MAX LENGTH CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX LENGTH CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM).click();

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX LENGTH CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_Arrow_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Length Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_60() throws Exception {
		String testCaseName = "TC_60";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MAX LENGTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MAX LENGTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MAX LENGTH CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Length Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_61() throws Exception {
		String testCaseName = "TC_61";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MAX LENGTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MAX LENGTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MAX LENGTH CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header "Min
	 * Height Cm " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_62() throws Exception {
		String testCaseName = "TC_62";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MIN HEIGHT CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header  MIN HEIGHT CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header  MIN HEIGHT CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN HEIGHT CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN HEIGHT CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Height Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_63() throws Exception {
		String testCaseName = "TC_63";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MIN HEIGHT CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old foR MIN HEIGHT CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MIN HEIGHT CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Min Height Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_64() throws Exception {
		String testCaseName = "TC_64";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MIN HEIGHT CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New foR MIN HEIGHT CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MIN HEIGHT CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_HEIGHT_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_HEIGHT_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Max
	 * Height Cm " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_65() throws Exception {
		String testCaseName = "TC_65";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX HEIGHT CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MAX HEIGHT CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MAX HEIGHT CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX HEIGHT CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX HEIGHT CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Height Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_66() throws Exception {
		String testCaseName = "TC_66";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for MAX HEIGHT CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old for  MAX HEIGHT CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for MAX HEIGHT CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Height Cm and view columns as selected on View Report
	 * Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_67() throws Exception {
		String testCaseName = "TC_67";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for MAX HEIGHT CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New for  MAX HEIGHT CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for MAX HEIGHT CM and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_HEIGHT_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_HEIGHT_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Min
	 * Width Cm " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_68() throws Exception {
		String testCaseName = "TC_68";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MIN WIDTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MIN WIDTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MIN WIDTH CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Thread.sleep(20000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN WIDTH CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN WIDTH CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Width Cm and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_69() throws Exception {
		String testCaseName = "TC_69";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for  Min Width Cm and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old for  Max Width Cm and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for Max Width Cm and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Width Cm and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_70() throws Exception {
		String testCaseName = "TC_70";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header NEW for  Min Width Cm and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header NEW for  Max Width Cm and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header NEW for Max Width Cm and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_WIDTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_WIDTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Max
	 * Width Cm " and view columns as selected on View Report Screen Created on
	 * : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_71() throws Exception {
		String testCaseName = "TC_71";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX WIDTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MAX WIDTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MAX WIDTH CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX WIDTH CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX WIDTH CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Width Cm and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_72() throws Exception {
		String testCaseName = "TC_72";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for  Max Width Cm and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old for  Max Width Cm and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for Max Width Cm and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Width Cm and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_73() throws Exception {
		String testCaseName = "TC_73";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for Max Width Cm  and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New for Max Width Cm  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for Max Width Cm and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_WIDTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_WIDTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description:Verify the user is able to Select or Unselect Header "Min
	 * Length plus Girth " and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_74() throws Exception {
		String testCaseName = "TC_74";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MIN LENGTH PLUS GIRTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header MIN LENGTH PLUS GIRTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header MIN LENGTH PLUS GIRTH CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MIN LENGTH PLUS GIRTH CM")) {
								temp++;
								break;
							}
						}

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM, 40);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MIN LENGTH PLUS GIRTH CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM, 40);

						BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Header "Max
	 * Length plus Girth " and view columns as selected on View Report Screen
	 * Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_77() throws Exception {
		String testCaseName = "TC_77";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Header MAX LENGTH PLUS GIRTH CM and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Header  MAX LENGTH PLUS GIRTH CM and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Header  MAX LENGTH PLUS GIRTH CM and view columns as selected on View Report Screen";
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {

				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						// BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys("100");

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

								break;
							}
						}
						List<WebElement> tabHeaders = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders = tabHeaders.size();
						Iterator<WebElement> itr = tabHeaders.iterator();

						while (itr.hasNext()) {
							String headerData = itr.next().getText();
							if (headerData.equals("MAX LENGTH PLUS GIRTH CM")) {
								temp++;
								break;
							}
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 15);
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						List<WebElement> tabHeaders2 = BrowserInitialization.driver.findElements(XpathRepository.by_HeaderDiffTable);
						int noOfHeaders2 = tabHeaders2.size();
						for (int p = 0; p < noOfHeaders2; p++) {
							if (tabHeaders2.get(p).getText().equals("MAX LENGTH PLUS GIRTH CM")) {
								temp--;
							}
						}

						if (noOfHeaders2 == (noOfHeaders - 1)) {
							temp++;
						}

						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM, 15);

						BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM).click();

						if (BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_CheckBox).getAttribute("class")
								.equals("mat-checkbox mat-accent mat-checkbox-anim-unchecked-checked mat-checkbox-checked")) {
							temp++;
						}
						BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

						if (temp == 3) {
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
						Backend.takeScreenshot(testCaseName, screenShotName,

								"FAIL");
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
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Max Length plus Girth and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_78() throws Exception {
		String testCaseName = "TC_78";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for Max Length plus Girth  and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old for Max Length plus Girth  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for Max Length plus Girth  and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Max Length plus Girth and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_79() throws Exception {
		String testCaseName = "TC_79";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for Max Length plus Girth  and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Newfor Max Length plus Girth  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Newfor Max Length plus Girth  and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));

							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MAX_LENGTH_PLUS_GIRTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "New" for Min Length plus Girth and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/

	@Test
	public void TC_76() throws Exception {
		String testCaseName = "TC_76";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header New for Min Length plus Girth  and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header New for Min Length plus Girth  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header New for Min Length plus Girth  and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
							// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
							// ObjectRepository.by_RefreshbttnCompareTab, 20);
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_New).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to Select or Unselect Sub Header
	 * "Old" for Min Length plus Girth and view columns as selected on View
	 * Report Screen Created on : 03/24/2018
	 *********************************************************************************************/
	@Test
	public void TC_75() throws Exception {
		String testCaseName = "TC_75";
		logger.info("Executing the Test Case No. " + testCaseName);
		int temp = 0;
		JavascriptExecutor js = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseDesc = "Verify the user is able to Select or Unselect Sub Header Old for Min Length plus Girth  and view columns as selected on View Report Screen";
		String passTestCaseDesc = "User is able to Select or Unselect Sub Header Old for Min Length plus Girth  and view columns as selected on View Report Screen";
		String failTestCaseDesc = "User is not able to Select or Unselect Sub Header Old for Min Length plus Girth  and view columns as selected on View Report Screen";
		{
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			try {
				if (testRunnable) {

					screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
					Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
					BrowserInitialization.driver.findElement

					(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

						Application.findButtonAndClick(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();

							BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
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
									Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
									Application.findButtonAndClick(XpathRepository.by_ViewSelectedBttn);
									break;
								}
							}

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 40);

							List<WebElement> subHeaders_before = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_before = subHeaders_before.size();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();
							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Old).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Subheader, 20);

							List<WebElement> subHeaders_after = BrowserInitialization.driver.findElements(XpathRepository.by_Subheader);

							int noofsubheader_after = subHeaders_after.size();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Arrow, 20);

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Arrow).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_New).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_MIN_LENGTH_PLUS_GIRTH_CM_Old).click();

							BrowserInitialization.driver.findElement(XpathRepository.by_listBttnDiffTable).click();

							if (noofsubheader_after == (noofsubheader_before - 1)) {

								temp++;

							}

							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("document.querySelector('table th:last-child').scrollIntoView();");

							if (temp == 2) {
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
							Backend.takeScreenshot(testCaseName, screenShotName,

									"FAIL");
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
				Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				logger.info("Execution is completed for Test Case No." + testCaseName);
				Reporter.testCaseEnd();
				logger.info("------------------------------------------------------------------");

			}
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
