package com.plefs.backend;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListner implements ITestListener {

	@Override
	public void onFinish(ITestContext arg0) {
		System.out.println("Test Case Execution Ended: " +arg0.getName());
			}

	@Override
	public void onStart(ITestContext arg0) {
		System.out.println("Test Case Execution Started: " +arg0.getName());
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		System.out.println("Test Case Execution Failed: " +arg0.getName());
		
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		System.out.println("Test Case Execution Skipped: " +arg0.getName());
		
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		System.out.println("Test Case Execution Passed: " +arg0.getName());
		
	}

}
