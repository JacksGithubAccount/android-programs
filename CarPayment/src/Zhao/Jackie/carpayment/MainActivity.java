package Zhao.Jackie.carpayment;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String CAR_PRICE = "CAR_PRICE";
	private static final String DOWN_PAYMENT = "DOWN_PAYMENT";
	private static final String INTEREST_RATE = "INTEREST_RATE";
	
	private double carPrice;
	private double downPayment;
	private double interestRate;
	private EditText EditText2yearPayment;
	private EditText EditText3yearPayment;
	private EditText EditText4yearPayment;
	private EditText EditText5yearPayment;
	private EditText editTextCarPrice;
	private EditText editTextDownPayment;
	private EditText editTextInterestRate;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if(savedInstanceState == null)
        {
        	carPrice = 0.0;
        	downPayment = 0.0;
        	interestRate = 0;
        }
        else
        {
        	carPrice = savedInstanceState.getDouble(CAR_PRICE);
        	downPayment = savedInstanceState.getDouble(DOWN_PAYMENT);
        	interestRate = savedInstanceState.getDouble(INTEREST_RATE);
        }
        //references
        EditText2yearPayment = (EditText) findViewById(R.id.EditText2yearPayment);
        EditText3yearPayment = (EditText) findViewById(R.id.EditText3yearPayment);
        EditText4yearPayment = (EditText) findViewById(R.id.EditText4yearPayment);
        EditText5yearPayment = (EditText) findViewById(R.id.editText5yearPayment);
        
        editTextCarPrice = (EditText) findViewById(R.id.editTextCarPrice);
        editTextCarPrice.addTextChangedListener(editTextCarPriceWatcher);
        
        editTextDownPayment = (EditText) findViewById(R.id.editTextDownPayment);
        editTextDownPayment.addTextChangedListener(editTextDownPaymentWatcher);
        
        editTextInterestRate = (EditText) findViewById(R.id.editTextInterstRate);
        editTextInterestRate.addTextChangedListener(editTextInterestRateWatcher);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    //updates
    private void updateStandard()
    {
    	double twoYearsMonthPayment = (carPrice - downPayment) / 24;
    	double twoPaymentwInterest = twoYearsMonthPayment + (twoYearsMonthPayment * (interestRate *.01));
    	double threeYearsMonthPayment = (carPrice - downPayment) / 36;
    	double threePaymentwInterest = threeYearsMonthPayment + (threeYearsMonthPayment * (interestRate * .01));
    	double fourYearsMonthPayment = (carPrice - downPayment) / 48;
    	double fourPaymentwInterest = fourYearsMonthPayment + (fourYearsMonthPayment * (interestRate * .01));
    	double fiveYearsMonthPayment = (carPrice - downPayment) / 60;
    	double fivePaymentwInterest = fiveYearsMonthPayment + (fiveYearsMonthPayment * (interestRate * .01));
    	
    	EditText2yearPayment.setText(String.format("%.02f",twoPaymentwInterest));
    	EditText3yearPayment.setText(String.format("%.02f",threePaymentwInterest));
    	EditText4yearPayment.setText(String.format("%.02f",fourPaymentwInterest));
    	EditText5yearPayment.setText(String.format("%.02f",fivePaymentwInterest));
    	
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    	super.onSaveInstanceState(outState);
    	
    	outState.putDouble(CAR_PRICE, carPrice);
    	outState.putDouble(DOWN_PAYMENT, downPayment);
    	outState.putDouble(INTEREST_RATE, interestRate);
    }
    
    //watcher
    private TextWatcher editTextCarPriceWatcher = new TextWatcher()
    {
    	public void onTextChanged(CharSequence s, int start, int before, int count)
    	{
    		try
    		{
    			carPrice = Double.parseDouble(s.toString());
    		}
    		catch(NumberFormatException e)
    		{
    			carPrice = 0.0;
    		}
    		updateStandard();
    	}
    	public void afterTextChanged(Editable s)
    	{
    		
    	}
    	public void beforeTextChanged(CharSequence s, int start, int count, int after)
    	{
    		
    	}
    };
    private TextWatcher editTextDownPaymentWatcher = new TextWatcher()
    {
    	public void onTextChanged(CharSequence s, int start, int before, int count)
    	{
    		try
    		{
    			downPayment = Double.parseDouble(s.toString());
    		}
    		catch(NumberFormatException e)
    		{
    			downPayment = 0.0;
    		}
    		updateStandard();
    	}
    	public void afterTextChanged(Editable s)
    	{
    		
    	}
    	public void beforeTextChanged(CharSequence s, int start, int count, int after)
    	{
    		
    	}
    };
    private TextWatcher editTextInterestRateWatcher = new TextWatcher()
    {
    	public void onTextChanged(CharSequence s, int start, int before, int count)
    	{
    		try
    		{
    			interestRate = Double.parseDouble(s.toString());
    		}
    		catch(NumberFormatException e)
    		{
    			interestRate = 0.0;
    		}
    		updateStandard();
    	}
    	public void afterTextChanged(Editable s)
    	{
    		
    	}
    	public void beforeTextChanged(CharSequence s, int start, int count, int after)
    	{
    		
    	}
    };
}
