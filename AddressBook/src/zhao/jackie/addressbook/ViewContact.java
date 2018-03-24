package zhao.jackie.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewContact extends Activity 
{
private long rowID;
private TextView nameTextView;
private TextView phoneTextView; 
private TextView emailTextView;
private TextView streetTextView;
private TextView cityTextView;

@Override
public void onCreate(Bundle savedInstanceState) 
{
   super.onCreate(savedInstanceState);
   setContentView(R.layout.view_contact);

   nameTextView = (TextView) findViewById(R.id.nameTextView);
   phoneTextView = (TextView) findViewById(R.id.phoneTextView);
   emailTextView = (TextView) findViewById(R.id.emailTextView);
   streetTextView = (TextView) findViewById(R.id.streetTextView);
   cityTextView = (TextView) findViewById(R.id.cityTextView);
   Bundle extras = getIntent().getExtras();
   rowID = extras.getLong(AddressBook.ROW_ID); 
}
@Override
protected void onResume()
{
   super.onResume();
   new LoadContactTask().execute(rowID);
}

// performs database query outside GUI thread
private class LoadContactTask extends AsyncTask<Long, Object, Cursor> 
{
   DatabaseConnector databaseConnector = 
      new DatabaseConnector(ViewContact.this);

   // perform the database access
   @Override
   protected Cursor doInBackground(Long... params)
   {
      databaseConnector.open();    
      // get a cursor containing all data on given entry
      return databaseConnector.getOneContact(params[0]);
   }

   // use the Cursor returned from the doInBackground method
   @Override
   protected void onPostExecute(Cursor result)
   {
      super.onPostExecute(result);

      result.moveToFirst();

      // get the column index for each data item
      int nameIndex = result.getColumnIndex("name");
      int phoneIndex = result.getColumnIndex("phone");
      int emailIndex = result.getColumnIndex("email");
      int streetIndex = result.getColumnIndex("street");
      int cityIndex = result.getColumnIndex("city");

      // fill TextViews with the retrieved data
      nameTextView.setText(result.getString(nameIndex));
      phoneTextView.setText(result.getString(phoneIndex));
      emailTextView.setText(result.getString(emailIndex));
      streetTextView.setText(result.getString(streetIndex));
      cityTextView.setText(result.getString(cityIndex));

      result.close();
      databaseConnector.close();
   } 
} 
   
@Override
public boolean onCreateOptionsMenu(Menu menu) 
{
   super.onCreateOptionsMenu(menu);
   MenuInflater inflater = getMenuInflater();
   inflater.inflate(R.menu.view_contact_menu, menu);
   return true;
}
@Override
public boolean onOptionsItemSelected(MenuItem item) 
{
   switch (item.getItemId())
   {
      case R.id.editItem:
         // create an Intent to launch the AddEditContact Activity
         Intent addEditContact =
            new Intent(this, AddEditContact.class);         
         // pass the selected contact's data as extras with the Intent
         addEditContact.putExtra(AddressBook.ROW_ID, rowID);
         addEditContact.putExtra("name", nameTextView.getText());
         addEditContact.putExtra("phone", phoneTextView.getText());
         addEditContact.putExtra("email", emailTextView.getText());
         addEditContact.putExtra("street", streetTextView.getText());
         addEditContact.putExtra("city", cityTextView.getText());
         startActivity(addEditContact);
         return true;
      case R.id.deleteItem:
         deleteContact(); 
         return true;
      default:
         return super.onOptionsItemSelected(item);
   } 
}
private void deleteContact()
{
   AlertDialog.Builder builder = 
      new AlertDialog.Builder(ViewContact.this);

   builder.setTitle(R.string.confirmTitle);
   builder.setMessage(R.string.confirmMessage); 

   builder.setPositiveButton(R.string.button_delete,
      new DialogInterface.OnClickListener()
      {
         public void onClick(DialogInterface dialog, int button)
         {
            final DatabaseConnector databaseConnector = 
               new DatabaseConnector(ViewContact.this);
            AsyncTask<Long, Object, Object> deleteTask =
               new AsyncTask<Long, Object, Object>()
               {
                  @Override
                  protected Object doInBackground(Long... params)
                  {
                     databaseConnector.deleteContact(params[0]); 
                     return null;
                  } 
                  @Override
                  protected void onPostExecute(Object result)
                  {
                     finish();
                  } 
               }; 
            deleteTask.execute(new Long[] { rowID });               
         } 
      } 
   ); 
   
   builder.setNegativeButton(R.string.button_cancel, null);
   builder.show();
} 
}
