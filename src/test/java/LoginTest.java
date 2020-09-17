import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@ExtendWith(SeleniumExtension.class)
public class LoginTest {

    private WebDriver driver;

    public LoginTest() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void loginTestMitarbeiter() {
        login("test@carlook.de", "0123456789");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        // wait for Landing Page
        WebElement ontheFlySearch = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div/div[3]/div/div[1]/div")));
        Assert.assertTrue(ontheFlySearch.getText().contains("Hallo, Max Mustermann!"));
        driver.quit();
    }

    @Test
    public void loginTestKunde() {
        login("danielprost@smail.de", "0123456789");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        // wait for Landing Page
        WebElement ontheFlySearch = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div/div[3]/div/div[1]/div")));
        Assert.assertTrue(ontheFlySearch.getText().contains("Hallo, Daniel Prost!"));
        driver.quit();
    }
    @Test
    public void loginTestExceptionMail() {
        login("daniel@smail.de", "0123456789");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for error Message
        WebElement error = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));
        Assert.assertTrue(error.getText().contains("Zur eingegebenen E-Mail Adresse existiert kein Konto!"));
        driver.quit();
    }

    @Test
    public void loginTestExceptionPassword() {
        login("danielprost@smail.de", "012345678");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for error Message
        WebElement error = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));
        Assert.assertTrue(error.getText().contains("Das eingegebene Passwort ist falsch!"));
        driver.quit();
    }

    private void login(String email, String password) {
        driver.get("localhost:8080");

        WebElement btnLoginHomePage = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[1]/div/div/div[3]/div/div[1]/div"));
        btnLoginHomePage.click();

        WebElement fldEmail = driver.findElement(By.xpath("//*[@id=\"gwt-uid-6\"]"));
        fldEmail.sendKeys(email);

        WebElement fldPassword = driver.findElement(By.xpath("//*[@id=\"gwt-uid-8\"]"));
        fldPassword.sendKeys(password);

        WebElement btnLoginLoginPage = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div/div[3]/div/div/table/tbody/tr[3]/td[3]/div/div[1]/div"));
        btnLoginLoginPage.click();
    }
}
