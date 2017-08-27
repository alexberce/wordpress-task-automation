package Core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class WebElement {
    private org.openqa.selenium.WebElement webElement;
    private WebDriver webDriver;

    public WebElement(org.openqa.selenium.WebElement webElement, WebDriver webDriver) {
        this.webDriver = webDriver;
        this.webElement = webElement;
    }

    public org.openqa.selenium.WebElement getElement() {
        return webElement;
    }

    public void click() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitForElement();
        try {
            webElement.click();
        } catch (WebDriverException wde) {
            wde.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void submit() {
        webElement.submit();
    }

    public void sendKeys(CharSequence... charSequences) {
        waitForElement();
        webElement.sendKeys(charSequences);
    }

    public void clear() {
        webElement.clear();
    }

    public List<WebElement> findElements(By by) {
        List<WebElement> result = new ArrayList<>();
        for (org.openqa.selenium.WebElement element : webElement.findElements(by)) {
            result.add(new WebElement(element, webDriver));
        }
        return result;
    }

    public WebElement findElement(By by) {
        return new WebElement(webElement.findElement(by), webDriver);
    }

    private void waitForElement() {
        WebDriverWait visibilityWait = new WebDriverWait(webDriver, 20);
        visibilityWait.until(ExpectedConditions.visibilityOf(webElement));
        visibilityWait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
}
