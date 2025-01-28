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

            if(rollsAffected <= 0) {
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

    public int insertBankStatement(BankStatement bankStatement){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int id = 0;

        try{
            conn = DB.getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO bankmoviment" +
                            "(dateTransition, type, amount)" +
                            "VALUES" +
                            "(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setDate(1,bankStatement.getDate());
            pstmt.setInt(2,bankStatement.getType());
            pstmt.setFloat(3,bankStatement.getAmount());

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected > 0){
                ResultSet rs = pstmt.getGeneratedKeys();
                while (rs.next()){
                    id = rs.getInt(1);
                }
                return id;
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
                            "(BankMoviment_id, Bank_id)" +
                            "VALUES" +
                            "(?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setInt(1, BankStatementId);
            pstmt.setInt(2, BankId);

            int rollsAffected = pstmt.executeUpdate();

            if(rollsAffected <= 0){
                System.out.println("No rown affected!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }
    }

    public BankStatement returnBankStatement(int idBankStatement) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bankmoviment where id = " + idBankStatement);

            while(rs.next()){
                Date date = rs.getDate("dateTransition");
                int type = rs.getInt("type");
                float amount = rs.getFloat("amount");

                BankStatement bankStatement = new BankStatement(idBankStatement, date, type, amount);

                return bankStatement;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return null;

    }

    public ArrayList<Transitions> returnTransitions(int idBank){
        ArrayList<Transitions> transitions = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from transitions where Bank_id = " + idBank);

            while(rs.next()){
                int idBankStatement = rs.getInt("BankMoviment_id");

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
                    " SET balance = ?" +
                    " WHERE" +
                    " (numberAccount = ?)");


            pstmt.setFloat(1,account.getBalance());
            pstmt.setInt(2,account.getNumberAccount());

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected <= 0){
                System.out.println("No rown affected!");
            }
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

    public BankCustomer returnBankCustomerCPF(String cpf) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bankcustomer where cpf = '" + cpf + "'");

            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date birthdayDate = rs.getDate("date");
                String phone = rs.getString("phone");

                BankCustomer bankCustomer = new BankCustomer(id,name,birthdayDate,cpf,phone);

                return bankCustomer;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return null;
    }

    public BankCustomer returnBankCustomer(int id) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bankcustomer where id = '" + id + "'");

            while (rs.next()){
                String name = rs.getString("name");
                Date birthdayDate = rs.getDate("date");
                String cpf = rs.getString("cpf");
                String phone = rs.getString("phone");

                BankCustomer bankCustomer = new BankCustomer(id,name,birthdayDate,cpf,phone);

                return bankCustomer;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return null;
    }

    public Account returnAccount(BankCustomer bankCustomer) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from account where BankCustomer_id = '" + bankCustomer.getId() + "'");

            while (rs.next()){
                int numberAccount = rs.getInt("numberAccount");
                int type = rs.getInt("type");
                String password = rs.getString("password");
                float balance = rs.getFloat("balance");

                Account account = new Account(bankCustomer.getId(),bankCustomer.getName(),bankCustomer.getDate(),bankCustomer.getCpf(),
                        bankCustomer.getPhone(),numberAccount,type,password,balance);

                return account;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return null;
    }

    public Bank returnBankAccount(Account account) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from bank where accountNumber = '" + account.getNumberAccount() + "'");

            while (rs.next()){
                int id = rs.getInt("id");

                Bank bank = new Bank(id,account);
                return bank;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return null;
    }

    public float returnBalance(int idBankCustomer) {
        float balance = 0;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from account where BankCustomer_id = '" + idBankCustomer + "'");

            while (rs.next()){
                balance = rs.getInt("balance");
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return balance;
    }

    public boolean verificationAccount(int numberAccount) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from account where numberAccount = '" + numberAccount + "'");

            if (rs.next()) {
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

    public int returnBankCustomerId(int numberAccount) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DB.getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery("select * from account where numberAccount = '" + numberAccount + "'");

            while (rs.next()) {
                int idBankCustomer = rs.getInt("BankCustomer_id");

                return idBankCustomer;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }

        return 0;
    }



}
