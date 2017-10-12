package com.ptms.loginPage;


import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class PTMS_Jar {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		TestListenerAdapter tla = new TestListenerAdapter(); // Line 1
		TestNG testng = new TestNG(); // line 2
		testng.setTestClasses(new Class[] { VerifyLoginCredentails.class }); // Line 3
		testng.addListener(tla); // Line 4
		testng.run(); // Line 5

	}

}
