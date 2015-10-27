package suite1;

import java.io.IOException;

import org.junit.Before;
import org.testng.annotations.Test;

import tests.TestBase;

public class RegisterTest extends TestBase{
	
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
