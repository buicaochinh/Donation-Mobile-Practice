package ie.app.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {

	private static HttpURLConnection 	httpCon = null;
	private static URL 					url;

	private static final String hostURL = "http://donationweb-4-0.herokuapp.com";
	private static final String LocalhostURL = "http://192.168.0.13:3000";
	

	public static void setup(String request) {
		try {
			url = new URL(hostURL + request);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setUseCaches(false);
            httpCon.setReadTimeout(15 * 1000); // 15 seconds to timeout
			httpCon.setRequestProperty( "Content-Type", "application/json" );
			httpCon.setRequestProperty("Accept", "application/json");
		}
		catch (Exception e)
		{
			Log.v("donate","REST SETUP ERROR" + e.getMessage());
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public static String get(String url) {

        BufferedReader reader = null;
        StringBuilder stringBuilder = null;

		try {
			setup(url);
            httpCon.setRequestMethod("GET");
            httpCon.setDoInput(true);
			httpCon.connect();

            Log.v("donate", "GET REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);

            reader.close();
            Log.v("donate", "JSON GET REQUEST : " + stringBuilder.toString());
		}

        catch (Exception e) {
			Log.v("donate","GET REQUEST ERROR" + e.getMessage());
		}

        return stringBuilder.toString();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static String delete(String url) {

		String response = null;

		try {
			setup(url);
			httpCon.setRequestMethod("DELETE");
			httpCon.connect();

			Log.v("donate", "DELETE REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

			response = httpCon.getResponseMessage();
			Log.v("donate", "JSON DELETE RESPONSE : " + response);
		}

		catch (Exception e) {
			Log.v("donate","DELETE REQUEST ERROR" + e.getMessage());
		}

		return response;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static String put(String url, String json) {
		String result = "";
		/*try {
			String strRequest = getBase() + url;
			HttpPut putRequest = new HttpPut(strRequest);
			putRequest.setHeader("Content-type", "application/json");
			putRequest.setHeader("accept", "application/json");
			//putRequest.setHeader("accept","text/plain");
			StringEntity s = new StringEntity(json);
			//s.setContentEncoding("UTF-8");
			s.setContentType("application/json");

			putRequest.setEntity(s);

			HttpResponse response = httpClient.execute(putRequest);
			result = getResult(response).toString();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}*/
		return result;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static String post(String url, String json) {

        OutputStreamWriter writer = null;
        StringBuilder stringBuilder = null;

        try {
            setup(url);
            httpCon.setRequestMethod("POST");
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.connect();

            Log.v("donate", "POST REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

            // read the output from the server
            writer = new OutputStreamWriter(httpCon.getOutputStream());
            writer.write(json);
            writer.close();

            stringBuilder = new StringBuilder();
            int HttpResult = httpCon.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                    stringBuilder.append(line + "\n");

                Log.v("donate", "JSON POST RESPONSE : " + stringBuilder.toString());
            }
        }

        catch (Exception e) {
            Log.v("donate","POST REQUEST ERROR" + e.getMessage());
        }

        return stringBuilder.toString();
	}
}