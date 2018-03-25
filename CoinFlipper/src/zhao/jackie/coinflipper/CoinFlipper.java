package zhao.jackie.coinflipper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout; 
import android.widget.RelativeLayout;

public class CoinFlipper extends Activity 
{
	private CoinView coinView;
	private CoinThread coinThread;
	private RelativeLayout frameLayout;
	
	String CoinFace = "Meme Face";
	Coin coin;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        coinView = (CoinView) findViewById(R.id.CoinView);
        coinThread = coinView.getThread();
        newCoin();
        frameLayout = (RelativeLayout) findViewById(R.id.frameLayout); 
        frameLayout.setOnTouchListener(frameLayoutListener);                                        
  
        if (savedInstanceState == null) {  
            coinThread.setRunning(true);   // First initialization  
        } else {  
            coinThread.restoreState(savedInstanceState);   // we are being restored: resume previous  
        }
       
    }
    private void newCoin()
    {
    	coin = new Coin(CoinFace);
    	coinThread.setCoin(coin);
    	coinView.setCoin(coin);
    }

    @Override
    public void onPause()
    {
       super.onPause(); // call the super method
       coinThread.setRunning(false);
       coinView.stopGame(); // terminates the game
    }
    private final int CoinTYPE_MENU_ID = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);	
        menu.add(menu.NONE, CoinTYPE_MENU_ID, menu.NONE, R.string.choices);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case CoinTYPE_MENU_ID:
    		final String[] possibleChoices = new String[] {"Troll Caegar","George Washington","Abraham Lincoln","Meme Face","Caegar","ICP"};
    		AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
    		choicesBuilder.setTitle("Face of Coin: ");
    		
    		choicesBuilder.setItems(possibleChoices, 
    				new DialogInterface.OnClickListener() 
    					{
						public void onClick(DialogInterface dialog, int item) 
						{
							CoinFace = possibleChoices[item].toString();
							newCoin();
						}
					});
    		AlertDialog choicesDialog = choicesBuilder.create();
    		choicesDialog.show();
    		return true;
    	}
    	return super.onOptionsItemSelected(item);    	
    }
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        // just have the View's thread save its state into our Bundle  
        super.onSaveInstanceState(outState);  
        coinThread.saveState(outState);  
    }
    public OnTouchListener frameLayoutListener = new OnTouchListener()
    {  
        public boolean onTouch(View v, MotionEvent event) 
        {  
        	coinThread.setHasClick(true);
        	coinThread.flipCoin();           
            return true; 
        }
    }; 
}
