import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.By.xpath;

public class AvicTest {
    private WebDriver driver;

    @BeforeTest
    public void proFileSetup() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testsSetUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, SECONDS);
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkButtonWillShowMore() {

        driver.findElement(xpath("//span[text()='Умный дом']/..")).click();
        driver.findElement(By.xpath("//div[@class='pagination']/a[text()='Показать еще 12']")).click();
        List<WebElement> list =driver.findElements(xpath("//div[@class='item-prod col-lg-3']"));
        Assert.assertEquals(list.size(),24);
    }

    @Test(priority = 2)
    public void checkAddingTwoItemsToCart() {
        driver.findElement(By.xpath("//div[@class='category-box-wrap hidden-mob ']" +
                "//a[@href='https://avic.ua/naushniki-apple-airpods-with-charging-case-mv7n2-244460-item']//div[@class='category-box__more more']")).click();
        driver.findElement(By.xpath("//a[text()='Купить']")).click();
        driver.findElement(By.xpath("//span[@class='js_plus btn-count btn-count--plus ']")).click();
        driver.findElement(By.xpath("//div[@class='btns-cart-holder']//a[contains(@class,'btn--orange')]")).click();
        String actualProductsCountInCart =
                driver.findElement(xpath("//div[contains(@class,'header-bottom__cart')]//div[contains(@class,'cart_count')]"))
                        .getText();
        Assert.assertEquals(actualProductsCountInCart, "2");
    }

    @Test(priority=3)
    public void checkSendAnEmailToSupport() {
        driver.findElement(By.xpath("//a[text()='Письмо директору']")).click();
        driver.findElement(By.xpath("//a[text()='Служба поддержки']")).click();
        driver.findElement(By.xpath("//div[@class='tab-content shown']//form/div/div//input[@placeholder='Ваше имя']")).sendKeys("Petrovich");
        driver.findElement(By.xpath("//div[@class='tab-content shown']" +
                        "//form/div/div/input[@placeholder='Электронная почта']"))
                .sendKeys("cyber_police@gmail.com");
        driver.findElement(By.xpath("//div[@class='tab-content shown']//form/div/div/textarea"))
                .sendKeys("Hello");
        driver.findElement(By.xpath("//div[@class='tab-content shown']" +
                        "//div[@class='form-field input-field col-xs-6']/button[contains(text(),'Отправить сообщение')]"))
                .click();
        Assert.assertEquals(driver.findElement(By.xpath("//div[text()='Сообщение успешно отправлено']")).getText(), "Сообщение успешно отправлено");
    }
    @Test(priority = 4)
    public void checkFilterByPrice() {
        driver.findElement(By.xpath("//span[@class='sidebar-item']")).click();
        driver.findElement(By.xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'apple-store')]")).click();
        driver.findElement(By.xpath("//div[@class='brand-box__title']/a[contains(@href,'ipad')]")).click();
        driver.findElement(By.xpath("//input[@class='form-control form-control-min']")).clear();
        driver.findElement(By.xpath("//input[@class='form-control form-control-min']")).sendKeys("30000");
        driver.findElement(By.xpath("//input[@class='form-control form-control-max']")).clear();
        driver.findElement(By.xpath("//input[@class='form-control form-control-max']")).sendKeys("40000" + "\n");
        driver.manage().timeouts().implicitlyWait(20,SECONDS);
        driver.findElement(By.xpath("//div[@class='form-group filter-group js_filter_parent open-filter-tooltip']/" +
                "/span[@class='filter-tooltip-inner']")).click();
        List<WebElement> webElements = driver.findElements(By.xpath("//div[@class='prod-cart__prise-new']"));
        Assert.assertTrue(checkFilter(webElements), String.valueOf(true));
    }

    public boolean checkFilter(List<WebElement> webElements) {
        boolean booleanVariable = true;
        for (int i = 0; i < webElements.size(); i++) {
            String stringFromChar = webElements.get(i).getText().split(" ")[0];
            int integerFromString = Integer.parseInt(stringFromChar);
            if (integerFromString > 40000 || integerFromString < 30000) {
                booleanVariable = false;
            }
        }
        return booleanVariable;
    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

