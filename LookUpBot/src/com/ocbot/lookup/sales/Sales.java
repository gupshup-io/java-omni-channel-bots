package com.ocbot.lookup.sales;

public class Sales
{

	private String username;
	private long plan;
	private long actual;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public long getPlan()
	{
		return plan;
	}

	public void setPlan(long plan)
	{
		this.plan = plan;
	}

	@Override
	public String toString()
	{
		return "Sales [username=" + username + ", plan=" + plan + ", actual=" + actual + "]";
	}

	public long getActual()
	{
		return actual;
	}

	public void setActual(long actual)
	{
		this.actual = actual;
	}

}
