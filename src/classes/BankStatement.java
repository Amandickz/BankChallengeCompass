package classes;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class BankStatement {

    private int id;
    private Date date;
    private int type;
    private float amount;

    public BankStatement(int type, float amount) {
        this.type = type;
        this.amount = amount;
        this.date = new Date(System.currentTimeMillis());
    }

    public BankStatement(int id, int type, float amount) {
        this.id = id;
        this.type = type;
        this.amount = amountCheck(amount, type);
        this.date = new Date(System.currentTimeMillis());
    }

    public BankStatement(int id, Date date, int type, float amount) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "BankStatement{" +
                "id=" + id +
                ", date=" + date +
                ", function=" + type +
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

    public String extract(BankStatement bankStatement) {
        String extract = "";

        if(bankStatement.getType() == 1){
            extract = "Date: " + bankStatement.dataConvertida(bankStatement.getDate())
            + " | Type: Deposit "
            + "| Amount: " + bankStatement.getAmount();
        } else if (bankStatement.getType() == 2) {
            extract = "Date: " + bankStatement.dataConvertida(bankStatement.getDate())
                    + " | Type: Withdraw "
                    + "| Amount: " + (bankStatement.getAmount() * -1);
        } else if (bankStatement.getType() == 3) {
            extract = "Date: " + bankStatement.dataConvertida(bankStatement.getDate())
                    + " | Type: Transfer "
                    + "| Amount: " + (bankStatement.getAmount() * -1);
        } else {
            extract = "Date: " + bankStatement.dataConvertida(bankStatement.getDate())
                    + " | Type: Recived Transfer "
                    + "| Amount: " + (bankStatement.getAmount());
        }

        return extract;
    }

}

/*tipos de padrão de function:
* 1 - Depósito
* 2 - Saque
* 3 - Tranferência - Cliente enviou
* 4 - Tranferência - Cliente recebidor (cliente do banco)*/
