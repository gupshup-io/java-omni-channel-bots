package com.ocbot.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

/**
 * Servlet implementation class RoboImageHandler
 */
@WebServlet("/qrcode")
public class QRCodeHandler extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private String url;
	private String logoUrl;

	public QRCodeHandler()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		handle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		handle(request, response);
	}

	private void handle(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			String sender = request.getParameter("sender");
			String channel = request.getParameter("channel");
			String message = request.getParameter("message");
			String data = route(message, sender);
			writer.print(data);

		} catch (Exception ex)
		{
			writer.println("There is some error in the Image URL " + ex.getMessage());
		}

		if (writer != null)
		{
			writer.close();
		}

	}

	private String route(String message, String sender) throws UnirestException, JSONException, UnsupportedEncodingException
	{

		String lMessage = message.toLowerCase();

		if (lMessage.equals("hi") || lMessage.equals("hello") || lMessage.equals("help"))
		{
			
			this.url =null;
			this.logoUrl=null;

			return "Hello There , Iam QRCodeBot and i will help you to create QR Codes for a url and place a logo on it . Please type in the url to be embedded into your Qrcode in following format : url http://someurl  ";
		}
		if (lMessage.contains("url "))
		{
			message  = message.replace("<", "").replace(">", "");
			String uMessage = message.substring(4).trim();
			this.url = uMessage;
			return "Thanks ,QR code url is now set , next give me your logo url in this format : logo http://someurl";
		}

		if (lMessage.contains("logo "))
		{
			
			message  = message.replace("<", "").replace(">", "");
			String uMessage = message.substring(5).trim();
			
			logoUrl = uMessage;
			String qrUrl =  getQRUrl();
			System.out.println(qrUrl);
			HttpResponse<JsonNode> response = Unirest.get(qrUrl).header("X-Mashape-Key", Konstants.MASHAPE_API_KEY).header("Accept", "application/json").asJson();
			System.out.println(response);
			JSONObject res = response.getBody().getObject();
			System.out.println(res.toString());
			return res.getString("url");
		} else
		{
			return "Sorry unable to parse your input";
		}

	}
	
	
	private String getQRUrl() throws UnsupportedEncodingException
	{
		
		String baseUrl ="https://ajith-qr-code-generator-with-logo.p.mashape.com/getQrcode?logo=" + this.logoUrl + "&type=url&value=" + this.url;
		return baseUrl;
	}

}
