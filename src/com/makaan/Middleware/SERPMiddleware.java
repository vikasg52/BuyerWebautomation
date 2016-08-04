package com.makaan.Middleware;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.makaan.Dictionary.SERP;
import com.makaan.Utilities.ConnectDB;
import com.makaan.Utilities.ExcelOperation;
import com.makaan.Utilities.Xls_Reader;
import com.makaan.Webhelper.Webhelper;
import com.makaan.Middleware.LoginMiddleware;

public class SERPMiddleware {

	public static Webhelper wb = null;
	public static SERP dict = null;
	public static WebDriver driver;
	public static ExcelOperation ex = null;
	public static ConnectDB db = null;
	public static Xls_Reader xls = new Xls_Reader("Files/Marketplace.xls");
	public static LoginMiddleware login = null;

	public SERPMiddleware() {
		System.out.println("inside Search Middleware Constructor");

		wb = new Webhelper();
		ex = new ExcelOperation();
		db = new ConnectDB();
		login = new LoginMiddleware();

		System.out.println("inside Middleware Constructor");

	}

	public static void OpenURL() throws Exception {
		String URL = null;
		URL = ReadSheet("SERP", "URL", 2);
		wb.InitiateDriver();
		System.out.println("Opening Buy Filters URL through Middleware");
		wb.GetURL(URL);
		wb.WaitUntill(dict.filterbar);

	}

	public static String ReadSheet(String Sheet, String Col_Name, int row_id)
			throws IOException, NoSuchElementException, TimeoutException {

		String data = xls.getCellData(Sheet, Col_Name, row_id);
		System.out.println("Data from sheet " + data);

		return (data);
	}

	public boolean ValiadateBuyFilters() throws Exception {

		String Path = null;
		if (wb.IsElementPresent(dict.filterbar)) {
			System.out.println("filter bar is present");
			wb.ClickbyXpath(dict.buy_rentdropdown);
			wb.ClickbyXpath(dict.buy);
			SetBudget("Buy", 2);
			Path = dict.BedroomType.concat(ReadSheet("SERP", "Bedroom", 2)).concat("')]");
			wb.WaitUntillVisiblility(dict.Bedroom);
			wb.ClickbyXpath(dict.Bedroom);
			wb.WaitUntillVisiblility(Path);
			wb.ClickbyXpath(Path);
			ValidateMorefilters("Buy");
			wb.ClickbyXpath(dict.SortByVal);
			wb.ClickbyXpath(".//label[@for='sortBy1']");
			wb.WaitUntillVisiblility(dict.PropertyCount);
			getPropertyValues(dict.PropertyCount);
			Reset();

		} else {
			System.err.println("Filter bar was not present");
			return false;
		}
		return true;
	}

	public boolean ValiadateRentFilters() throws Exception {
		Thread.sleep(2000);
		String Path = null;
		if (wb.IsElementPresent(dict.filterbar)) {
			System.out.println("filter bar is present");
			wb.ClickbyXpath(dict.buy_rentdropdown);
			wb.ClickbyXpath(dict.rent);
			SetBudget("Rent", 3);
			Path = dict.BedroomType.concat(ReadSheet("SERP", "Bedroom", 3)).concat("')]");
			wb.WaitUntillVisiblility(dict.Bedroom);
			wb.ClickbyXpath(dict.Bedroom);
			wb.WaitUntillVisiblility(Path);
			wb.ClickbyXpath(Path);
			ValidateMorefilters("rent");
			wb.ClickbyXpath(dict.SortByVal);
			wb.ClickbyXpath(".//label[@for='sortBy1']");
			wb.WaitUntillVisiblility(dict.PropertyCount);
			getPropertyValues(dict.PropertyCount);
			Reset();

		} else {
			System.err.println("Filter bar was not present");
			return false;
		}
		return true;
	}

	public void SetBudget(String Type, int RowId) throws Exception {
		String Path = null;
		Path = dict.BudgetMinValue.concat(ReadSheet("SERP", Type + " Min", RowId)).concat("']");
		Thread.sleep(1000);
		wb.ClickbyXpath(dict.Budget);
		Thread.sleep(1000);
		wb.ClickbyXpath(dict.BudgetMin);
		wb.WaitUntillVisiblility(Path);
		wb.ClickbyXpath(Path);
		Path = dict.BudgetMaxValue.concat(ReadSheet("SERP", Type + " Max", RowId)).concat("']");
		Thread.sleep(1000);
		wb.ClickbyXpath(dict.Budget);
		wb.WaitUntillVisiblility(dict.BudgetMax);
		wb.ClickbyXpath(dict.BudgetMax);
		wb.WaitUntillVisiblility(Path);
		wb.ClickbyXpath(Path);
	}

	public void ValidateMorefilters(String Type) throws Exception {

		System.out.println("Checked all Filters applied through URL");
		wb.WaitUntillVisiblility(dict.MoreFilter);
		wb.ClickbyXpath(dict.MoreFilter);
		wb.WaitUntillVisiblility(dict.PropertyTypeAppartment);
		wb.ClickbyXpath(dict.PropertyTypeAppartment);
		wb.WaitUntillVisiblility(dict.PostedByAgent);
		wb.ClickbyXpath(dict.PostedByAgent);
		wb.WaitUntillVisiblility(dict.SelectBathroom);
		wb.ClickbyXpath(dict.SelectBathroom);
		if (Type.equalsIgnoreCase("Buy")) {
			wb.ClickbyXpath(dict.AgeofPropertyAny);
			wb.ClickbyXpath(dict.SelectNew_Resale);
			wb.ClickbyXpath(dict.Selectnew);
		}
	}

	public void getPropertyValues(String Value) {
		System.out.println("Propert count on page is +" + wb.getText(Value));
	}

	public void Reset() throws Exception {
		wb.WaitUntillVisiblility(dict.ResetButton);
		wb.ClickbyXpath(dict.ResetButton);
	}

	public boolean ValidateSellerType() throws Exception {
		System.out.println("Inside Validate Seller Type");
		String Type = wb.getText(dict.SellerType);
		if (Type.contains("agent")) {
			wb.ClickbyXpath(dict.SellerLink);
			if (wb.IsElementPresent(dict.VerifyAgent)) {
				System.out.println(wb.getText(dict.VerifyAgent));
			} else {
				wb.Back();
				return false;
			}
		} else if (Type.contains("builder")) {
			wb.ClickbyXpath(dict.SellerLink);
			Thread.sleep(2000);
			if (wb.IsElementPresent(dict.VerifyBuilder)) {
				System.out.println(wb.getText(dict.VerifyBuilder));
			} else {
				wb.Back();
				return false;
			}
		} else {
			System.out.println("Seller Type is otherthan agent or Builder");
			return true;
		}
		wb.Back();
		return true;
	}

	public boolean VerifyLocality() {
		System.out.println("Inside Locality Link verification");
		try {
			if (wb.IsElementPresent(dict.LocationLink)) {
				wb.ClickbyXpath(dict.LocationLink);
				try {
					if (wb.IsElementPresent(dict.VerifyLocality)) {
						System.out.println("Locality link is opening");
					}
				} catch (Exception e) {
					System.out.println("not able to verify Locality page");
					wb.Back();
					return false;
				}

			}
		} catch (Exception e1) {
			System.out.println("Some error occurred while opening from Location link");
			wb.Back();
			return false;

		}
		wb.Back();
		return true;

	}

	public boolean VerifyProject() {
		System.out.println("Inside Project Link verification");
		try {
			if (wb.IsElementPresent(dict.ProjectLink)) {
				wb.ClickbyXpath(dict.ProjectLink);
				Thread.sleep(2000);
				try {
					if (wb.IsElementPresent(dict.VerifyProperty)) {
						System.out.println("Project link is opening");
					}
				} catch (Exception e) {
					wb.Back();
					System.err.println("Not able to verify project page");
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println("Project link was not present");
			return true;
		}
		wb.Back();
		return true;
	}

	public boolean VerifyReadMore() throws Exception {
		System.out.println("Inside Read More Link verification");
		if (SwitchWindow(dict.ViewMoreProperty)) {

		} else {
			System.err.println("Some error occurred while opening from read more link");
			return false;
		}
		return true;
	}

	public boolean VerifyPropertyLink() throws Exception {
		System.out.println("Inside Property Link verification");
		if (SwitchWindow(dict.PropertyLink)) {
			System.out.println("Property Link opened");
		} else {
			System.err.println("Some error occurred while opening from property link");
			return false;
		}
		return true;
	}

	public boolean VerifyPropertyCard() throws Exception {
		System.out.println("Inside Property card verification");
		if (wb.IsElementPresent(dict.FirstPropertycard)) {
			Thread.sleep(2000);
			// System.out.println("No of images found " +
			// wb.getText(dict.ImageCount));
			// wb.ClickbyXpath(dict.Nextimage);
			// Thread.sleep(2000);
			System.out.println("Price of propertyis" + (wb.getText(dict.Propertyprice)));

		} else {
			System.err.println("Some error occurred while opening Seller Link");
			return false;
		}
		return true;
	}

	public boolean SwitchWindow(String Path) throws Exception {
		String URL = null;
		driver = wb.getDriver();
		String parentHandle = driver.getWindowHandle();
		driver.findElement(By.xpath(Path)).click();
		Thread.sleep(2000);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		try {
			URL = driver.getCurrentUrl();
			System.out.println("URL of opened window is " + URL);
			if (wb.IsElementPresent(dict.VerifyProperty)) {
				driver.close();
				driver.switchTo().window(parentHandle);
				return true;
			} else
				driver.close();
			driver.switchTo().window(parentHandle);
			System.err.println("Page does not open due to response code");
			return false;
		} catch (Exception e) {
			driver.close();
			driver.switchTo().window(parentHandle);
			e.printStackTrace();
		}
		return false;
	}

	public boolean VerifyShortList() throws Exception {
		System.out.println("Inside validate shortlist property");
		wb.ClickbyXpath(dict.Shortlist);
		if (wb.IsElementPresent(dict.Shortlisted)) {
			System.out.println("property is been shortlisted");
		} else {
			System.err.println("There is problem in shortlisting property");
			return false;
		}
		return true;
	}

	public boolean SetAlert() throws Exception {
		System.out.println("Inside Test Verify Set Alert");
		wb.PageRefresh();
		wb.ClickbyXpath(dict.SetAlert);
		String email = null;
		if (wb.getText(dict.SetAlertBox).contains("stay informed")) {
			wb.enterTextByxpath(dict.SetAlertName, "testSearch");

			email = ReadSheet("Login", "UserName", 4);
			wb.enterTextByxpath(dict.SetAlertEmail, email);

			wb.ClickbyXpath(dict.SetAlertsubmit);

			if (wb.getText(dict.SetAlertMesage).contains("you've already set an alert for this requirement")) {
				System.err.println("already set alert for this try later");
				return false;
			} else if (wb.getText(dict.SetAlertMesage).contains("successfully")) {
				System.out.println("Successfully saved");

				CleanSaveSearch();
			}
		} else {
			System.out.println("Set alert button is not workin, popup does not open");
			return false;
		}
		return true;
	}

	public void CleanSaveSearch() throws Exception {
		wb.GetURL(ReadSheet("Login", "URL", 2));
		login.MakaanLogin();
		wb.WaitUntill(dict.BuyerJourney);

		wb.ClickbyXpath(dict.BuyerJourney);

		try {
			if (wb.getText(dict.SaveSearchText).equalsIgnoreCase("testsearch")) {
				wb.ClickbyXpath(dict.DeleteSaveSearch);

				wb.ClickbyXpath(dict.RemoveButton);

			}
		} catch (NoSuchElementException ne) {
			System.err.println("Not able to delete save search");
		}

	}

	public boolean Pagination() throws Exception {
		System.out.println("Inside test Pagination Verification");
		wb.scrollup(dict.Pagination);
		wb.ClickbyXpath(dict.NextPage);

		String URL = wb.CurrentURL();
		wb.scrollup(dict.Pagination);
		if (URL.contains(wb.getText(dict.PageActive))) {
			System.out.println("Pagination was successful");
		} else {
			System.err.println("pagination do not occur");
			return false;
		}

		return true;
	}

	public boolean SideCards() throws Exception {
		System.out.println("Validating card Track your journey");
		String URL = null;
		wb.PageRefresh();
		closechat();
		if (wb.IsElementPresent(dict.TrackJourney)) {
			wb.WaitUntill(dict.TrackJourney);
			wb.ClickbyXpath(dict.TrackJourney);
			Thread.sleep(1000);
			URL = wb.CurrentURL();
			if (URL.contains("my-journey")) {
				System.out.println("track your journey card was verified");
			} else {
				System.err.println("track your journey card was not verified as URL did not open");
				return false;
			}
			wb.Back();

		}
		wb.scrolldown();
		wb.scrollup(dict.AppCard);
		closechat();
		if (wb.IsElementPresent(dict.AppCard)) {
			driver = wb.getDriver();
			String parentHandle = driver.getWindowHandle();
			driver.findElement(By.xpath(dict.AppCard)).click();
			Thread.sleep(1000);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}
			URL = driver.getCurrentUrl();
			System.out.println("URL of opened window is " + URL);
			driver.close();
			driver.switchTo().window(parentHandle);
			if (URL.contains("apps")) {
				System.out.println("download app card was pesent");
			} else {
				System.err.println("Download app card was not present");
				return false;
			}
		}

		return true;
	}

	public static void CloseAll() {
		db.Close();
		wb.CloseBrowser();
	}

	public static void closechat() throws Exception {
		if (wb.IsElementPresentById("inner-wrapper")) {
			wb.ClickbyXpath(".//textarea[@id='input']");
			wb.ClickbyXpath(".//div[@class='cross']");
			System.out.println("Closed mchat popup");
		} else {
			System.out.println("mchat popup was not present");
		}
	}
	
}