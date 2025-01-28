package classes;

public class Transitions {

    int idBank;
    int idBankStatement;

    public Transitions(int idBank, int idBankStatement) {
        this.idBank = idBank;
        this.idBankStatement = idBankStatement;
    }

    public int getIdBank() {
        return idBank;
    }

    public void setIdBank(int idBank) {
        this.idBank = idBank;
    }

    public int getIdBankStatement() {
        return idBankStatement;
    }

    public void setIdBankStatement(int idBankStatement) {
        this.idBankStatement = idBankStatement;
    }

    @Override
    public String toString() {
        return "Transitions{" +
                "idBank=" + idBank +
                ", idBankStatement=" + idBankStatement +
                '}';
    }
}
