import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.model.objects.dao.UserDAO;
import org.se2final.model.objects.dto.User;

@ExtendWith(SeleniumExtension.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterTest {
    private WebDriver driver;
    private User testUser;

    public RegisterTest() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
    }

    public User getKunde(){
        User tempUser = new User();
        tempUser.setName("Theodor");
        tempUser.setSurname("Testobjekt");
        tempUser.setGender("Herr");
        tempUser.setEmail("testobjekt@test.de");
        tempUser.setPasswort("0123456789");
        tempUser.setRolle(Roles.KUNDE);

        return tempUser;
    }

    public static User getMitarbeiter(){
        User tempUser = new User();
        tempUser.setName("Theodor");
        tempUser.setSurname("Testobjekt");
        tempUser.setGender("Herr");
        tempUser.setEmail("testobjekt@carlook.de");
        tempUser.setPasswort("0123456789");
        tempUser.setRolle(Roles.MITARBEITER);

        return tempUser;
    }
    @Test
    public void aNotCompleteError(){
        testUser = getKunde();
        testUser.setName("");
        register(testUser, testUser.getPasswort());

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("Bitte füllen Sie alle Felder aus!"));
        driver.quit();
    }

    @Test
    public void bDifferentPasswords(){
        testUser = getKunde();
        register(testUser, "012345678");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("Passwörter stimmen nicht überein!"));
        driver.quit();
    }

    @Test
    public void cPasswordLength(){
        testUser = getKunde();
        testUser.setPasswort("012345678");
        register(testUser, "012345678");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("Passwortlänge muss 10 Zeichen betragen!"));
        driver.quit();
    }

    @Test
    public void dWorkerMailAdress(){
        testUser = getKunde();
        testUser.setRolle(Roles.MITARBEITER);
        register(testUser, testUser.getPasswort());

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("Als Angestellter muss ihre E-Mail Adresse mit \"@carlook.de\" enden!"));
        driver.quit();
    }

    @Test
    public void eGuiltyEMail(){
        testUser = getKunde();
        testUser.setEmail("test.test");
        register(testUser, testUser.getPasswort());

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("E-Mail Adresse hat kein gültiges Format!"));
        driver.quit();
    }

    @Test
    public void eEmailIsAlreadyRegistered(){
        testUser = getKunde();
        testUser.setEmail("test@carlook.de");
        register(testUser, testUser.getPasswort());

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("E-Mail Adresse ist bereits registriert!"));
        driver.quit();
    }


    @Test
    public void zRegisterCutsomerWithNoException() {
        testUser = getKunde();
        register(testUser, testUser.getPasswort());

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("Das Konto wurde erfolgreich erstellt!"));
        driver.quit();
        DeleteTestUser(testUser);
    }

    @Test
    public void zRegisterWorkerWithNoException() {
        testUser = getMitarbeiter();
        register(testUser, testUser.getPasswort());

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for success
        WebElement sucessCreation = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("v-Notification")));
        Assert.assertTrue(sucessCreation.getText().contains("Das Konto wurde erfolgreich erstellt!"));
        driver.quit();
        DeleteTestUser(testUser);
    }

    public void DeleteTestUser(User user){
        UserDAO.getInstance().getUserID(user);
        UserDAO.getInstance().deleteCustomer(user.getId());
    }

    private void register(User currentUser, String confirmPassword){
        driver.get("localhost:8080");
        WebElement startRegisterButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[1]/div/div/div[3]/div/div[3]/div"));
        startRegisterButton.click();
        WebElement rolle = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div/div[3]/div/div/table/tbody/tr[1]/td[3]/div/select"));
        rolle.sendKeys(currentUser.getRolle());
        WebElement anrede = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div/div[3]/div/div/table/tbody/tr[2]/td[3]/div/select"));
        anrede.sendKeys(currentUser.getGender());
        WebElement nachname = driver.findElement(By.xpath("//*[@id=\"gwt-uid-10\"]"));
        nachname.sendKeys(currentUser.getSurname());
        WebElement vorname = driver.findElement(By.xpath("//*[@id=\"gwt-uid-12\"]"));
        vorname.sendKeys(currentUser.getName());
        WebElement email = driver.findElement(By.xpath("//*[@id=\"gwt-uid-14\"]"));
        email.sendKeys(currentUser.getEmail());
        WebElement passwort1 = driver.findElement(By.xpath("//*[@id=\"gwt-uid-16\"]"));
        passwort1.sendKeys(currentUser.getPasswort());
        WebElement passwort2 = driver.findElement(By.xpath("//*[@id=\"gwt-uid-18\"]"));
        passwort2.sendKeys(confirmPassword);
        WebElement registerButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div/div[3]/div/div/table/tbody/tr[8]/td[3]/div/div[1]/div"));
        registerButton.click();
    }
}
