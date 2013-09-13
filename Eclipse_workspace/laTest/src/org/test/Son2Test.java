package org.test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Son2Test extends BaseTest {

	@BeforeTest()
	public void everyTest1() {
		System.out.println("This is executed before test2");
	}

	@Test()
	public void son2Test() {
		System.out.println("This is a son2 test");
	}

	@Test()
	public void son2Test2() {
		System.out.println("This is a son2 two test");
	}
}
