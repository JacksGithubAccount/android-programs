package zhao.jackie.diceroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class DiceRoller extends Activity 
{
	private DiceView diceView;
	private DiceThread diceThread;
	//private List<String> fileNameList;
	//private Map<String, Boolean> diceMap;
	//private GestureDetector gestureDetector;
	private FrameLayout frameLayout;
	
	int diceSide = 6;
	//int numberOfDice = 0;
	//ArrayList<Dice> diceAmount;
	Dice dice;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
     //   fileNameList = new ArrayList<String>();
      //  diceMap = new HashMap<String, Boolean>();
       
       // diceAmount = new ArrayList<Dice>();
       // diceAmount.add(numberOfDice,dice);
  //      gestureDetector = new GestureDetector(this, gestureListener);
        
        //String[] diceNames = getResources().getStringArray(R.array.diceTypeList);
        
       // for(String die : diceNames)
       // {
       // 	diceMap.put(die, true);
      //  }
        diceView = (DiceView) findViewById(R.id.diceView);
        diceThread = diceView.getThread();
        newDice();
      //  getResource();        
        //loadNextDice();
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout); 
        frameLayout.setOnTouchListener(frameLayoutListener);                                        
  
        if (savedInstanceState == null) {  
            diceThread.setRunning(true);   // First initialization  
        } else {  
            diceThread.restoreState(savedInstanceState);   // we are being restored: resume previous  
        }
       
    }
    private void newDice()
    {
    	dice = new Dice(diceSide);
    	diceThread.setDice(dice);
    	diceView.setDice(dice);
    }
   // public void addDice()
   // {
    	//dice = new Dice(diceSide);
    	//diceAmount.add(numberOfDice, dice);
    //	diceView.setDice(dice, diceAmount);
  //  }
 //   public void getResource()
  //  {
    //    AssetManager asset = getAssets();
  //      fileNameList.clear();
  //  	try
  //  	{
  // 			Set<String> dice = diceMap.keySet();
   	//		for (String die : dice)
   //			{
   //				if(diceMap.get(die))
   	//			{
    //				String[] paths = asset.list(die);
    //				for(String path :paths)
    //					fileNameList.add(path.replace(".png",""));
   	//			}
   	//		}
   	//	}
   //		catch(IOException e)
   	//	{
    //   		Log.e("Mother of God", "Error loading image file names", e);
  //     	}
   // }
   // private void loadNextDice()
    //{	
		//AssetManager assets = getAssets();
	//	InputStream stream;
		//String imageName = "d" + diceSide + "_" + dice.getSideValue(diceSide) + ".png";
		//try
	//	{
		//	stream = assets.open("d" + diceSide + "-" + dice.getSideValue(diceSide) + ".png");
			//BitmapDrawable diceDrawable = (BitmapDrawable)Drawable.createFromStream(stream,imageName);
	//		diceThread.setBitmapImage(diceDrawable);
		//}
		//catch(IOException e)
		//{
		//}
    //}

    @Override
    public void onPause()
    {
       super.onPause(); // call the super method
       diceThread.setRunning(false);
       diceView.stopGame(); // terminates the game
    }
    //@Override
    //public boolean onTouchEvent(MotionEvent event)
    //{
      // int action = event.getAction();
//
  //     if (action == MotionEvent.ACTION_DOWN ||
    //      action == MotionEvent.ACTION_MOVE)
      // {
      //    diceView.alignThrow(event);
       //} 
       //return gestureDetector.onTouchEvent(event);
    //}
    private final int DICETYPE_MENU_ID = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);	
        menu.add(menu.NONE, DICETYPE_MENU_ID, menu.NONE, R.string.choices);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case DICETYPE_MENU_ID:
    		final String[] possibleChoices = new String[] {"4","6","8","10","12","100"};
    		AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
    		choicesBuilder.setTitle("Number of Sides");
    		
    		choicesBuilder.setItems(possibleChoices, 
    				new DialogInterface.OnClickListener() 
    					{
						public void onClick(DialogInterface dialog, int item) 
						{
							diceSide = Integer.parseInt(possibleChoices[item].toString());
							newDice();
						//	numberOfDice++;
						//	addDice();
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
        diceThread.saveState(outState);  
    }
  //  SimpleOnGestureListener gestureListener = new SimpleOnGestureListener()
  //  {
    //   @Override
      // public boolean onDoubleTap(MotionEvent e)
       //{
         // diceView.rollDice(e);
          //return true;
       //} 
  //  };
    public OnTouchListener frameLayoutListener = new OnTouchListener()
    {  
        public boolean onTouch(View v, MotionEvent event) 
        {  
        	diceThread.rollDice();
            diceThread.setPosition(event.getX(), event.getY());
            return true; 
        }
    }; 
}
