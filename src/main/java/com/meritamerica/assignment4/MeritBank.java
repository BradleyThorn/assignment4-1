package com.meritamerica.assignment4;

import java.io.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

public class MeritBank {
	
	static FraudQueue fraud;
	static String[] accountData = new String[24];
	static long accountNumber;
	static AccountHolder[] accountHolders;
	static CDOffering[] CDOfferingsArray = new CDOffering[0];

	
	
	static void addAccountHolder(AccountHolder accountHolder) {
		AccountHolder[] temp = Arrays.copyOf(accountHolders, accountHolders.length + 1);
		accountHolders = temp;
		accountHolders[accountHolders.length-1] = accountHolder;
	}
	
	static AccountHolder[] getAccountHolders(){
		return accountHolders;
	}
	
	static CDOffering[] getCDOfferings() {
		return CDOfferingsArray;
	}
	
	static CDOffering getBestCDOffering(double depositAmount){
		double bestValue = 0.0;
		CDOffering bestOfferingAvailable = null;
		if(CDOfferingsArray == null) {
			return null;
		} else {
			for(CDOffering offering: CDOfferingsArray) {
				if(futureValue(depositAmount, offering.getInterestRate(), offering.getTerm()) > bestValue) {
					bestOfferingAvailable = offering;
					bestValue = futureValue(depositAmount, offering.getInterestRate(), offering.getTerm());
				}
			}
		}
		return bestOfferingAvailable;
		
	}
	
	static CDOffering getSecondBestCDOffering(double depositAmount){
		CDOffering secondBest = null;
		double bestValue = 0.0;
		CDOffering bestOfferingAvailable = null;
		if(CDOfferingsArray == null) {
			return null;
		} else {
			for(CDOffering offering: CDOfferingsArray) {
				if(futureValue(depositAmount, offering.getInterestRate(), offering.getTerm()) > bestValue) {
					secondBest = bestOfferingAvailable;
					bestOfferingAvailable = offering;
					bestValue = futureValue(depositAmount, offering.getInterestRate(), offering.getTerm());
				}
			}
		}
		return secondBest;
	}
	
	static void clearCDOfferings(){
		CDOfferingsArray = null;
	}
	static void clearAccountHolders()
	{
		accountHolders = null;
	}
	
	static void setCDOfferings(CDOffering[] offerings){
		CDOfferingsArray = offerings;
	}
	
	static long getNextAccountNumber() {
		return accountNumber;
	}
	
	static void setNextAccountNumber(long nextAccountNumber)
	{

		accountNumber = nextAccountNumber;
	}
	
	static double totalBalances() {
		double totalBalance = 0.0;
		for(AccountHolder accountHolder: accountHolders) {
			totalBalance += accountHolder.getCombinedBalance();
		}
		return totalBalance;
	}
	
	static double futureValue(double presentValue, double interestRate, int term) {
		double futureValue = presentValue* Math.pow((1+ interestRate),term);
		return futureValue;
	}
	
	static boolean readFromFile(String fileName)
	{
		clearCDOfferings();
		accountHolders = new AccountHolder[0];
		
		try 
		{
			BufferedReader rd = new BufferedReader(new FileReader(fileName));
			//read account number
			long num = Long.parseLong(rd.readLine().trim());
			setNextAccountNumber(num);
			//read number of CD Offerings
			int cdo = Integer.parseInt(rd.readLine().trim());
			CDOffering[] cdoArr = new CDOffering[cdo];
			for (int x = 0; x < cdoArr.length; x++)
			{
				//reads CD Offerings for number of CD Offerings
				cdoArr[x] = CDOffering.readFromString(rd.readLine().trim());
			}
			setCDOfferings(cdoArr);
			//reads number of account holders
			int ac = Integer.parseInt(rd.readLine().trim());
			AccountHolder[] acArr = new AccountHolder[ac];
			for (int x = 0; x < acArr.length; x++)
			{
				//reads account holder info
				acArr[x] = AccountHolder.readFromString(rd.readLine().trim());
				//reads number of checking accounts
				int numChk = Integer.parseInt(rd.readLine().trim());
				for (int c = 0; c < numChk; c++)
				{
					//making our checking accounts
					CheckingAccount chk = CheckingAccount.readFromString(rd.readLine().trim());
					acArr[x].addCheckingAccount(chk);
					//reads checking account transactions
					int numTran = Integer.parseInt(rd.readLine().trim());
					for (int t = 0; t < numTran; t++)
					{
						//making our checking account transactions
						Transaction tran = Transaction.readFromString(rd.readLine().trim());
						acArr[x].checkingArray.get(c).addTransaction(tran);
					}
				}
				int numSav = Integer.parseInt(rd.readLine().trim());
				for (int s = 0; s < numSav; s++) 
				{
					SavingsAccount sav = SavingsAccount.readFromString(rd.readLine().trim());
					acArr[x].addSavingsAccount(sav);
					int numTran = Integer.parseInt(rd.readLine().trim());
					for (int t = 0; t < numTran; t++)
					{
						Transaction tran = Transaction.readFromString(rd.readLine().trim());
						acArr[x].savingsArray.get(s).addTransaction(tran);
					}
				}
				int numCD = Integer.parseInt(rd.readLine().trim());
				for (int d = 0; d < numCD; d++)
				{
					CDAccount cdA = CDAccount.readFromString(rd.readLine().trim());
					acArr[x].addCDAccount(cdA);
					int numTran = Integer.parseInt(rd.readLine().trim());
					for (int t = 0; t < numTran; t++)
					{
						Transaction tran = Transaction.readFromString(rd.readLine().trim());
						acArr[x].cdAccountArray.get(d).addTransaction(tran);
					}
				}
				
				
				
				addAccountHolder(acArr[x]);
				
			}
			rd.close();
			
			
			
		}
		catch(IOException ex)
		{			
			return false;
		} 
		catch (ParseException ex) {
			
			return false;
		} 
		catch (java.lang.NumberFormatException ex)
		{
			return false;
		}
		catch (java.lang.Exception ex)
		{
			return false;
		}
		
		return true;
	}
	
	static AccountHolder[] sortAccountHolders()
	{
		Arrays.sort(accountHolders);
		
		return accountHolders;
	}
	
	public static FraudQueue getFraudQueue()
	{
		return fraud;
	}
	
	public static BankAccount getBankAccount(long accountId)
	{
		for (AccountHolder a:MeritBank.getAccountHolders())
		{
			for (BankAccount b:a.getCheckingAccounts())
			{
				if (b.getAccountNumber() == accountId)
					return b;
			}
			for (BankAccount b:a.getSavingsAccounts())
			{
				if (b.getAccountNumber() == accountId)
					return b;
			}
			for (BankAccount b:a.getCDAccounts())
			{
				if (b.getAccountNumber() == accountId)
					return b;
			}
		}
		return null;
	}
	/*
	 * The math here is beyond me
	public static double recursiveFutureValue(double amount, int years, double interestRate)
	{
		balance * Math.pow(1+ offerings.getInterestRate(), offerings.getTerm());
		
		if (years)
	}
	*/

}

