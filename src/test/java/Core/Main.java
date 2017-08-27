package Core;

import Main.*;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class Main {

    private static WebDriver driver;

    protected static WindowHandler windowHandler;

    private static WebElementsHandler webElementsHandler;

    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "./src/main/resources/drivers/chromedriver.exe");

        Main.driver = new ChromeDriver();
        Main.windowHandler = new WindowHandler(Main.driver);
        Main.webElementsHandler = new WebElementsHandler(Main.driver);
    }

    @Before
    public abstract void runBeforeTestMethod();

    @After
    public abstract void runAfterTestMethod();

    @AfterClass
    public static void tearDown() throws Exception {
        Main.driver.quit();
    }

    protected WebDriver getDriver(){
        return Main.driver;
    }

    protected WindowHandler getWindowHandler(){
        return Main.windowHandler;
    }

    protected WebElementsHandler getWebElementsHandler(){
        return Main.webElementsHandler;
    }

    protected void goToPage(String url){
        this.getWindowHandler().goToPage(url);
    }
}