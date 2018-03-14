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
public class ChainServiceTest {
	@Autowired
	private ChainService service;
	@Test
	public void test1() {
		try {
			System.out.println(service.getInfo());
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

}
