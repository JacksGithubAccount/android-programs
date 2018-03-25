package zhao.jackie.coinflipper;

import java.util.Random;

public class Coin {
	private int[] side;
	private Random random = new Random();
	private String coinFace;
	private int result;
	
	public Coin(String CoinFace)
	{
		this.coinFace = CoinFace;
		side = new int[2];
		for(int i = 1;i<2; i++)
		{
			side[i] = i;
		}
	}
	public int getResult()
	{
		return result;
	}
	public String getCoinFace()
	{
		return coinFace;
	}
	public int FlipCoin()
	{
		result = random.nextInt(side.length);
		return side[result];
	}
}
