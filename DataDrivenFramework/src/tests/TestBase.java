package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import org.apache.jasper.compiler.Node.GetProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class TestBase {

	public static Properties CONFIG = null;
	public static WebDriver driver = null;
	public static EventFiringWebDriver evDriver = null;
	public static boolean isLoggedIn=false;

	public void initialize() throws IOException {
		if (evDriver == null) {
			CONFIG = new Properties();
			FileInputStream in = new FileInputStream(("C:\\Users\\Galil1\\Desktop\\eclipse\\tempGit\\DDF\\DataDrivenFramework\\src\\config\\config.properties"));
			CONFIG.load(in);

			if (CONFIG.getProperty("browser").equals("Firefox")) {
				driver = new FirefoxDriver();
			}

			evDriver = new EventFiringWebDriver(driver);
			evDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		else
			System.out.println("Already initialized!");
	}
	
	public static WebElement getObject(String xpath)
	{
		try{
		return driver.findElement(By.xpath(CONFIG.getProperty(xpath)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/*public static void main(String [] args) throws IOException
	{

		//WebDriver driver = new FirefoxDriver();
		TestBase tb = new TestBase();
		tb.initialize();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		driver.get("http://www.deviantart.com");
		

		//driver.findElement(By.xpath("//*[@id='login-username']")).sendKeys("Pass1");
		getObject("login-user-name").sendKeys("Pass1");
		//driver.findElement(By.xpath("//*[@id='login-password']")).sendKeys("Pass2");
		getObject("login-password").sendKeys("Pass2");
		driver.findElement(By.xpath("//*[@id='login-remember-label']")).click();

		System.out.println("Success.. Byebye!!");
	}*/

}
