package com.plefs.gpr.backend;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

public class Application {
	static Logger logger = Logger.getLogger(Application.class);
	private static final Object Enable = null;
	static String Date;
	public static boolean status = false;

	public static String system_date() {

		/*
		 * //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
		 * DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm/dd/yyyy");
		 * LocalDate sysDate = LocalDate.now(); String formattedDate =
		 * dtf.format(sysDate).replace("/", ""); if
		 * (formattedDate.startsWith("0")) { Date = formattedDate.substring(1,
		 * 9); logger.info(Date); } Date = formattedDate; // logger.info(Date);
		 * return Date;
		 */
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate sysDate = LocalDate.now();
		String formattedDate = dtf.format(sysDate);
		Date = formattedDate;
		// logger.info(Date);
		return Date;
	}

	public static void ScrollByVisibleElement(By xpath) {
		JavascriptExecutor js = (JavascriptExecutor) BrowserInitialization.driver;
		WebElement Element = BrowserInitialization.driver.findElement(xpath);
		js.executeScript("arguments[0].scrollIntoView();", Element);
	}

	public static boolean isElementPresent(By xpath) {
		try {
			BrowserInitialization.driver.findElement(xpath);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	public static void clickUsingJavaScript(By ElementXpath) {
		System.out.println("Entered clickUsingJavaScript");
		WebElement webElement = BrowserInitialization.driver.findElement(ElementXpath);
		JavascriptExecutor executor = (JavascriptExecutor) BrowserInitialization.driver;
		executor.executeScript("arguments[0].click();", webElement);
		status = true;

	}

	public static void findButtonAndClick(By xpath) throws Exception {
		Actions clickbutton = new Actions(BrowserInitialization.driver);
		WebElement button = BrowserInitialization.driver.findElement(xpath);
		clickbutton.moveToElement(button).click().perform();
		status = true;

	}

	public static boolean sort_Ascending(By xpath) {

		ArrayList<WebElement> List_GetAtoZ = (ArrayList<WebElement>) BrowserInitialization.driver.findElements(xpath);

		ArrayList<String> Str_StoreAtoZ = new ArrayList<String>();
		for (WebElement ExtractAtoZ : List_GetAtoZ)

		{

			Str_StoreAtoZ.add(ExtractAtoZ.getText());
		}

		ArrayList<String> Str_SortAtoZ = new ArrayList<String>();

		for (WebElement AtoZ : List_GetAtoZ)

		{

			Str_SortAtoZ.add(AtoZ.getText());

		}
		// Sort A to Z

		Collections.sort(Str_SortAtoZ);
		int counterB = 1;
		for (int i = 1; i < Str_StoreAtoZ.size(); i++) {
			// logger.info("" + Str_StoreAtoZ.get(i) + "\t" +
			// (Str_SortAtoZ.get(i)));

			// Compare the results
			if (Str_StoreAtoZ.get(i).equals(Str_SortAtoZ.get(i)))

			{

				counterB++;
				// logger.info(i + " sorted in ascending");

			} else

			{
				Boolean flag = false;

				// logger.info("Failed");

				Assert.assertEquals(Enable, flag);
			}

		}
		if (counterB == Str_StoreAtoZ.size()) {

			return true;

		} else {

			return false;
		}

	}

	public static boolean sort_descending(By xpath)

	{
		ArrayList<WebElement> List_GetZtoA = (ArrayList<WebElement>) BrowserInitialization.driver.findElements(xpath);

		ArrayList<String> Str_StoreZtoA = new ArrayList<String>();
		for (WebElement ExtractZtoA : List_GetZtoA)

		{

			Str_StoreZtoA.add(ExtractZtoA.getText());
		}

		ArrayList<String> Str_SortZtoA = new ArrayList<String>();

		for (WebElement ZtoA : List_GetZtoA)

		{

			Str_SortZtoA.add(ZtoA.getText());

		}
		// Sort Z to A

		Collections.sort(Str_SortZtoA, Collections.reverseOrder());

		// logger.info(Str_SortZtoA);
		int counterB = 1;
		for (int i = 1; i < Str_StoreZtoA.size(); i++) {
			// logger.info("" + Str_StoreZtoA.get(i) + "\t" +
			// (Str_SortZtoA.get(i)));

			// Compare the results
			if (Str_StoreZtoA.get(i).equals(Str_SortZtoA.get(i)))

			{

				counterB++;
				// logger.info(i + " sorted in descending");

			} else

			{
				Boolean flag = false;

				// logger.info("Failed");

				Assert.assertEquals(Enable, flag);
			}

		}
		if (counterB == Str_StoreZtoA.size()) {

			return true;

		} else {

			return false;
		}

	}

	public static boolean isValidDate(String inDate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY");
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			logger.info(pe.getMessage());
			return false;
		}
		return true;
	}

	public static void ViewerLogin() throws IOException {
		String url = Backend.getProperty("URL");
		BrowserInitialization.driver.navigate().to(url);
		String username = Backend.getProperty("ViewerUser");
		String password = Backend.getProperty("ViewerUser");
		BrowserInitialization.driver.findElement(XpathRepository.by_User_name).sendKeys(username);
		BrowserInitialization.driver.findElement(XpathRepository.by_Password).sendKeys(password);
		BrowserInitialization.driver.findElement(XpathRepository.by_Submit_button).click();
		Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 120);

	}

	public static void logoutUser() {
		BrowserInitialization.driver.findElement(XpathRepository.by_LogoutDropDown).click();
		BrowserInitialization.driver.findElement(XpathRepository.by_LogoutButton).click();
	}

	public static void waitForCursor() throws InterruptedException {
		Thread.sleep(2000);
		for (int j = 1; j <= 180; j++) {
			try {

				if (BrowserInitialization.driver.findElement(By.xpath("//h1[@class='hidden loading-text']")).isDisplayed()) {
					Thread.sleep(2000);
					break;
				}
			} catch (Exception e) {
				Thread.sleep(1000);
			}

		}
	}

	/**********************************************************************************************
	 * Description:This method will will check the format of particular cell in
	 * the excel . on :16/05/2018
	 *********************************************************************************************/

	public static String cellToString(XSSFCell cell) {

		Object result;

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			result = cell.getNumericCellValue();
			break;

		case Cell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue();
			break;

		case Cell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;

		default:
			throw new RuntimeException("Unknown Cell Type");
		}

		return result.toString();
	}

	public static void clickInLoop(By xpath) throws InterruptedException {

		for (int i = 0; i <= 60; i++) {
			try {
				BrowserInitialization.driver.findElement(xpath).click();
				break;
			} catch (Exception e) {
				Thread.sleep(1000);
			}
		}

	}

	public static void waitForStatustoC(int k) throws InterruptedException {
		for (int i = 0; i <= 240; i++) {
			if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + k + "]")).getText().equals("C")) {
				break;
			} else {
				Thread.sleep(1000);

			}
		}

	}

	public static void createExtract(String testCaseName) throws Exception {
		String Policygrid = DataObject.getVariable("DropDownMenu", testCaseName);
		Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
		policygrid_dropdown.selectByVisibleText(Policygrid);
		BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
		Thread.sleep(100000);
		// Backend.waitForElement(BrowserInitialization.driver,XpathRepository.by_successmsg,
		// 120);
		String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
		BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
		BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
		findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
		Thread.sleep(4000);
		findButtonAndClick(XpathRepository.by_newcreatedextract);
		findButtonAndClick(XpathRepository.by_4218extract);
		Thread.sleep(2000);
		findButtonAndClick(XpathRepository.by_CompareSelectedButton);
		Thread.sleep(30000);

	}

	public static int createExtracts(String testCaseName) throws Exception {
		int temp = 0;
		int p = 1;
		waitForCursor();
		Select dropDownMenu = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
		for (int i = 1; i <= 2; i++) {
			dropDownMenu.selectByVisibleText(DataObject.getVariable("DropDownMenu", testCaseName));
			Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 120);
			clickInLoop(XpathRepository.by_CreateExtractButton);
			waitForCursor();
			Backend.waitForElementToBeClickable(BrowserInitialization.driver, XpathRepository.by_SuccessMessage, 120);
			waitForCursor();
			Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitFileNameDataExtractTab, 120);
			for (p = 1; p <= 30; p++) {
				if (BrowserInitialization.driver.findElement(By.xpath("(//td[@class='data-column column-statusCd'])[" + 1 + "]")).getText().equals("C")) {
					break;
				} else {
					Thread.sleep(1000);
				}
			}
			if (p == 31) {
				clickInLoop(XpathRepository.by_RefreshbttnExtractTab);
				waitForCursor();
			}
			findButtonAndClick(XpathRepository.by_messageClose);
		}

		for (int k = 1; k <= 2; k++) {
			Application.findButtonAndClick(By.xpath("(//td[@class='select-column'])[" + k + "]"));
			temp++;
		}
		return temp;
	}

}
