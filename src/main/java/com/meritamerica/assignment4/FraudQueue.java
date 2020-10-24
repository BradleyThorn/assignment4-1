package com.meritamerica.assignment4;

import java.util.*;

public class FraudQueue {
	
	FraudQueue(){}
	
	
	ArrayList<Transaction> fraud = new ArrayList<Transaction>();
	
	
	public void addTransaction(Transaction transaction)
	{
		fraud.add(transaction);
	}
	
	public Transaction getTransaction()
	{
		Transaction t = fraud.get(0);
		fraud.remove(0);
		return t;
	}

}
