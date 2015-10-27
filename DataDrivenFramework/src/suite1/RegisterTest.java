package suite1;

import java.io.IOException;

import org.junit.Before;
import org.testng.annotations.Test;

import tests.TestBase;

public class RegisterTest extends TestBase{
	
	public String id;
	public String name;
	public String city;
	public String country;
	
	
	
	public RegisterTest(String id, String name, String city, String country) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.country = country;
	}
	
	@Before
	public void beforetest() throws IOException
	{
		initialize();
	}
	@org.junit.Test
	public void registerTest()
	{
		evDriver.get(CONFIG.getProperty("testSiteName"));
		
	}
	
}
