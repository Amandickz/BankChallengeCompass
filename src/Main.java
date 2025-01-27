import classes.Account;
import classes.Bank;
import classes.BankStatement;
import control.AccountConfiguration;
import verifications.CPFVerification;
import verifications.DateVerification;
import verifications.PhoneVerification;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static ArrayList<Bank> bankListAccounts = new ArrayList<>();

    public static void main(String[] args) throws ParseException {

        Scanner scan = new Scanner(System.in);

        int option, check = 0;
        CPFVerification cpfVerification = new CPFVerification();

        do {

            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            option = scan.nextInt();

            if (option == 1) {
                String cpf;
                System.out.print("\nType your CPF: ");
                String digitedCpf = scan.next();
                if(cpfVerification.verification(digitedCpf)){
                    cpf = cpfVerification.convertionCPF(digitedCpf);
                    for (Bank b : bankListAccounts) {
                        if (b.getAccount().getCpf().equals(cpf)) {
                            check++;
                        }
                    }
                    scan.nextLine();
                    if(check == 0) {
                        accountOpening(scan,cpf);
                    } else {
                        System.out.println("\nAccount with this CPF was founded." +
                                "\nPlease, make a login in your account.\n");
                    }
                } else {
                    System.out.println("Your CPF have something wrong." +
                            "\nPlease check and try again.");
                }
                check = 0;
            } else if (option == 2) {
                if (bankListAccounts.isEmpty()) {
                    System.out.println("\nAny account founded in the registers.\n");
                } else {
                    System.out.print("\nType your CPF: ");
                    String cpf = scan.next();

                    cpf = cpfVerification.convertionCPF(cpf);

                    for (Bank b : bankListAccounts) {
                        if (b.getAccount().getCpf().equals(cpf)) {
                            System.out.print("Password Account: ");
                            String password = scan.next();
                            if (b.getAccount().getPassword().equals(password)) {
                                loginMenu(scan, b.getId());
                            }
                        }
                    }
                }
            } else if (option == 0) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.\n");
            }

        } while (option != 3);

        scan.close();
    }

    private static void accountOpening(Scanner scan, String cpf) throws ParseException {
        AccountConfiguration accountConfiguration = new AccountConfiguration();
        PhoneVerification phoneVerification = new PhoneVerification();
        DateVerification dateVerification = new DateVerification();
        int contPhone = 0;
        String digitedPhone;

        System.out.print("Enter with your complete name: ");
        String nome = scan.nextLine();

        do{
            System.out.print("Enter your phone number: ");
            digitedPhone = scan.next();
            if (phoneVerification.verificationValidFormat(digitedPhone)){
                break;
            } else {
                contPhone++;
            }
        } while (contPhone < 3 );

        if (contPhone != 3) {
            String phone = phoneVerification.formatPhone(digitedPhone);

            System.out.print("Enter with your Birthday (dd/MM/yyyy): ");
            String dataNascimento = scan.next();

            Date dataNascimentoSQL = accountConfiguration.dateFormatStringtoSQL(dataNascimento);
            LocalDateTime today = LocalDateTime.now();

            if (dateVerification.verificationBirthday(dataNascimentoSQL, today)){
                System.out.println("\nHow tyoe of account you want to open?: ");
                System.out.println("1 - Current Account");
                System.out.println("2 - Savings Account");
                System.out.println("3 - Salary Account");
                System.out.print("Option: ");
                int opConta = scan.nextInt();

                System.out.println("\nCreating your Account...\n");

                int numberAccount = accountConfiguration.createNumberAccount();

                System.out.print("Please, digit a password: ");
                String password = scan.next();

                System.out.println("\nChecking your password...\n");

                System.out.print("You want to make a initial deposit? (y/n) ");
                char deposit = scan.next().charAt(0);

                float value = 0;

                if (deposit == 'y') {
                    System.out.print("Enter with a deposit value: ");
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

                Account account = new Account(idAccount, nome, dataNascimentoSQL, cpf, phone, numberAccount, opConta, password, value);
                bankListAccounts.add(new Bank(idAccount, account));

                System.out.println("\n");

                for (Bank b : bankListAccounts) {
                    System.out.println(b);
                }

                System.out.println("\n");
            } else {
                System.out.println("Please, check your date birthday and try again.");
            }

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
                    System.out.println("========= Bank Menu =========");
                    System.out.println("|| 1. Deposit              ||");
                    System.out.println("|| 2. Withdraw             ||");
                    System.out.println("|| 3. Check Balance        ||");
                    System.out.println("|| 4. Transfer             ||");
                    System.out.println("|| 5. Bank Statement       ||");
                    System.out.println("|| 0. Exit                 ||");
                    System.out.println("=============================");
                    System.out.print("Choose an option: ");
                    int option = scan.nextInt();

                    if (option == 0) {
                        break;
                    } else if (option == 1) {
                        System.out.print("\nDigite o valor a ser depositado: ");
                        float amount = scan.nextFloat();
                        if (bank.getAccount().deposit(amount)){
                            System.out.println("\nDepósito realizado com sucesso!\n");
                            bank.newBankStatement(option,amount);
                        } else {
                            System.out.print("\nErro ao depositar! Por favor, verifique e tente novamente!\n");
                        }
                    } else if (option == 2) {
                        System.out.print("\nDigite o valor a ser sacado: ");
                        float amount = scan.nextFloat();
                        if (bank.getAccount().withdraw(amount)){
                            System.out.println("\nSaque realizado com sucesso!\n");
                            bank.newBankStatement(option,amount);
                        } else {
                            System.out.print("\nErro ao saque! Por favor, verifique seu saldo e tente novamente!\n");
                        }
                    } else if (option == 3) {
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

                    } else if (option == 4) {
                        if (bank.getBankStatement().isEmpty()){
                            System.out.print("\nNenhuma movimentação realizado\n");
                        } else {
                            for (BankStatement bS : bank.getBankStatement()){
                                System.out.println(bS);
                            }
                        }
                    } else if (option == 5) {
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