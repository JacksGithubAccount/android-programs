package zhao.jackie.diceroller;

import java.util.Random;

public class Dice 
{
	private int[] side;
	private Random random = new Random();
	private int rolledNumber;
	private int numberSide;
	
	public Dice(int numberSide)
	{
		this.numberSide = numberSide;
		if(numberSide == 100)
		{
			side = new int[10];
			for(int i = 1;i<10; i++)
			{
				side[i] = i;
			}
		}
		else
		{
			side = new int[numberSide];
			for(int i = 1;i<numberSide; i++)
			{
				side[i] = i;
			}
		}
	}
	public int getSideValue(int i)
	{
		return side[i];
	}
	public int getNumberOfSides()
	{
		return numberSide;
	}
	public int getRolledNumber()
	{
		return rolledNumber;
	}
	public int rollDice()
	{
		rolledNumber = random.nextInt(side.length);
		return side[rolledNumber];
	}
}
