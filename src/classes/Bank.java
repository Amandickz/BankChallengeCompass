package classes;

import java.util.ArrayList;

public class Bank {

    private int id;
    private static final int agency = 1234;
    Account account;
    ArrayList<BankStatement> bankStatement = new ArrayList<>();

    public Bank(int id, Account account) {
        this.id = id;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public int getAgency() {
        return agency;
    }

    public Account getAccount() {
        return account;
    }

    public ArrayList<BankStatement> getBankStatement() {
        return bankStatement;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", agency=" + agency +
                ", account=" + account +
                '}';
    }

    public boolean newBankStatement(int function, float amount) {
        int id;
        if (bankStatement.isEmpty()){
            id = 0;
        } else {
            id = bankStatement.get(bankStatement.size() - 1).getId() + 1;
        }

        bankStatement.add(new BankStatement(id,function,amount));

        return true;
    }
}
