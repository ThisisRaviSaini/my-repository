package com.plefs.gpr.backends;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.ITestContext;

public class TestConfiguration {
	static Logger logger = Logger.getLogger(TestConfiguration.class);
	public static int i = 0;
	public static int j = 0;

	public static void beforeSuite() {
		try {
			if (j == 0) {
				j++;
				Backend.projectCleanup();
				DOMConfigurator.configure("log4j.xml");
				Backend.getCurrentDateTime();
				Reporter.intilizeReporter();
			}
		} catch (Exception e) { 
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void beforeTest(String US_Name ) throws IOException {
		try {
			//System.out.println(testContext.getName());
			System.out.println("Executing the before Test");
//			if (!testContext.getName().contains("failed")) {

				BrowserInitialization.intilaize();
				Backend.login();
				UserStoryName.setUSName(US_Name);
				Backend.displayUSName(US_Name);
				// uftexecution.clearStatus(US_Name);
//			}
//			else {
//				System.out.println("Skipping the Exceution");
//			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void afterTest() throws Exception {
		try {
			System.out.println("Executing the After Test");
			BrowserInitialization.driver.quit();
			// uftexecution.UFTExecution();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}

	}

	public static void afterSuite() throws Exception {
		try {
			if (i == 0) {
				i++;
				Reporter.terminateReporter();
				Zipper.zipReport(Reporter.folderPath);
				Backend.takeReportScreenshot();
				System.out.println("COPYING THE DATA");
				//Copier.copy();
				// Mailer.sendEmail(Reporter.folderPath);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startTest(final ITestContext testContext) {
		System.out.println(testContext.getName()); // it prints "Check name test"
	}

}
