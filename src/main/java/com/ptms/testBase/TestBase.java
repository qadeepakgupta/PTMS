package com.ptms.testBase;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author Deepak Gupta
 * @Created Date 31-07-2017
 *
 */
public class TestBase {

	public static final Logger log = Logger.getLogger(TestBase.class.getName());

	public WebDriver driver;


	public Properties					OR	= new Properties();
	public static ExtentReports	extent;
	public static ExtentTest		test;
	public ITestResult				result;


	static {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");
		extent = new ExtentReports(
				System.getProperty("user.dir") + "/src/main/java/com/ptms/report/testScriptReport" + formater.format(calendar.getTime()) + ".html",
				false);
	}

	public void loadData() throws IOException {
		File file = new File(System.getProperty("user.dir") + "/Config/config.properties");
		FileInputStream f = new FileInputStream(file);
		OR.load(f);

	}

	public void setDriver(EventFiringWebDriver driver) {
		this.driver = driver;
	}

	public void appendURL(String newURL) {
		String oURL = driver.getCurrentUrl();
		String nURL = oURL + newURL;
		driver.get(nURL);
	}

	public void openBrowser() throws IOException {
		try {

			String log4jConfPath = "log4j.properties";
			PropertyConfigurator.configure(log4jConfPath);
			loadData();
			selectBrowser(OR.getProperty("browser"));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			getUrl(OR.getProperty("url"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkBrowserOS() {
		// Get Browser name and version.
		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();

		String browserName = caps.getBrowserName();
		String browserVersion = caps.getVersion();

		// Get OS name.
		String os = System.getProperty("os.name").toLowerCase();
		log.info("Operaing System: " + os + ", Browser Name and Version: " + browserName + " " + browserVersion);

	}

	public void selectBrowser(String browser) {
		try {
			// System.out.println("Browser Name:" + OR.getProperty("browser"));
			if (System.getProperty("os.name").contains("Window")) {
				if (browser.equals("chrome")) {
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
					options.addArguments("disable-infobars");
					Map<String, Object> prefs = new HashMap<String, Object>();
					prefs.put("credentials_enable_service", false);
					prefs.put("profile.password_manager_enabled", false);
					options.setExperimentalOption("prefs", prefs);
					driver = new ChromeDriver(options);
					checkBrowserOS();

				} else if (browser.equals("firefox")) {
					System.out.println(System.getProperty("user.dir"));
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/drivers/geckodriver.exe");
					driver = new FirefoxDriver();
					checkBrowserOS();

				}
			} else if (System.getProperty("os.name").contains("Mac")) {
				if (browser.equals("chrome")) {
					System.out.println(System.getProperty("user.dir"));
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
					driver = new ChromeDriver();
					// driver = new EventFiringWebDriver(dr);
					// eventListener = new WebEventListener();
					// driver.register(eventListener);
				} else if (browser.equals("firefox")) {
					System.out.println(System.getProperty("user.dir"));
					System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir") + "/drivers/geckodriver");
					driver = new FirefoxDriver();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getUrl(String url) {
		log.info("Navigating to :-" + url);
		driver.get(url);
		driver.manage().window().maximize();

	}


	public void getresult(ITestResult result) {
		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(LogStatus.PASS, result.getName() + " test is pass");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
		} else if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.ERROR, result.getName() + " test is failed" + result.getThrowable());
			String screen = captureScreen("");
			test.log(LogStatus.FAIL, test.addScreenCapture(screen));
		} else if (result.getStatus() == ITestResult.STARTED) {
			test.log(LogStatus.INFO, result.getName() + " test is started");
		}
	}

	@AfterMethod()
	public void afterMethod(ITestResult result) {
		getresult(result);
		test.log(LogStatus.INFO, result.getName() + " test finished");
	}

	@BeforeMethod()
	public void beforeMethod(Method result) {
		test = extent.startTest(result.getName());
		test.log(LogStatus.INFO, result.getName() + " test Started");
	}

	@AfterClass(alwaysRun = true)
	public void endTest() throws InterruptedException {
		closeBrowser();

	}

	public void closeBrowser() throws InterruptedException {
		// driver.quit();
		extent.endTest(test);
		extent.flush();

	}

	public void waitForElement(WebDriver driver, int timeOutInSeconds, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void getScreenShot(String name) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/src/main/java/com/ptms/screenshot/";
			File destFile = new File((String) reportDirectory + name + "_" + formater.format(calendar.getTime()) + ".png");


			FileUtils.copyFile(scrFile, destFile);
			// This will help us to link the screen shot in testNG report
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void getScreenShot(WebDriver driver, ITestResult result, String folderName) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		String methodName = result.getName();

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/src/main/java/com/ptms/";
			File destFile = new File(
					(String) reportDirectory + "/" + folderName + "/" + methodName + "_" + formater.format(calendar.getTime()) + ".png");

			FileUtils.copyFile(scrFile, destFile);

			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getScreenShotOnSucess(WebDriver driver, ITestResult result) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		String methodName = result.getName();

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/src/main/java/com/ptms/";
			File destFile = new File(
					(String) reportDirectory + "/failure_screenshots/" + methodName + "_" + formater.format(calendar.getTime()) + ".png");

			FileUtils.copyFile(scrFile, destFile);

			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String captureScreen(String fileName) {
		if (fileName == "") {
			fileName = "Issue_Screen_Shot";
		}
		File destFile = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/src/main/java/com/ptms/screenshot/";
			destFile = new File((String) reportDirectory + fileName + "_" + formater.format(calendar.getTime()) + ".png");
			FileUtils.copyFile(scrFile, destFile);
			// This will help us to link the screen shot in testNG report
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destFile.toString();
	}

	public WebElement waitForElement(WebDriver driver, WebElement element, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}


}
