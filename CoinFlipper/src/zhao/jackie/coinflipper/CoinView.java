package zhao.jackie.coinflipper;

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

public class CoinView extends SurfaceView implements SurfaceHolder.Callback
{
	private CoinThread coinThread;
	private Coin coin;
	private Activity activity;
	private double totalElapsedTime;
	private boolean dialogIsDisplayed;
	private Drawable CoinDrawable;
	
	private boolean gameOver;
	
	private int lineWidth;
	private int screenWidth;
	private int screenHeight;
	
	private Point CoinStart;
	private Point CoinEnd;
	   private int CoinSpeed;
	private int numberOfCoin;
	
	private Paint CoinPaint;
	private Paint textPaint;
	private Paint backgroundPaint;
	public CoinView(Context context, AttributeSet attrs)  
    {  
     super(context, attrs);  
     activity = (Activity) context; 
     getHolder().addCallback(this);
     CoinStart = new Point();
     CoinEnd = new Point();
     
     CoinPaint = new Paint();
     textPaint = new Paint();
     backgroundPaint = new Paint();
     
     coinThread = new CoinThread(getHolder(), context, new Handler());
    }
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
	      super.onSizeChanged(w, h, oldw, oldh); 
	}
	public void setCoin(Coin coin)
	{
		this.coin = coin;
	}
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    	boolean retry = true;
        coinThread.setRunning(false);
        
        while (retry)
        {
           try
           {
              coinThread.join();
              retry = false;
           } // end try
           catch (InterruptedException e)
           {
           } // end catch
        } 
    }

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		coinThread.setScreenSize(width, height);  
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
        coinThread.setRunning(true);  
        coinThread.start();  
		
	}
	public void newGame()
	{
		numberOfCoin = 0;
		if(gameOver)
		{
			gameOver = false;
			coinThread = new CoinThread(getHolder(), getContext(), new Handler());
			coinThread.start();
		}
	}
	
    public void stopGame()
    {
       if (coinThread != null)
          coinThread.setRunning(false);
    }
    
    public CoinThread getThread() { return coinThread; } 
}
