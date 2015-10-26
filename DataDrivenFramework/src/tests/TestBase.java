package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class TestBase {

	
	public static Properties CONFIG = null;
	public static WebDriver driver = null;
	public static EventFiringWebDriver evDriver = null;
	
	public void initialize() throws IOException
	{
		CONFIG = new Properties();
		FileInputStream in = new FileInputStream(System.getProperty("user.dir"+"//src//config//config.properties"));
		CONFIG.load(in);
		
		if(CONFIG.getProperty("browser").equals("Firefox"))
		{
			driver = new FirefoxDriver();
		}
		
		 evDriver = new EventFiringWebDriver(driver);

	}
	
	
	
	
	
	
	
	public static void main(String[] args) 
	{
		
	}

}
