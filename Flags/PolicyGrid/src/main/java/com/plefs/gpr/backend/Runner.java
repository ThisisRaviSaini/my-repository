package com.plefs.gpr.backend;

import java.io.File;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.TestNG;
import org.testng.collections.Lists;

public class Runner {
	static Logger logger = Logger.getLogger(Runner.class);

	public static void main(String[] args) throws Exception {
		runTestNg();
		Mailer.sendEmail(Reporter.folderPath);
		reRunFailedSuite();
	}

	public static void runTestNg() {
		TestNG testng = new TestNG();
		List<String> suite = Lists.newArrayList();
		suite.add("src\\TestNG.xml");
		testng.setTestSuites(suite); 
		testng.run();
	}

	public static void reRunFailedSuite() {
		try {
			String path = "test-output\\testng-failed.xml";
			File failedTestNg = new File(path);
			if (failedTestNg.exists()) {
				TestNG testng = new TestNG();
				List<String> suites = Lists.newArrayList();
				suites.add(path);
				testng.setTestSuites(suites);
				DOMConfigurator.configure("log4j.xml");
				Backend.getCurrentDateTime();
				Reporter.intilizeReporter();
				testng.run();
				Reporter.terminateReporter();
				Zipper.zipReport(Reporter.folderPath);
				Backend.takeReportScreenshot();
				Copier.copy();
				Mailer.sendEmail(Reporter.folderPath);
			} else {
				logger.info("All Test Cases Passed Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
