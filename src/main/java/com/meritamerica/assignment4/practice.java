package com.meritamerica.assignment4;

public class practice {
	
	
	public static int factorial(int m)
	{
		if (m==1 || m == 0)
		{
			return 1;
		}
		else
		{
			return m*factorial(m-1);
		}
	}
	
	public static void main (String[] args)
	{
		System.out.println(factorial(5));
	}

}
