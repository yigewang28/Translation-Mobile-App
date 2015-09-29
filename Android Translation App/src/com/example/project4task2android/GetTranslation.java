package com.example.project4task2android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import android.os.AsyncTask;


/**
 * This class communicate with the web service using HTTP request/response.
 * First, it send the httprequest to the web service, 
 * then web service will deal with the translation part using a translation api(youdao),
 * and finally sends back the translation in a xml format.
 */
public class GetTranslation {
	MainActivity trans = null;

	/**
	 * search is the public GetTranslation method. 
	 * Its arguments are the search term, and the MainActivity object that called it.  
	 * This provides a callback path such that the translationReady method in that object is called
	 * when the translation is available from the search.
	 * 
	 * @param searchTerm the word or sentence that waits to be translated
	 * @param trans instance of MainActivity
	 */
	public void search(String searchTerm, MainActivity trans) {
		this.trans = trans;
		new AsyncFlickrSearch().execute(searchTerm);
	}

	/**
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 * 
	 * @author Yige
	 *
	 */
	private class AsyncFlickrSearch extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			try {
				return doPut(params[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String word) {
			trans.translationReady(word);
		}


		/**
		 * Get the information from server. (Not used in this application)
		 * 
		 * @return String from server
		 * @throws MalformedURLException
		 * @throws IOException
		 */
		@SuppressWarnings("unused")
		public String doGet() throws MalformedURLException, IOException {
			String address = "http://1-dot-yigewangproject4.appspot.com/project4task2server";
			URL url = new URL(address);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "text/xml");
			InputStream response = con.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(response));

			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();
			return inputLine;
		}

		/**
		 * Send Http request with searchTerm to server and get back the translation result.
		 * 
		 * @param content word or sentence the user typed in
		 * @return translation from the server
		 * @throws MalformedURLException
		 * @throws ProtocolException
		 * @throws IOException
		 */
		private String doPut(String content) throws MalformedURLException, ProtocolException, IOException {
			String address = "http://1-dot-yigewangproject4.appspot.com/project4task2server";

			URL url = new URL(address);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true); 
			con.setRequestMethod("PUT");
			con.setRequestProperty("Accept-Charset", "UTF-8");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(content);
			out.close();

			InputStream response = con.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(response, "UTF-8"));

			String inputLine;
			inputLine = in.readLine();
			System.out.println(inputLine);
			in.close();

			return inputLine;

		}
	}
}
