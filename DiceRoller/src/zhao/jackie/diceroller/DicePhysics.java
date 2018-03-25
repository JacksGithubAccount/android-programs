package zhao.jackie.diceroller;

import android.os.Bundle;

public class DicePhysics {
	    double mYspeed;  
	    double mXspeed;  
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
	  
	    public DicePhysics(double x, double y, CollisionCheck collisionCheck)  
	    {  
	        mYspeed = 0;  
	        mXspeed = 0;  
	        mGravity = 0.5;  
	        mWind = -0.1;  
	        mRadius = 5;  
	        mFriction = 0.8;  
	        mPrecision = 1000.0;  
	        mX = mX_old = 25;  
	        mY = mY_old = 25;  
	        mCollisionCheck = collisionCheck;  
	    }  
	  
	    void setPosition(float x, float y) {  
	        mX = mX_old = (double)x;  
	        mY = mY_old = (double)y;  
	    }  
	  
	    double getX() { return mX; }  
	    double getY() { return mY; }  
	  
	    public void update()  
	    {  
	        double spot_x, spot_y;  
	        double collisions = 0;  
	        double sum_x = 0;  
	        double sum_y = 0;  
	        mXspeed += mWind;  
	        mYspeed += mGravity;  
	        for (double i = 0.0; i < mPrecision; ++i) {  
	            spot_x = mX + mRadius * Math.sin((i / mPrecision) * 2 * Math.PI);  
	            spot_y = mY - mRadius * Math.cos((i / mPrecision) * 2 * Math.PI);  
	            if (mCollisionCheck.check(spot_x, spot_y)) {  
	                collisions += 1;  
	                sum_x += spot_x;  
	                sum_y += spot_y;  
	            }  
	        }  
	        if (collisions > 0) {  
	            double Dice_dir = 0;  
	            if (mXspeed != 0)  
	                Dice_dir = Math.atan(mYspeed / -mXspeed);  
	            if (-mXspeed < 0)  
	                Dice_dir += Math.PI;  
	            if (-mXspeed >= 0 && mYspeed < 0)  
	                Dice_dir += 2 * Math.PI;  
	            spot_x = sum_x / collisions;  
	            spot_y = sum_y / collisions;  
	            double x_cat = spot_x - mX;  
	            double y_cat = spot_y - mY;  
	            double Dice_coll = 0;  
	            if (x_cat != 0)  
	                Dice_coll = Math.atan(y_cat / x_cat);  
	            if (x_cat < 0)  
	                Dice_coll += Math.PI;  
	            if (x_cat >= 0 && y_cat < 0)  
	                Dice_coll += 2 * Math.PI;  
	            double ground_rotation = Dice_coll - (Math.PI / 2);  
	            if (ground_rotation < 0)  
	                ground_rotation += Math.PI;  
	            double bounce_angle = Math.PI - Dice_dir - 2 * (ground_rotation);  
	            if (bounce_angle < 0)  
	                bounce_angle += 2 * Math.PI;  
	            double speed = Math.sqrt((mYspeed * mYspeed)+(mXspeed * mXspeed));  
	            mXspeed = speed * Math.cos(bounce_angle) * mFriction ;  
	            mYspeed = speed * Math.sin(bounce_angle) * -mFriction ;  
	            mX = mX_old;  
	            mY = mY_old;  
	        }
	        else 
	        {  
	            mX_old = mX;  
	            mY_old = mY;  
	        }  
	        mY += mYspeed;  
	        mX += mXspeed;  
	    }  
	  
	    public Bundle saveState(Bundle map) {  
	        map.putDouble("DicePhysics.mYspeed", Double.valueOf(mYspeed));  
	        map.putDouble("DicePhysics.mXspeed", Double.valueOf(mXspeed));  
	        map.putDouble("DicePhysics.mGravity", Double.valueOf(mGravity));  
	        map.putDouble("DicePhysics.mWind", Double.valueOf(mWind));  
	        map.putDouble("DicePhysics.mRadius", Double.valueOf(mRadius));  
	        map.putDouble("DicePhysics.mFriction", Double.valueOf(mFriction));  
	        map.putDouble("DicePhysics.mPrecision", Double.valueOf(mPrecision));  
	        map.putDouble("DicePhysics.mX", Double.valueOf(mX));  
	        map.putDouble("DicePhysics.mX_old", Double.valueOf(mX_old));  
	        map.putDouble("DicePhysics.mY", Double.valueOf(mY));  
	        map.putDouble("DicePhysics.mY_old", Double.valueOf(mY_old));  
	        return mCollisionCheck.saveState(map);  
	    }  
	  
	    public synchronized void restoreState(Bundle savedState) {  
	    mYspeed = savedState.getDouble("DicePhysics.mYSpeed");  
	    mXspeed = savedState.getDouble("DicePhysics.mXSpeed");  
	    mGravity = savedState.getDouble("DicePhysics.mGravity");  
	    mWind = savedState.getDouble("DicePhysics.mWind");  
	    mRadius = savedState.getDouble("DicePhysics.mRadius");  
	    mFriction = savedState.getDouble("DicePhysics.mFriction");  
	    mPrecision = savedState.getDouble("DicePhysics.mPrecision");  
	    mX = savedState.getDouble("DicePhysics.mX");  
	    mX_old = savedState.getDouble("DicePhysics.mX_old");  
	    mY = savedState.getDouble("DicePhysics.mY");  
	        mY_old = savedState.getDouble("DicePhysics.mY_old");  
	        mCollisionCheck.restoreState(savedState);  
	    }  
}
