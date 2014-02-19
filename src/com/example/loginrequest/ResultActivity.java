package com.example.loginrequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout);

		Intent intent = getIntent();
		String uname = intent.getStringExtra(MainActivity.UNAME);
		int count = intent.getIntExtra(MainActivity.COUNT, 1);

		// Display
		String message = "Welcome " + uname + "\nYou have logged in " + count + " times.";

		TextView messageView = (TextView) findViewById(R.id.defaultMessage);
		messageView.setText(message);
	}

	public void logoutUser(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
