package com.ocbot.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

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
 * Servlet implementation class WikipediaHandler
 */
@WebServlet("/face")
public class FaceDetectionHandler extends HttpServlet
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
		Enumeration en = request.getParameterNames();

		while (en.hasMoreElements())
		{

			String paramName = (String) en.nextElement();
			System.out.println(paramName + " = " + request.getParameter(paramName) + "<br/>");

		}

		String sender = request.getParameter("sender");
		String channel = request.getParameter("channel");
		String message = request.getParameter("message");

		try
		{
			String data = route(message, sender);
			writer.println(data);
		} catch (Exception ex)
		{

			System.out.println("There is some error in the Image URL " + ex.getMessage());
			writer.println("There is some error in the Image URL " + ex.getMessage());
		}

		writer.close();

	}

	private String route(String message, String sender) throws UnirestException, JSONException
	{
		String lMessage = message.toLowerCase();

		switch (lMessage)
		{
		case "hi":
			return "Hello Iam FaceBot,give me a face image url in the format face http://some-image-url , let me tell you intresting facts.";
		default:
			return getFaceData(message);

		}

	}

	private String getFaceData(String message) throws UnirestException, JSONException
	{

		if (message.toLowerCase().contains("analyze") || message.toLowerCase().contains("face"))
		{
			String url = message.replace("analyze", "").replace("Analyze", "").replace("face", "").replace("Face", "").trim();
			url = url.replace("<", "").replace(">", "");
			System.out.println("Url :" + url);
			HttpResponse<JsonNode> response = Unirest.get("https://faceplusplus-faceplusplus.p.mashape.com/detection/detect?attribute=glass%2Cpose%2Cgender%2Cage%2Crace%2Csmiling&url=" + url).header("X-Mashape-Key", Konstants.MASHAPE_API_KEY).header("Accept", "application/json").asJson();
			JSONObject data = response.getBody().getObject();
			JSONObject face = data.getJSONArray("face").getJSONObject(0);
			JSONObject attribute = face.getJSONObject("attribute");
			String race = attribute.getJSONObject("race").getString("value");
			String gender = attribute.getJSONObject("gender").getString("value");
			int smiling = attribute.getJSONObject("smiling").getInt("value");
			int age = attribute.getJSONObject("age").getInt("value");
			String glass = attribute.getJSONObject("glass").getString("value");
			StringBuilder sBuiler = new StringBuilder();
			sBuiler.append("Gender :" + gender).append(" | Age :" + age).append(" | Smiling :" + smiling).append(" | Race :" + race).append(" | Glass :" + glass);
			return sBuiler.toString();
		} 
		else
		{
			return "Oops Sorry !!! Iam not able to parse this ";
		}

	}

}
