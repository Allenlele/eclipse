package org.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseTest {

	@BeforeSuite()
	public void baseTest() {
		System.out.println("This is a BaseTest");
	}

	@BeforeMethod()
	public void everyTest() {
		System.out.println("This is excuted before every method");
	}
}
