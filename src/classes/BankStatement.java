package classes;

import java.sql.Date;

public class BankStatement {

    private int id;
    private Date date;
    private int function;
    private float amount;

    public BankStatement(int id, int function, float amount) {
        this.id = id;
        this.function = function;
        this.amount = amountCheck(amount, function);
        this.date = new Date(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getFunction() {
        return function;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "BankStatement{" +
                "id=" + id +
                ", date=" + date +
                ", function=" + function +
                ", amount=" + amount +
                '}';
    }

    protected float amountCheck(float amount, int function) {
        if (function == 2 || function == 3) {
            amount *= -1;
        }
        return amount;
    }
}
