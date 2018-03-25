package zhao.jackie.coinflipper;

import android.os.Bundle;  
  
public interface CollisionCheck  
{  
    boolean check(double x, double y);  
    Bundle saveState(Bundle map);  
    void restoreState(Bundle savedState);  
}  