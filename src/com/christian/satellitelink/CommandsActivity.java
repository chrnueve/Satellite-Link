package com.christian.satellitelink;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CommandsActivity extends Activity {
	final ArrayList<String> commandArray = new ArrayList<>();
	int selection=0;
	ArrayAdapter<String> arrayAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commands);
		ListView list = (ListView) findViewById(R.id.listView2);

		readArray();
		arrayAdapter = new ArrayAdapter<String>(
	            this, 
	            android.R.layout.simple_list_item_1,
	            commandArray );
		 

         list.setAdapter(arrayAdapter); 
         final Button addButton = (Button) findViewById(R.id.addButton);
         addButton.setOnClickListener(new View.OnClickListener() {
             @Override
			public void onClick(View v) {
                EditText added = (EditText) findViewById(R.id.commandAdd);
                if(added.getText().length()!=0){
                String addToArray = added.getText().toString();
                commandArray.add(addToArray);
                added.setText("");
                saveArray();
                
                arrayAdapter.notifyDataSetChanged();
                }else{
                	Toast.makeText(CommandsActivity.this, "Empty commands are not allowed", Toast.LENGTH_SHORT).show();
                }
             }
         });
         list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	   public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
        		   selection = position;
        	     System.out.println("Long click");
        	     startActionMode(modeCallBack);
        	     view.setSelected(true);
        	     return true;
        	   }
        	});
         
		
	}
	public boolean onCreateOptionsMenu(Menu menu) {
        
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.commands, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.PDPTester) {
			commandArray.add("AT+CGDCONT?");
			saveArray();
            arrayAdapter.notifyDataSetChanged();
			return true;
		}
		if (id == R.id.PDPContext) {
			commandArray.add("AT+CGDCONT=7,\"IP\",\"mvs.bgan.inmarsat.com\",,0,1");
			saveArray();
            arrayAdapter.notifyDataSetChanged();
			return true;
		}
		if (id == R.id.ContextActivator) {
			commandArray.add("AT+CGACT=1,7");
			saveArray();
            arrayAdapter.notifyDataSetChanged();
			return true;
		}
		if (id == R.id.dataPath) {
			commandArray.add("AT+CGDATA=\"IP\",1 ");
			saveArray();
            arrayAdapter.notifyDataSetChanged();
			return true;
		}
		if (id == R.id.disconnectCommand) {
		AlertDialog.Builder alert = new AlertDialog.Builder(CommandsActivity.this);

     	   alert.setTitle("Change disconnect command:");
     	   

     	   // Set an EditText view to get user input 
     	   final EditText input = new EditText(CommandsActivity.this);
     	   alert.setView(input);
     	   input.setText("AT+CGACT=0,7");
     	   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
     	   public void onClick(DialogInterface dialog, int whichButton) {
     	     String value =""+ input.getText();
     	     saveDisconnect(value);
     	     }
     	   });

     	   alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
     	     public void onClick(DialogInterface dialog, int whichButton) {
     	       // Canceled.
     	     }
     	   });

     	   alert.show();
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

		   public boolean onPrepareActionMode(ActionMode mode, Menu menu){  
		    return false;
		   }

		  public void onDestroyActionMode(ActionMode mode) {
		    mode = null;   
		   }

		  public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			  mode.setTitle("Options");
			  mode.getMenuInflater().inflate(R.menu.options, menu);
			  return true;
			 }

		  public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			  final int id = item.getItemId();
			  switch (id) {
			    case R.id.delete: {
			      commandArray.remove( selection );
			      arrayAdapter.notifyDataSetChanged();
			             mode.finish();
			      break;
			           }
			           case R.id.edit: {
			        	   AlertDialog.Builder alert = new AlertDialog.Builder(CommandsActivity.this);

			        	   alert.setTitle("Change command value:");
			        	   

			        	   // Set an EditText view to get user input 
			        	   final EditText input = new EditText(CommandsActivity.this);
			        	   alert.setView(input);
			        	   input.setText(commandArray.get(selection));
			        	   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        	   public void onClick(DialogInterface dialog, int whichButton) {
			        	     String value =""+ input.getText();
			        	     commandArray.set(selection, value);
			        	     arrayAdapter.notifyDataSetChanged();
			        	     }
			        	   });

			        	   alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        	     public void onClick(DialogInterface dialog, int whichButton) {
			        	       // Canceled.
			        	     }
			        	   });

			        	   alert.show();
			           }
			            
			          default:
			             return false;

			  }
			return false;
		  }
		};
		public void saveDisconnect(String disconnectCommand){
			try {
				   //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
				   FileOutputStream output = openFileOutput("disconnect.txt",MODE_WORLD_READABLE);
				   
				   DataOutputStream dout = new DataOutputStream(output);
				   dout.writeInt(1); // Save line count
				   dout.writeUTF(disconnectCommand);
				   dout.flush(); // Flush stream ...
				   dout.close(); // ... and close.
				}
				catch (IOException exc) { exc.printStackTrace(); }
		}
		public String readDisconnect(){
			
			try{
				FileInputStream input = openFileInput("disconnect.txt"); // Open input stream
				DataInputStream din = new DataInputStream(input);
				int sz = din.readInt(); // Read line count
				for (int i=0;i<sz;i++) { // Read lines
				   String line = din.readUTF();
				   return line;
				}
				din.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			return null;
			}
public void saveArray(){
	try {
		   //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
		   FileOutputStream output = openFileOutput("commands.txt",MODE_WORLD_READABLE);
		   
		   DataOutputStream dout = new DataOutputStream(output);
		   dout.writeInt(commandArray.size()); // Save line count
		   for(String line : commandArray) // Save lines
		      dout.writeUTF(line);
		   dout.flush(); // Flush stream ...
		   dout.close(); // ... and close.
		}
		catch (IOException exc) { exc.printStackTrace(); }
}
public void readArray(){
	try{
	FileInputStream input = openFileInput("commands.txt"); // Open input stream
	DataInputStream din = new DataInputStream(input);
	int sz = din.readInt(); // Read line count
	for (int i=0;i<sz;i++) { // Read lines
	   String line = din.readUTF();
	   commandArray.add(line);
	}
	din.close();
	}catch(IOException e){
		e.printStackTrace();
	}
}
	
}