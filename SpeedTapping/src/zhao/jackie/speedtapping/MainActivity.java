package zhao.jackie.speedtapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button[] button = new Button[16];
	private Button startButton;
	private TextView textViewTimer;
	private boolean[] numberPressed = new boolean[16];
	private List<Integer> numbers;
	private double totalElapsedTime = 0;
	private double timeLeft;
	//private TimerThread timerThread;
	long starttime = 0;
	Timer timer = new Timer();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        numbers = new ArrayList<Integer>();
        textViewTimer = (TextView)findViewById(R.id.textViewTimer);
        button[0] = (Button)findViewById(R.id.Button1);
        button[1] = (Button)findViewById(R.id.Button2);
        button[2] = (Button)findViewById(R.id.Button3);
        button[3] = (Button)findViewById(R.id.Button4);
        button[4] = (Button)findViewById(R.id.Button5);
        button[5] = (Button)findViewById(R.id.Button6);
        button[6] = (Button)findViewById(R.id.Button7);
        button[7] = (Button)findViewById(R.id.Button8);
        button[8] = (Button)findViewById(R.id.Button9);
        button[9] = (Button)findViewById(R.id.Button10);
        button[10] = (Button)findViewById(R.id.Button11);
        button[11] = (Button)findViewById(R.id.Button12);
        button[12] = (Button)findViewById(R.id.Button13);
        button[13] = (Button)findViewById(R.id.Button14);
        button[14] = (Button)findViewById(R.id.Button15);
        button[15] = (Button)findViewById(R.id.Button16);
        button[0].setOnClickListener(buttonListener0);
        button[1].setOnClickListener(buttonListener1);
        button[2].setOnClickListener(buttonListener2);
        button[3].setOnClickListener(buttonListener3);
        button[4].setOnClickListener(buttonListener4);
        button[5].setOnClickListener(buttonListener5);
        button[6].setOnClickListener(buttonListener6);
        button[7].setOnClickListener(buttonListener7);
        button[8].setOnClickListener(buttonListener8);
        button[9].setOnClickListener(buttonListener9);
        button[10].setOnClickListener(buttonListener10);
        button[11].setOnClickListener(buttonListener11);
        button[12].setOnClickListener(buttonListener12);
        button[13].setOnClickListener(buttonListener13);
        button[14].setOnClickListener(buttonListener14);
        button[15].setOnClickListener(buttonListener15);
        startButton = (Button)findViewById(R.id.ButtonStart);
        startButton.setOnClickListener(startButtonListener);
        newGame();
    }
    //tells handler to send a message
    class firstTask extends TimerTask {

         @Override
         public void run() {
             h.sendEmptyMessage(0);
         }
    };

    private void newGame()
    {
    	numbers.clear();
    	for(int i = 0; i <= 15; i ++)
    	{
    		numberPressed[i] = false;
    		numbers.add(i + 1);  		
    	}
    	Collections.shuffle(numbers);
    	for(int i = 0; i <= 15; i ++)
    	{
    		button[i].setText(numbers.get(i).toString());
    	}
    	timeLeft = 30;
    	//timerThread = new TimerThread();
    	//timerThread.setRunning(true);
    	starttime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new firstTask(), 0,500);
    }
    private boolean compareButtonPressed(CharSequence number)
    {
    	int numberr = Integer.parseInt((String) number);
 
    		if(numberr == 1)
    		{
    			numberPressed[0] = true;
    		}
    		else if(numberr == 2)
    		{
    			if(numberPressed[0] == true)
    			{
    				numberPressed[1] = true;
    			}
    		}
    		else 
    		{
    			for(int i = numberr; i >= 2; i--)
		  		{
    				if(numberPressed[i - 2] == true)
    				{    				
    		  			numberPressed[numberr - 1] = true;
    	    		}
    		  	}
    		}
    	if(numberr == 16 && numberPressed[15] == true)
    	{
    		return true;
    	}
    	return false;
    }
  //this  posts a message to the main thread from our timertask
    //and updates the textfield
   final Handler h = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
           long millis = System.currentTimeMillis() - starttime;
           int seconds = (int) (millis / 1000);
           int minutes = seconds / 60;
           seconds     = seconds % 60;

           textViewTimer.setText(String.format("%d:%02d", minutes, seconds));
           totalElapsedTime = minutes * 60 + seconds;
            return false;
        }
    });
    @SuppressWarnings("unused")
	private void timerCheck(double elapsedTimeMS)
    {
    	double interval = elapsedTimeMS / 1000.0;
    	timeLeft -= interval; // subtract from time left
    	String timeLeftString = Double.toString(timeLeft);
    	textViewTimer.setText(timeLeftString);
        // if the timer reached zero
        if (timeLeft <= 0.0)
        {
           timeLeft = 0.0;
           endGame(); // show the losing dialog
        }
    }
    private void checkForEnd(Boolean end)
    {
    	if(end == true)
    	{
    		//stop timer
    		endGame();
    	}
    }
    private void endGame()
    {
    	timer.cancel();
        timer.purge();
    	//timerThread.setRunning(false);
		showGameOverDialog(R.string.win);
    }
    boolean dialogIsDisplayed;
    public void showGameOverDialog(int messageId)
    {
       // create a dialog displaying the given String
       final AlertDialog.Builder dialogBuilder = 
          new AlertDialog.Builder(getBaseContext());
       dialogBuilder.setTitle(getResources().getString(messageId));
       dialogBuilder.setCancelable(false);

       // display number of shots fired and total time elapsed
       dialogBuilder.setMessage(getResources().getString(
          R.string.results_format, totalElapsedTime));
       dialogBuilder.setPositiveButton(R.string.reset_game,
          new DialogInterface.OnClickListener()
          {
             // called when "Reset Game" Button is pressed
             public void onClick(DialogInterface dialog, int which)
             {
                dialogIsDisplayed = false;
                newGame(); // set up and start a new game
             } 
          } 
       ); 

       runOnUiThread(
          new Runnable() {
             public void run()
             {
                dialogIsDisplayed = true;
                dialogBuilder.show(); // display the dialog
             } // end method run
          } // end Runnable
       ); // end call to runOnUiThread
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public OnClickListener buttonListener0 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[0].getText()));
		}
    };
    public OnClickListener buttonListener1 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[1].getText()));
		}
    };
    public OnClickListener buttonListener2 = new OnClickListener() {
		public void onClick(View v) {			
			checkForEnd(compareButtonPressed(button[2].getText()));
		}
    };
    public OnClickListener buttonListener3 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[3].getText()));
		}
    };
    public OnClickListener buttonListener4 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[4].getText()));
		}
    };
    public OnClickListener buttonListener5 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[5].getText()));
		}
    };
    public OnClickListener buttonListener6 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[6].getText()));
		}
    };
    public OnClickListener buttonListener7 = new OnClickListener() {
		public void onClick(View v) {		
			checkForEnd(compareButtonPressed(button[7].getText()));
		}
    };
    public OnClickListener buttonListener8 = new OnClickListener() {
		public void onClick(View v) {	
			checkForEnd(compareButtonPressed(button[8].getText()));
		}
    };
    public OnClickListener buttonListener9 = new OnClickListener() {
		public void onClick(View v) {	
			checkForEnd(compareButtonPressed(button[9].getText()));
		}
    };
    public OnClickListener buttonListener10 = new OnClickListener() {
		public void onClick(View v) {	
			checkForEnd(compareButtonPressed(button[10].getText()));
		}
    };
    public OnClickListener buttonListener11 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[11].getText()));
		}
    };
    public OnClickListener buttonListener12 = new OnClickListener() {
		public void onClick(View v) {		
			checkForEnd(compareButtonPressed(button[12].getText()));
		}
    };
    public OnClickListener buttonListener13 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[13].getText()));
		}
    };
    public OnClickListener buttonListener14 = new OnClickListener() {
		public void onClick(View v) {
			checkForEnd(compareButtonPressed(button[14].getText()));
		}
    };
    public OnClickListener buttonListener15 = new OnClickListener() {
		public void onClick(View v) {	
			checkForEnd(compareButtonPressed(button[15].getText()));
		}
    };
    public OnClickListener startButtonListener = new OnClickListener() {
		public void onClick(View v) {
			newGame();
		}
    };
    
    //private class TimerThread extends Thread
    //{
       //private boolean threadIsRunning = true; // running by default     
       // changes running state
       //public void setRunning(boolean running)
       //{
       //   threadIsRunning = running;
      // } // end method setRunning
       
       // controls the game loop
      // @Override
       //public void run()
       //{
        //  long previousFrameTime = System.currentTimeMillis(); 
         
          //while (threadIsRunning)
          //{
              //     long currentTime = System.currentTimeMillis();
            //       double elapsedTimeMS = currentTime - previousFrameTime;
          //         totalElapsedTime += elapsedTimeMS / 1000.00; 
        //           timerCheck(elapsedTimeMS);
      //             previousFrameTime = currentTime; // update previous time
     //     } // end while
     //  } // end method run
    //}
}
