package classes;

import java.sql.Date;

public class Bank {

    private int id;
    private static final int agency = 1234;
    Account account;

    public Bank(int id, Account account) {
        this.id = id;
        this.account = account;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", agency=" + agency +
                ", account=" + account +
                '}';
    }
}
