package com.makaan.Test;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.makaan.Middleware.HomeMiddleware;
import com.makaan.Middleware.SearchMiddleware;

public class HomeTest {

	HomeMiddleware hm = new HomeMiddleware();

	@BeforeClass
	public void InitiateDriver() throws NoSuchElementException {
		try {
			System.out.println("Inside Test Initiate Driver");

			hm.OpenURL();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "not able to Initiate Driver due to exception");
		}

	}

	@Test(priority =0)
	public void closechatpopup() throws Exception {
		System.out.println("Inside Test Validate Download app button");
			hm.closechat(); 
			
	}
	
	@Test(priority =1)
	//@Test(enabled = false)
	public void ValidateDownloadAppButton() throws Exception {
		System.out.println("Inside Test Validate Download app button");
		if (hm.VerifyAppButton()) {
			System.out.println("Download App button is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Download App button due to exception");
		}
	}

	 @Test (priority =2)
	// @Test(enabled = false)
	public void ValidateSellerLink() throws Exception {
		System.out.println("Inside Test Validate Seller Link");
		if (hm.VerifySeller()) {
			System.out.println("Seller Link is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Seller Linkn due to exception");
		}
	}

	@Test(priority = 3)
	//@Test (enabled = false)
	public void ValidateBuyerJourney() throws Exception {
		System.out.println("Inside Test Validate Buyer Journey");
		if (hm.VerifyBuyerJourney()) {
			System.out.println("Buyer Journey Link is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Buyer Journey due to exception");
		}
	}
	
	@Test(priority = 4)
	//@Test (enabled = false)
	public void BuyerJourneyDrawer() throws Exception {
		System.out.println("Inside Test Validate Journey in Drawer");
		hm.closechat(); 
		if (hm.VerifyMenuDrawerBuyerJourney()) {
			System.out.println("Buyer Journey in  Drawer is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Journey in  Menu Drawer due to exception");
		}
	}
		
	@Test(priority = 5)
	// @Test (enabled = false)
	public void CityDrawer() throws Exception {
		System.out.println("Inside Test Validate Top City in Drawer");
		if (hm.VerifyMenuDrawerCities()) {
			System.out.println("Top City in  Drawer is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Top City in  Menu Drawer due to exception");
		}
	}
	
	@Test(priority = 6)
	 //@Test (enabled = false)
	public void BuilderDrawer() throws Exception {
		System.out.println("Inside Test Validate Top Builder in Drawer");
		if (hm.VerifyMenuDrawerBuilder()) {
			
			System.out.println("Top builders in  Drawer is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Top Builders in  Menu Drawer due to exception");
		}
	}
	
	@Test(priority = 7)
	 //@Test (enabled = false)
	public void BrokerDrawer() throws Exception {
		System.out.println("Inside Test Validate Top Brokers in Drawer");
		if (hm.VerifyMenuDrawerBrokers()) {
			System.out.println("Top Brokers in  Drawer is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Top Brokers in  Menu Drawer due to exception");
		}
	}
		
	@Test(priority = 8)
	 //@Test (enabled = false)
	public void MakaanIQDrawer() throws Exception {
		System.out.println("Inside Test Validate MAkaan IQ in Drawer");
		if (hm.VerifyMenuDrawerMakaanIQ()) {
			System.out.println("Makaan IQ in  Drawer is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Makaan IQ in  Menu Drawer due to exception");
		}
	}
	@Test(priority = 9)
	 //@Test (enabled = false)
	public void AppDrawer() throws Exception {
		System.out.println("Inside Test Validate Download App in Drawer");
		if (hm.VerifyMenuDrawerApp()) {
			System.out.println("Download App in  Drawer is validated");
		} else {
			Assert.assertTrue(false, "not able to Validate Download APP in  Menu Drawer due to exception");
		}
	}
	
	
	
	@Test(priority = 10)
		 //@Test (enabled = false)
		public void VerifyAppSection() throws Exception {
			System.out.println("Inside Test Verify App Section");
			if (hm.DownloadAppSection()) {
				System.out.println("App Section is validated");
			} else {
				Assert.assertTrue(false, "not able to Validate App Section due to exception");
			}
		}

	@AfterClass
	public void Close() {
		try {
			hm.CloseAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
