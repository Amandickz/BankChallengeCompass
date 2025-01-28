package db;

import classes.*;

import java.sql.*;
import java.util.ArrayList;

public class DBManipulation {

    public int insertBankCustomer(BankCustomer bankCustomer) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int id = 0;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO bankcustomer" +
                            "(name, date, cpf, phone)" +
                            "VALUES" +
                            "(?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setString(1, bankCustomer.getName());
            pstmt.setDate(2, bankCustomer.getDate());
            pstmt.setString(3, bankCustomer.getCpf());
            pstmt.setString(4, bankCustomer.getPhone());

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected > 0){
                ResultSet rs = pstmt.getGeneratedKeys();
                while (rs.next()){
                    id = rs.getInt(1);
                    System.out.println("Done! ID = " + id);
                }
            } else {
                System.out.println("No rown affected!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }

        return id;
    }

    public void insertAccount(Account account, int idBankCustomer) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO account" +
                            "(numberAccount, type, password, balance, BankCustomer_id)" +
                            "VALUES" +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setInt(1, account.getNumberAccount());
            pstmt.setInt(2, account.getType());
            pstmt.setString(3, account.getPassword());
            pstmt.setFloat(4, account.getBalance());
            pstmt.setInt(5, idBankCustomer);

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected > 0){
                System.out.println("Done! Rolls Affected = " + rollsAffected);
            } else {
                System.out.println("No rown affected!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }

    }

    public int insertBank(Account account){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int id = 0;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO bank" +
                            "(accountNumber)" +
                            "VALUES" +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setInt(1, account.getNumberAccount());

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected > 0){
                ResultSet rs = pstmt.getGeneratedKeys();
                while (rs.next()){
                    id = rs.getInt(1);
                    System.out.println("Done! ID = " + id);
                }
            } else {
                System.out.println("No rown affected!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }

        return id;
    }

    public int insertBankStatement(BankStatement bankStatement, int idBank){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int id = 0;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO bankstatement" +
                            "(date, fuction, amount)" +
                            "VALUES" +
                            "(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setDate(1, bankStatement.getDate());
            pstmt.setInt(2, bankStatement.getFunction());
            pstmt.setFloat(3, bankStatement.getAmount());

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected > 0){
                ResultSet rs = pstmt.getGeneratedKeys();
                while (rs.next()){
                    id = rs.getInt(1);
                    System.out.println("Done! ID = " + id);
                }

                pstmt = conn.prepareStatement(
                        "INSERT INTO transitions" +
                                "(Bank_id, BankStatement_id)" +
                                "VALUES" +
                                "(?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );

                pstmt.setInt(1, idBank);
                pstmt.setInt(2, id);

                rollsAffected = pstmt.executeUpdate();

            } else {
                System.out.println("No rown affected!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }

        return id;
    }

    public void insertTransaction(int BankId, int BankStatementId){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int id = 0;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO transitions" +
                            "(Bank_id, BankStatement_id)" +
                            "VALUES" +
                            "(?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setInt(1, BankId);
            pstmt.setInt(2, BankStatementId);

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected > 0){
                System.out.println("Done! Rolls Affected = " + rollsAffected);
            } else {
                System.out.println("No rown affected!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }
    }

    public ArrayList<BankCustomer> returnBankCustomer() {
        ArrayList<BankCustomer> bankCustomers = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bankcustomer");

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date date = rs.getDate("date");
                String cpf = rs.getString("cpf");
                String phone = rs.getString("phone");

                bankCustomers.add(new BankCustomer(id, name, date, cpf, phone));
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return bankCustomers;

    }

    public ArrayList<Account> returnAccount(ArrayList<BankCustomer> bankCustomers) {
        ArrayList<Account> accounts = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from account");

            while(rs.next()){
                int numberAccount = rs.getInt("numberAccount");
                int type = rs.getInt("type");
                String password = rs.getString("password");
                float balance = rs.getFloat("balance");
                int bankCustomerId = rs.getInt("BankCustomer_id");

                for(BankCustomer bankCustomer : bankCustomers){
                    if(bankCustomer.getId() == bankCustomerId){
                        Account account = new Account(bankCustomer.getName(), bankCustomer.getDate(), bankCustomer.getCpf(),
                                bankCustomer.getPhone(), numberAccount, type, password, balance);
                        accounts.add(account);
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return accounts;
    }

    public ArrayList<Bank> returnBank(ArrayList<Account> accounts) {
        ArrayList<Bank> banks = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bank");

            while(rs.next()){
                int id = rs.getInt("id");
                int numberAccount = rs.getInt("numberAccount");

                for(Account account : accounts){
                    if(account.getNumberAccount() == numberAccount){
                        banks.add(new Bank(id, account));
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return banks;
    }

    public ArrayList<BankStatement> returnBankStatement(BankStatement bankStatement) {
        ArrayList<BankStatement> bankStatements = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bankstatement");

            while(rs.next()){
                int id = rs.getInt("id");
                Date date = rs.getDate("date");
                int function = rs.getInt("function");
                float amount = rs.getFloat("amount");

                bankStatements.add(new BankStatement(id,date,function,amount));
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return bankStatements;

    }

    public ArrayList<Transitions> returnTransitions(){
        ArrayList<Transitions> transitions = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from transitions");

            while(rs.next()){
                int idBank = rs.getInt("Bank_id");
                int idBankStatement = rs.getInt("BankStatement_id");

                transitions.add(new Transitions(idBank,idBankStatement));
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return transitions;
    }

    public void alterBalanceAccount(Account account) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement("UPDATE account" +
                    " SET banlance = ?" +
                    " WHERE" +
                    " (numberAccount = ?)");


            pstmt.setFloat(1,account.getBalance());
            pstmt.setInt(2,account.getNumberAccount());

            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Done! Rows affected: " + rowsAffected);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }
    }

    public boolean foundCPF(String cpf) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bankcustomer where cpf = '" + cpf + "'");

            if (rs != null){
                return true;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return false;
    }

}
