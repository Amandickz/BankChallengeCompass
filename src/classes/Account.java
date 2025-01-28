package classes;

import java.sql.Date;

public class Account extends BankCustomer{

    private int numberAccount;
    private int type;
    private String password;
    private float balance;

    public Account(String name, Date date, String cpf, String phone, int numberAccount, int type, String password, float balance) {
        super(name, date, cpf, phone);
        this.numberAccount = numberAccount;
        this.type = type;
        this.password = password;
        this.balance = balance;
    }

    public Account(int id, String name, Date date, String cpf, String phone, int numberAccount, int type, String password, float balance) {
        super(id, name, date, cpf, phone);
        this.numberAccount = numberAccount;
        this.type = type;
        this.password = password;
        this.balance = balance;
    }

    public int getNumberAccount() {
        return numberAccount;
    }

    public int getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public float getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "numberAccount=" + numberAccount +
                ", type=" + type +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }

    public boolean withdraw(float amount) {
        if (amount > balance) {
            return false;
        } else {
            balance -= amount;
        }
        return true;
    }

    public boolean deposit(float amount) {
        balance += amount;
        return true;
    }

    public boolean internalTransfer(Bank client, Bank destination, float amount) {
        client.getAccount().withdraw(amount);
        destination.getAccount().deposit(amount);
        return true;
    }

    public boolean externalTransfer(Bank client, float amount) {
        client.getAccount().withdraw(amount);
        return true;
    }
}
