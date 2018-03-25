package zhao.jackie.diceroller;

import android.os.Bundle;  
  
public class DiceCollisionCheck implements CollisionCheck  
{  
    double mDiceRadius;  
    double mScreenWidth;  
    double mScreenHeight;  
  
    DiceCollisionCheck(double DiceRadius, double screenWidth, double screenHeight)  
    {  
        set(DiceRadius, screenWidth, screenHeight);  
    }  
  
    void set(double DiceRadius, double screenWidth, double screenHeight)  
    {  
        mDiceRadius = DiceRadius;  
        mScreenWidth = screenWidth;  
        mScreenHeight = screenHeight;  
    }  
  
    public boolean check(double x, double y)  
    {  
        if (x - mDiceRadius < 0 || x + mDiceRadius >= mScreenWidth -75 ||  
            y - mDiceRadius < 0 || y + mDiceRadius >= mScreenHeight -75)  
            return true;  
        return false;  
    }  
  
    public synchronized Bundle saveState(Bundle map)  
    {  
    map.putDouble("DiceCollisionCheck.mDiceRadius", Double.valueOf(mDiceRadius));  
        map.putDouble("DiceCollisionCheck.mScreenWidth", Double.valueOf(mScreenWidth));  
        map.putDouble("DiceCollisionCheck.mScreenHeight", Double.valueOf(mScreenHeight));  
        return map;  
    }  
  
    public synchronized void restoreState(Bundle savedState)  
    {  
        mDiceRadius = savedState.getDouble("DiceCollisionCheck.mDiceRadius");  
        mScreenWidth = savedState.getDouble("DiceCollisionCheck.mScreenWidth");  
        mScreenHeight = savedState.getDouble("DiceCollisionCheck.mScreenHeight");  
    }  
}  