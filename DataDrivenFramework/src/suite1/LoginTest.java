package suite1;

import java.io.IOException;

import org.junit.Before;

import tests.TestBase;

public class LoginTest extends TestBase{

	@Before
	public void BeforeTest()
	{
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@org.junit.Test
	public void loginTest()
	{
		evDriver.get(CONFIG.getProperty("testSiteName"));
	}


}
