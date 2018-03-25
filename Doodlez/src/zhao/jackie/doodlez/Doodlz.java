package zhao.jackie.doodlez;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Doodlz extends Activity
{
private DoodlzView DoodlzView; 
private SensorManager sensorManager; 
private float acceleration; 
private float currentAcceleration; 
private float lastAcceleration; 
private AtomicBoolean dialogIsVisible = new AtomicBoolean();

private static final int COLOR_MENU_ID = Menu.FIRST;
private static final int WIDTH_MENU_ID = Menu.FIRST + 1;
private static final int ERASE_MENU_ID = Menu.FIRST + 2;
private static final int CLEAR_MENU_ID = Menu.FIRST + 3;
private static final int SAVE_MENU_ID = Menu.FIRST + 4;

private static final int ACCELERATION_THRESHOLD = 15000;

private Dialog currentDialog; 

@Override
protected void onCreate(Bundle savedInstanceState) 
{
   super.onCreate(savedInstanceState);
   setContentView(R.layout.main);

   DoodlzView = (DoodlzView) findViewById(R.id.DoodlzView);
   
   acceleration = 0.00f; 
   currentAcceleration = SensorManager.GRAVITY_EARTH;    
   lastAcceleration = SensorManager.GRAVITY_EARTH;    
  
   enableAccelerometerListening();
} 
@Override
protected void onPause()
{
   super.onPause();
   disableAccelerometerListening(); 
} 
private void enableAccelerometerListening()
{
   sensorManager = 
      (SensorManager) getSystemService(Context.SENSOR_SERVICE);
   sensorManager.registerListener(sensorEventListener, 
      sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
      SensorManager.SENSOR_DELAY_NORMAL);
}
private void disableAccelerometerListening()
{
   if (sensorManager != null) 
   {         
      sensorManager.unregisterListener(
         sensorEventListener, 
         sensorManager.getDefaultSensor(
            SensorManager.SENSOR_ACCELEROMETER));         
      sensorManager = null;     
   }
} 
private SensorEventListener sensorEventListener = 
   new SensorEventListener()
   {
      public void onSensorChanged(SensorEvent event)
      {  
         if (!dialogIsVisible.get())
         {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
   
            lastAcceleration = currentAcceleration;
   
            currentAcceleration = x * x + y * y + z * z;
   
            acceleration = currentAcceleration * 
               (currentAcceleration - lastAcceleration);
   
            if (acceleration > ACCELERATION_THRESHOLD)
            {
               AlertDialog.Builder builder = 
                  new AlertDialog.Builder(Doodlz.this);

               builder.setMessage(R.string.message_erase);
               builder.setCancelable(true);
             
               builder.setPositiveButton(R.string.button_erase,
                  new DialogInterface.OnClickListener() 
                  {
                     public void onClick(DialogInterface dialog, int id) 
                     {
                       dialogIsVisible.set(false);
                       DoodlzView.clear(); 
                     }
                  } 
               ); 
               builder.setNegativeButton(R.string.button_cancel,
                  new DialogInterface.OnClickListener() 
                  {
                     public void onClick(DialogInterface dialog, int id) 
                     {
                       dialogIsVisible.set(false);
                       dialog.cancel();
                     } 
                  } 
               );
   
               dialogIsVisible.set(true); 
               builder.show(); 
            }
         }
      }
      public void onAccuracyChanged(Sensor sensor, int accuracy)
      {
      } 
   };

@Override
public boolean onCreateOptionsMenu(Menu menu) 
{
   super.onCreateOptionsMenu(menu); 
   menu.add(Menu.NONE, COLOR_MENU_ID, Menu.NONE, 
      R.string.menuitem_color);
   menu.add(Menu.NONE, WIDTH_MENU_ID, Menu.NONE, 
      R.string.menuitem_line_width);
   menu.add(Menu.NONE, ERASE_MENU_ID, Menu.NONE, 
      R.string.menuitem_erase);
   menu.add(Menu.NONE, CLEAR_MENU_ID, Menu.NONE, 
      R.string.menuitem_clear);
   menu.add(Menu.NONE, SAVE_MENU_ID, Menu.NONE, 
      R.string.menuitem_save_image);

   return true; 
}

// handle choice from options menu
@Override
public boolean onOptionsItemSelected(MenuItem item) 
{
   switch (item.getItemId()) 
   {
      case COLOR_MENU_ID:
         showColorDialog();
         return true;
      case WIDTH_MENU_ID:
         showLineWidthDialog(); 
         return true; 
      case ERASE_MENU_ID:
         DoodlzView.setDrawingColor(Color.WHITE); 
         return true; 
      case CLEAR_MENU_ID:
         DoodlzView.clear(); 
         return true; 
      case SAVE_MENU_ID:     
         DoodlzView.saveImage();
         return true;
   } 
   
   return super.onOptionsItemSelected(item); 
} 

private void showColorDialog()
{
   currentDialog = new Dialog(this);
   currentDialog.setContentView(R.layout.color_dialog);
   currentDialog.setTitle(R.string.title_color_dialog);
   currentDialog.setCancelable(true);
  
   final SeekBar alphaSeekBar = 
      (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
   final SeekBar redSeekBar = 
      (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
   final SeekBar greenSeekBar = 
      (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
   final SeekBar blueSeekBar = 
      (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

   alphaSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
   redSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
   greenSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
   blueSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
  
   final int color = DoodlzView.getDrawingColor();
   alphaSeekBar.setProgress(Color.alpha(color));
   redSeekBar.setProgress(Color.red(color));
   greenSeekBar.setProgress(Color.green(color));
   blueSeekBar.setProgress(Color.blue(color));        
   
   Button setColorButton = (Button) currentDialog.findViewById(
      R.id.setColorButton);
   setColorButton.setOnClickListener(setColorButtonListener);

   dialogIsVisible.set(true); 
   currentDialog.show();
}
private OnSeekBarChangeListener colorSeekBarChanged = 
  new OnSeekBarChangeListener() 
{
   public void onProgressChanged(SeekBar seekBar, int progress,
      boolean fromUser) 
   {
      SeekBar alphaSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
      SeekBar redSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
      SeekBar greenSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
      SeekBar blueSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);
      View colorView = 
         (View) currentDialog.findViewById(R.id.colorView);

      colorView.setBackgroundColor(Color.argb(
         alphaSeekBar.getProgress(), redSeekBar.getProgress(), 
         greenSeekBar.getProgress(), blueSeekBar.getProgress()));
   }
   public void onStartTrackingTouch(SeekBar seekBar) 
   {
   }
   public void onStopTrackingTouch(SeekBar seekBar) 
   {
   }
};

private OnClickListener setColorButtonListener = new OnClickListener() 
{
   public void onClick(View v) 
   {
      SeekBar alphaSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
      SeekBar redSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
      SeekBar greenSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
      SeekBar blueSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

      DoodlzView.setDrawingColor(Color.argb(
         alphaSeekBar.getProgress(), redSeekBar.getProgress(), 
         greenSeekBar.getProgress(), blueSeekBar.getProgress()));
      dialogIsVisible.set(false);
      currentDialog.dismiss();
      currentDialog = null;
   } 
};

private void showLineWidthDialog()
{
   currentDialog = new Dialog(this);
   currentDialog.setContentView(R.layout.width_dialog);
   currentDialog.setTitle(R.string.title_line_width_dialog);
   currentDialog.setCancelable(true);
   
   SeekBar widthSeekBar = 
      (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
   widthSeekBar.setOnSeekBarChangeListener(widthSeekBarChanged);
   widthSeekBar.setProgress(DoodlzView.getLineWidth()); 
    
   Button setLineWidthButton = 
      (Button) currentDialog.findViewById(R.id.widthDialogDoneButton);
   setLineWidthButton.setOnClickListener(setLineWidthButtonListener);
   
   dialogIsVisible.set(true);
   currentDialog.show();
}

private OnSeekBarChangeListener widthSeekBarChanged = 
   new OnSeekBarChangeListener() 
   {
      Bitmap bitmap = Bitmap.createBitmap( 
         400, 100, Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap); 
      
      public void onProgressChanged(SeekBar seekBar, int progress,
         boolean fromUser) 
      {  
         ImageView widthImageView = (ImageView) 
            currentDialog.findViewById(R.id.widthImageView);

         Paint p = new Paint();
         p.setColor(DoodlzView.getDrawingColor());
         p.setStrokeCap(Paint.Cap.ROUND);
         p.setStrokeWidth(progress);
         
         bitmap.eraseColor(Color.WHITE);
         canvas.drawLine(30, 50, 370, 50, p);
         widthImageView.setImageBitmap(bitmap);
      }
      public void onStartTrackingTouch(SeekBar seekBar) 
      {
      }
      public void onStopTrackingTouch(SeekBar seekBar) 
      {
      }
   };

private OnClickListener setLineWidthButtonListener = 
   new OnClickListener() 
   {
      public void onClick(View v) 
      {
         SeekBar widthSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
         DoodlzView.setLineWidth(widthSeekBar.getProgress());
         dialogIsVisible.set(false);
         currentDialog.dismiss();
         currentDialog = null;
      }
   }; 
}