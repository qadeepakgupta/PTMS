package com.ptms.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * @author Deepak Gupta
 * @Created Date 11-Oct-2017
 *
 */

public class LoginPage {

	public static final Logger log = Logger.getLogger(LoginPage.class.getName());

	WebDriver driver;


	@FindBy(id = "t1")
	private WebElement loginUsername;

	@FindBy(id = "p1")
	private WebElement loginPassword;

	@FindBy(xpath = "//span[contains(text(),'Login')]")
	private WebElement loginButton;

	@FindBy(xpath = "//span[contains(text(),'Confirm')]")
	private WebElement confirmButton;


	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void waitForElementClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}


	public void loginToApplication(String username, String password) throws InterruptedException {
		log.info("Starting loginToApplication() method ");

		loginUsername.sendKeys(username);
		log.info("entered username:-" + username + " and object is " + loginUsername.toString());
		loginPassword.sendKeys(password);
		log.info("entered password:-" + password + " and object is " + loginPassword.toString());

		waitForElementClickable(loginButton);
		loginButton.click();
		log.info("clicked on login button and object is:- " + loginButton.toString());
		checkCurrentSession();
		log.info("Ending loginToApplication() method ");
	}


	public void checkCurrentSession() throws InterruptedException {
		Thread.sleep(2000);
		log.info("Starting checkCurrentSession() method ");
		try {
			if (confirmButton.isDisplayed()) {
				waitForElementClickable(confirmButton);
				confirmButton.click();
				log.info("You are currently logged in from a different session. PopUp is displayed and option Yes is clicked for continue");
			}
			log.info("Ending checkCurrentSession() method ");
		} catch (Exception e) {
			log.error("Ending checkCurrentSession() method ");
			log.error("PopUp is not displayed");

		}
	}


}