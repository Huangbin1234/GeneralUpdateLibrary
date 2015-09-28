package com.way.weather.update;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 更新Request
 * 
 * @author TXG
 * 
 */
public class UpdateRequest {

	public static String sendPost(List<NameValuePair> ps, String strUrl) {

		HttpParams params = null;
		HttpResponse httpResponse = null;
		String result = null;
		try {
			HttpClient client = new DefaultHttpClient();
			params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 8000);// 连接时间
			HttpConnectionParams.setSoTimeout(params, 8000);// 套接字超时时间
			HttpPost httpPost = new HttpPost(strUrl);
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			if (ps != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(ps, HTTP.UTF_8));
			}
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
