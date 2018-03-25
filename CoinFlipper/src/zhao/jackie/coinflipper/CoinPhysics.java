package zhao.jackie.coinflipper;

import android.os.Bundle;

public class CoinPhysics {
	    double mYspeed;  
	    double mGravity;  
	    double mWind;  
	    double mRadius;  
	    double mFriction;  
	    double mPrecision;  
	    double mX;  
	    double mX_old;  
	    double mY;  
	    double mY_old;  
	    CollisionCheck mCollisionCheck;  
	  
	    public CoinPhysics(double x, double y, CollisionCheck collisionCheck)  
	    {  
	        mYspeed = 0;   
	        mGravity = 0.5;  
	        mX = mX_old = 100;  
	        mY = mY_old = 475;  
	        mCollisionCheck = collisionCheck;  
	    }  
	  
	    void setPosition(float x, float y) {  
	    }  
	  
	    double getX() { return mX; }  
	    double getY() { return mY; }  
	  
	    public void update()  
	    {  
	        double spot_x = 0, spot_y;  

	        mYspeed -= mGravity;  
	        	spot_y = mY;
	            if (mCollisionCheck.check(spot_x, spot_y)) {  
	                mYspeed += 1;
	            }
	            if(mY == 477)
	            {
	            	mYspeed = 0;
	            }
 
	        mY += mYspeed;
	    }  
	  
	    public Bundle saveState(Bundle map) {  
	        map.putDouble("CoinPhysics.mYspeed", Double.valueOf(mYspeed));   
	        map.putDouble("CoinPhysics.mGravity", Double.valueOf(mGravity));  
	        map.putDouble("CoinPhysics.mWind", Double.valueOf(mWind));  
	        map.putDouble("CoinPhysics.mRadius", Double.valueOf(mRadius));  
	        map.putDouble("CoinPhysics.mFriction", Double.valueOf(mFriction));  
	        map.putDouble("CoinPhysics.mPrecision", Double.valueOf(mPrecision));  
	        map.putDouble("CoinPhysics.mX", Double.valueOf(mX));  
	        map.putDouble("CoinPhysics.mX_old", Double.valueOf(mX_old));  
	        map.putDouble("CoinPhysics.mY", Double.valueOf(mY));  
	        map.putDouble("CoinPhysics.mY_old", Double.valueOf(mY_old));  
	        return mCollisionCheck.saveState(map);  
	    }  
	  
	    public synchronized void restoreState(Bundle savedState) {  
	    mYspeed = savedState.getDouble("CoinPhysics.mYSpeed");   
	    mGravity = savedState.getDouble("CoinPhysics.mGravity");  
	    mWind = savedState.getDouble("CoinPhysics.mWind");  
	    mRadius = savedState.getDouble("CoinPhysics.mRadius");  
	    mFriction = savedState.getDouble("CoinPhysics.mFriction");  
	    mPrecision = savedState.getDouble("CoinPhysics.mPrecision");  
	    mX = savedState.getDouble("CoinPhysics.mX");  
	    mX_old = savedState.getDouble("CoinPhysics.mX_old");  
	    mY = savedState.getDouble("CoinPhysics.mY");  
	        mY_old = savedState.getDouble("CoinPhysics.mY_old");  
	        mCollisionCheck.restoreState(savedState);  
	    }  
}
