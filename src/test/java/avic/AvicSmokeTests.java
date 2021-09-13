package avic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.openqa.selenium.By.xpath;
import static org.testng.Assert.*;

public class AvicSmokeTests {
    private static final Integer EXPECTED_AMOUNT = 1550;

    private WebDriver driver;

    @BeforeTest
    public void profileSetup() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testsSetup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatActionGoodsContainsBadge() {
        driver.findElement(By.xpath(("(//a[contains(@href, 'skidki')])[1]"))).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mm-0")));
        List<WebElement> elementList = driver.findElements(xpath("//div[@class='prod-cart height']"));
        List<WebElement> elementList1 = driver.findElements(By.xpath("//div[@class='prod-cart height']//img[@data-src='https://avic.ua/assets/cache/badges/aktsiya-2-badge_sm.png']"));
        assertEquals(elementList.size(), elementList1.size());
    }

    @Test(priority = 2)
    public void checkThatTradeinEstimatesMonetaryValue() {
        driver.findElement(By.xpath(("//div[contains(@class, 'header')]//a[@href='/tradein']"))).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mm-0")));
        driver.findElement(By.xpath(("//button[text()='Вперед']"))).click();
        driver.findElement(By.xpath(("//button[text()='Вперед']"))).click();
        driver.findElement(By.xpath(("//div[@style='display: block;']//button[text()='Да']"))).click();
        driver.findElement(By.xpath(("//button[text()='Вперед']"))).click();
        driver.findElement(By.xpath(("//div[@style='display: block;']//button[text()='Да']"))).click();
        driver.findElement(By.xpath(("//button[text()='Вперед']"))).click();
        driver.findElement(By.xpath(("(//button[@class='main-btn main-btn--tradein is-fullwidth'])[1]"))).click();
        driver.findElement(By.xpath(("//button[text()='Вперед']"))).click();
        driver.findElement(By.xpath(("(//button[@class='main-btn main-btn--tradein is-fullwidth'])[3]"))).click();
        driver.findElement(By.xpath(("//button[text()='Вперед']"))).click();
        String value = driver.findElement(By.xpath(("//h2"))).getText();

        assertMoneyPresent(value);
    }

    @Test(priority = 4)
    public void checkCorrectСalculationBenefit() {
        driver.findElement(xpath("//span[@class='sidebar-item']")).click();
        driver.findElement(xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'apple-store')]")).click();
        driver.findElement(xpath("//div[@class='brand-box__title']/a[contains(@href,'iphone')]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        String[] str = driver.findElement(By.xpath(("(//div[@class='prod-cart__status-box bg-orange'])[1]"))).getText()
                .split(" ");
        int benefit = fromString(str[2]);
        String[] str1 = driver.findElement(By.xpath(("(//div[@class='prod-cart__prise']//div[contains(@class,'old')])[1]"))).getText().split(" ");
        int oldPrice = fromString(str1[0]);
        String[] str2 = driver.findElement(By.xpath(("(//div[@class='prod-cart__prise-new'])[1]"))).getText().split(" ");
        int newPrice = fromString(str2[0]);
        int expectedBenefit = oldPrice-newPrice;
        assertEquals(expectedBenefit, benefit);
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }

    private static void assertMoneyPresent(String value) {
        var strings = value.split(" ");
        var amount = fromString(strings[0]);
        assertTrue(strings.length == 2, "Expect value 2");
        assertNotNull(amount, "Amount should be present");
        assertEquals(EXPECTED_AMOUNT, amount, "Found incorrect sum we expect 1550");
    }

    private static Integer fromString(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            System.out.println("Found incorrect value " + value);
            return null;
        }
    }
}
