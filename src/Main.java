import classes.Account;
import classes.Bank;
import classes.BankStatement;
import control.AccountConfiguration;
import verifications.CPFVerification;
import verifications.PhoneVerification;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static ArrayList<Bank> bankListAccounts = new ArrayList<>();

    public static void main(String[] args) throws ParseException {

        Scanner scan = new Scanner(System.in);

        int opcao, check = 0;
        CPFVerification cpfVerification = new CPFVerification();

        do {

            System.out.println("\nSelecione a opção: ");
            System.out.println("1 - Criar Conta");
            System.out.println("2 - Logar Conta");
            System.out.println("3 - Sair");
            opcao = scan.nextInt();

            if (opcao == 1) {
                String cpf;
                System.out.print("\nDigite seu CPF: ");
                String digitedCpf = scan.next();
                if(cpfVerification.verification(digitedCpf)){
                    cpf = cpfVerification.convertionCPF(digitedCpf);
                    for (Bank b : bankListAccounts) {
                        if (b.getAccount().getCpf().equals(digitedCpf)) {
                            check++;
                        }
                    }
                    scan.nextLine();
                    if(check == 0) {
                        accountOpening(scan,cpf);
                    } else {
                        System.out.println("\nCPF já cadastrado, por favor, realize o login na sua conta.\n");
                    }
                } else {
                    System.out.println("Your CPF have something wrong.");
                }
                check = 0;
            } else if (opcao == 2) {
                if (bankListAccounts.isEmpty()) {
                    System.out.println("\nNenhuma conta cadastrada no sistema!\n");
                } else {
                    System.out.print("\nDigite seu CPF: ");
                    String cpf = scan.next();
                    System.out.print("Senha: ");
                    String password = scan.next();
                    for (Bank b : bankListAccounts) {
                        if (b.getAccount().getCpf().equals(cpf)) {
                            System.out.println("\nConta Localizada!\n");
                            if (b.getAccount().getPassword().equals(password)) {
                                System.out.println("\nTudo certo! Entrando na sua conta\n");
                                loginMenu(scan, b.getId());
                            }
                        }
                    }
                }
            } else if (opcao == 3) {
                break;
            } else {
                System.out.println("Opção incorreta! Tente novamente\n");
            }

        } while (opcao != 3);

        scan.close();
    }

    private static void accountOpening(Scanner scan, String cpf) throws ParseException {
        AccountConfiguration accountConfiguration = new AccountConfiguration();
        PhoneVerification phoneVerification = new PhoneVerification();
        int contPhone = 0;
        String digitedPhone = new String();

        System.out.print("Digite seu Nome Completo: ");
        String nome = scan.nextLine();

        do{
            System.out.print("Digite seu Telefone: ");
            digitedPhone = scan.next();
            if (phoneVerification.verificationValidFormat(digitedPhone)){
                break;
            } else {
                contPhone++;
            }
        } while (contPhone < 3 );

        if (phoneVerification.verificationValidFormat(digitedPhone)) {
            String phone = phoneVerification.formatPhone(digitedPhone);

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

            int idAccount = 0;

            if (bankListAccounts.isEmpty()) {
                idAccount = 1;
            } else {
                for (Bank b : bankListAccounts) {
                    idAccount = b.getAccount().getId() + 1;
                }
            }

            Date dataNascimentoSQL = accountConfiguration.dateFormatStringtoSQL(dataNascimento);

            Account account = new Account(idAccount, nome, dataNascimentoSQL, cpf, phone, numberAccount, opConta, password, value);
            bankListAccounts.add(new Bank(idAccount, account));

            System.out.println("\n");

            for (Bank b : bankListAccounts) {
                System.out.println(b);
            }

            System.out.println("\n");
        } else if (contPhone == 3) {
            System.out.println("You tried to digit your phone 3 times.\n" +
                    "Please check your number and try again.");
        } else {
            System.out.println("Somenthing wrong was happened!\n");
        }
    }

    private static void loginMenu(Scanner scan, int idBank) {

        for (Bank bank : bankListAccounts) {
            if (bank.getId() == idBank) {
                do {
                    System.out.println("\nSeja bem-vindo " + bank.getAccount().getName());
                    System.out.println("Saldo Atual");
                    System.out.println("R$ " + bank.getAccount().getBalance());
                    System.out.println("1 - Realizar Deposito");
                    System.out.println("2 - Realizar Saque");
                    System.out.println("3 - Realizar Transferência");
                    System.out.println("4 - Visualizar Extrato");
                    System.out.println("5 - Visualizar Saldo");
                    System.out.println("6 - Sair");
                    int opcao = scan.nextInt();

                    if (opcao == 6) {
                        break;
                    } else if (opcao == 1) {
                        System.out.print("\nDigite o valor a ser depositado: ");
                        float amount = scan.nextFloat();
                        if (bank.getAccount().deposit(amount)){
                            System.out.println("\nDepósito realizado com sucesso!\n");
                            bank.newBankStatement(opcao,amount);
                        } else {
                            System.out.print("\nErro ao depositar! Por favor, verifique e tente novamente!\n");
                        }
                    } else if (opcao == 2) {
                        System.out.print("\nDigite o valor a ser sacado: ");
                        float amount = scan.nextFloat();
                        if (bank.getAccount().withdraw(amount)){
                            System.out.println("\nSaque realizado com sucesso!\n");
                            bank.newBankStatement(opcao,amount);
                        } else {
                            System.out.print("\nErro ao saque! Por favor, verifique seu saldo e tente novamente!\n");
                        }
                    } else if (opcao == 3) {
                        System.out.println("\nA Tranferência vai ser: ");
                        System.out.println("1 - Entre contas do Banco");
                        System.out.println("2 - Para contas externas");
                        int opTranfer = scan.nextInt();

                        if (opTranfer == 1) {
                            Bank accountRecive = null;
                            System.out.print("\nDigite o número da conta que irá receber a transferência: ");
                            int accountNumber = scan.nextInt();
                            for (Bank b : bankListAccounts) {
                                if (b.getAccount().getNumberAccount() == accountNumber) {
                                    accountRecive = new Bank(b.getId(), b.getAccount());
                                    System.out.print("Digite o valor que você deseja transferir: R$ ");
                                    float amount = scan.nextFloat();

                                    if(bank.getAccount().getBalance() >= amount){
                                        System.out.print("Confimar transferência da sua conta para\n" +
                                                b.getAccount().getName() + "\n(s/n) ");
                                        char confirm = scan.next().charAt(0);
                                        if (confirm == 's') {
                                            if (bank.getAccount().internalTransfer(bank,b,amount)){
                                                System.out.println("\nTransferência realizada com sucesso realizada com sucesso!\n");
                                            } else {
                                                System.out.println("\nErro ao realizar a trasnferência");
                                            }
                                        }

                                        for (Bank accountAtualization : bankListAccounts) {
                                            System.out.println(accountAtualization.getBankStatement());
                                        }

                                    } else {
                                        System.out.print("\nErro ao transferir o valor. Saldo Indisponível!\n");
                                    }
                                }
                            }

                            if(accountRecive == null){
                                System.out.println("\nConta digitada não localizada." +
                                        "\nPor favor, verifique os dados e tente novamente.\n");
                            }
                        } else {
                            System.out.print("\nDigite o CPF da conta que irá receber a transferência: ");
                            String cpf = scan.next();

                            System.out.print("Digite o valor que você deseja transferir: R$ ");
                            float amount = scan.nextFloat();

                            if(bank.getAccount().getBalance() >= amount){
                                System.out.print("Confimar transferência da sua conta para o CPF\n" +
                                        cpf + "\n(s/n) ");
                                char confirm = scan.next().charAt(0);
                                if (confirm == 's') {
                                    if (bank.getAccount().externalTransfer(bank,amount)){
                                        System.out.println("\nTransferência realizada com sucesso realizada com sucesso!\n");
                                    } else {
                                        System.out.println("\nErro ao realizar a trasnferência");
                                    }
                                }

                                for (Bank accountAtualization : bankListAccounts) {
                                    System.out.println(accountAtualization.getBankStatement());
                                }

                            } else {
                                System.out.print("\nErro ao transferir o valor. Saldo Indisponível!\n");
                            }
                        }

                    } else if (opcao == 4) {
                        if (bank.getBankStatement().isEmpty()){
                            System.out.print("\nNenhuma movimentação realizado\n");
                        } else {
                            for (BankStatement bS : bank.getBankStatement()){
                                System.out.println(bS);
                            }
                        }
                    } else if (opcao == 5) {
                        System.out.println("\nSaldo Atual: " + bank.getAccount().getBalance());
                        float amountTotal = 0;
                        for (BankStatement bS : bank.getBankStatement()){
                            amountTotal += bS.getAmount();
                        }
                        System.out.println("Movimentações do dia: R$ " + amountTotal);
                    }

                } while (true);
            }
        }
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