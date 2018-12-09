package com.plefs.gpr.backends;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Copier {
	static Logger logger = Logger.getLogger(Copier.class);

	public static void copy() throws IOException {
		String dataPath = Backend.getProperty("ReportsCopyPath");
		File srcDirReport = new File("Reports");
		File destDirReport = new File(dataPath + "/" + Reporter.reportName);
		// File srcDirTestngReport = new File("test-output");
		// Hello File destDirTestNgReport = new File(dataPath + "/test-output");

		FileUtils.copyDirectory(srcDirReport, destDirReport);
		// FileUtils.copyDirectory(srcDirTestngReport, destDirTestNgReport);
		logger.info("Reports Copied Successfully at location " + Backend.getProperty("ReportsCopyPath"));

	}

	public static void copy(File sourceLocation, File targetLocation) throws IOException {
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			copyFile(sourceLocation, targetLocation);
		}
	}

	public static void copyDirectory(File source, File target) throws IOException {
		if (!target.exists()) {
			target.mkdir();
		}

		for (String f : source.list()) {
			copy(new File(source, f), new File(target, f));
		}
	}

	public static void copyFile(File source, File target) throws IOException {
		try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(target)) {
			byte[] buf = new byte[1024];
			int length;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
		}
	}
}
