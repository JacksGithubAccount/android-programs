package zhao.jackie.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector 
{
private static final String DATABASE_NAME = "UserContacts";
private SQLiteDatabase database; 
private DatabaseOpenHelper databaseOpenHelper; 

public DatabaseConnector(Context context) 
{
   databaseOpenHelper = 
      new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
}

// open the database connection
public void open() throws SQLException 
{
   database = databaseOpenHelper.getWritableDatabase();
}

// close the database connection
public void close() 
{
   if (database != null)
      database.close(); 
}

// inserts a new contact in the database
public void insertContact(String name, String email, String phone, 
   String state, String city) 
{
   ContentValues newContact = new ContentValues();
   newContact.put("name", name);
   newContact.put("email", email);
   newContact.put("phone", phone);
   newContact.put("street", state);
   newContact.put("city", city);

   open();
   database.insert("contacts", null, newContact);
   close();
}
public void updateContact(long id, String name, String email, 
   String phone, String state, String city) 
{
   ContentValues editContact = new ContentValues();
   editContact.put("name", name);
   editContact.put("email", email);
   editContact.put("phone", phone);
   editContact.put("street", state);
   editContact.put("city", city);
   open(); 
   database.update("contacts", editContact, "_id=" + id, null);
   close(); 
}
// return a Cursor with all contact information in the database
public Cursor getAllContacts() 
{
   return database.query("contacts", new String[] {"_id", "name"}, 
      null, null, null, null, "name");
} 
public Cursor getOneContact(long id) 
{
   return database.query(
      "contacts", null, "_id=" + id, null, null, null, null);
}

public void deleteContact(long id) 
{
   open(); 
   database.delete("contacts", "_id=" + id, null);
   close(); 
} 

private class DatabaseOpenHelper extends SQLiteOpenHelper 
{
   public DatabaseOpenHelper(Context context, String name,
      CursorFactory factory, int version) 
   {
      super(context, name, factory, version);
   } 

   // creates the contacts table when the database is created
   @Override
   public void onCreate(SQLiteDatabase db) 
   {
      // query to create a new table named contacts
      String createQuery = "CREATE TABLE contacts" +
         "(_id integer primary key autoincrement," +
         "name TEXT, email TEXT, phone TEXT," +
         "street TEXT, city TEXT);";
               
      db.execSQL(createQuery);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, 
       int newVersion) 
   {
   } 
}
}

