package zhao.jackie.coinflipper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;

public class CoinThread extends Thread
{
	BitmapDrawable[] trollCaegar;
	BitmapDrawable[] georgeWashington;
	BitmapDrawable[] abrahamLincoln;
	BitmapDrawable[] memeFace;
	BitmapDrawable[] caegar;
	BitmapDrawable[] icp;
	private SurfaceHolder surfaceHolder;
	private Handler handler;
	private Context context;
	long mLastTime;  
	private Paint backgroundPaint;
	private AtomicBoolean hasClick;
	private boolean threadIsRunning = true;
    CoinCollisionCheck mCoinCollisionCheck;  
    CoinPhysics mCoinPhysics;  
    static final double mCoinRadius = 5.0;
    Coin coin;
	
    public CoinThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
    	this.surfaceHolder = surfaceHolder;
    	this.context = context;
    	this.handler = handler;
    	setName("CoinThread");
    	hasClick = new AtomicBoolean(false);
    	backgroundPaint = new Paint();
    	backgroundPaint.setColor(Color.WHITE);
    	
    	trollCaegar = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.troll_caegar_heads), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.troll_caegar_tails)};
    	georgeWashington = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.george_washington_heads), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.george_washington_tails)};
    	abrahamLincoln = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.abraham_lincoln_heads), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.abraham_lincoln_tails)};
    	memeFace = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.meme_face_heads), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.meme_face_tails)};
    	caegar = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.caegar_heads), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.caegar_tails)};
    	icp = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.shaggy_2_dope), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.violent_j)};
    	
    	mCoinCollisionCheck = new CoinCollisionCheck(mCoinRadius, 0, 0);  
        mCoinPhysics = new CoinPhysics(100, 450, mCoinCollisionCheck);  
  
        mLastTime = 0; 
    }
    public void setCoin(Coin coin)
    {
    	this.coin = coin;
    	flipCoin();
    }
    public void setHasClick(Boolean clicked)
    {
    	hasClick.set(clicked);
    }
    public void flipCoin()
    {
    	coin.FlipCoin();
    }
    public void setRunning(boolean running)
    {
       threadIsRunning = running;
    }
    @Override
    public void run()
    {
       Canvas canvas = null;
       while (threadIsRunning)
       {
          try
          {
             canvas = surfaceHolder.lockCanvas(null);                           
             synchronized(surfaceHolder)
             {
				if(hasClick.get() == true)
            	 {
					if(mCoinPhysics.getY() == 475)
           		 	{
           			 	hasClick.set(false);
           		 	}
            		 updatePhysics(); 
            		 flipCoin();
            	 }
                 drawGameElements(canvas);
             }
          }
          finally
          {
             if (canvas != null) 
                surfaceHolder.unlockCanvasAndPost(canvas);
          }
       }
    }
    
    public void setScreenSize(float width, float height) {  
        mCoinCollisionCheck.set(mCoinRadius, width, height);  
    }  
  
    public void setPosition(float x, float y) {  
        mCoinPhysics.setPosition(x, y);  
    }  
  
    public Bundle saveState(Bundle map) {  
        synchronized (surfaceHolder) {  
            if (map != null) {  
                map = mCoinCollisionCheck.saveState(map);  
                map = mCoinPhysics.saveState(map);  
            }  
        }  
        return map;  
    }  
  
    public synchronized void restoreState(Bundle savedState) {  
        synchronized (surfaceHolder) {  
            threadIsRunning = false;  
            mCoinCollisionCheck.restoreState(savedState);  
            mCoinPhysics.restoreState(savedState);  
            threadIsRunning = true;  
        }  
    }
    protected void drawGameElements(Canvas canvas)
    {  
    	
    	 canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    	 if(coin.getCoinFace() == "Troll Caegar")
    	 {
    		 canvas.drawBitmap(trollCaegar[coin.getResult()].getBitmap(), (float)mCoinPhysics.getX(), (float)mCoinPhysics.getY(), null);   
    	 }
    	 if(coin.getCoinFace() == "George Washington")
    	 {
    		 canvas.drawBitmap(georgeWashington[coin.getResult()].getBitmap(), (float)mCoinPhysics.getX(), (float)mCoinPhysics.getY(), null);   
    	 }
    	 if(coin.getCoinFace() == "Abraham Lincoln")
    	 {
    		 canvas.drawBitmap(abrahamLincoln[coin.getResult()].getBitmap(), (float)mCoinPhysics.getX(), (float)mCoinPhysics.getY(), null);   
    	 }
    	 if(coin.getCoinFace() == "Meme Face")
    	 {
    		 canvas.drawBitmap(memeFace[coin.getResult()].getBitmap(), (float)mCoinPhysics.getX(), (float)mCoinPhysics.getY(), null);   
    	 }
    	 if(coin.getCoinFace() == "Caegar")
    	 {
    		 canvas.drawBitmap(caegar[coin.getResult()].getBitmap(), (float)mCoinPhysics.getX(), (float)mCoinPhysics.getY(), null);   
    	 }
    	 if(coin.getCoinFace() == "ICP")
    	 {
    		 canvas.drawBitmap(icp[coin.getResult()].getBitmap(), (float)mCoinPhysics.getX(), (float)mCoinPhysics.getY(), null);   
    	 }
    	 canvas.restore();
    }
    private void updatePhysics()  
    {  
        mCoinPhysics.update();  
    } 
}
