import classes.Bank;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ParseException {

        Scanner scan = new Scanner(System.in);

        int opcao;
        ArrayList<Bank> bankListAccounts = new ArrayList<>();

        do {

            System.out.println("Selecione a opção: ");
            System.out.println("1 - Criar Conta");
            System.out.println("2 - Logar Conta");
            System.out.println("3 - Sair");
            opcao = scan.nextInt();

            if (opcao == 1) {
                System.out.print("Digite seu CPF: ");
                String cpf = scan.nextLine();
                if(bankListAccounts.isEmpty()){
                    System.out.print("Digite seu Nome Completo: ");
                    String nome = scan.nextLine();
                    System.out.print("Digite seu E-mail: ");
                    String email = scan.nextLine();
                    System.out.print("Digite sua Data de Nascimento (dd/MM/yyyy): ");
                    String dataNascimento = scan.nextLine();

                    System.out.println("Okay! Tudo Certo! Vamos continuar...");

                    System.out.println("Selecione o tipo de conta que você deseja abrir: ");
                    System.out.println("1 - Conta Corrente");
                    System.out.println("2 - Conta Poupanca");
                    System.out.println("3 - Conta Salário");
                    System.out.print("Opção: ");
                    int opConta = scan.nextInt();

                    System.out.println("\nCriando a sua conta...");




                }
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