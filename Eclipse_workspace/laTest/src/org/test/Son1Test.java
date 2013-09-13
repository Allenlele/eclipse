package org.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Son1Test extends BaseTest {

	@BeforeTest()
	public void everyTest1(){
		System.out.println("This is executed before test1");
	}
	
	@BeforeMethod()
	public void everyTest3() {
		System.out.println("This is excuted before every son1 method");
	}

	@Test()
	public void son1Test() {
		System.out.println("This is a son1 test");
	}

	@Test()
	public void son1Test2() {
		System.out.println("This is a son1 two test");
	}
}
