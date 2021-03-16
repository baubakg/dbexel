package dbexel.presentation.aat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.support.ui.Select;

public class Test_EditAttributeValuePOC {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		// driver = new HtmlUnitDriver();
		baseUrl = "file:///C:/Users/gandomi/Documents/Private/Dev/DBEXEL/src/test/resources/testExamples/scenarios/ShowDistinctWorkSheet_A.htm";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testEditAttributeValuePOC() throws Exception {
		driver.get(baseUrl);

		driver.findElement(By.id("editLink_1")).click();

		// WebElement l_table = driver.findElement(By.id("contentTable"));

		List<WebElement> rows = driver.findElement(By.id("contentTable"))
				.findElements(By.tagName("tr"));

		assertEquals(3, rows.size());

		driver.findElement(By.id("addAttrValue")).click();
		driver.findElement(By.name("newAddedAttributes[0]")).clear();
		driver.findElement(By.name("newAddedAttributes[0]")).sendKeys("e");
		driver.findElement(By.name("newAddedAttributeValues[0]")).clear();
		driver.findElement(By.name("newAddedAttributeValues[0]"))
				.sendKeys("16");
		
		
		
		driver.findElement(By.id("EditWorkSheetEntry_0")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alert.getText();
		} finally {
			acceptNextAlert = true;
		}
	}
}
