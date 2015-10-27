package suite1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
	LoginTest.class,
	RegisterTest.class
})
public class FirstSuiteRunner {
	
	

}
