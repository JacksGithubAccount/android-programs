package zhao.jackie.bouncingball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class BallView extends SurfaceView implements SurfaceHolder.Callback
{
	private BallThread ballThread;
	private Activity activity;
	private boolean dialogIsDisplayed;
	
	private boolean gameOver;
	private double totalElapsedTime;
	   
	   
	private Point ball;
	private int ballVelocityX; // cannonball's x velocity
	private int ballVelocityY; // cannonball's y velocity
	private int ballRadius; // cannonball radius
	private int ballSpeed;
	private int ballPointX;
    private int ballPointY;
	   // variables for the blocker and target
	private Line blocker; // start and end points of the blocker
	private int blockerDistance; // blocker distance from left
	private int blockerBeginning; // blocker distance from top
	private int blockerEnd; // blocker bottom edge distance from top
	private int initialBlockerVelocity; // initial blocker speed multiplier
	private float blockerVelocity; // blocker speed multiplier during game
	
	private int lineWidth;
	private int screenWidth;
	private int screenHeight;
	   
	private Paint blockerPaint;
	private Paint ballPaint;
	private Paint textPaint;
	private Paint backgroundPaint;
	   
	public BallView(Context context, AttributeSet attrs)  
    {  
     super(context, attrs);  
     activity = (Activity) context; 
     getHolder().addCallback(this); 
     
     blocker = new Line();
     blockerPaint = new Paint();
     textPaint = new Paint();
     backgroundPaint = new Paint();
     ballPaint = new Paint();
    }
	
	/*public BallView(Context context)
    {
        super(context);
    }
    public BallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }*/
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
	      super.onSizeChanged(w, h, oldw, oldh);
	      
	      screenWidth = w;
	      screenHeight = h;
	      // configure instance variables related to the blocker
	      blockerDistance = h * 7 / 8; // blocker 5/8 screen width from left
	      blockerBeginning = w * 3 / 8; // distance from top 1/8 screen height
	      blockerEnd = w * 6 / 8; // distance from top 3/8 screen height
	      initialBlockerVelocity = w / 2; // initial blocker speed multiplier
	      lineWidth = w / 24;
	      
	      ballRadius = w / 36; // cannonball radius 1/36 screen width
	      ballSpeed = w * 1 / 2;
	      ballPointX = ballRadius;
	      ballPointY = screenHeight / 2;
	      ballVelocityX = ballSpeed * -1;
	      ballVelocityY = ballSpeed * -1;
	      
	      blocker.start = new Point(blockerDistance, blockerBeginning);
	      blocker.end = new Point(blockerDistance, blockerEnd);
	      ball = new Point(ballPointX, ballPointY);
	      
	      blockerPaint.setStrokeWidth(lineWidth);
	      textPaint.setTextSize(w / 20);
	      textPaint.setAntiAlias(true);
	      ballPaint.setStrokeWidth(lineWidth * 1.5f);
	      backgroundPaint.setColor(Color.WHITE);
	      
	      newGame();
	}
	public void newGame()
	{
		blocker.start.set(blockerDistance, blockerBeginning);
	    blocker.end.set(blockerDistance, blockerEnd);
	    ball.set(ballPointX, ballPointY);
	    
	    if (gameOver)
	      {
	         gameOver = false; // the game is not over
	         ballThread = new BallThread(getHolder());
	         ballThread.start();
	      }
	}
	
	 private void updatePositions(double elapsedTimeMS)
	   {
	      double interval = elapsedTimeMS / 1000.0; // convert to seconds
	      if (ball.x + ballRadius > blockerDistance && 
	                ball.x - ballRadius < blockerDistance &&
	                ball.y + ballRadius > blocker.start.y &&
	                ball.y - ballRadius < blocker.end.y)
	             {
	                ballVelocityY *= -1; // reverse cannonball's direction
	             }
	      // update the blocker's position
	      double blockerUpdate = interval * blockerVelocity;
	      blocker.start.x += blockerUpdate;
	      blocker.end.x += blockerUpdate;	
	      
	      double targetUpdateY = interval * ballVelocityY;
	      ball.y += targetUpdateY;
	      ball.y += targetUpdateY;
	      double targetUpdateX = interval * ballVelocityX;
	      ball.x += targetUpdateX;
	      ball.x += targetUpdateX;
	      //ball.x += interval * ballVelocityX;
	      //ball.y += interval * ballVelocityY;
	      
	      if (ball.y < 0)
	      {
	          ballVelocityY *= -1;
	      }
	      if(ball.x > screenWidth || ball.x < 0)
	      {
	    	  ballVelocityX *= -1;
	      }
	      
	        if (ball.y - ballRadius > screenHeight)
	        {
	        	//penalty
	        	gameOver = true; // the game is over
	            ballThread.setRunning(false);
	            showGameOverDialog(R.string.lose);
	        }
	           
	   }
	 

	
    protected void drawGameElements(Canvas canvas) 
    {  
    	 canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), 
    	         backgroundPaint);
    	 canvas.drawCircle(ball.x, ball.y, ballRadius,
    	            ballPaint);
        canvas.drawLine(blocker.start.y, blocker.start.x, blocker.end.y,
                blocker.end.x, blockerPaint);
      ball.y += ballVelocityY ;  
      ball.x += ballVelocityX ;
      //(int) Math.round(System.currentTimeMillis() % (this.getHeight()*2))
    }
	 public double alignPaddle(MotionEvent event)
	 {
		 Point touchPoint = new Point((int) event.getX(), (int) event.getY());
		 double xCord = touchPoint.x;
		 return xCord;
	 }
    private void showGameOverDialog(int messageId)
    {
       // create a dialog displaying the given String
       final AlertDialog.Builder dialogBuilder = 
          new AlertDialog.Builder(getContext());
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
             } // end method onClick
          } // end anonymous inner class
       ); // end call to setPositiveButton

       activity.runOnUiThread(
          new Runnable() {
             public void run()
             {
                dialogIsDisplayed = true;
                dialogBuilder.show(); // display the dialog
             } // end method run
          } // end Runnable
       ); // end call to runOnUiThread
    }
    public void stopGame()
    {
       if (ballThread != null)
          ballThread.setRunning(false);
    }
    public void surfaceChanged(SurfaceHolder holder, int format,
    	      int width, int height)
    	   {
    	   } 
    public void surfaceCreated(SurfaceHolder holder)
    {
       if (!dialogIsDisplayed)
       {
          ballThread = new BallThread(holder);
          ballThread.setRunning(true);
          ballThread.start(); // start the game loop thread
       } // end if
    }
    public void surfaceDestroyed(SurfaceHolder holder)
    {
       // ensure that thread terminates properly
       boolean retry = true;
       ballThread.setRunning(false);
       
       while (retry)
       {
          try
          {
             ballThread.join();
             retry = false;
          } // end try
          catch (InterruptedException e)
          {
          } // end catch
       } // end while
    }
    private class BallThread extends Thread
    {
       private SurfaceHolder surfaceHolder; // for manipulating canvas
       private boolean threadIsRunning = true; // running by default
       
       // initializes the surface holder
       public BallThread(SurfaceHolder holder)
       {
          surfaceHolder = holder;
          setName("BallThread");
       } // end constructor
       
       // changes running state
       public void setRunning(boolean running)
       {
          threadIsRunning = running;
       } // end method setRunning
       
       // controls the game loop
       @Override
       public void run()
       {
          Canvas canvas = null; // used for drawing
          long previousFrameTime = System.currentTimeMillis(); 
         
          while (threadIsRunning)
          {
             try
             {
                canvas = surfaceHolder.lockCanvas(null);               
                
                // lock the surfaceHolder for drawing
                synchronized(surfaceHolder)
                {
                   long currentTime = System.currentTimeMillis();
                   double elapsedTimeMS = currentTime - previousFrameTime;
                   totalElapsedTime += elapsedTimeMS / 1000.00; 
                   updatePositions(elapsedTimeMS); // update game state
                   drawGameElements(canvas); // draw 
                   previousFrameTime = currentTime; // update previous time
                } // end synchronized block
             } // end try
             finally
             {
                if (canvas != null) 
                   surfaceHolder.unlockCanvasAndPost(canvas);
             }
          }
       }
    } 
}
