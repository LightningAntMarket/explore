package com.lightningant.explorer.service;

import com.lightningant.explorer.StartExplorer;
import com.lightningant.explorer.exception.BeidouchainException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=StartExplorer.class)
//@WebAppConfiguration
public class TransactionServiceTest {
	@Autowired
	private TransactionService transService;
	@Test
	public void test1() {
		try {
			System.out.println(transService.getrawtransaction("0026528a670563243639400f2f265c25fa0aa54f66d107eedd980e4be3a8f9b9"));
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

}
