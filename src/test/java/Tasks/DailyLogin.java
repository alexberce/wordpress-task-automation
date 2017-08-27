package Tasks;

import Core.Main;
import XPaths.WordPressLoginPage;
import XPaths.WordPressMenu;
import XPaths.WordPressPostsPage;
import org.apache.commons.io.LineIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.*;
import java.net.HttpURLConnection;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import java.net.URL;

public class DailyLogin extends Main {

    private static final String fileURL = "https://raw.githubusercontent.com/florincirtog/test/master/data.txt";

    @Before
    public void runBeforeTestMethod() {
    }

    @After
    public void runAfterTestMethod() {
    }

    @Test
    public void dailyPostTask() {
        try {
            LineIterator linesIterator = this.getSitesAddresses();

            while (linesIterator.hasNext()) {
                String[] line = linesIterator.nextLine().split(" ");
                String siteUrl = line[0];
                String username = line[1];
                String password = line[2];

                if(siteUrl.isEmpty() || username.isEmpty() || password.isEmpty())
                    continue;

                try {
                    this.loginToWebsite(siteUrl, username, password);
                    this.createNewPost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            linesIterator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewPost() {
        String postTitle = "This post was generated automatically";

        this.getWebElementsHandler().clickElementOrFail(By.xpath(WordPressMenu.POSTS_MENU), "Cannot find item Posts in WordPress menu");

        this.getWebElementsHandler().clickElementOrFail(By.xpath(WordPressPostsPage.ADD_NEW_POST), "Cannot find 'Add new post' button");

        this.getWebElementsHandler().clickElementOrFail(By.xpath(WordPressPostsPage.TITLE_POST), "Cannot find 'Post title' input");

        (new Actions(this.getDriver()))
            .moveToElement(this.getWebElementsHandler().findElement(By.xpath(WordPressPostsPage.TITLE_POST)))
                .click()
                .sendKeys(postTitle)
                .build()
                .perform();

        this.getWebElementsHandler().clickElementOrFail(By.xpath(WordPressPostsPage.PUBLISH_POST), "Publish post button is not there!");
    }

    /**
     * @param siteUrl  Site url
     * @param username Site username
     * @param password Site password
     */
    private void loginToWebsite(String siteUrl, String username, String password) {
        this.getWindowHandler().goToPage(siteUrl);
        this.getWebElementsHandler().fillElementOrFail(By.xpath(WordPressLoginPage.USER_LOGIN), username, "Cannot find username input");
        this.getWebElementsHandler().fillElementOrFail(By.xpath(WordPressLoginPage.PASSWORD_LOGIN), password, "Cannot find password input");

        this.getWebElementsHandler().clickElementOrFail(By.xpath(WordPressLoginPage.SUBMIT_LOGIN), "Submit button is not there!");
    }

    /**
     * @return LineIterator
     */
    private LineIterator getSitesAddresses() throws Exception {
        try {
            URL url = new URL(DailyLogin.fileURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return new LineIterator(bufferedReader);
        } catch (NoSuchElementException | IOException e) {
            throw new Exception("Cannot read file!");
        }
    }
}
