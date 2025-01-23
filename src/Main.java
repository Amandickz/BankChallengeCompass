import classes.Account;
import classes.Bank;
import configurations.AccountConfiguration;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ParseException {

        Scanner scan = new Scanner(System.in);

        int opcao;
        ArrayList<Bank> bankListAccounts = new ArrayList<>();
        AccountConfiguration accountConfiguration = new AccountConfiguration();

        do {

            System.out.println("Selecione a opção: ");
            System.out.println("1 - Criar Conta");
            System.out.println("2 - Logar Conta");
            System.out.println("3 - Sair");
            opcao = scan.nextInt();

            if (opcao == 1) {

                System.out.print("Digite seu CPF: ");
                String cpf = scan.next();

                if(bankListAccounts.isEmpty()){
                    System.out.print("Digite seu Nome Completo: ");
                    String nome = scan.next();
                    scan.nextLine();
                    System.out.print("Digite seu Telfone: ");
                    String phone = scan.next();
                    System.out.print("Digite sua Data de Nascimento (dd/MM/yyyy): ");
                    String dataNascimento = scan.next();

                    System.out.println("\nOkay! Tudo Certo! Vamos continuar...\n");

                    System.out.println("Selecione o tipo de conta que você deseja abrir: ");
                    System.out.println("1 - Conta Corrente");
                    System.out.println("2 - Conta Poupanca");
                    System.out.println("3 - Conta Salário");
                    System.out.print("Opção: ");
                    int opConta = scan.nextInt();

                    System.out.println("\nCriando a sua conta...\n");

                    int numberAccount = accountConfiguration.createNumberAccount();

                    System.out.print("Por favor, digite uma senha: ");
                    String password = scan.next();

                    System.out.println("\nChecando a senha... Vamos proceguir!\n");

                    System.out.print("Deseja fazer um depósito inicial? (s/n) ");
                    char deposit = scan.next().charAt(0);

                    float value = 0;

                    if (deposit == 's') {
                        System.out.print("Digite o valor a ser depositado: ");
                        value = scan.nextFloat();
                    }

                    int idAccount;

                    if (bankListAccounts.isEmpty()) {
                        idAccount = 0;
                    } else {
                        idAccount = bankListAccounts.size() + 1;
                    }

                    Date dataNascimentoSQL = accountConfiguration.dateFormatStringtoSQL(dataNascimento);

                    Account account = new Account(idAccount,nome,dataNascimentoSQL,cpf,phone,numberAccount,opConta,password,value);
                    System.out.println(account.toString());
                    bankListAccounts.add(new Bank(idAccount, account));
                    System.out.println("Cheguei até aqui! Quantidade de Contas: " + bankListAccounts.size());

                    for (Bank b : bankListAccounts) {
                        System.out.println(b);
                    }

                }
            } else if (opcao == 2) {

            } else if (opcao == 3) {
                break;
            } else {
                System.out.println("Opção incorreta! Tente novamente\n");
            }

        } while (opcao != 3);

        scan.close();
    }
}

        /*System.out.print("Digite sua Data de Nascimento (dd/MM/yyyy): ");
        String dataNascimento = scan.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dataConvertida1 = new Date();
        dataConvertida1 = sdf.parse(dataNascimento);
        System.out.println("Data com o util: " + dataConvertida1);

        java.sql.Date dataConvertida2 = new java.sql.Date(dataConvertida1.getTime());
        System.out.println("Data em SQL: " + dataConvertida2);

        System.out.println("Conversão retornada: " + sdf.format(dataConvertida2));*/