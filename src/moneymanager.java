import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.awt.event.*;
import java.sql.*;


public class moneymanager{

    public static void main(String[] args){
        try {
        //BACKEND SETUP
            String url = "jdbc:mysql://localhost:3306/expensemanagerjava";
            String username = "root";
            String password = "admin";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement code = con.createStatement();

            try {
                code.execute("CREATE TABLE ACCOUNT(NAME VARCHAR(20) PRIMARY KEY,DEBIT NUMERIC(65,3), CREDIT NUMERIC(65,3),TOTAL NUMERIC(65,3), ACCOUNT_BALANCE NUMERIC(65,3));");
                code.execute("CREATE TABLE DEBIT(AMOUNT NUMERIC(65,3), DATE_OF_TRANSACTION DATE, CATEGORY VARCHAR(25), NOTE VARCHAR(200));");
                code.execute("CREATE TABLE CREDIT(AMOUNT NUMERIC(65,3), DATE_OF_TRANSACTION DATE, CATEGORY VARCHAR(25), NOTE VARCHAR(200));");
                code.execute("CREATE TABLE TRANSFER(AMOUNT NUMERIC(65,3), DATE_OF_TRANSACTION DATE, CATEGORY VARCHAR(25), NOTE VARCHAR(200));");
                code.execute("CREATE TABLE STATEMENTS(DATE_OF_TRANSACTION DATE, AMOUNT NUMERIC(65,3), TYPE_OF_TRANSACTION VARCHAR(11), CATEGORY VARCHAR(25), NOTE VARCHAR(200));");
                code.execute("INSERT INTO ACCOUNT VALUES('Kshiti',0,0,0,0);");
            }catch (SQLException e){
//                JOptionPane.showMessageDialog(null,"");
                System.out.println(e);
            }
            //code.execute("UPDATE ACCOUNT SET DEBIT=0 WHERE NAME='Kshiti'");
            ResultSet accountinfo = code.executeQuery("SELECT * FROM ACCOUNT");
            accountinfo.next();
            /*
            ResultSet debitinfo = code.executeQuery("SELECT * FROM DEBIT");
            ResultSet creditinfo = code.executeQuery("SELECT * FROM CREDIT");
            ResultSet transferinfo = code.executeQuery("SELECT * FROM TRANSFER");

            debitinfo.next();
            creditinfo.next();
            transferinfo.next();

             */

            double accountbalance = accountinfo.getDouble("ACCOUNT_BALANCE");
            double income = accountinfo.getDouble("CREDIT");
            double expense = accountinfo.getDouble("DEBIT");
            double total = accountinfo.getDouble("TOTAL");
            String name = accountinfo.getString("NAME");

            String accountbalancestr = Double.toString(accountbalance);
            String incomestr = Double.toString(income);
            String expensestr = Double.toString(expense);
            String totalstr = Double.toString(total);

            String[] monthlist = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            String[] datelist = new String[31];
            String[] yearlist = new String[10];
            Calendar instance = Calendar.getInstance();
            int currentyear = instance.get(Calendar.YEAR);
            for (int i = 1; i <= 31; i++) {
                datelist[i - 1] = Integer.toString(i);
            }
            for (int i = 0; i < 10; i++) {
                yearlist[i] = Integer.toString(currentyear - i);
            }

            String[] categories = {"Food", "Self Development", "Transportation", "Beauty", "Household", "Health", "Apparel", "Education", "Gift"};

            //STYLING COMPONENTS
            Font H1FONT = new Font("Arial", Font.BOLD, 30);
            //Font H3FONT = new Font("Arial", Font.BOLD, 22);
            //Font H5FONT = new Font("Arial", Font.BOLD, 16);

            //APP
            JFrame app = new JFrame();

            //JDialog message = new JDialog();

            JPanel UserInfo = new JPanel(new GridLayout(3, 2, 0, 20));
            UserInfo.setBounds(9, 190, 670, 200);
            UserInfo.setBackground(Color.BLACK);
            UserInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "HOME"));
            UserInfo.setVisible(false);

            JPanel ViewData = new JPanel(new GridLayout(3, 2, 0, 20));
            ViewData.setBounds(9, 190, 670, 200);
            ViewData.setBackground(Color.BLACK);
            ViewData.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "VIEW DATA"));
            ViewData.setVisible(false);

            JPanel AddTransaction = new JPanel(new GridLayout(6, 2, 0, 20));
            AddTransaction.setBounds(9, 190, 670, 300);
            AddTransaction.setBackground(Color.BLACK);
            AddTransaction.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "ADD TRANSACTION"));
            AddTransaction.setVisible(false);

            JLabel Heading = new JLabel("EXPENSE MANAGER");
            Heading.setBounds(200, 50, 350, 30);
            Heading.setFont(H1FONT);
            Heading.setForeground(Color.WHITE);
            app.add(Heading);

            JButton home_btn = new JButton("Home");
            home_btn.setBounds(30, 100, 200, 40);
            app.add(home_btn);

            JButton view_data_btn = new JButton("View Data");
            view_data_btn.setBounds(220, 100, 200, 40);
            app.add(view_data_btn);

            JButton add_new_btn = new JButton("Add Transaction");
            add_new_btn.setBounds(410, 100, 200, 40);
            add_new_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    UserInfo.setVisible(false);
                    ViewData.setVisible(false);
                    AddTransaction.setVisible(true);
                }
            });
            app.add(add_new_btn);

            //------------- HOME PANEL ------------------
            JLabel user_name_lbl = new JLabel("NAME: ");
            user_name_lbl.setForeground(Color.WHITE);
            UserInfo.add(user_name_lbl);

            JLabel user_name = new JLabel(name);
            user_name.setForeground(Color.WHITE);
            UserInfo.add(user_name);

            JLabel account_balance_lbl = new JLabel("ACCOUNT BALANCE: ");
            account_balance_lbl.setForeground(Color.WHITE);
            UserInfo.add(account_balance_lbl);

            JLabel account_balance = new JLabel(accountbalancestr);
            account_balance.setForeground(Color.WHITE);
            UserInfo.add(account_balance);

            JButton change_info_btn = new JButton("Change Info");
            UserInfo.add(change_info_btn);

            JButton reset_info_btn = new JButton("Reset Info");
            reset_info_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        code.execute("TRUNCATE TABLE ACCOUNT");
                        code.execute("TRUNCATE TABLE CREDIT");
                        code.execute("TRUNCATE TABLE DEBIT");
                        code.execute("TRUNCATE TABLE TRANSFER");
                        code.execute("TRUNCATE TABLE STATEMENTS");
                        code.execute("INSERT INTO ACCOUNT VALUES('Kshiti',0,0,0,0)");
                        JOptionPane.showMessageDialog(null,"Data has been successfully reset");

                    }catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }

                }
            });
            UserInfo.add(reset_info_btn);


            home_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    UserInfo.setVisible(true);
                    ViewData.setVisible(false);
                    AddTransaction.setVisible(false);

                    try{
                        ResultSet accountinfo = code.executeQuery("SELECT * FROM ACCOUNT");
                        accountinfo.next();

                        double accountbalance = accountinfo.getDouble("ACCOUNT_BALANCE");

                        String accountbalancestr = Double.toString(accountbalance);

                        account_balance.setText(accountbalancestr);

                    } catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }
                }
            });

            //-------------------- VIEW DATA PANEL -----------------------
            JLabel IncomeLabel = new JLabel("Current Income: ");
            IncomeLabel.setForeground(Color.WHITE);
            ViewData.add(IncomeLabel);

            JLabel Income = new JLabel(incomestr);
            Income.setForeground(Color.GREEN);
            ViewData.add(Income);

            JLabel ExpenseLabel = new JLabel("Current Expense: ");
            ExpenseLabel.setForeground(Color.WHITE);
            ViewData.add(ExpenseLabel);

            JLabel Expense = new JLabel(expensestr);
            Expense.setForeground(Color.RED);
            ViewData.add(Expense);

            JLabel Totalabel = new JLabel("Total: ");
            Totalabel.setForeground(Color.WHITE);
            ViewData.add(Totalabel);

            JLabel Total = new JLabel(totalstr);
            Total.setForeground(Color.WHITE);
            ViewData.add(Total);

            view_data_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    UserInfo.setVisible(false);
                    ViewData.setVisible(true);
                    AddTransaction.setVisible(false);

                    try{
                        ResultSet accountinfo = code.executeQuery("SELECT * FROM ACCOUNT");
                        accountinfo.next();

                        double income = accountinfo.getDouble("CREDIT");
                        double expense = accountinfo.getDouble("DEBIT");
                        double total = accountinfo.getDouble("TOTAL");

                        String incomestr = Double.toString(income);
                        String expensestr = Double.toString(expense);
                        String totalstr = Double.toString(total);

                        Income.setText(incomestr);
                        Expense.setText(expensestr);
                        Total.setText(totalstr);

                    } catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }

                }
            });

            //--------------- ADD NEW TRANSACTION PANEL ---------------------------
            JLabel Amount = new JLabel("AMOUNT: ");
            Amount.setForeground(Color.WHITE);
            AddTransaction.add(Amount);

            JTextField AmountInput = new JTextField();
            AddTransaction.add(AmountInput);

            JLabel Date = new JLabel("DATE: ");
            Date.setForeground(Color.WHITE);
            AddTransaction.add(Date);

            JPanel DateInput = new JPanel(new GridLayout(1, 3));
            AddTransaction.add(DateInput);

            JComboBox eDate = new JComboBox(datelist);
            //eDate.setBounds(100,240,50,20);
            DateInput.add(eDate);

            JComboBox eMonth = new JComboBox(monthlist);
            //eMonth.setBounds(160,240,120,20);
            DateInput.add(eMonth);

            JComboBox eYear = new JComboBox(yearlist);
            //eYear.setBounds(290,240,90,20);
            DateInput.add(eYear);

            JLabel Category = new JLabel("CATEGORY: ");
            Category.setForeground(Color.WHITE);
            AddTransaction.add(Category);

            JComboBox CategoryInput = new JComboBox(categories);
            AddTransaction.add(CategoryInput);

            JLabel Note = new JLabel("NOTE: ");
            Note.setForeground(Color.WHITE);
            AddTransaction.add(Note);

            JTextField NoteInput = new JTextField();
            AddTransaction.add(NoteInput);

            JButton debit_btn = new JButton("Debit");
            debit_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    double amount = Double.parseDouble(AmountInput.getText());
                    String date = eYear.getSelectedItem().toString() + "-" + (eMonth.getSelectedIndex()+1) + "-" + eDate.getSelectedItem().toString();
                    String category = ""+ CategoryInput.getSelectedItem().toString();
                    String note = NoteInput.getText();
                    try {
                        ResultSet accountinfo = code.executeQuery("SELECT * FROM ACCOUNT");
                        accountinfo.next();
                        double accountbalance = accountinfo.getDouble("ACCOUNT_BALANCE");
                        double expense = accountinfo.getDouble("DEBIT");
                        double total = accountinfo.getDouble("TOTAL");

                        accountbalance -= amount;
                        expense -= amount;
                        total -= amount;

                        code.execute("UPDATE ACCOUNT SET ACCOUNT_BALANCE='"+accountbalance+"', DEBIT='"+expense+"', TOTAL='"+total+"' WHERE NAME='Kshiti';");


                    }catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }

                    try {
                        code.execute("INSERT INTO DEBIT VALUES('"+amount+"','"+date+"','"+category+"','"+note+"');");
                        code.execute("INSERT INTO STATEMENTS VALUES('"+date+"','"+amount+"','Debit','"+category+"','"+note+"');");
                        JOptionPane.showMessageDialog(null,"Data Added Successfully");

                        AmountInput.setText("");
                        eDate.setSelectedIndex(0);
                        eMonth.setSelectedIndex(0);
                        eYear.setSelectedIndex(0);
                        CategoryInput.setSelectedIndex(0);
                        NoteInput.setText("");

                    }  catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }

                }
            });
            AddTransaction.add(debit_btn);

            JButton credit_btn = new JButton("Credit");
            credit_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    double amount = Double.parseDouble(AmountInput.getText());
                    String date = eYear.getSelectedItem().toString() + "-" + (eMonth.getSelectedIndex()+1) + "-" + eDate.getSelectedItem().toString();
                    String category = ""+ CategoryInput.getSelectedItem().toString();
                    String note = NoteInput.getText();

                    try {
                        ResultSet accountinfo = code.executeQuery("SELECT * FROM ACCOUNT");
                        accountinfo.next();
                        double accountbalance = accountinfo.getDouble("ACCOUNT_BALANCE");
                        double income = accountinfo.getDouble("CREDIT");
                        double total = accountinfo.getDouble("TOTAL");

                        accountbalance += amount;
                        income += amount;
                        total += amount;

                        code.execute("UPDATE ACCOUNT SET ACCOUNT_BALANCE='"+accountbalance+"', CREDIT='"+income+"', TOTAL='"+total+"' WHERE NAME='Kshiti'");

                    }catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }

                    try {
                        code.execute("INSERT INTO CREDIT VALUES('"+amount+"','"+date+"','"+category+"','"+note+"');");
                        code.execute("INSERT INTO STATEMENTS VALUES('"+date+"','"+amount+"','Credit','"+category+"','"+note+"');");
                        JOptionPane.showMessageDialog(null,"Data Added Successfully");

                        AmountInput.setText("");
                        eDate.setSelectedIndex(0);
                        eMonth.setSelectedIndex(0);
                        eYear.setSelectedIndex(0);
                        CategoryInput.setSelectedIndex(0);
                        NoteInput.setText("");

                    }  catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }
                }
            });
            AddTransaction.add(credit_btn);

            JButton transfer_btn = new JButton("Transfer");
            transfer_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    double amount = Double.parseDouble(AmountInput.getText());
                    String date = eYear.getSelectedItem().toString() + "-" + (eMonth.getSelectedIndex()+1) + "-" + eDate.getSelectedItem().toString();
                    String category = ""+ CategoryInput.getSelectedItem().toString();
                    String note = NoteInput.getText();

                    try {
                        code.execute("INSERT INTO TRANSFER VALUES('"+amount+"','"+date+"','"+category+"','"+note+"');");
                        code.execute("INSERT INTO STATEMENTS VALUES('"+date+"','"+amount+"','Transfer','"+category+"','"+note+"');");
                        JOptionPane.showMessageDialog(null,"Data Added Successfully");

                        AmountInput.setText("");
                        eDate.setSelectedIndex(0);
                        eMonth.setSelectedIndex(0);
                        eYear.setSelectedIndex(0);
                        CategoryInput.setSelectedIndex(0);
                        NoteInput.setText("");

                    }  catch (SQLException error){
                        JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
                    }
                }
            });
            AddTransaction.add(transfer_btn);


            //PANEL ADDITION
            app.add(UserInfo);
            app.add(ViewData);
            app.add(AddTransaction);

            //APP CONFIGURATION
            app.setLayout(null);
            app.setSize(700, 550);
            app.setVisible(true);
            app.setTitle("EXPENSE MANAGER");
            app.getContentPane().setBackground(Color.BLACK);

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
            System.out.println(e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error Occurred. Try Again");
            System.out.println(e);
        }
    }
}