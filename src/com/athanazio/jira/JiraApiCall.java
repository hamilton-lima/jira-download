package com.athanazio.jira;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import sun.misc.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @see http://developer.zendesk.com/documentation/rest_api/search.html
 * @author hamilton
 * 
 */
public class JiraApiCall {

	private static final String GETALLPROJECTS = "/rest/api/2/project";
	private static final String GETALLISSUES = "/rest/api/2/search";
	private String username;
	private String url;
	private String domain;
	private Object password;
	private CloseableHttpClient httpclient;
	private String auth;

	public String toString() {
		return "JiraApiCall [username=" + username + ", url=" + url + ", domain=" + domain + ", password=" + password
				+ "]";
	}

	public JiraApiCall(String domain, String username, String password) {
		this.domain = domain;
		this.url = "https://" + domain + ".atlassian.net";
		this.username = username;
		this.password = password;

		String temp = username + ":" + password;
		auth = new String(Base64.encodeBase64(temp.getBytes()));

		httpclient = HttpClients.custom().build();
	}

	public void cleanup() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// GET
	public JiraProject[] getAllProjects() {
		String result = get(GETALLPROJECTS);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JiraProject[] projects = gson.fromJson(result, JiraProject[].class);
		return projects;
	}

	public JiraIssue[] getIssues(String key) {
		// %3D --> '='
		String param = "project%3D'" + key + "'";

		String result = get(GETALLISSUES + "?jql=" + param );
		System.out.println("**** ISSUES = " + result);
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		JiraProject[] projects = gson.fromJson(result, JiraProject[].class);

		JiraIssue[] issues = new JiraIssue[0];
		return issues;
	}

	
	@SuppressWarnings("finally")
	private String get(String command) {
		String target = url + command;
		CloseableHttpResponse response = null;

		try {
			HttpGet get = new HttpGet(target);
			get.setHeader("Authorization", "Basic " + auth);
			get.setHeader("Content-Type", "application/json");

			response = httpclient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(target + " : " + statusCode);

			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity);
			System.out.println("content length=" + entity.getContentLength());
			System.out.println("content =" + content);

			return content;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public void saveFile(String target, File targetFile) throws Exception {

		CloseableHttpResponse response = null;

		try {
			HttpGet get = new HttpGet(target);
			get.setHeader("Authorization", "Basic " + auth);
			get.setHeader("Content-Type", "application/json");

			response = httpclient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(target + " : " + statusCode);
			HttpEntity entity = response.getEntity();

			try (FileOutputStream outstream = new FileOutputStream(targetFile)) {
				entity.writeTo(outstream);
				outstream.flush();
				outstream.close();
			}

		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	// public String call(String command, Map parameters) throws IOException {
	//
	// // String encoding == new String(Base64.encodeBase64(auth.getBytes()));
	// String target = url + command;
	//
	// HttpPost post = new HttpPost(target);
	// post.setHeader("Content-Type", "application/json");
	//
	// // List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	// // Iterator iterator = parameters.keySet().iterator();
	// // while (iterator.hasNext()) {
	// // String key = (String) iterator.next();
	// // nvps.add(new BasicNameValuePair(key, (String) parameters.get(key)));
	// // }
	// // post.setEntity(new UrlEncodedFormEntity(nvps));
	//
	// Gson gson = new GsonBuilder().setPrettyPrinting().create();
	// String json = gson.toJson(parameters);
	//
	// System.out.println(json);
	// post.setEntity(new StringEntity(json));
	//
	// CloseableHttpResponse response = httpclient.execute(post);
	// int statusCode = response.getStatusLine().getStatusCode();
	// System.out.println(target + " : " + statusCode);
	//
	// try {
	// HttpEntity entity = response.getEntity();
	// return EntityUtils.toString(entity);
	// } finally {
	// response.close();
	// }
	//
	// }

}
