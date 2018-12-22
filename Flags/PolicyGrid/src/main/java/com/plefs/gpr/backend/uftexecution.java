package com.plefs.gpr.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class uftexecution {

	static Logger logger = Logger.getLogger(Backend.class);

	public void zipFolder(final Path sourceFolderPath, Path zipPath) throws Exception {
		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
		Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
				Files.copy(file, zos);
				zos.closeEntry();
				return FileVisitResult.CONTINUE;
			}
		});
		zos.close();
	}

	public static void uftzip() throws Exception {

		uftexecution zf = new uftexecution();
		String uftdestpath = Backend.getProperty("uftscreenshotPath");
		// System.out.println(uftdestpath);
		File folder = new File(uftdestpath);
		File[] listOfFiles = folder.listFiles();
		int nooffiles = listOfFiles.length;
		for (int i = 0; i < nooffiles; i++) {
			String folderToZip = Backend.getProperty("uftscreenshotPath") + "/" + listOfFiles[i].getName();
			String zipName = Backend.getProperty("uftscreenshotPath") + "/" + listOfFiles[i].getName() + ".zip";
			zf.zipFolder(Paths.get(folderToZip), Paths.get(zipName));
		}

	}

	public static void testcasestatus(String status, String TC_ID) throws IOException {
		String userStoryName = UserStoryName.getUSName();
		String FilePath = Backend.getProperty("TestDataSheetPath");
		File inputWorkbook = new File(FilePath);
		FileInputStream fis = new FileInputStream(inputWorkbook);
		XSSFWorkbook w = new XSSFWorkbook(fis);
		XSSFSheet sheet = w.getSheet(userStoryName);
		XSSFRow rowHeader = sheet.getRow(0);
		int columncount = rowHeader.getPhysicalNumberOfCells();
		int rowcount = sheet.getPhysicalNumberOfRows();
		int p = 0;
		for (int k = 0; k < columncount; k++) {
			if (rowHeader.getCell(k).getStringCellValue().equals("Status")) {
				p = k;

			}
		}
		for (int j = 1; j < rowcount; j++) {
			for (int i = 0; i < columncount; i++) {
				if (sheet.getRow(j).getCell(i).getStringCellValue().equals(TC_ID)) {
					sheet.getRow(j).getCell(p).setCellValue(status);
				}
			}
		}
		FileOutputStream FileOutput = new FileOutputStream(inputWorkbook);
		w.write(FileOutput);
		FileOutput.close();
	}

	public static void clearStatus(String US_Name) throws IOException {

		String FilePath = Backend.getProperty("TestDataSheetPath");
		File inputWorkbook = new File(FilePath);
		FileInputStream fis = new FileInputStream(inputWorkbook);
		XSSFWorkbook w = new XSSFWorkbook(fis);
		XSSFSheet sheet = w.getSheet(US_Name);
		XSSFRow rowHeader = sheet.getRow(0);
		int columncount = rowHeader.getPhysicalNumberOfCells();
		int rowcount = sheet.getPhysicalNumberOfRows();
		int p = 0;
		for (int k = 0; k < columncount; k++) {
			if (rowHeader.getCell(k).getStringCellValue().equals("Status")) {
				p = k;
				for (int i = 1; i < rowcount; i++) {
					sheet.getRow(i).createCell(p).setCellValue("");
				}
			}
		}

		FileOutputStream FileOutput = new FileOutputStream(inputWorkbook);
		w.write(FileOutput);
		FileOutput.close();

	}

	public static void createUFTsheet() throws Exception {

		String userStoryName = UserStoryName.getUSName();
		Backend.readExcel();
		String FilePath = Backend.getProperty("TestDataSheetPath");
		File inputWorkbook = new File(FilePath);
		FileInputStream fis = new FileInputStream(inputWorkbook);
		XSSFWorkbook w = new XSSFWorkbook(fis);
		XSSFSheet sheet = w.getSheet(userStoryName);
		XSSFRow rowHeader = sheet.getRow(0);
		int columncount = rowHeader.getPhysicalNumberOfCells();
		int rowcount = sheet.getPhysicalNumberOfRows();
		int p = 0;
		for (int k = 0; k < columncount; k++) {
			if (rowHeader.getCell(k).getStringCellValue().equals("Status")) {
				p = k;

			}
		}
		String Path = Backend.getProperty("UFTPath");
		String UFTFilePath = Path + "/US_" + userStoryName + ".xlsx";
		XSSFWorkbook uftw = new XSSFWorkbook();
		XSSFSheet uftsheet = uftw.createSheet("Actual");
		XSSFRow uftrowHeader = uftsheet.createRow(0);
		uftrowHeader.createCell(0).setCellValue("Actual_Result");
		uftrowHeader.createCell(1).setCellValue("Attachment_Req");
		uftrowHeader.createCell(2).setCellValue("File-path");
		uftrowHeader.createCell(3).setCellValue("Status");

		for (int i = 1; i < rowcount; i++) {
			String TC_ID = sheet.getRow(i).getCell(0).getStringCellValue();
			if (sheet.getRow(i).getCell(p).getStringCellValue().equals("PASS")) {
				String Actual = DataObject.getVariable("Pass Description", TC_ID);
				uftsheet.createRow(i).createCell(0).setCellValue(Actual);
				uftsheet.getRow(i).createCell(1).setCellValue("Yes");
				String scrnshot = Backend.getProperty("uftscreenshotPath") + "/" + userStoryName + "_" + TC_ID + ".zip";
				uftsheet.getRow(i).createCell(2).setCellValue(scrnshot);
				uftsheet.getRow(i).createCell(3).setCellValue("Passed");
			} else if (sheet.getRow(i).getCell(p).getStringCellValue().equals("FAIL")) {
				String Actual = DataObject.getVariable("Fail Description", TC_ID);
				uftsheet.createRow(i).createCell(0).setCellValue(Actual);
				uftsheet.getRow(i).createCell(1).setCellValue("No");
				String scrnshot = Backend.getProperty("uftscreenshotPath") + "/" + userStoryName + "_" + TC_ID + ".zip";
				uftsheet.getRow(i).createCell(2).setCellValue(scrnshot);
				uftsheet.getRow(i).createCell(3).setCellValue("Failed");
			} else {
				String Actual = DataObject.getVariable("Fail Description", TC_ID);
				uftsheet.createRow(i).createCell(0).setCellValue(Actual);
				uftsheet.getRow(i).createCell(1).setCellValue("No");
				String scrnshot = Backend.getProperty("uftscreenshotPath") + "/" + userStoryName + "_" + TC_ID + ".zip";
				uftsheet.getRow(i).createCell(2).setCellValue(scrnshot);
				uftsheet.getRow(i).createCell(3).setCellValue("Failed");
			}
		}
		FileOutputStream FileOutput = new FileOutputStream(UFTFilePath);
		uftw.write(FileOutput);
		FileOutput.close();
	}

	public static void UFTExecution() throws Exception {
		String UFTFlag = Backend.getProperty("UFTExecutionFlag");
		if (UFTFlag.equals("Y")) {

			createUFTsheet();
			logger.info("Actual Sheet for UFT is created");
		}
	}

	public static void UFTFiles() throws Exception {
		copyFile();
		uftzip();
		deletefolders();
		String uftdestpath = Backend.getProperty("uftscreenshotPath");
		logger.info("Screenshot for UFT are placed in path: " + uftdestpath);

	}

	public static void copyFile() throws IOException {
		String reportpath = Backend.getProperty("ReportPath");
		String uftdestpath = Backend.getProperty("uftscreenshotPath");
		File folder = new File(reportpath);
		File[] listOfFiles = folder.listFiles();
		String Screenshotpath = reportpath + "/" + listOfFiles[0].getName() + "/Screenshots";
		File src = new File(Screenshotpath);
		File dest = new File(uftdestpath);
		try {
			FileUtils.copyDirectory(src, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deletefolders() throws IOException {
		String uftdestpath = Backend.getProperty("uftscreenshotPath");
		// System.out.println(uftdestpath);
		File folder = new File(uftdestpath);
		File[] listOfFiles = folder.listFiles();
		int nooffiles = listOfFiles.length;
		for (int i = 0; i < nooffiles; i++) {
			if (listOfFiles[i].isDirectory()) {
				String foldername = uftdestpath + "/" + listOfFiles[i].getName();
				Backend.deleteDirectory(new File(foldername));
			}

		}
	}

}
