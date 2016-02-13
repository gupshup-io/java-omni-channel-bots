package com.ocbot.lookup.sales;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Servlet implementation class SalesBotHandler
 */
@WebServlet("/sales1")
public class SalesBotHandler extends HttpServlet
{
	private HashMap<String, Sales> salesMap = new HashMap<String, Sales>();
	private String chartUrl = "http://chart.apis.google.com/chart?cht=bvg&chs=350x300&chd=t:%s&chxr=1,0,10000&chds=0,10000&chco=159ceb|ffa000&chbh=65,0,35&chxt=x,y,x&chxl=0:|Total+Plan|Total+Actual&chxs=2,000000,12&chtt=Total+Sales+Report&chts=000000,20&chg=0,25,5,5";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		handle(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		handle(request, response);

	}

	private void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter writer = response.getWriter();
		String sender = request.getParameter("sender").trim();
		String channel = request.getParameter("channel");
		String message = request.getParameter("message");
		Enumeration en = request.getParameterNames();

		while (en.hasMoreElements())
		{

			String paramName = (String) en.nextElement();
			System.out.println(paramName + " = " + request.getParameter(paramName) + "\n");

		}

		try
		{
			String data = route(message, sender);
			writer.println(data);
		} catch (Exception ex)
		{

			System.out.println("There is some error " + ex.getMessage());
			writer.println("There is some error" + ex.getMessage());
		}

		writer.close();

	}

	private String route(String message, String sender) throws UnirestException, JSONException
	{
		String lMessage = message.toLowerCase();

		if (lMessage.equals("hi") || lMessage.equals("hello") || lMessage.equals("help"))
		{
			//return "Hi, I'm the SalesBot. My job is to collect Plan and Actual sales from you and share reports and charts back with you. To submit your plan sales amount, say: plan <amount> (for example: plan 2500)";
			return "Hi, I'm the SalesBot. I collect Plan & Actual sales data, then create reports and charts. To submit plan sales amount, say: plan <amt> (for eg: plan 2500)";
		}

		if (lMessage.startsWith("plan "))
		{
			System.out.println("Inside plan");
			String uMessage = message.substring(5).trim();
			long plan = Long.parseLong(uMessage);

			Sales s = salesMap.get(sender);
			if (s == null)
			{
				s = new Sales();
			}
			s.setPlan(plan);
			s.setUsername(sender);
			salesMap.put(sender, s);
			return "Thank you for submitting the plan amount. To submit the actual sales amount, say: actual <amount>  (for example: actual 3000)";

		}

		else if (lMessage.startsWith("actual "))
		{
			System.out.println("Inside actuals");
			String uMessage = message.substring(7).trim();
			long actual = Long.parseLong(uMessage);
			Sales s = salesMap.get(sender);
			if (s == null)
			{
				return "you need to first fill plan data before submitting Actuals";
			} else
			{
				s.setActual(actual);
				return "Thank you for submitting the actual amount. To generate report, say: report. To generate chart, say: chart.";

			}

		} else if (lMessage.equals("chart"))
		{

			return computeSales(lMessage);
		}
		else if (lMessage.equals("report"))
		{

			return computeSales(lMessage);
		}

		else
		{
			return "Oops Sorry !!! Iam not able to parse your input , just type help ";
		}

	}

	private String computeSales(String mode)
	{

		long totalPlan = 0, totalActual = 0;
		int counter = 0;
		if (salesMap.size() == 0)
		{
			return "There is no sales data to compute reports";
		} else
		{
			for (Map.Entry<String, Sales> entry : salesMap.entrySet())
			{
				String name = entry.getKey();
				long plan = entry.getValue().getPlan();
				long actual = entry.getValue().getActual();
				totalPlan += plan;
				totalActual += actual;
				counter = salesMap.size();
				
				
				System.out.println("**********************************");
				System.out.println("Tplan :"+totalPlan);
				System.out.println("TActual :"+totalActual);
				System.out.println(chartUrl);
				System.out.println("**********************************");
			

			}
			
			if(mode.equals("chart"))
			{
				chartUrl = String.format(chartUrl, totalPlan + "," + totalActual);
				return "Total Sales :" + "\n" + chartUrl;
			}
			else
			{
				long pSales = ((totalActual*100)/totalPlan);
				return "Sales : "+pSales+"% | Total Plan Amount : "+totalPlan +"$ | Total Actual Amount : "+totalActual +"$ | "+"Count : "+counter;
			}

			

		}

	}

}
