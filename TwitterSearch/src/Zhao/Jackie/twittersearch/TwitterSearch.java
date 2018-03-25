package Zhao.Jackie.twittersearch;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.Menu;

public class TwitterSearch extends Activity {

	private SharedPreferences savedSearches;
	private TableLayout queryTableLayout;
	private EditText queryEditText;
	private EditText tagEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        savedSearches = getSharedPreferences("searches", MODE_PRIVATE);
        queryTableLayout = (TableLayout) findViewById(R.id.tableLayoutQuery);
        queryEditText = (EditText)findViewById(R.id.editTextQuery);
        tagEditText = (EditText)findViewById(R.id.editTextTag);
        Button saveButton = (Button)findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(saveButtonListener);
        Button clearTagsButton = (Button)findViewById(R.id.buttonClearTags);
        clearTagsButton.setOnClickListener(clearTagsButtonListener);
        refreshButtons(null);
    }
    public void refreshButtons(String newTag)
    {
    	String[] tags = savedSearches.getAll().keySet().toArray(new String[0]);
    	Arrays.sort(tags, String.CASE_INSENSITIVE_ORDER);
    	
    	if(newTag != null)
    	{
    		makeTagGUI(newTag, Arrays.binarySearch(tags, newTag));
    	}
    	else
    	{
    		for(int index = 0; index < tags.length; index++)
    		{
    			makeTagGUI(tags[index], index);
    		}
    	}
    	
    	
    	
    	
    	
    	
    }
    private void makeTag(String query, String tag)
    {
    	String originalQuery = savedSearches.getString(tag, null);
    	
    	SharedPreferences.Editor preferencesEditor = savedSearches.edit();
    	preferencesEditor.putString(tag, query);
    	preferencesEditor.apply();
    	if(originalQuery == null)
    	{
    		refreshButtons(tag);
    	}
    }
    private void makeTagGUI(String tag, int index)
    {
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View newTagView = inflater.inflate(R.layout.new_tag_view, null);
    	Button newTagButton = (Button) newTagView.findViewById(R.id.buttonNewTag);
    	newTagButton.setText(tag);
    	newTagButton.setOnClickListener(queryButtonListener);
    	
    	Button newEditButton = (Button) newTagView.findViewById(R.id.buttonNewEdit);
    	newEditButton.setOnClickListener(editButtonListener);
    	
    	queryTableLayout.addView(newTagView, index);
    }
    private void clearButtons()
    {
    	queryTableLayout.removeAllViews();
    }
    public OnClickListener saveButtonListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		if(queryEditText.getText().length()> 0 && tagEditText.getText().length()> 0)
    		{
    			makeTag(queryEditText.getText().toString(), tagEditText.getText().toString());
    			queryEditText.setText("");
    			tagEditText.setText("");
    			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tagEditText.getWindowToken(), 0);
    		}
    		else
    		{
    			AlertDialog.Builder builder = new AlertDialog.Builder(TwitterSearch.this);
    			builder.setTitle(R.string.missingTitle);
    			builder.setPositiveButton(R.string.OK, null);
    			builder.setMessage(R.string.missingMessage);
    			AlertDialog errorDialog = builder.create();
    			errorDialog.show();
    		}
    	}
    };
    
    public OnClickListener queryButtonListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		String buttonText = ((Button)v).getText().toString();
    		String query = savedSearches.getString(buttonText,null);
    		
    		String urlString = getString(R.string.searchURL)+ query;
    		Intent getURL = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
    		startActivity(getURL);
    	}
    };
    public OnClickListener editButtonListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		TableRow buttonTableRow = (TableRow) v.getParent();
    		Button searchButton = (Button) buttonTableRow.findViewById(R.id.buttonNewTag);
    		String tag = searchButton.getText().toString();
    		tagEditText.setText(tag);
    		queryEditText.setText(savedSearches.getString(tag, null));
    	}
    };
    public OnClickListener clearTagsButtonListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		AlertDialog.Builder builder = new AlertDialog.Builder(TwitterSearch.this);
    		builder.setTitle(R.string.confirmTitle);
    		builder.setPositiveButton(R.string.erase, new DialogInterface.OnClickListener()
    		{
    			public void onClick(DialogInterface dialog, int button)
    			{
    				clearButtons();
    				SharedPreferences.Editor preferencesEditor = savedSearches.edit();
    				preferencesEditor.clear();
    				preferencesEditor.apply();
    			}
    		}
    	);
    	builder.setCancelable(true);
    	builder.setNegativeButton(R.string.cancel, null);
    	builder.setMessage(R.string.confirmMessage);
    	AlertDialog confirmDialog = builder.create();
    	confirmDialog.show();
    	}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_twitter_search, menu);
        return true;
    }
}
