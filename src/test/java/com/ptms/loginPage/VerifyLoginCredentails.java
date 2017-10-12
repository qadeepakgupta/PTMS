package com.ptms.loginPage;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ptms.testBase.TestBase;
import com.ptms.uiActions.DashboardPage;
import com.ptms.uiActions.LoginPage;

/**
 * 
 * @author Deepak Gupta
 * @Created Date 31-07-2017
 *
 */

public class VerifyLoginCredentails extends TestBase {

	public static final Logger	log	= Logger.getLogger(VerifyLoginCredentails.class.getName());
	LoginPage						loginPage;
	DashboardPage					dashPage;

	@BeforeClass
	public void setUp() throws IOException {
		openBrowser();
	}

	@Test
	public void testLoginCredentails() throws InterruptedException {
		try {
			log.info("============= Starting testLoginCredentails() Test =============");

			loginPage = new LoginPage(driver);
			loginPage.loginToApplication(OR.getProperty("ValidUsername"), OR.getProperty("ValidPassword"));

			Thread.sleep(2000);

			dashPage = new DashboardPage(driver);
			dashPage.verifyDashboardHeaderName(OR.getProperty("DashboardHeaderName"));
			dashPage.clickToSearchButton(OR.getProperty("TaskName"));
			dashPage.clickToUpdateTaskIcon();

			log.info("============= Finished testLoginCredentails() Test =============");

		} catch (Exception e) {
			log.error("============= Finished testLoginCredentails() Test =============");
			e.printStackTrace();
			log.error("Exception is: " + e.getMessage());
			getScreenShot("testLoginCredentails");
			Assert.fail();

		}

	}

}
