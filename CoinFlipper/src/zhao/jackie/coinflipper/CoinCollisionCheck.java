package zhao.jackie.coinflipper;

import android.os.Bundle;  
  
public class CoinCollisionCheck implements CollisionCheck  
{  
    double mCoinRadius;  
    double mScreenWidth;  
    double mScreenHeight;  
  
    CoinCollisionCheck(double CoinRadius, double screenWidth, double screenHeight)  
    {  
        set(CoinRadius, screenWidth, screenHeight);  
    }  
  
    void set(double CoinRadius, double screenWidth, double screenHeight)  
    {  
        mCoinRadius = CoinRadius;  
        mScreenWidth = screenWidth;  
        mScreenHeight = screenHeight;  
    }  
  
    public boolean check(double x, double y)  
    {  
        if (y - mCoinRadius < 250 || y + mCoinRadius >= mScreenHeight -75)  
            return true;  
        return false;  
    }  
  
    public synchronized Bundle saveState(Bundle map)  
    {  
    map.putDouble("CoinCollisionCheck.mCoinRadius", Double.valueOf(mCoinRadius));  
        map.putDouble("CoinCollisionCheck.mScreenWidth", Double.valueOf(mScreenWidth));  
        map.putDouble("CoinCollisionCheck.mScreenHeight", Double.valueOf(mScreenHeight));  
        return map;  
    }  
  
    public synchronized void restoreState(Bundle savedState)  
    {  
        mCoinRadius = savedState.getDouble("CoinCollisionCheck.mCoinRadius");  
        mScreenWidth = savedState.getDouble("CoinCollisionCheck.mScreenWidth");  
        mScreenHeight = savedState.getDouble("CoinCollisionCheck.mScreenHeight");  
    }  
}  