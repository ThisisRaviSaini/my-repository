package com.plefs.backends;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;



public class BaseClass {

	
	public static ObjectRepository obj_repository=null;
	public static CommonMethods obj_common_methods=null;
	public static DataObject obj_data_object=null;
	public  static WebDriver driver= null;
	
	

	public void intilaize() throws InterruptedException, IOException 
	{
		System.out.println("entered intialize");
		
		String Browser = CommonMethods.Call_config("Browser");
		if(Browser.equalsIgnoreCase("Chrome"))
		{
			
			obj_repository=new ObjectRepository();
			obj_common_methods=new CommonMethods();
			obj_data_object=new DataObject();
		//	String driverPath = CommonMethods.Call_config("DriverPath");
		//	System.setProperty("webdriver.chrome.driver", "D:\\Ravi Saini\\My Workspace\\DropNet\\Drivers\\chromedriver.exe"); 
			WebDriverManager.chromedriver().setup();
			System.out.println("opening chrome");
			driver= new ChromeDriver();
			driver.manage().window().maximize();
			String url = CommonMethods.Call_config("URL");
			driver.get(url);
			System.out.println("Url Launched on Chrome Browser");
		}
		else if(Browser.equalsIgnoreCase("IE"))
			
		{
			
	        obj_repository=new ObjectRepository();
			obj_common_methods=new CommonMethods();
			obj_data_object=new DataObject();
			String driverPath = CommonMethods.Call_config("DriverPath");
			
			System.setProperty("webdriver.ie.driver", driverPath+"/IEDriverServer.exe"); 
			System.out.println("opening IE");
			driver= new InternetExplorerDriver();
			driver.manage().window().maximize();
			String url = CommonMethods.Call_config("URL");
			driver.navigate().to(url);
			System.out.println("Url Launched IE Browser");
			
		}
		else
		{
			System.out.println("This is not Supported Browser");	
		}
		
														
	}
							
}
