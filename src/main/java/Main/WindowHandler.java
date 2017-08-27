package Main;

import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WindowHandler {
    private static WindowHandler instance = null;
    private static WebDriver driver;

    private WindowHandler(){
        // Exists only to defeat instantiation.
    }

    public WindowHandler(WebDriver driver){
        WindowHandler.driver = driver;

        this.getDriver().manage().window().maximize();
        this.getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        WindowHandler.instance = this;
    }

    public static WindowHandler getInstance(){
        if(WindowHandler.instance == null) {
            WindowHandler.instance = new WindowHandler();
        }
        return WindowHandler.instance;
    }

    private WebDriver getDriver(){
        return WindowHandler.driver;
    }

    public void iAmOnPage(String title){
        String parentWindowId = this.getDriver().getWindowHandle();

        try {
            String pageTitle = this.getDriver().switchTo().window(parentWindowId).getTitle();
            if (pageTitle.equals(title)) {
                assertEquals(title, this.getDriver().getTitle());
            }
        } finally {
            this.getDriver().switchTo().window(parentWindowId);
        }
    }

    public void goToPage(String url){
        this.getDriver().get(url);
    }

    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    public String getCurrentUrl(){
        return this.getDriver().getCurrentUrl();
    }

    public void wait(int seconds){
        System.out.printf("\nWaiting %d seconds", seconds);
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException | IllegalMonitorStateException e) {
            e.printStackTrace();
        }
    }

    public void switchToIframe(String iframeId){
        this.getDriver().switchTo().frame(iframeId);
    }

    public void switchToDefault(){
        this.getDriver().switchTo().defaultContent();
    }
}