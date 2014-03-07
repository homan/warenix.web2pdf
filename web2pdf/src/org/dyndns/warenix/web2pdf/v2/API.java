package org.dyndns.warenix.web2pdf.v2;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dyndns.warenix.web2pdf.GsonUtil;

import android.util.Log;

public class API {

	public static final String HOST = "exp-warenix.rhcloud.com";
	public static final String DOMAIN = "pdf";

	/**
	 * e.g. http://exp-warenix.rhcloud.com/pdf/convert
	 */
	public static final String BASE_URL = String.format("http://%s/%s", HOST,
			DOMAIN);

	public static final String SERVICE_CONVERT = "convert";

	public static final String URL_CONVERT = String.format("%s/%s", BASE_URL,
			SERVICE_CONVERT);
	private static final String TAG = "API";

	/**
	 * make call to API
	 * 
	 * @param request
	 * @return
	 */
	public static Object makeCall(BaseService service)
			throws ClientProtocolException, IOException {
		Log.d(TAG, "makeCall()" + "url" + service.path);
		String jsonString = GsonUtil.getGson().toJson(service.payload);
		Log.d(TAG, "makeCall()" + "jsonString:" + jsonString);
		String result = httpPost(service.path, jsonString);
		Log.d(TAG, "makeCall()" + "result:" + result);
		return GsonUtil.getGson().fromJson(result, service.responseClass);
	}

	public static String httpPost(String path, String jsonString)
			throws ClientProtocolException, IOException {
		// instantiates httpclient to make request
		DefaultHttpClient httpclient = new DefaultHttpClient();

		// url with the post data
		HttpPost httpost = new HttpPost(path);

		// passes the results to a string builder/entity
		StringEntity se = new StringEntity(jsonString, "utf-8");

		// sets the post request as the resulting string
		httpost.setEntity(se);
		// sets a request header so the page receviing the request
		// will know what to do with it
		httpost.setHeader("Accept", "application/json");
		httpost.setHeader("Content-type", "application/json");

		// Handles what is returned from the page
		HttpResponse httpResponse = httpclient.execute(httpost);
		String retSrc = EntityUtils.toString(httpResponse.getEntity());
		return retSrc;
	}

}
