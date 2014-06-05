package com.christian.satellitelink;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class SendDisconnectThread extends AsyncTask<String, Void, Void> {
	String output= "";
	 private ProgressDialog dialog;
	 public SendDisconnectThread(MainActivity mainActivity) {
		 dialog = new ProgressDialog(mainActivity);
	}
	
	 @Override
	    protected void onPreExecute() {
	        dialog.setMessage("Running disconnect commands, please wait.");
	        dialog.show();
	    }
	 @Override
	    protected void onPostExecute(Void result) {
	        if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	        Log.d("OUTPUT", output);
	        MainActivity.setLogText(output);
	    }
	 @Override
	    protected Void doInBackground(String... params) {
	        output =TelnetConnection.SendDisconnectCommand(params[0], params[1]);
	        return null;
	    }
	     
	}