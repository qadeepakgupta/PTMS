package com.ptms.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardPage {

	public static final Logger	log	= Logger.getLogger(DashboardPage.class.getName());
	WebDriver						driver;


	@FindBy(xpath = "//span[contains(text(),'Search')]")
	private WebElement searchButton;

	@FindBy(className = "leftwww")
	private WebElement headerName;

	@FindBy(xpath = "//label[text()='Project Name']//following :: input[2]")
	private WebElement projectNameTextField;

	@FindBy(xpath = "//a[text()='Miscellaneous Tasks']")
	private WebElement selectProjectName;

	@FindBy(xpath = "//label[text()='Project Name']//following :: input[3]")
	private WebElement taskNameTextField;
	
	@FindBy(xpath = "//*[@id='1gridupdate']/span/span")
	private WebElement updateTaskNameIcon;
	


	public DashboardPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public String dashboardPanelHeading() {
		log.info("Dashboard panel heading is:- " + headerName.getText());
		return headerName.getText();
	}


	public void clickToUpdateTaskIcon() {
		waitForElementClickable(updateTaskNameIcon);
		updateTaskNameIcon.click();
		log.info("clicked on update task icon and object is:- " + updateTaskNameIcon.toString());
	}
	
	
	public void clickToSearchButton(String projectName, String taskName) throws InterruptedException {
		searchProjectName(projectName);
		log.info("entered project name:-" + projectName + " and object is " + projectNameTextField.toString());
		Thread.sleep(2000);
		searchTaskName(taskName);
		log.info("entered task name:-" + taskName + " and object is " + taskNameTextField.toString());
		searchButton.click();
		log.info("clicked on Search button and object is:- " + searchButton.toString());
	}

	public void clickToSearchButton(String taskName)  {
		searchTaskName(taskName);
		log.info("entered task name:-" + taskName + " and object is " + taskNameTextField.toString());
		searchButton.click();
		log.info("clicked on Search button and object is:- " + searchButton.toString());
	}

	
	
	public void waitForElementClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}


	public void searchProjectName(String projectName) {
		projectNameTextField.sendKeys(projectName);
		projectNameTextField.sendKeys(Keys.ENTER);

	}


	public void searchTaskName(String taskName) {
		taskNameTextField.sendKeys(taskName);
	}


	public void verifyDashboardHeaderName(String headerName) {
		Assert.assertEquals(dashboardPanelHeading(), headerName);
	}
}
