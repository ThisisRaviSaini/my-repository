package com.plefs.gpr.backends;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

public class Mailer {
	static Logger logger = Logger.getLogger(Mailer.class);
	public static int p = 0;

	public static void sendEmail(String zippedReport) throws Exception {
		String host = "smtp.mail.fedex.com";
		String sender = Backend.getProperty("MailSender");
		String recipient = Backend.getProperty("MailRecipients");

		Properties properties = System.getProperties();

		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		try {
			String cid = Backend.currentDateTime;
			String extentReportName = "execution-report_" + Backend.currentDateTime + ".zip";
			Message message = new MimeMessage(session);
			Multipart multiPart = new MimeMultipart();

			message.setFrom(new InternetAddress(sender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

			if (p == 0) {
				message.setSubject("GPR-2 Automation: Policy Grid Compare Execution Report");
				p++;
			} else {
				message.setSubject("GPR-2 Automation Retried: Policy Grid Compare Execution Report");
			}

			MimeBodyPart extentReport = new MimeBodyPart();
			String ZippedFilename = zippedReport + ".zip";
			DataSource ZippedFileSource = new FileDataSource(ZippedFilename);
			extentReport.setDataHandler(new DataHandler(ZippedFileSource));
			extentReport.setFileName(extentReportName);

			MimeBodyPart testngReport = new MimeBodyPart();
			String zippedTestngReport = "test-output\\emailable-report.html";
			DataSource testngReportSource = new FileDataSource(zippedTestngReport);
			testngReport.setDataHandler(new DataHandler(testngReportSource));
			testngReport.setFileName("testng-report_" + Backend.currentDateTime + ".html");

			MimeBodyPart reportDashView = new MimeBodyPart();
			DataSource reportDashViewSource = new FileDataSource("Resources\\report-dashview.png");
			reportDashView.setDataHandler(new DataHandler(reportDashViewSource));
			reportDashView.setFileName("report-dashview.png");

			MimeBodyPart policyGridLogger = new MimeBodyPart();
			DataSource policyGridLoggerSource = new FileDataSource("Resources\\logger.log");
			policyGridLogger.setDataHandler(new DataHandler(policyGridLoggerSource));
			policyGridLogger.setFileName("logger_" + Backend.currentDateTime + ".log");

			MimeBodyPart imageInBody = new MimeBodyPart();
			imageInBody.attachFile("Resources\\report-dashview.png");
			imageInBody.setContentID("<" + cid + ">");
			imageInBody.setDisposition(MimeBodyPart.INLINE);
			multiPart.addBodyPart(imageInBody);

			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText("<html>" + " <body>" + "  <p style='font-family:Calibri;'>Hi Team,</p>"
					+ "<p style='font-family:Calibri;'> Please find the following GPR2 Compare GUI Automation execution summary</p>"
					+ "  <img src=\"cid:" + cid + "\"style='border:50 solid black'/>"

					+ "<p style='font-family:Calibri;'>For detailed execution report of the Policy Grid Monthly Processes screen, please find the attached  <b>"
					+ "testng-report.html" + "</b> and <b> logger.log </b>file.</p>"
					+ "To access the Policy Grid Monthly Processes screen, please click <a href="
					+ Backend.getProperty("URL") + ">here</a>"
					+ "<p style='font-family:Calibri;'><b>Note:-</b> If image is not diplayed in the mail body then, please find the <b>report-dashview.png</b> file in the attachements.</p>"

					+ "<p style='font-family:Calibri;'>Thanks and Regards,<br>GTM PLEFS</p>" + "<p><br></p>"

					+ "<p style='font-family:Calibri;'><font size='1'><center><<------------------------------------------------This is an automated email triggered from GPR Automation framework-------------------------------------------->></center></font></p>"

					+ "</body>" + "</html>", "US-ASCII", "html");

			multiPart.addBodyPart(extentReport);
			multiPart.addBodyPart(testngReport);
			multiPart.addBodyPart(reportDashView);
			multiPart.addBodyPart(policyGridLogger);
			multiPart.addBodyPart(textPart);

			message.setContent(multiPart);
			logger.info("Sending Mail......");
			try {
				Transport.send(message);
			} catch (Exception e) {

				e.printStackTrace();
			}

			logger.info("Mail Sent Succesfully");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}