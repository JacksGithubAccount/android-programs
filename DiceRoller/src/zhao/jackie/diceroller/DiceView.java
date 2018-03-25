package zhao.jackie.diceroller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DiceView extends SurfaceView implements SurfaceHolder.Callback
{
	private DiceThread diceThread;
	private Dice dice;
	//private ArrayList<Dice> diceAmount;
	private Activity activity;
	private double totalElapsedTime;
	private boolean dialogIsDisplayed;
	private Drawable diceDrawable;
	
	private boolean gameOver;
	
	private int lineWidth;
	private int screenWidth;
	private int screenHeight;
	
	private Point diceStart;
	private Point diceEnd;
	//private int diceVelocityX; // dice's x velocity
	//private int diceVelocityY;
	//private int diceRadius; // cannonball radius
	   private int diceSpeed;
	private int numberOfDice;
	
	private Paint dicePaint;
	private Paint textPaint;
	private Paint backgroundPaint;
	public DiceView(Context context, AttributeSet attrs)  
    {  
     super(context, attrs);  
     activity = (Activity) context; 
     getHolder().addCallback(this);
     diceStart = new Point();
     diceEnd = new Point();
     
     dicePaint = new Paint();
     textPaint = new Paint();
     backgroundPaint = new Paint();
     
     diceThread = new DiceThread(getHolder(), context, new Handler());
    }
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
	      super.onSizeChanged(w, h, oldw, oldh);
	      
	      //screenWidth = w;
	     // screenHeight = h;
	     // lineWidth = w / 24;
	      
	      //diceRadius = w / 36;
	      //diceSpeed = w * 3 / 2;
	      
	      //diceStart.set(w/4, screenHeight/4);
	    //  diceEnd.set(w/10, h/8);
	     // diceVelocityX = diceSpeed * -1;
	     // diceVelocityY = diceSpeed * -1;
	      
	      //textPaint.setTextSize(w / 20);
	      //textPaint.setAntiAlias(true);
	      //dicePaint.setStrokeWidth(lineWidth * 1.5f);
	    //  dicePaint.setColor(Color.BLACK);
	  //    backgroundPaint.setColor(Color.GRAY);
	      
	//      newGame();	      
	}
	public void setDice(Dice dice)
	{
		this.dice = dice;
		//this.diceAmount = diceAmount;
	}
	   //private void updatePositions(double elapsedTimeMS)
	   //{
		   //needs to stop shrinking the dice
	    //  double interval = elapsedTimeMS / 1000.0;
	     //    diceStart.x += interval * diceVelocityX;
	      //   diceStart.y += interval * diceVelocityY;
	       //  diceEnd.x += interval * diceVelocityX;
	        // diceEnd.y += interval * diceVelocityY;
	      //check for collision detection with borders of screen
	      //if (diceStart.y > screenHeight || diceStart.y < 0)
	      //{
	        //  diceVelocityY *= -1;
	      //}
	      //if(diceStart.x > screenWidth || diceStart.x < 0)
	      //{
	    	//  diceVelocityX *= -1;
	      //}
	      
	   //}

  //  public void rollDice(MotionEvent event)
  //  {
    	//double angle = alignThrow(event);
    	//dice.rollDice();
    	//diceStart.x = screenWidth /4; 
        //diceStart.y = screenHeight / 4; 
        //diceEnd.x = screenWidth /10; 
        //diceEnd.y = screenHeight / 8; 

        //diceVelocityX = (int) (diceSpeed * Math.sin(angle));

        //diceVelocityY = (int) (-diceSpeed * Math.cos(angle));
    	//++numberOfDice;
  //  }
	//public double alignThrow(MotionEvent event)
	//{
	//	Point touchPoint = new Point((int) event.getX(), (int) event.getY());
	//	double centerMinusY = (screenHeight / 2 - touchPoint.y);
	//	double angle = 0;
	//	if (centerMinusY != 0)
	 //        angle = Math.atan((double) touchPoint.x / centerMinusY);
//
	 //   if (touchPoint.y > screenHeight / 2)
	//         angle += Math.PI;
	//	
	//	return angle;
	//}
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    	boolean retry = true;
        diceThread.setRunning(false);
        
        while (retry)
        {
           try
           {
              diceThread.join();
              retry = false;
           } // end try
           catch (InterruptedException e)
           {
           } // end catch
        } 
    }

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		diceThread.setScreenSize(width, height);  
	 //      if (!dialogIsDisplayed)
	   //    {
	     //     diceThread = new DiceThread(holder, context, handler);
	       //   diceThread.setRunning(true);
	         // diceThread.start(); // start the game loop thread
	       //}		
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
        diceThread.setRunning(true);  
        diceThread.start();  
		
	}
	public void newGame()
	{
		numberOfDice = 0;
		if(gameOver)
		{
			gameOver = false;
			diceThread = new DiceThread(getHolder(), getContext(), new Handler());
			diceThread.start();
		}
	}
	
    public void stopGame()
    {
       if (diceThread != null)
          diceThread.setRunning(false);
    }
    
    public DiceThread getThread() { return diceThread; } 
}
