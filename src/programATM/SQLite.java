package programATM;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLite {
	
	public SQLite()
	{
		
	}
	
	public void connect()
	{
		String jdbcUrl = "jdbc:sqlite:/Users/mariobujor/Documents/sem2/Java/Seminar/Bancomat/utilizatori.db";
	
		try {
		Connection connection = DriverManager.getConnection(jdbcUrl);
		
		String sql = "select * from utilizatori;";
		
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		
		while(result.next())
		{
			String iban = result.getString("iban");
			String name = result.getString("name");
			String balance = result.getString("balance");
			String pin = result.getString("pin");
			
			System.out.println(iban+"|"+name+"|"+balance+"|"+pin);
		}
		connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public BankAccount getUser(String iban)
	{
		String jdbcUrl = "jdbc:sqlite:/Users/mariobujor/Documents/sem2/Java/Seminar/Bancomat/utilizatori.db";
		BankAccount contRet = null;
		try {
		Connection connection = DriverManager.getConnection(jdbcUrl);
		
		String sql = "select * from utilizatori where iban="+iban+";";
		
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		
		if(result.next())
		{
			//exista userul
			contRet = new BankAccount(Integer.parseInt(result.getString("iban")),
					Float.parseFloat(result.getString("balance")),
					result.getString("name"),
					Integer.parseInt(result.getString("pin")));
		}
		connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return contRet;
	}
	
	public void setBalanceForUser(int iban, float newBalance)
	{
		String jdbcUrl = "jdbc:sqlite:/Users/mariobujor/Documents/sem2/Java/Seminar/Bancomat/utilizatori.db";
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl);
			BankAccount contRet = null;
			contRet = getUser(Integer.toString(iban));
			if(contRet!=null)
			{
				String sql = new String("update utilizatori set balance="+newBalance
						+" where iban="+iban+";");
				Statement statement = connection.createStatement();
					@SuppressWarnings("unused")
				boolean result = statement.execute(sql);
				System.out.println("update complete");
			}
			else
			{
				System.out.println("user nu exista");
			}
			connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertUser(String iban,String name,String pin)
	{
		String jdbcUrl = "jdbc:sqlite:/Users/mariobujor/Documents/sem2/Java/Seminar/Bancomat/utilizatori.db";
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl);
			Statement statement = connection.createStatement();
			String sql = new String("insert into utilizatori(iban,name,balance,pin) values("+iban+",'"
					+name+"',0,"+pin+");");
			@SuppressWarnings("unused")
			boolean result = statement.execute(sql);
			connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
		
		
}
