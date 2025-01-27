package db;

import classes.Account;
import classes.Bank;
import classes.BankCustomer;

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

    public void insertBankStatement(){

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

}
