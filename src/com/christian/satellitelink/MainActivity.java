package com.christian.satellitelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends Activity {
	static TextView logText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadSavedPreferences();
		
		TelnetConnection telnet = new TelnetConnection(MainActivity.this);
		logText = (TextView) findViewById(R.id.LogText2);
		
		Button connectButton= (Button) findViewById(R.id.ConnectButton);
		Button disconnectButton= (Button) findViewById(R.id.DisconnectButton);
		connectButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	EditText editIP = (EditText) findViewById(R.id.ServerIP);
		    	EditText editPort = (EditText) findViewById(R.id.ServerPort);
		    	SendCommandThread task = new SendCommandThread(MainActivity.this);
	        	task.execute(editIP.getText().toString(),editPort.getText().toString());
		    	savePreferences("Server_IP", editIP.getText().toString());
		    	savePreferences("Server_Port", editPort.getText().toString());
		    }
		});
		disconnectButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	EditText editIP = (EditText) findViewById(R.id.ServerIP);
		    	EditText editPort = (EditText) findViewById(R.id.ServerPort);
		    	SendDisconnectThread task = new SendDisconnectThread(MainActivity.this);
	        	task.execute(editIP.getText().toString(),editPort.getText().toString());
		    	savePreferences("Server_IP", editIP.getText().toString());
		    	savePreferences("Server_Port", editPort.getText().toString());
		    }
		});
//		final Switch connSwitch = (Switch) findViewById(R.id.ConnectionSwitch);
//		Toast.makeText(this, "Connection is:" + TestConnection.isGoogleReachable(), Toast.LENGTH_SHORT).show();
//		connSwitch.setChecked(TestConnection.isGoogleReachable());
//		connSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//		    @Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		    	
//		    	Log.d("SWITCH", "Switch changed to:"+ connSwitch.isChecked());
//		    	EditText editIP = (EditText) findViewById(R.id.ServerIP);
//		    	EditText editPort = (EditText) findViewById(R.id.ServerPort);
//
//		    	
//		    	if(isChecked){
//			    	Log.d("CONN", "Send connect command");
//			    	
//			    	SendCommandThread task = new SendCommandThread(MainActivity.this);
//		        	task.execute(editIP.getText().toString(),editPort.getText().toString());
//		        }else{
//			    	Log.d("CONN", "Send disconnect command");
//			    	SendDisconnectThread task = new SendDisconnectThread(MainActivity.this);
//		        	task.execute(editIP.getText().toString(),editPort.getText().toString());
//		        }
//		    	
//		    	savePreferences("Server_IP", editIP.getText().toString());
//		    	savePreferences("Server_Port", editPort.getText().toString());
//
//		        
//		    }
//		});
//
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent k = new Intent(MainActivity.this, CommandsActivity.class);
			startActivity(k);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadSavedPreferences() {
		EditText serverIP = (EditText)findViewById(R.id.ServerIP);
    	EditText serverPort = (EditText)findViewById(R.id.ServerPort);  
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String iP = sharedPreferences.getString("Server_IP", "Server_IP");
		String port = sharedPreferences.getString("Server_Port", "Server_Port");

		serverIP.setText(iP);
		serverPort.setText(port);
	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static void setLogText(String text){
		logText.append(text);
		
	}
	
}
