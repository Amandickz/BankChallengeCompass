package classes;

import java.sql.Date;

public class BankCustomer {

    private int id;
    private String name;
    private Date date;
    private String cpf;
    private String phone;

    public BankCustomer(String name, Date date, String cpf, String phone) {
        this.name = name;
        this.date = date;
        this.cpf = cpf;
        this.phone = phone;
    }

    public BankCustomer(int id, String name, Date date, String cpf, String phone) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cpf = cpf;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "BankCustomer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", cpf='" + cpf + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
