package mk.ukim.finki.jmm.todolist.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Downloader {

	// constructor
	public Downloader() {

	}

	/**
	 * 
	 * @param url
	 * @param handler
	 * @throws Exception
	 */
	public static void getFromUrl(String url, OnContentDownloaded<?> handler)
			throws Exception {
		HttpResponse httpResponse = null;
		InputStream is = null;
		String content = "";
		// Making HTTP request
		// defaultHttpClient
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(url);

		httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		is = httpEntity.getContent();

		if (httpResponse != null
				&& httpResponse.getStatusLine().getStatusCode() == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			content = sb.toString();
			handler.onContentDownloaded(content, httpResponse.getStatusLine()
					.getStatusCode());
		}

	}
}
