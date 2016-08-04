package com.makaan.Test;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.makaan.Middleware.ConnectNowMiddleware;;

public class ConnectNowTest {

	ConnectNowMiddleware in = new ConnectNowMiddleware();
	@BeforeClass
	public void InitiateDriver() throws NoSuchElementException {
		try {
			System.out.println("Inside Test Initiate Driver");
			
			in.OpenURL();

		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "not able to Initiate Driver due to exception");
		}

	}

	
	
	@Test(priority =1)
	//@Test(enabled = false)
	public void ConnectNow() throws Exception {
		System.out.println("Inside Test Connect Now");
			if(in.ConnectNow()){
				System.out.println("Successfully completed Connect Now Functionality");
			}
			else{
				Assert.assertTrue(false, "not able to Validate Connect Now due to exception");
		}
	}
	
	@Test(priority =2)
	//@Test (enabled = false)
	public void ValidateDB() throws Exception {
		System.out.println("Inside Test VAlidateDB");
			if(in.VerifyEnquiry()){
				System.out.println("Successfully completed Validate DB Test");
			}
			else{
				Assert.assertTrue(false, "not able to Validate DB due to exception");
		}
	}
	
	//@Test(enabled = false)
	@Test (priority = 3)
	public void ViewNumberInLead() throws Exception {
		System.out.println("Inside Test View Phone Number");
			if(in.ViewPhoneNumber("Inner Lead")){
				System.out.println("Successfully completed view phone number Functionality inside lead form");
			}
			else{
				Assert.assertTrue(false, "not able to Validate View phone  inside lead form number due to exception");
		}
	}
	
	//@Test(enabled = false)
	@Test (priority = 4)
	public void ViewNumberonSERP() throws Exception {
		System.out.println("Inside Test View Phone Number");
			if(in.ViewPhoneNumber("SERP")){
				System.out.println("Successfully completed view phone number Functionality on SERP form");
			}
			else{
				Assert.assertTrue(false, "not able to Validate View phone on SERP form number due to exception");
		}
	}
	
	//@Test(enabled = false)
	@Test (priority = 5)
	public void ContactSeller() throws Exception {
		System.out.println("Inside Test Contact Seller");
			if(in.ContactSellers()){
				System.out.println("Successfully completed Contact Seller");
			}
			else{
				Assert.assertTrue(false, "not able to Validate Contact sellers due to exception");
		}
	}
	
	
	
	@AfterClass
	public void Close() {
		try {
			in.CloseBrowser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
