package com.plefs.gpr.backends;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class Zipper {
	static Logger logger = Logger.getLogger(Zipper.class);
	public static void zipReport(String reportPath) throws Exception {
		FileOutputStream fos = new FileOutputStream(reportPath + ".zip");
		ZipOutputStream zos = new ZipOutputStream(fos);
		addDirToZipArchive(zos, new File(reportPath), null);
		zos.flush();
		fos.flush();
		zos.close();
		fos.close();
	}

	public static void addDirToZipArchive(ZipOutputStream zos, File fileToZip, String parentDirectoryName) throws Exception {
		if (fileToZip == null || !fileToZip.exists()) {
			return;
		}

		String zipEntryName = fileToZip.getName();
		if (parentDirectoryName != null && !parentDirectoryName.isEmpty()) {
			zipEntryName = parentDirectoryName + "/" + fileToZip.getName();
		}

		if (fileToZip.isDirectory()) {
			for (File file : fileToZip.listFiles()) {

				if (file.isDirectory() && file.getName().equals("Screenshots")) {
					continue;
				} else {
					addDirToZipArchive(zos, file, zipEntryName);
				}

			}
		} else {
			byte[] buffer = new byte[1024];
			FileInputStream fis = new FileInputStream(fileToZip);
			zos.putNextEntry(new ZipEntry(zipEntryName));
			int length;
			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}
			zos.closeEntry();
			fis.close();
		}
	}
}
