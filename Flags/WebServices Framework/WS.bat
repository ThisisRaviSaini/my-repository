set projectLocation=C:\Automation\GPR3_WS_Selenium
cd %projectLocation%
set classpath=%projectLocation%\bin;%projectLocation%\Jars\*
java org.testng.TestNG %projectLocation%\bin\testng.xml
pause