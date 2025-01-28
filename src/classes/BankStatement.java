package classes;

import java.sql.Date;
import java.text.SimpleDateFormat;

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

    public BankStatement(int id, Date date, int function, float amount) {
        this.id = id;
        this.date = date;
        this.function = function;
        this.amount = amount;
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

    protected String dataConvertida(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

}

/*tipos de padrão de function:
* 1 - Depósito
* 2 - Saque
* 3 - Tranferência - Cliente enviou
* 4 - Tranferência - Cliente recebidor (cliente do banco)*/
