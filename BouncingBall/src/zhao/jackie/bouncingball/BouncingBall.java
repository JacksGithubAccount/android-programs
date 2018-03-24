package zhao.jackie.bouncingball;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;

public class BouncingBall extends Activity 
{
	private BallView ballView;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballmain);
        
        BallView ballView = (BallView) this.findViewById(R.id.ballView); 
    }
    
    @Override
    public void onPause()
    {
       super.onPause(); // call the super method
       ballView.stopGame(); // terminates the game
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	int action = event.getAction();
       
       if (action == MotionEvent.AXIS_X || action == MotionEvent.ACTION_MOVE)
       	{
    	  ballView.alignPaddle(event);
    	}
       return true;
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bouncing_ball, menu);
        return true;
    }
}
