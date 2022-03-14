package programATM;

public class BankAccount {
	private float balance;
	private String name;
	private int passWd;
	private int acces;
	private int iban;
	
	public BankAccount()
	{
		balance = 0;
		name = "n/a";
	}
	
	public BankAccount(int iban,float balance, String name, int passWd)
	{
		this.iban = iban;
		this.balance = balance;
		this.name = name;
		this.passWd = passWd;
	}
	
	public BankAccount(BankAccount b)
	{
		this.balance = b.balance;
		this.name = b.name;
	}
	
	public int withdraw(float sum)
	{
		if(sum>0)
		{
			if(balance-sum>=0)
			{
				balance-=sum;
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}
	
	public int deposit(float sum)
	{
		if(sum>0)
		{
			balance+=sum;
			return 1;
		}
		else
		{
			return 0;		
		}
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getIban()
	{
		return this.iban;
	}
	
	
	public float getBalance()
	{
		return this.balance;
	}
	public int checkPass(int pass)
	{
		if(this.passWd == pass)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public void setAcces(int i) {
		acces = i;
	}
	public int getAcces()
	{
		return acces;
	}
	
}
