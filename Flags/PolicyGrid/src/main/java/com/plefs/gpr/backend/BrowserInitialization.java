package com.plefs.gpr.backend;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class BrowserInitialization {
	static Logger logger = Logger.getLogger(BrowserInitialization.class);

	public static WebDriver driver = null;

	/**********************************************************************************************
	 * Description:This method will open the specified browser. Created on
	 * :01/29/2018
	 *********************************************************************************************/
	public static void intilaize() {

		try {
			String browser = Backend.getProperty("Browser");
			switch (browser) {
		
			case "HTML Unit": {
				String driverPath = Backend.getProperty("DriverPath");
				//System.setProperty("webdriver.gecko.driver", driverPath + "/geckodriver.exe");
				logger.info("<<<------------------Opening HTML Unit Browser------------------->>>");
				driver = new HtmlUnitDriver();
				openApplication();
				break;
			}
			case "Phantom": {
				String driverPath = Backend.getProperty("DriverPath");
				System.setProperty("phantomjs.binary.path", driverPath + "/phantomjs.exe");
				logger.info("<<<------------------Opening Phantom Browser------------------->>>");
				driver = new HtmlUnitDriver();
				openApplication();
				break;
			}
			case "Gecko": {
				String driverPath = Backend.getProperty("DriverPath");
				System.setProperty("webdriver.gecko.driver", driverPath + "/geckodriver.exe");
				logger.info("<<<------------------Opening Gecko Browser------------------->>>");
				driver = new FirefoxDriver();
				openApplication();
				break;
			}
			case "Chrome": {
				String driverPath = Backend.getProperty("DriverPath");
				System.setProperty("webdriver.chrome.driver", driverPath + "/chromedriver.exe");
				logger.info("<<<------------------Opening Chrome Browser------------------->>>");
				driver = new ChromeDriver();
				openApplication();
				break;
			}
			case "ChromeHeadless": {
				String driverPath = Backend.getProperty("DriverPath");
				System.setProperty("webdriver.chrome.driver", driverPath + "/chromedriver.exe");
				logger.info("<<<------------------Opening Chrome Headless Browser------------------->>>");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless");
				driver = new ChromeDriver(options);
				openApplication();
				break;
			}
			case "IE": {
				String driverPath = Backend.getProperty("DriverPath");
				System.setProperty("webdriver.ie.driver", driverPath + "IEDriverServer.exe");
				logger.info("<<<------------------Opening Internet Explorer Browser------------------->>>");
				driver = new InternetExplorerDriver();
				openApplication();
				break;
			}

			case "FireFox": {
				String driverPath = Backend.getProperty("DriverPath");
				System.setProperty("webdriver.firefox.marionette", driverPath + "FirefoxDriverServer.exe");
				logger.info("<<<------------------Opening FireFox Browser------------------->>>");
				driver = new FirefoxDriver();
				openApplication();
				break;

			}
			default:
				logger.info("No Suitable Browser's Driver found for the specified Browser ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**********************************************************************************************
	 * Description:This method will maximize the browser window. Created on
	 * :01/29/2018
	 *********************************************************************************************/

	public static void openApplication() {
		try {
			driver.manage().window().maximize();
			String url = Backend.getProperty("URL");
			driver.navigate().to(url);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
