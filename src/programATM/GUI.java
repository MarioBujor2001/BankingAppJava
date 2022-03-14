package programATM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI {
	
	private SQLite conectionToSql;
	
	public GUI()
	{
		conectionToSql = new SQLite();
	}

	public void ATM(BankAccount b)
	{
		
		JFrame frame_atm = new JFrame();
		JPanel panel_atm = new JPanel();
		JTextField input_sum = new JTextField("Suma?");
		input_sum.setColumns(20);
		JLabel labelInfo = new JLabel("Buna ziua "+b.getName()+" aveti:"+b.getBalance()+"$");
		JLabel labelTransaction = new JLabel("");
		JButton button_deposit = new JButton("Deposit");
		JButton button_withdrw = new JButton("Withdraw");
		JButton button_logOut = new JButton("LogOut");
		JButton button_transfer = new JButton("Transfer");
		
		//afisare pe ecran a butoanelor
		panel_atm.add(labelInfo);
		panel_atm.add(input_sum);
		panel_atm.add(button_deposit);
		panel_atm.add(button_withdrw);
		panel_atm.add(button_transfer);
		panel_atm.add(button_logOut);
		panel_atm.add(labelTransaction);	
		
		//buton1
		button_deposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = input_sum.getText();
				float sum = 0;
				try{
					sum = Float.parseFloat(s);
					if(b.deposit(sum)==1)
					{
						labelInfo.setText("Buna ziua "+b.getName()+", aveti:"+b.getBalance()+"$");
						input_sum.setText("");
						labelTransaction.setText("Ati depus:"+sum+"$");
						conectionToSql.setBalanceForUser(b.getIban(), b.getBalance());
					}
					else
					{
						labelTransaction.setText("Suma incorecta!");
						input_sum.setText("");
					}
					}
				catch(NumberFormatException nfe)
				{
					labelTransaction.setText("Format Suma incorect!");
					input_sum.setText("");
				}
				
			}
		});
		
		//buton2
		button_withdrw.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String s = input_sum.getText();
				float sum = 0;
				try{
					sum = Float.parseFloat(s);
					if(b.withdraw(sum)==1)
					{
						labelInfo.setText("Buna ziua "+b.getName()+", aveti:"+b.getBalance()+"$");
						input_sum.setText("");
						labelTransaction.setText("Ati retras:"+sum+"$");
						conectionToSql.setBalanceForUser(b.getIban(), b.getBalance());
					}
					else
					{
						labelTransaction.setText("Sold Insuficient!");
						input_sum.setText("");
					}
					}
				catch(NumberFormatException nfe)
				{
					labelTransaction.setText("Format Suma incorect!");
					input_sum.setText("");
				}
				
			}
		});
		
		//butonul3
		button_logOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame_atm.setVisible(false);
				LogIn("");
			}
		});
		
		//butonul4 
		button_transfer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				transferFrom(b, labelTransaction, labelInfo);
			}
		});
		
		
		panel_atm.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel_atm.setLayout(new GridLayout(0,1));
		
		panel_atm.setBackground(Color.lightGray);
		
		frame_atm.add(panel_atm, BorderLayout.CENTER);
		frame_atm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_atm.setTitle("BankAccount");
		frame_atm.pack();
		frame_atm.setLocationRelativeTo(null);//pozitionare pe mijl ecranului
		frame_atm.setVisible(true);
	}
	
	public void LogIn(String message)
	{
		JFrame L_frame = new JFrame();
		JPanel L_panel = new JPanel();
		JLabel L_label_info = new JLabel(message);
		JLabel L_label_pin = new JLabel("Introduceti PIN:");
		JLabel L_label_name = new JLabel("Introduceti Iban:");
				
		JTextField L_inputName = new JTextField();
		JPasswordField L_pass = new JPasswordField();
		L_pass.setColumns(10);
		JButton L_buttonLogIn = new JButton("LogIn!");
		JButton L_buttonCreate = new JButton("Creare Cont!");
		
		L_panel.add(L_label_name);
		L_panel.add(L_inputName);
		L_panel.add(L_label_pin);
		L_panel.add(L_pass);
		L_panel.add(L_buttonLogIn);
		L_panel.add(L_label_info);
		
		L_buttonLogIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String iban = new String(L_inputName.getText());
				iban = iban.replaceAll(" ","");
				
				boolean result = iban.matches("[0-9]+");
				
				BankAccount contLoad = null;
				if(result == true)
				{
					contLoad = conectionToSql.getUser(iban);//incarc userul intr-un obiect
				}
				if(contLoad!=null)//daca exista userul si e incarcat in obiect
					{//verificare parola
					String pass = new String(L_pass.getPassword());
					int pin = 0;
					try{
						pin = Integer.parseInt(pass);
						if(contLoad.checkPass(pin)==1)
						{
							contLoad.setAcces(1);
							ATM(contLoad);
							L_frame.setVisible(false);
						}
						else
						{
							L_label_info.setText("PIN Gresit!");
							L_pass.setText("");
						}
						}
					catch(NumberFormatException nfe)
					{
						L_label_info.setText("PIN introdus gresit!");
						L_pass.setText("");
					}
					}
				else
				{
					L_label_info.setText("Utilizator Inexistent");
					L_pass.setText("");
					L_inputName.setText("");
					L_panel.add(L_buttonCreate);
					
				}
			}
		});
		
		L_buttonCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createUser();
				L_frame.setVisible(false);
			}
		});
		L_panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		L_panel.setLayout(new GridLayout(0,1));
		L_frame.add(L_panel, BorderLayout.CENTER);
		L_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		L_frame.setTitle("Login");
		L_frame.pack();
		L_frame.setLocationRelativeTo(null);
		L_frame.setVisible(true);
	}
	
	public void createUser()
	{
		JFrame C_frame = new JFrame();
		JPanel C_panel = new JPanel();
		
		JLabel C_labelName = new JLabel("Nume:");
		JLabel C_labelIban = new JLabel("Iban:");
		JLabel C_labelPin = new JLabel("Pin:");
		JLabel C_errLabel = new JLabel("");
		JButton C_arrow = new JButton("<- Back");
		
		JTextField C_inputName = new JTextField(); C_inputName.setColumns(20);
		JTextField C_inputIban = new JTextField(); C_inputIban.setColumns(20);
		JPasswordField C_inputPin = new JPasswordField(); C_inputPin.setColumns(20);
		
		C_arrow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				LogIn("Creare User Abortata");
				C_frame.setVisible(false);
			}
		});
		
		JButton C_buttonCreate = new JButton("Create!");
		C_buttonCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String name = C_inputName.getText();
				String iban = C_inputIban.getText();
				String pin = new String(C_inputPin.getPassword());
				@SuppressWarnings("unused")
				int Iiban = 0;
				@SuppressWarnings("unused")
				int Ipin = 0;
				try {
					Iiban = Integer.parseInt(iban);
					Ipin = Integer.parseInt(pin);
					//System.out.println(Iiban+" "+Ipin+" "+name);
					for(int i=0;i<name.length();i++)
					{
						char c = name.charAt(i);
						if(!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z'))
						{
							throw new NumberFormatException();
						}
					}
					if(pin.length()!=4)
					{
						throw new NumberFormatException();
					}
					BankAccount check = null;
					check = conectionToSql.getUser(iban);
					if(check!=null)
					{
						throw new ClassNotFoundException();
					}
					//dupa linia asta ne-am asigurat ca datele introduse sunt integre
					//C_errLabel.setText("S-a creat Userul!");
					conectionToSql.insertUser(iban,name,pin);
					LogIn("User creat cu succes!");
					C_frame.setVisible(false);
				}
				catch(NumberFormatException ceva)
				{
					//input incorect!
					C_errLabel.setText("Format Pin/Iban/Nume incorect!");
					C_inputPin.setText("");
					C_inputIban.setText("");
					C_inputName.setText("");
				}
				catch(ClassNotFoundException altceva)
				{
					C_errLabel.setText("User exista deja!");
					C_inputPin.setText("");
					C_inputIban.setText("");
					C_inputName.setText("");
				}
				
			}
		});
		
		C_panel.add(C_arrow);
		C_panel.add(C_labelName);
		C_panel.add(C_inputName);
		C_panel.add(C_labelIban);
		C_panel.add(C_inputIban);
		C_panel.add(C_labelPin);
		C_panel.add(C_inputPin);
		C_panel.add(C_buttonCreate);
		C_panel.add(C_errLabel);
		
		
		C_panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		C_panel.setLayout(new GridLayout(0,1));
		C_frame.add(C_panel, BorderLayout.CENTER);
		C_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		C_frame.setTitle("CreateAccount");
		C_frame.pack();
		C_frame.setLocationRelativeTo(null);
		C_frame.setVisible(true);
	}

	public void transferFrom(BankAccount b, JLabel infoTrans, JLabel infoSum)
	{
		JFrame T_frame = new JFrame();
		JPanel T_panel = new JPanel();
		
		JLabel T_iban = new JLabel("Iban:");
		JLabel T_sum = new JLabel("Suma:");
		JLabel T_err = new JLabel("");
		JTextField T_inputIban = new JTextField(""); T_inputIban.setColumns(20);
		JTextField T_inputSum = new JTextField(""); T_inputSum.setColumns(20);
		JButton T_submit = new JButton("Send!");
		
		T_panel.add(T_iban);
		T_panel.add(T_inputIban);
		T_panel.add(T_sum);
		T_panel.add(T_inputSum);
		T_panel.add(T_submit);
		T_panel.add(T_err);
		
		T_submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String sum = T_inputSum.getText();
				String iban = T_inputIban.getText();
				float iSum = 0;
				@SuppressWarnings("unused")
				int iIban = 0;
				try {
					iSum = Float.parseFloat(sum);
					iIban = Integer.parseInt(iban);
					
					BankAccount check = null;
					check = conectionToSql.getUser(iban);
					if(check!=null)
					{
						if(b.withdraw(iSum)==1)
						{
							check.deposit(iSum);//depozitez banii lui
							conectionToSql.setBalanceForUser(check.getIban(), check.getBalance());
							conectionToSql.setBalanceForUser(b.getIban(),b.getBalance());
							infoTrans.setText("Ati transferat:"+sum+"$");
							infoSum.setText("Buna ziua "+b.getName()+", aveti:"+b.getBalance()+"$");
						}
						else
						{
							throw new RuntimeException();
						}
					}
					else
					{
						throw new ClassNotFoundException();
					}
					T_frame.setVisible(false);
				}
				catch(NumberFormatException ceva)
				{
					T_err.setText("Suma/Iban incorecte!");
					T_inputSum.setText("");
					T_inputIban.setText("");
				}
				catch(ClassNotFoundException altceva)
				{
					T_err.setText("Userul cautat nu exista!");
					T_inputSum.setText("");
					T_inputIban.setText("");
				}
				catch(RuntimeException sum_invalid)
				{
					T_err.setText("Sold insuficient!");
					T_inputSum.setText("");
				}
			}
		});
		
		T_panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		T_panel.setLayout(new GridLayout(0,1));
		T_panel.setBackground(Color.lightGray);
		T_frame.add(T_panel, BorderLayout.CENTER);
		T_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		T_frame.setTitle("MoneyTransfer");
		T_frame.pack();
		T_frame.setLocationRelativeTo(null);//pozitionare pe mijl ecranului
		T_frame.setVisible(true);
	}
}
