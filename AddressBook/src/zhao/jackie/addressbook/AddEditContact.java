package zhao.jackie.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditContact extends Activity 
{
private long rowID;
private EditText nameEditText;
private EditText phoneEditText;
private EditText emailEditText;
private EditText streetEditText;
private EditText cityEditText;

@Override
public void onCreate(Bundle savedInstanceState) 
{
   super.onCreate(savedInstanceState);
   setContentView(R.layout.add_contact);

   nameEditText = (EditText) findViewById(R.id.nameEditText);
   emailEditText = (EditText) findViewById(R.id.emailEditText);
   phoneEditText = (EditText) findViewById(R.id.phoneEditText);
   streetEditText = (EditText) findViewById(R.id.streetEditText);
   cityEditText = (EditText) findViewById(R.id.cityEditText);
   
   Bundle extras = getIntent().getExtras();
   if (extras != null)
   {
      rowID = extras.getLong("row_id");
      nameEditText.setText(extras.getString("name"));  
      emailEditText.setText(extras.getString("email"));  
      phoneEditText.setText(extras.getString("phone"));  
      streetEditText.setText(extras.getString("street"));  
      cityEditText.setText(extras.getString("city"));  
   } 
   Button saveContactButton = 
      (Button) findViewById(R.id.saveContactButton);
   saveContactButton.setOnClickListener(saveContactButtonClicked);
}

// responds to event generated when user clicks the Done Button
OnClickListener saveContactButtonClicked = new OnClickListener() 
{
   public void onClick(View v) 
   {
      if (nameEditText.getText().length() != 0)
      {
         AsyncTask<Object, Object, Object> saveContactTask = 
            new AsyncTask<Object, Object, Object>() 
            {
               @Override
               protected Object doInBackground(Object... params) 
               {
                  saveContact();
                  return null;
               } 
               @Override
               protected void onPostExecute(Object result) 
               {
                  finish();
               }
            };
         saveContactTask.execute((Object[]) null); 
      }
      else
      {
         AlertDialog.Builder builder = 
            new AlertDialog.Builder(AddEditContact.this);
         builder.setTitle(R.string.errorTitle); 
         builder.setMessage(R.string.errorMessage);
         builder.setPositiveButton(R.string.errorButton, null); 
         builder.show(); 
      } 
   } 
}; 

// saves contact information to the database
private void saveContact() 
{
   // get DatabaseConnector to interact with the SQLite database
   DatabaseConnector databaseConnector = new DatabaseConnector(this);

   if (getIntent().getExtras() == null)
   {
      databaseConnector.insertContact(
         nameEditText.getText().toString(),
         emailEditText.getText().toString(), 
         phoneEditText.getText().toString(), 
         streetEditText.getText().toString(),
         cityEditText.getText().toString());
   }
   else
   {
      databaseConnector.updateContact(rowID,
         nameEditText.getText().toString(),
         emailEditText.getText().toString(), 
         phoneEditText.getText().toString(), 
         streetEditText.getText().toString(),
         cityEditText.getText().toString());
   }
} 
}
