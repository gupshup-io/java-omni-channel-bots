package com.ocbot.lookup;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@WebServlet("/quote")
public class QuoteHandler extends HttpServlet
{

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
		String sender = request.getParameter("sender");
		String channel = request.getParameter("channel");
		String message = request.getParameter("message");
		
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
		
		if(lMessage.equals("quote"))
		{
			HttpResponse<JsonNode> response = Unirest.post("https://andruxnet-random-famous-quotes.p.mashape.com/?cat=famous")
					.header("X-Mashape-Key", Konstants.MASHAPE_API_KEY)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Accept", "application/json")
					.asJson();
			
			JSONObject data = response.getBody().getObject();
			System.out.println(data);
			return data.getString("quote") + " - "+ data.getString("author");
			
		}
		if(lMessage.equals("hi") || lMessage.equals("hello") || lMessage.equals("help"))
		{
			return "Hi, I'm the QuoteBot. I have a large collection of quotable quotes to share with you. Whenever you want one, just say: quote";
		}
		else
		{
			return "Sorry not able to parse your input , try \'Help\'";
		}
		

	}

}
