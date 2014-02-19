package com.example.loginrequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String UNAME = "com.example.loginrequest.USERNAME";
	public final static String COUNT = "com.example.loginrequest.COUNT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EditText uname = (EditText) findViewById(R.id.txtUname);
		uname.requestFocus();

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	public void login(View view) {
		String uName = ((EditText) findViewById(R.id.txtUname)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.txtPwd)).getText()
				.toString();
		LoginTask loginTask = new LoginTask();
		String[] args = { uName, password, "login" };
		loginTask.execute(args);
	}

	public void addUser(View view) {
		String uName = ((EditText) findViewById(R.id.txtUname)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.txtPwd)).getText()
				.toString();
		LoginTask loginTask = new LoginTask();
		String[] args = { uName, password, "add" };
		loginTask.execute(args);
	}

	public void handleResult(String[] result) {
		TextView message = (TextView) findViewById(R.id.message);
		try {
			JSONObject jsonObj = new JSONObject(result[1]);
			int count = jsonObj.getInt("count");
			int errCode = jsonObj.getInt("errCode");
			if (errCode == 1) {
				Intent intent = new Intent(this, ResultActivity.class);
				intent.putExtra(UNAME, result[0]);
				intent.putExtra(COUNT, count);
				startActivity(intent);
			} else {
				/*
				 * probably should have a default but not sure what it would be
				 * *
				 */
				switch (errCode) {
				case -1:
					message.setText("Invalid Username and Password.");
					break;
				case -2:
					message.setText("User already exists");
					break;
				case -3:
					message.setText("User cannot be empty");
					break;
				case -4:
					message.setText("Password cannot be longer than 128 characters");
					break;
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class LoginTask extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... args) {
			String uname = args[0];
			String pwd = args[1];

			DefaultHttpClient httpClient = null;
			HttpPost httpPost = null;
			HttpResponse response = null;

			try {
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(
						"http://peaceful-anchorage-1078.herokuapp.com/users/"
								+ args[2]);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("content-type",
						"application/json"));

				StringEntity input = new StringEntity("{\"user\": \"" + uname
						+ "\",\"password\": \"" + pwd + "\"}");
				input.setContentType("application/json");
				httpPost.setEntity(input);

				for (NameValuePair h : nvps) {
					httpPost.addHeader(h.getName(), h.getValue());
				}

				response = httpClient.execute(httpPost);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));

				String output;
				String message = "";
				while ((output = br.readLine()) != null) {
					message += output;
				}

				String[] result = new String[2];
				result[0] = uname;
				result[1] = message;
				return result;

			} catch (MalformedURLException e) {
				String[] arr = new String[0];
				return arr;

			} catch (IOException e) {
				String[] arr = new String[0];
				return arr;
			}
		}

		@Override
		protected void onPostExecute(String[] result) {
			handleResult(result);
		}
	}

}
