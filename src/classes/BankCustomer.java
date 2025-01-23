package classes;

import java.sql.Date;

public class BankCustomer {

    private int id;
    private String name;
    private Date date;
    private String cpf;
    private String email;

    public BankCustomer(String name, Date date, String cpf, String email) {
        this.name = name;
        this.date = date;
        this.cpf = cpf;
        this.email = email;
    }

    public BankCustomer(int id, String name, Date date, String cpf, String email) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cpf = cpf;
        this.email = email;
    }

    public int getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "BankCustomer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
