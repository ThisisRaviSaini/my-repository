package backend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Mailer {
	static Logger logger = Logger.getLogger(Mailer.class);
	public static WebDriver driver = null;
	public static File dashViewScreenshot = null;

	public static void takeReportScreenshot(String reportName) throws IOException {
		File f = new File(reportName);
		if (f.exists()) {
			String driverPath = Backend.getProperty("DriverPath");
			System.setProperty("webdriver.chrome.driver", driverPath + "/chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.navigate().to(f.toURI().toURL());
			driver.findElement(By.xpath("(//i[text()='track_changes'])[1]")).click();
			WebElement dashBoardElement = driver.findElement(By.xpath("//div[@id='charts-row']"));
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage fullImg = ImageIO.read(screenshot);
			BufferedImage elementScreenshot = fullImg.getSubimage(dashBoardElement.getLocation().getX(),
					dashBoardElement.getLocation().getY(), dashBoardElement.getSize().getWidth(),
					dashBoardElement.getSize().getHeight());
			ImageIO.write(fullImg, "png", screenshot);
			String resourcesLocation = Backend.getProperty("ResourcesPath");
			dashViewScreenshot = new File(resourcesLocation + "\\report-dashview.png");
			FileUtils.copyFile(screenshot, dashViewScreenshot);
			logger.info("Screenshot Captured");
			driver.quit();
		}
	}

	public static String zipFile(String filePath) throws IOException {
		String zipFileName = null;
		File file = new File(filePath);
		if (file.exists()) {
			zipFileName = Backend.getProperty("ExtentReportPath") + "/"
					+ file.getName().replace(".html", "").concat(".zip");
			FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			zos.putNextEntry(new ZipEntry(file.getName()));
			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			zos.write(bytes, 0, bytes.length);
			zos.closeEntry();
			zos.close();
		} else {
			zipFileName = "null";
		}
		return zipFileName;
	}

	public static void sendEmail(String zipFileName, String UserStoryName) throws IOException {
		if (zipFileName != "null") {
			String host = "smtp.mail.fedex.com";
			String sender = Backend.getProperty("MailSender");
			String recipient = Backend.getProperty("MailRecipients");
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			try {
				String cid = Backend.getCurrentDateTime();
				Message message = new MimeMessage(session);
				Multipart multiPart = new MimeMultipart();

				message.setFrom(new InternetAddress(sender));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

				message.setSubject("GPR-3 Automation:" + UserStoryName + " WebServices Report");

				MimeBodyPart extentReport = new MimeBodyPart();
				String ZippedFilename = zipFileName;
				DataSource ZippedFileSource = new FileDataSource(ZippedFilename);
				extentReport.setDataHandler(new DataHandler(ZippedFileSource));
				extentReport.setFileName(zipFileName);

				MimeBodyPart reportDashView = new MimeBodyPart();
				DataSource reportDashViewSource = new FileDataSource("Resources\\report-dashview.png");
				reportDashView.setDataHandler(new DataHandler(reportDashViewSource));
				reportDashView.setFileName("report-dashview.png");

				MimeBodyPart policyGridLogger = new MimeBodyPart();
				DataSource policyGridLoggerSource = new FileDataSource("Resources\\logger.log");
				policyGridLogger.setDataHandler(new DataHandler(policyGridLoggerSource));
				policyGridLogger.setFileName("logger.log");

				MimeBodyPart imageInBody = new MimeBodyPart();
				imageInBody.attachFile("Resources\\report-dashview.png");
				imageInBody.setContentID("<" + cid + ">");
				imageInBody.setDisposition(MimeBodyPart.INLINE);
				multiPart.addBodyPart(imageInBody);

				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText("<html>" + " <body>" + "  <p style='font-family:Calibri;'>Hi Team,</p>"
						+ "<p style='font-family:Calibri;'> Please find the following GPR3 Web Service execution summary</p>"
						+ "  <img src=\"cid:" + cid + "\"style='border:50 solid black'/>"

						+ "<p style='font-family:Calibri;'>For detailed execution analysis of the Web Service , please find the attached"
						+ "<b> logger.log </b>file.</p>"

						+ "<p style='font-family:Calibri;'>Thanks and Regards,<br>GTM PLEFS</p>" + "<p><br></p>"

						+ "<p style='font-family:Calibri;'><font size='1'><center><<------------------------------------------------This is an automated email triggered from GPR Automation Team-------------------------------------------->></center></font></p>"

						+ "</body>" + "</html>", "US-ASCII", "html");

				multiPart.addBodyPart(extentReport);
				multiPart.addBodyPart(policyGridLogger);
				multiPart.addBodyPart(textPart);

				message.setContent(multiPart);
				logger.info("Sending Mail for WS:" + UserStoryName);
				Transport.send(message);

				logger.info("Mail Sent Succesfully");
			} catch (MessagingException mex) {
				mex.printStackTrace();
			}
		} else {
			System.out.println("No Report found for WS:" + UserStoryName);
		}
	}

}