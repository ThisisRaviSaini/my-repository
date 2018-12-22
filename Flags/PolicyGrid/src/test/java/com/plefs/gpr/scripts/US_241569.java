package com.plefs.gpr.scripts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.TestRunner;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.beust.testng.TestNG;
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

public class US_241569 {
	Logger logger = Logger.getLogger(US_241569.class);

	/**********************************************************************************************
	 * Description: Verify the user is able to select the OFFERING_CONSTRAINTS
	 * grid in the drop down menu of the Screen Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 4)
	public void TC_01() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				String Expected_PG = "OFFERING_CONSTRAINTS";
				String Actual_PG = BrowserInitialization.driver.findElement(XpathRepository.by_OfferingConstraintOption).getText();
				if (Actual_PG.equals(Expected_PG)) {
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

	/**********************************************************************************************
	 * Description: Verify the user is able to select the
	 * OFFERING_DIMENSION_CONSTRAINTS grid in the drop down menu of the Screen
	 * Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 4)
	public void TC_02() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				String Expected_PG = "OFFERING_DIMENSION_CONSTRAINTS";
				String Actual_PG = BrowserInitialization.driver.findElement(XpathRepository.by_OfferingDimConstraintOption).getText();
				if (Actual_PG.equals(Expected_PG)) {
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

	/**********************************************************************************************
	 * Description: Verify the user is not be able to select more than one
	 * element in the drop down menu of the Screen Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 4)
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				if (!policygrid_dropdown.isMultiple()) {
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

	/**********************************************************************************************
	 * Description: Verify the user is able to create a new Extract of
	 * OFFERING_CONSTRAINTS by clicking on the Extract Button. Created on :
	 * 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 1)
	public void TC_04() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_04";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 80);
				Thread.sleep(20000);
				int prv_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
				// logger.info("Record Count "+prv_record_count);
				int exp_record_count = prv_record_count + 1;
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				// logger.info(Policygrid);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_successmsg, 40);
				Thread.sleep(50000);
				String latestextractedgrid = BrowserInitialization.driver.findElement(XpathRepository.by_firstrow).getText();
				// logger.info(latestextractedgrid);
				// logger.info(Policygrid);
				int cur_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
				// logger.info("Current Record Count "+cur_record_count);
				// logger.info(prv_record_count);
				if (cur_record_count > prv_record_count && latestextractedgrid.equals(Policygrid)) {
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
				try {
					throw new SkipException(testCaseName + " is Skipped");
				} catch (SkipException e) {

					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
			;
		} finally {
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify the user is able to view the message when a new
	 * Extract of OFFERING_CONSTRAINTS is added in the Extract Table. Created on
	 * : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 5)
	public void TC_05() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_05";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				// logger.info(Policygrid);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_successmsg, 40);
				BrowserInitialization.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				String Expected_msg = "Successfully added extract: " + Policygrid;
				// logger.info(Expected_msg);
				String Actual_msg = BrowserInitialization.driver.findElement(XpathRepository.by_successmsg).getText();
				// logger.info(Actual_msg);
				if (Actual_msg.contains(Expected_msg)) {
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

	/**********************************************************************************************
	 * Description: Verify the user is able to create a new Extract of
	 * OFFERING_DIMENSION_CONSTRAINTS by clicking on the Extract Button. Created
	 * on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 2)
	public void TC_06() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_06";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable("TC_06");
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				// logger.info("clicked on Extract report");
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_Lasttimecomparetblrecord, 40);
				Thread.sleep(30000);
				int prv_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
				// logger.info("Record Count "+prv_record_count);
				int exp_record_count = prv_record_count + 1;
				// String Policygrid = DataObject.getVariable("DropDownMenu",
				// testCaseName);
				String Policygrid = "OFFERING_DIMENSION_CONSTRAINTS";
				// logger.info(Policygrid);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_successmsg, 40);
				Thread.sleep(60000);
				String latestextractedgrid = BrowserInitialization.driver.findElement(XpathRepository.by_firstrow).getText();
				// logger.info(latestextractedgrid);
				// logger.info(Policygrid);
				int cur_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
				// logger.info("Current Record Count " +
				// cur_record_count);
				// logger.info(exp_record_count);
				if (cur_record_count > prv_record_count && latestextractedgrid.equals(Policygrid)) {
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

	/**********************************************************************************************
	 * Description: Verify the user is able to view the message when a new
	 * Extract of OFFERING_DIMENSION_CONSTRAINTS is added in the Extract Table.
	 * Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 7)
	public void TC_07() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_07";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);

				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				Thread.sleep(50000);
				// String Policygrid = DataObject.getVariable("DropDownMenu",
				// testCaseName);
				String Policygrid = "OFFERING_DIMENSION_CONSTRAINTS";
				// logger.info(Policygrid);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_successmsg, 40);
				// BrowserInitialization.driver.manage().timeouts().implicitlyWait(20,
				// TimeUnit.SECONDS);
				Thread.sleep(100000);

				String Expected_msg = "Successfully added extract: " + Policygrid;
				// logger.info(Expected_msg);
				String Actual_msg = BrowserInitialization.driver.findElement(XpathRepository.by_successmsg).getText();
				// logger.info(Actual_msg);
				if (Actual_msg.contains(Expected_msg)) {
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

	/**********************************************************************************************
	 * Description: Verify the error message"Please select an Extract Type to
	 * create extract!" Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 8)
	public void TC_08() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_08";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			// logger.info(testRunnable);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				String Expected_msg = "Please select an Extract Type to create extract!";
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
				// ObjectRepository.by_warningmsg, 40);
				Thread.sleep(80000);
				String Actual_msg = BrowserInitialization.driver.findElement(XpathRepository.by_warningmsg).getText();
				// logger.info(Actual_msg);
				if (Actual_msg.contains(Expected_msg)) {
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
				try {
					throw new SkipException(testCaseName + " is Skipped");
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		}

		finally {
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	/**********************************************************************************************
	 * Description: Verify a new row of OFFERING_CONSTRAINTS is added in the
	 * Extract table when user click on the Extract Button Created on :
	 * 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 1)
	public void TC_09() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_09";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				String Exp_Date = Application.system_date();
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GPRExtendreportstitle, 10);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
				// ObjectRepository.by_successmsg, 40);
				Thread.sleep(60000);
				String ActDate = BrowserInitialization.driver.findElement(XpathRepository.by_DateCreatedFirstRow).getText();
				String latestextractedgrid = BrowserInitialization.driver.findElement(XpathRepository.by_firstrow).getText();
				// logger.info(latestextractedgrid);
				// logger.info(Policygrid);
				// logger.info(ActDate);
				// logger.info(Exp_Date);
				if (ActDate.equals(Exp_Date) && latestextractedgrid.equals(Policygrid)) {
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

	/**********************************************************************************************
	 * Description: Verify a new row of OFFERING_DIMENSION_CONSTRAINTS is added
	 * in the Extract table when user click on the Extract Button Created on :
	 * 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 3)
	public void TC_10() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_10";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				String Exp_Date = Application.system_date();
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_Lasttimecomparetblrecord, 60);
				String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
				// logger.info(Policygrid);
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				// BackendFunctionality.waitForElement(BrowserInitialization.driver,
				// XpathRepository.by_successmsg, 40);
				Thread.sleep(50000);
				String ActDate = BrowserInitialization.driver.findElement(XpathRepository.by_DateCreatedFirstRow).getText();
				String latestextractedgrid = BrowserInitialization.driver.findElement(XpathRepository.by_firstrow).getText();
				// logger.info(latestextractedgrid);
				// logger.info(Policygrid);
				// logger.info(Exp_Date);
				// logger.info(ActDate);
				if (ActDate.equals(Exp_Date) && latestextractedgrid.equals(Policygrid)) {
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

	/**********************************************************************************************
	 * Description: Verify a new row is not added in the Extract table when user
	 * click on the Extract Button Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 11)
	public void TC_11() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_11";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				Thread.sleep(8000);
				int prv_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
				// logger.info("Record Count "+prv_record_count);
				Application.findButtonAndClick(XpathRepository.by_extractbtn);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_warningmsg, 20);
				// BrowserInitialization.driver.manage().timeouts().implicitlyWait(20,
				// TimeUnit.SECONDS);
				Thread.sleep(8000);
				int cur_record_count = Integer.parseInt(BrowserInitialization.driver.findElement(XpathRepository.by_records).getText());
				// logger.info("Current Record Count "+cur_record_count);
				if (cur_record_count == prv_record_count) {
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

	/**********************************************************************************************
	 * Description: Verify the Extract Type,Date Created, Time Created, Extract
	 * Id, File Name, Approval Status, Name-ID, Date/Time columns are displayed
	 * in the Extract table. Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	@Test(priority = 12)
	public void TC_12() throws Exception {
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String testCaseName = "TC_12";
		logger.info("Executing the Test Case No. " + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			// logger.info(testRunnable);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Application.findButtonAndClick(XpathRepository.by_GPRExtendreports);
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_Lasttimecomparetblrecord, 60);
				String ExpectedET = "Extract Type";
				String ExpectedDC = "Date Created";
				String ExpectedTC = "Time Created";
				String ExpectedEI = "Extract ID";
				String ExpectedFN = "File Name";
				String ExpectedAS = "Approval Status";
				String ExpectedNI = "FedEx ID";
				String ExpectedDT = "Date/Time";
				String ActualET = BrowserInitialization.driver.findElement(XpathRepository.by_actualET).getText();
				// logger.info(ActualET);
				String ActualDC = BrowserInitialization.driver.findElement(XpathRepository.by_actualDC).getText();
				// logger.info(ActualDC);
				String ActualTC = BrowserInitialization.driver.findElement(XpathRepository.by_actualTC).getText();
				// logger.info(ActualTC);
				String ActualEI = BrowserInitialization.driver.findElement(XpathRepository.by_actualEI).getText();
				// logger.info(ActualEI);
				String ActualFN = BrowserInitialization.driver.findElement(XpathRepository.by_actualFN).getText();
				// logger.info(ActualFN);
				String ActualAS = BrowserInitialization.driver.findElement(XpathRepository.by_actualAS).getText();
				// logger.info(ActualAS);
				String ActualNI = BrowserInitialization.driver.findElement(XpathRepository.by_actualNI).getText();
				// logger.info(ActualNI);
				String ActualDT = BrowserInitialization.driver.findElement(XpathRepository.by_actualDT).getText();
				// logger.info(ActualDT);
				// String file_name = Backend.makeDirectory(testCaseName);
				if (ExpectedET.equals(ActualET) && ExpectedDC.equals(ActualDC) && ExpectedTC.equals(ActualTC) && ExpectedEI.equals(ActualEI) && ExpectedFN.equals(ActualFN)
						&& ExpectedAS.equals(ActualAS) && ExpectedNI.equals(ActualNI) && ExpectedDT.equals(ActualDT)) {
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
