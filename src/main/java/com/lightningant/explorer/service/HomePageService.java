package com.lightningant.explorer.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.lightningant.explorer.entity.HomePage;
import com.lightningant.explorer.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public class HomePageService{
	
	private HomePage homePage = new HomePage();
	
	private Queue<Transaction> queue  = new ConcurrentLinkedQueue<Transaction>();
	
	
	public void AddTransaction(Transaction trans){
		//保证queue 十个以下
		while(queue.size() >=10){
			queue.poll();
		}		
		queue.add(trans);
	}
	
	
	public Object[] getTransactionArray() {
		return queue.toArray();
	}
	
	public int getTransactionSize(){
		return queue.size();
	}
	
	public HomePage getHomePage() {
		return homePage;
	}

	public void setHomePage(HomePage homePage) {
		this.homePage = homePage;
	}
	
}
