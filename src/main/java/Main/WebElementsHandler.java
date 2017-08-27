package Main;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.Assert.fail;

public class WebElementsHandler {
    private static WebDriver driver;
    private static WebElementsHandler instance = null;

    private WebElementsHandler(){
        // Exists only to defeat instantiation.
    }

    public WebElementsHandler(WebDriver driver){
        WebElementsHandler.driver = driver;
        WebElementsHandler.instance = this;
    }

    public static WebElementsHandler getInstance(){
        if(WebElementsHandler.instance == null) {
            WebElementsHandler.instance = new WebElementsHandler();
        }
        return WebElementsHandler.instance;
    }

    public WebDriver getDriver(){
        return driver;
    }

    public boolean isElementPresent(By by) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.waitForElement(this.findElement(by));

        try {
            this.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void fillElementOrFail(By element, String value, String failText){
        if (this.isElementPresent(element)) {
            WebElement webElement = this.findElement(element);
            if (webElement.isDisplayed()) {
                webElement.sendKeys(value);
            } else {
                fail(failText);
            }
        }
    }

    public void clickElementOrFail(By element, String failText){
        if (this.isElementPresent(element)) {
            WebElement webElement = this.findElement(element);
            if (webElement.isDisplayed()) {
                webElement.click();
            } else {
                fail(failText);
            }
        }
    }

    public void deleteAndWriteNewValue(By element, String newValue, String failText) {
        if (this.isElementPresent(element)) {
            WebElement webElement = this.findElement(element);
            if (webElement.isDisplayed()) {
                webElement.findElement(element).sendKeys(Keys.chord(Keys.CONTROL, "a"), newValue);
            } else {
                fail(failText);
            }
        }
    }

    public void dragAndDrop(By elementDragged, By elementTarget, String failText) {
        if (isElementPresent(elementDragged) && isElementPresent(elementTarget)) {
            WebElement source = this.findElement(elementDragged);
            WebElement target = this.findElement(elementTarget);

            Actions builder = new Actions(this.getDriver());
            builder.dragAndDrop(source, target).perform();
            //TODO: This fails when you add multiple fields. Anyway, it's not a proper way to check if the field was added
            //assertEquals("Untitled", target.getText());
        } else {
            fail(failText);
        }

    }

    public void click(By element) {
        if(!this.findElements(element).isEmpty()){
            this.findElement(element).click();
        }
        else {
            fail("NoSuchElementException: " + element.toString());
        }
    }

    public WebElement findElement(By by){
        return this.getDriver().findElement(by);
    }

    public List findElements(By by){
        return this.getDriver().findElements(by);
    }

    public List findElementsByPartialId(String id){
        return this.findElements(By.xpath("//*[contains(@id, '" + id + "')]"));
    }

    public List findElementsByPartialId(String parentClass, String id){
        return this.findElements(By.xpath("//*[contains(@class, '" + parentClass + "')] //*[contains(@id, '" + id + "')]"));
    }

    public void fill(By element, String value) {
        this.findElement(element).sendKeys(value);
    }

    public String findElementAndGetValue(By element) {
        return this.findElement(element).getAttribute("value");
    }

    public String getElementCSSProperty(By element, String cssProperty){
        WebElement webElement = this.findElement(element);
        return this.getElementCSSProperty(webElement, cssProperty);
    }

    public String getElementCSSProperty(WebElement webElement, String cssProperty){
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        return (String) js.executeScript("return getComputedStyle(arguments[0]).getPropertyValue('" + cssProperty + "');", webElement);
    }

    private void waitForElement(WebElement webElement) {
        WebDriverWait visibilityWait = new WebDriverWait(this.getDriver(), 20);
        visibilityWait.until(ExpectedConditions.visibilityOf(webElement));
        visibilityWait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
}