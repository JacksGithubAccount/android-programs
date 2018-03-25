package zhao.jackie.wordscramble;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import java.util.Random;
import java.util.ArrayList;


public class WordScramble extends Activity {
	private static final String WORD = "WORD";
	private static final String DEFINITION = "DEFINITION";
	private static final String SCORE = "SCORE";
	private int score;
	int wordLenth = 3;
	int index = 0;
	private String word;
	private String definition;
	private TextView textViewWord;
	private TextView textViewDefinition;
	private TextView textViewCorrect;
	private TextView textViewScore;
	private EditText editTextAnswer;
	private String[] threeWords = new String[]{"bat","car","one","two","fix", "but", "map", "sit", "tin", "air"};
	private String[] threeDef = new String [] {"Lives in a cave.","Four wheeled driving vehicle.","singular","one plus one.","to repair.","show doubt","has directions","not stand","symbol is SN","you breathe this"};
	private String[] fourWords = new String[]{"four","core","fire","save","rock","flat","gush","trap","star","desk"};
	private String[] fourDef = new String[]{"three more after one","center","burns stuff","prevents losing progress","hurts when you throw it","no bumps","strong water flow","don't step on it","lots of light","you write on this"};
	private String[] fiveWords = new String[] {"zebra","break","witch","cloud","board","water","write","right","chair","screen"};
	private String[] fiveDef = new String[]{"Striped guy","lunch in the middle of work","female warlock","floats in the sky","a piece of wood","you can drink this","involves letters","when you are not wrong or left","You sit in this","stare into it"};
	private String[] sixWords = new String[]{"pummel","supple","bridge","vortex","gamble","scotch","marker","eraser","sneeze","create"};
	private String[] sixDef = new String[]{"punching alot","mmm","cross it","sucks things in","usually end up with an empty wallet","A type of tape","a signal","end of a pencil","boogers fly everywhere","maken wallpapers"};
	private String[] sevenWords = new String[]{"warrior","fortune","defense","teacher","content","adapter","listless","bulwark","prevail","ceiling"};
	private String[] sevenDef = new String[]{"good guy","luck involves this","Not a prosecutor","guy everyone hates in school","the stuff inside","charges laptops","without a series of items","sturdy defense","make it through","above you when in a room"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(savedInstanceState == null)
        {
        	word = newWord();
        	definition = getDefinition();
        }
        else
        {
        	word = savedInstanceState.getString(WORD);
        	definition = savedInstanceState.getString(DEFINITION);
        	score = savedInstanceState.getInt(SCORE);
        }
        
        editTextAnswer = (EditText) findViewById(R.id.editTextAnswer);
        textViewWord = (TextView) findViewById(R.id.textViewWord);
        textViewDefinition = (TextView) findViewById(R.id.textViewDefinition);
        textViewCorrect = (TextView) findViewById(R.id.textViewCorrect);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        Button submitButton = (Button)findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(submitButtonListener);
        
        textViewWord.setText(scrambler(word));
        textViewDefinition.setText(definition);
    }
    private final int DIFFICULTY_MENU_ID = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
        menu.add(menu.NONE, DIFFICULTY_MENU_ID, menu.NONE, "Number of Letters");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case DIFFICULTY_MENU_ID:
    		final String[] possibleChoices = new String[] {"3","4","5","6","7"};
    		AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
    		choicesBuilder.setTitle("Number of Letters");
    		
    		choicesBuilder.setItems(possibleChoices, 
    				new DialogInterface.OnClickListener() 
    					{
						public void onClick(DialogInterface dialog, int item) 
						{
							wordLenth = Integer.parseInt(possibleChoices[item].toString());	
							word = newWord();
				        	definition = getDefinition();
							textViewWord.setText(scrambler(word));
					        textViewDefinition.setText(definition);
						}
					});
    		AlertDialog choicesDialog = choicesBuilder.create();
    		choicesDialog.show();
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    	super.onSaveInstanceState(outState);
    	
    	outState.putString( WORD, word);
    	outState.putString(DEFINITION, definition);
    	outState.putInt(SCORE, score);
    }
    public OnClickListener submitButtonListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		String answer = editTextAnswer.getText().toString().toLowerCase().trim();
    		//here isn't working
    		if(word == answer)
    		{
    			textViewCorrect.setText("CORRECT!");
    			score++;
    			
    			textViewCorrect.setText("Next Word!");
        		textViewWord.setText(scrambler(newWord()));
        		String scoreStr = Integer.toString(score);
        		textViewScore.setText("Score: "+scoreStr);
    		}
    		else
    		{
    			textViewCorrect.setText(answer + " " + word);
    		}
    	}
    };
    public String scrambler(String word)
    {
    	String Scrambled = "";
    	Random random = new Random();
    	ArrayList<String> wordArray = new ArrayList<String>();
    	for(int i = 0; i<word.length(); i++)
    	{
    		int m = i;
    		wordArray.add(word.substring(i, m+1));
    	}
    	while(!wordArray.isEmpty())
    	{
    		int index = random.nextInt(wordArray.size());
    		Scrambled = Scrambled + wordArray.get(index);
    		wordArray.remove(index);
    	}
    	return Scrambled;
    }
    public String newWord()
    {
    	Random random = new Random();		
		index = random.nextInt(10);
		String word = "";
		
		if(wordLenth == 3)
		{
			word = threeWords[index];
		}
		if(wordLenth == 4)
		{
			word = fourWords[index];
		}
		if(wordLenth == 5)
		{
			word = fiveWords[index];
		}
		if(wordLenth == 6)
		{
			word = sixWords[index];
		}
		if(wordLenth == 7)
		{
			word = sevenWords[index];
		}
		return word;
    }
    public String getDefinition()
    {
    	String definition = "";
    	if(wordLenth == 3)
		{
    		definition = threeDef[index];
		}
		if(wordLenth == 4)
		{
			definition = fourDef[index];
		}
		if(wordLenth == 5)
		{
			definition = fiveDef[index];
		}
		if(wordLenth == 6)
		{
			definition = sixDef[index];
		}
		if(wordLenth == 7)
		{
			definition = sevenDef[index];
		}
		return definition;
    }

}
