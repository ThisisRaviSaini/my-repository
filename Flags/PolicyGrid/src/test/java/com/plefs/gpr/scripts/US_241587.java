package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class US_241587 {
	Logger logger = Logger.getLogger(US_241587.class);
	public static String exceptionMsg = "Exception occured in the script";

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view GPR extracts header
	// in URSA screen
	// Created by : Ravi Saini
	// Created on : 18-April-2018
	// Last updated by : Ravi Saini
	// Last updated : 18-April-2018
	// *********************************************************************************************//

	@Test(priority = 1)
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user are able to create Extract.
	// Created by : Ravi Saini
	// Created on : 18-April-2018
	// Last updated by : Ravi Saini
	// Last updated : 18-April-2018
	// *********************************************************************************************//

	@Test(priority = 2)
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						Select dropDownMenu = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
						dropDownMenu.selectByVisibleText(DataObject.getVariable("DropDownMenu", testCaseName));
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 120);
						Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 120);
						Application.clickInLoop(XpathRepository.by_CreateExtractButton); // Application.clickInLoop(XpathRepository.by_CreateExtractButton);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
						Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage).isDisplayed()) {
							Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 120);
							Application.waitForCursor();
							Thread.sleep(10000);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user are able to create Extract.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(priority = 3)
	public void TC_05() throws Exception {
		String testCaseName = "TC_05";
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
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(15000);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 45);
								if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage).getText().contains(DataObject.getVariable("SuccessMessage", testCaseName))) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
	// Description: Verify that an Admin user is able to 'Approve' the extract
	// file (on the Comparison Report).
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 4)
	public void TC_07() throws Exception {
		String testCaseName = "TC_07";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;
		int temp3 = 0;
		String extractId = null;

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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							extractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractIDDataExtractTab).getText();
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(20000);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Application.clickInLoop(XpathRepository.by_messageClose);
								for (int p = 1; p <= 10; p++) {
									String extractedExtractId = BrowserInitialization.driver
											.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
									if (extractedExtractId.equals(extractId)) {
										Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]"));

										Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
										Application.clickInLoop(XpathRepository.by_approveButton);
										Thread.sleep(3000);
										Application.clickInLoop(XpathRepository.by_ReturnBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
										break;
									}
								}
								Thread.sleep(13000);
								for (int q = 1; q <= 10; q++) {

									if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-fileStorageSeqNbr'])[" + q + "]")).getText().equals(extractId)) {

										if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + q + "]")).getText().equals("A")) {
											temp3++;
											break;
										}

									}

								}

								if (temp3 == 1) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view the existing extract
	// on Policy Grid Monthly Processes Screen.
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 5)
	public void TC_09() throws Exception {
		String testCaseName = "TC_09";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		int temp = 0;
		int temp2 = 0;
		int temp3 = 0;
		String extractId = null;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				Thread.sleep(10000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						if (Application.createExtracts(testCaseName) == 2) {
							extractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractIDDataExtractTab).getText();
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(15000);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								for (int p = 1; p <= 10; p++) {
									String extractedExtractId = BrowserInitialization.driver
											.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
									if (extractedExtractId.equals(extractId)) {
										Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]"));
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
										Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
										Application.clickInLoop(XpathRepository.by_DenyButton);
										Thread.sleep(3000);
										Application.clickInLoop(XpathRepository.by_ReturnBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
										break;
									}
								}
								Application.waitForCursor();
								Thread.sleep(13000);
								for (int q = 1; q <= 10; q++) {

									if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-fileStorageSeqNbr'])[" + q + "]")).getText().equals(extractId)) {
										if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + q + "]")).getText().equals("D")) {
											temp3++;
											break;
										}

									}

								}

								if (temp3 == 1) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view the existing extract
	// on Policy Grid Monthly Processes Screen.
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 6)
	public void TC_11() throws Exception {
		String testCaseName = "TC_11";
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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTable).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view comparison report on
	// Policy Grid Monthly Processes Screenk
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 20)
	public void TC_14() throws Exception {
		String testCaseName = "TC_14";
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
					Application.logoutUser();
					Application.ViewerLogin();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
						Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							js = (JavascriptExecutor) BrowserInitialization.driver;
							js.executeScript("window.scrollBy(0,-200)");
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompTable).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view Approved (A) status
	// of a report on Policy Grid Monthly Processes Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(priority = 8)
	public void TC_15() throws Exception {
		String testCaseName = "TC_15";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						for (int i = 1; i <= 10; i++) {

							if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + i + "]")).getText().equals("A")) {
								temp++;
								break;
							}
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view Approved (A) status
	// of a report on Policy Grid Monthly Processes Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(priority = 9)
	public void TC_17() throws Exception {
		String testCaseName = "TC_17";
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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						for (int i = 1; i <= 10; i++) {

							if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + i + "]")).getText().equals("D")) {
								temp++;
								break;
							}
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify the user is able to view List button for "selection
	// of Columns to be displayed" on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 10)
	public void TC_19() throws Exception {
		String testCaseName = "TC_19";
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
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
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
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								break;
							}
						}
						int noOfRows = BrowserInitialization.driver.findElements(By.xpath("//table//tr")).size();
						if (noOfRows >= 1) {
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
					throw new SkipException(testCaseName + " is Skipped");
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
	// Description: Verify that an Admin user is able to Approve when the
	// extract file is in "C" Status on Policy Grid Monthly Processes Screen App
	// Screen for only one time.
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 11)
	public void TC_21() throws Exception {
		String testCaseName = "TC_21";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;
		int temp3 = 0;
		String extractId = null;

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
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							extractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractIDDataExtractTab).getText();
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Application.waitForCursor();
								Thread.sleep(5000);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								for (int p = 1; p <= 10; p++) {
									String extractedExtractId = BrowserInitialization.driver
											.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
									if (extractedExtractId.equals(extractId)) {
										BrowserInitialization.driver.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]")).click();
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
										Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
										Application.clickInLoop(XpathRepository.by_approveButton);
										Thread.sleep(3000);
										Application.clickInLoop(XpathRepository.by_ReturnBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
										break;
									}
								}
								Application.waitForCursor();
								Thread.sleep(45000);
								for (int q = 1; q <= 10; q++) {

									if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-fileStorageSeqNbr'])[" + q + "]")).getText().equals(extractId)) {

										if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + q + "]")).getText().equals("A")) {
											temp3++;
											break;
										}

									}

								}

								if (temp3 == 1) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view the existing extract
	// on Policy Grid Monthly Processes Screen.
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 12)
	public void TC_22() throws Exception {
		String testCaseName = "TC_22";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		int temp = 0;
		int temp2 = 0;
		int temp3 = 0;
		String extractId = null;

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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						if (Application.createExtracts(testCaseName) == 2) {
							extractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractIDDataExtractTab).getText();
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);

								for (int p = 1; p <= 10; p++) {
									String extractedExtractId = BrowserInitialization.driver
											.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
									if (extractedExtractId.equals(extractId)) {
										Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]"));
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
										Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 40);
										Application.clickInLoop(XpathRepository.by_DenyButton);
										Thread.sleep(3000);
										Application.clickInLoop(XpathRepository.by_ReturnBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
										break;
									}
								}
								Application.waitForCursor();
								Thread.sleep(60000);
								for (int q = 1; q <= 10; q++) {
									if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-fileStorageSeqNbr'])[" + q + "]")).getText().equals(extractId)) {
										if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + q + "]")).getText().equals("D")) {
											temp3++;
											break;
										}

									}

								}

								if (temp3 == 1) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view the existing extract
	// on Policy Grid Monthly Processes Screen.
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 13)
	public void TC_23() throws Exception {
		String testCaseName = "TC_23";
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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						Application.logoutUser();
						if (BrowserInitialization.driver.getCurrentUrl().contains(DataObject.getVariable("Logout URL", testCaseName))) {
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
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}
	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view GPR extracts header
	// in URSA screen
	// Created by : Ravi Saini
	// Created on : 18-April-2018
	// Last updated by : Ravi Saini
	// Last updated : 18-April-2018
	// *********************************************************************************************//

	@Test(priority = 14)
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
				Application.ViewerLogin();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					if (BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}
	// ********************************************************************************************//
	// Description: Verify that Admin user are able to create Extract.
	// Created by : Ravi Saini
	// Created on : 18-April-2018
	// Last updated by : Ravi Saini
	// Last updated : 18-April-2018
	// *********************************************************************************************//

	@Test(priority = 15)
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						Select dropDownMenu = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
						dropDownMenu.selectByVisibleText(DataObject.getVariable("DropDownMenu", testCaseName));
						Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 120);
						Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 120);
						Application.clickInLoop(XpathRepository.by_CreateExtractButton);
						Application.waitForCursor();
						Thread.sleep(60000);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
						Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}
	// ********************************************************************************************//
	// Description: Verify that Admin user are able to create Extract.
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(priority = 16)
	public void TC_06() throws Exception {
		String testCaseName = "TC_06";
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								if (BrowserInitialization.driver.findElement(XpathRepository.by_SuccessMessage).getText().contains(DataObject.getVariable("SuccessMessage", testCaseName))) {

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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that an Admin user is able to 'Approve' the extract
	// file (on the Comparison Report).
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 17)
	public void TC_08() throws Exception {
		String testCaseName = "TC_08";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;
		int temp3 = 0;
		String extractId = null;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 30);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						if (Application.createExtracts(testCaseName) == 2) {
							extractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractIDDataExtractTab).getText();

							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 80);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);

								for (int p = 1; p <= 10; p++) {
									String extractedExtractId = BrowserInitialization.driver
											.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
									if (extractedExtractId.equals(extractId)) {
										Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]"));
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 15);
										Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 40);
										Application.clickInLoop(XpathRepository.by_approveButton);
										Thread.sleep(3000);
										break;
									}
								}

								if (BrowserInitialization.driver.findElement(XpathRepository.by_ErrorMessage).getText().contains(DataObject.getVariable("Error Message", testCaseName))) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that an Admin user is able to 'Approve' the extract
	// file (on the Comparison Report).
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 18)
	public void TC_10() throws Exception {
		String testCaseName = "TC_10";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
		int temp = 0;
		int temp2 = 0;

		String extractId = null;

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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (Application.createExtracts(testCaseName) == 2) {
							extractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractIDDataExtractTab).getText();
							if (BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectedButton).isEnabled()) {
								Application.clickInLoop(XpathRepository.by_CompareSelectedButton);
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);

								for (int p = 1; p <= 10; p++) {
									String extractedExtractId = BrowserInitialization.driver
											.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
									if (extractedExtractId.equals(extractId)) {
										Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]"));
										Application.waitForCursor();
										Thread.sleep(20000);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
										Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
										Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
										Application.clickInLoop(XpathRepository.by_DenyButton);
										Thread.sleep(3000);
										break;
									}
								}

								if (BrowserInitialization.driver.findElement(XpathRepository.by_ErrorMessage).getText().contains(DataObject.getVariable("Error Message", testCaseName))) {
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
								Reporter.setTestDetails("FAIL", testCaseDesc, "Compare Selected button is disabled ", screenshotPath);
								Assert.fail();
							}

						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus("Could not select the policy grid Extract", "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, "Could not select the policy grid Extract", screenshotPath);
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view the existing extract
	// on Policy Grid Monthly Processes Screen.
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 19)
	public void TC_12() throws Exception {
		String testCaseName = "TC_12";
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
					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						if (BrowserInitialization.driver.findElement(XpathRepository.by_ExtractTable).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view comparison report on
	// Policy Grid Monthly Processes Screenk
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 7)
	public void TC_13() throws Exception {
		String testCaseName = "TC_13";
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

					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						js = (JavascriptExecutor) BrowserInitialization.driver;
						js.executeScript("window.scrollBy(0,-200)");
						if (BrowserInitialization.driver.findElement(XpathRepository.by_CompTable).isDisplayed()) {
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}
	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view Approved (A) status
	// of a report on Policy Grid Monthly Processes Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(priority = 21)
	public void TC_16() throws Exception {
		String testCaseName = "TC_16";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		JavascriptExecutor js = null;
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
					Application.logoutUser();
					Application.ViewerLogin();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
						Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
						BrowserInitialization.driver.navigate().refresh();
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
						if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
							for (int i = 1; i <= 10; i++) {
								if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + i + "]")).getText().equals("A")) {
									temp++;
									break;
								}
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify that Admin user is able to view Approved (A) status
	// of a report on Policy Grid Monthly Processes Screen
	// Created by : Ravi Saini
	// Created on : 03/05/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test(priority = 22)
	public void TC_18() throws Exception {
		String testCaseName = "TC_18";
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
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {

						for (int i = 1; i <= 10; i++) {

							if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + i + "]")).getText().equals("D")) {
								temp++;
								break;
							}
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
			Application.clickInLoop(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}

	// ********************************************************************************************//
	// Description: Verify the user is able to view List button for "selection
	// of Columns to be displayed" on View Report Screen
	// Created by : Ravi Saini
	// Created on : 03/16/2018
	// Last updated by : Ravi Saini
	// Last updated : 03/16/2018
	// *********************************************************************************************//
	@Test(priority = 23)
	public void TC_20() throws Exception {
		String testCaseName = "TC_20";
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
				Application.clickInLoop

				(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);
				Thread.sleep(20000);
				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {

					Application.clickInLoop(XpathRepository.by_GprExtractReportLink);
					BrowserInitialization.driver.navigate().refresh();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 120);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).clear();
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(DataObject.getVariable("LimitField", testCaseName));
						BrowserInitialization.driver.findElement(XpathRepository.by_LimitTxtBoxCompareTab).sendKeys(Keys.ENTER);
						Thread.sleep(2000);
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_TimeCreatedDataCompTab, 65);
						String limit = DataObject.getVariable("LimitField", testCaseName);
						String limitS = limit.substring(0, 4);
						int limitI = Integer.parseInt(limitS);
						String extractId = DataObject.getVariable("Extract ID", testCaseName).substring(0, 4);
						for (int p = 1; p <= limitI; p++) {
							String extractedExtractId = BrowserInitialization.driver
									.findElement(By.xpath("(//data-table[@headertitle='Comparison Reports']//td[@class='data-column column-newFileExtractSeqNbr'])[" + p + "]")).getText();
							if (extractedExtractId.equals(extractId)) {
								Application.clickInLoop(By.xpath("(//data-table[@headertitle='Comparison Reports']//input[@type='checkbox'])[" + (p + 1) + "]"));
								Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_ViewSelectedBttn, 120);
								Application.clickInLoop(XpathRepository.by_ViewSelectedBttn);
								Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_listBttnDiffTable, 120);
								break;
							}
						}
						int noOfRows = BrowserInitialization.driver.findElements(By.xpath("//table//tr")).size();
						if (noOfRows >= 1) {
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
					throw new SkipException(testCaseName + " is Skipped");
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
