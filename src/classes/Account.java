package classes;

import java.sql.Date;

public class Account extends BankCustomer{

    private int id;
    private int numberAccount;
    private char type;
    private float balance;

    public Account(int idCustomer, String name, Date date, String cpf, String email, int id, int numberAccount, char type, float balance) {
        super(idCustomer, name, date, cpf, email);
        this.id = id;
        this.numberAccount = numberAccount;
        this.type = type;
        this.balance = balance;
    }

    public Account(int idCustomer, String name, Date date, String cpf, String email, int numberAccount, char type, float balance) {
        super(idCustomer, name, date, cpf, email);
        this.numberAccount = numberAccount;
        this.type = type;
        this.balance = balance;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getNumberAccount() {
        return numberAccount;
    }

    public char getType() {
        return type;
    }

    public float getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", numberAccount=" + numberAccount +
                ", type=" + type +
                ", balance=" + balance +
                '}';
    }
}
