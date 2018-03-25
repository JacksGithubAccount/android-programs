package zhao.jackie.diceroller;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;

public class DiceThread extends Thread
{
	BitmapDrawable[] d4;
	BitmapDrawable[] d6;
	BitmapDrawable[] d8;
	BitmapDrawable[] d10;
	BitmapDrawable[] d12;
	BitmapDrawable[] d100;
	private SurfaceHolder surfaceHolder;
	private Handler handler;
	private Context context;
	private final int FRAME_RATE = 30; 
	long mLastTime;  
	private Paint backgroundPaint;
	private boolean threadIsRunning = true;
    DiceCollisionCheck mDiceCollisionCheck;  
    DicePhysics mDicePhysics;  
    DicePhysics mDicePhysics2;
    static final double mDiceRadius = 5.0;
    Dice dice;
    Dice dice2 = new Dice(10);
	
    public DiceThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
    	this.surfaceHolder = surfaceHolder;
    	this.context = context;
    	this.handler = handler;
    	
    	setName("diceThread");
    	
    	backgroundPaint = new Paint();
    	backgroundPaint.setColor(Color.GRAY);
    	
    	d4 = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.d4_1), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d4_2), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d4_3),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d4_4)};
    	d6 = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_1), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_2), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_3),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_4), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_5),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_6)};
    	d8 = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_1), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_2), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_3),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_4), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_5),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_6), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_7),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d8_8)};
    	d10 = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_1), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_2), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_3),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_4), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_5),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_6), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_7),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_8), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_9),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d10_10)};
    	d12 = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_1), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_2), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_3),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_4), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_5),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_6), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_7),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_8), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_9),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_10), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_11),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d12_12)};
    	d100 = new BitmapDrawable[] {(BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_10), 
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_20), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_30),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_40), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_50),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_60), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_70),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_80), (BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_90),
    			(BitmapDrawable) context.getResources().getDrawable(R.drawable.d100_00)};
    	
    	mDiceCollisionCheck = new DiceCollisionCheck(mDiceRadius, 0, 0);  
        mDicePhysics = new DicePhysics(50, 50, mDiceCollisionCheck);  
  
        mLastTime = 0; 
    }
    public void setDice(Dice dice)
    {
    	this.dice = dice;
    	rollDice();
        if(dice.getNumberOfSides()== 100)
        {
        	mDicePhysics2 = new DicePhysics(50, 50, mDiceCollisionCheck);
        }
    }
    public void rollDice()
    {
    	dice.rollDice();
    	dice2.rollDice();
    }
   // public void setBitmapImage(BitmapDrawable diceBitmap)
   // {
   // 	this.diceBitmap = diceBitmap;
   // }
	//public DiceThread(SurfaceHolder holder)
    //{
    //   surfaceHolder = holder;
    //   setName("diceThread");
       
    //   backgroundPaint = new Paint();
   	//backgroundPaint.setColor(Color.GRAY);
    //}
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
            	 updatePhysics();
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
        mDiceCollisionCheck.set(mDiceRadius, width, height);  
    }  
  
    public void setPosition(float x, float y) {  
        mDicePhysics.setPosition(x, y);  
        if(dice.getNumberOfSides()== 100)
        {
        	mDicePhysics2.setPosition(x,y);
        }
    }  
  
    public Bundle saveState(Bundle map) {  
        synchronized (surfaceHolder) {  
            if (map != null) {  
                map = mDiceCollisionCheck.saveState(map);  
                map = mDicePhysics.saveState(map);  
                if(dice.getNumberOfSides()== 100)
                {
                	mDicePhysics2.saveState(map);
                }
            }  
        }  
        return map;  
    }  
  
    public synchronized void restoreState(Bundle savedState) {  
        synchronized (surfaceHolder) {  
            threadIsRunning = false;  
            mDiceCollisionCheck.restoreState(savedState);  
            mDicePhysics.restoreState(savedState);  
            if(dice.getNumberOfSides()== 100)
            {
            	mDicePhysics2.restoreState(savedState);
            }
            threadIsRunning = true;  
        }  
    }
    protected void drawGameElements(Canvas canvas) 
    {  
    	
    	 canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    	 //BitmapDrawable diceBitmap = (BitmapDrawable) context.getResources().getDrawable(R.drawable.d6_1);  
    	 if(dice.getNumberOfSides() == 4)
    	 {
    		 canvas.drawBitmap(d4[dice.getRolledNumber()].getBitmap(), (float)mDicePhysics.getX(), (float)mDicePhysics.getY(), null);   
    	 }
    	 if(dice.getNumberOfSides() == 6)
    	 {
    		 canvas.drawBitmap(d6[dice.getRolledNumber()].getBitmap(), (float)mDicePhysics.getX(), (float)mDicePhysics.getY(), null);   
    	 }
    	 if(dice.getNumberOfSides() == 8)
    	 {
    		 canvas.drawBitmap(d8[dice.getRolledNumber()].getBitmap(), (float)mDicePhysics.getX(), (float)mDicePhysics.getY(), null);   
    	 }
    	 if(dice.getNumberOfSides() == 10)
    	 {
    		 canvas.drawBitmap(d10[dice.getRolledNumber()].getBitmap(), (float)mDicePhysics.getX(), (float)mDicePhysics.getY(), null);  
    	 }
    	 if
    	 (dice.getNumberOfSides() == 12)
    	 {
    		 canvas.drawBitmap(d12[dice.getRolledNumber()].getBitmap(), (float)mDicePhysics.getX(), (float)mDicePhysics.getY(), null);   
    	 }
    	 if(dice.getNumberOfSides() == 100)
    	 {
    		 canvas.drawBitmap(d10[dice2.getRolledNumber()].getBitmap(), (float)mDicePhysics2.getX(), (float)mDicePhysics2.getY(), null);
    		 canvas.drawBitmap(d100[dice.getRolledNumber()].getBitmap(), (float)mDicePhysics.getX(), (float)mDicePhysics.getY(), null);
    	 }
    	 canvas.restore();
    }
    private void updatePhysics()  
    {  
        mDicePhysics.update();  
        if(dice.getNumberOfSides()== 100)
        {
        	mDicePhysics2.update();
        }
    } 
}
