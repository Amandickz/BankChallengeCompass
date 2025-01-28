import classes.Account;
import classes.Bank;
import classes.BankCustomer;
import classes.BankStatement;
import control.AccountConfiguration;
import db.DBManipulation;
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

    //static ArrayList<Bank> bankListAccounts = new ArrayList<>();

    public static void main(String[] args) throws ParseException {

        Scanner scanner = new Scanner(System.in);
        mainMenu(scanner);

        scanner.close();
        System.out.println("Application closed");

        /*int option, check = 0;
        CPFVerification cpfVerification = new CPFVerification();

        do {

            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            option = scanner.nextInt();

            if (option == 1) {
                String cpf;
                System.out.print("\nType your CPF: ");
                String digitedCpf = scanner.next();
                if(cpfVerification.verification(digitedCpf)){
                    cpf = cpfVerification.convertionCPF(digitedCpf);
                    for (Bank b : bankListAccounts) {
                        if (b.getAccount().getCpf().equals(cpf)) {
                            check++;
                        }
                    }
                    scanner.nextLine();
                    if(check == 0) {
                        accountOpening(scanner,cpf);
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
                    String cpf = scanner.next();

                    cpf = cpfVerification.convertionCPF(cpf);

                    for (Bank b : bankListAccounts) {
                        if (b.getAccount().getCpf().equals(cpf)) {
                            System.out.print("Password Account: ");
                            String password = scanner.next();
                            if (b.getAccount().getPassword().equals(password)) {
                                loginMenu(scanner, b.getId());
                            }
                        }
                    }
                }
            } else if (option == 0) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.\n");
            }

        } while (option != 3);*/


    }

    public static void mainMenu(Scanner scanner) throws ParseException {
        CPFVerification cpfVerification = new CPFVerification();
        DBManipulation db = new DBManipulation();
        String digitedCpf, cpf;
        boolean running = true;

        while (running) {
            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("\nLogin Account");
                    System.out.print("CPF: ");
                    digitedCpf = scanner.next();
                    cpf = cpfVerification.convertionCPF(digitedCpf);
                    BankCustomer bankCustomer = db.returnBankCustomer(cpf);
                    if(bankCustomer == null) {
                        System.out.println("CPF does not account. Please open your account first.");
                    } else {
                        System.out.print("Password: ");
                        String password = scanner.next();
                        Account account = db.returnAccountCostumer(bankCustomer);
                        if(account == null) {
                            System.out.println("Account doesn't localizated. Please try again.");
                        } else {
                            if(account.getPassword().equals(password)) {
                                bankMenu(scanner, account);
                            }
                        }
                    }
                    return;
                case 2:
                    System.out.print("Digit your CPF: ");
                    digitedCpf = scanner.next();
                    cpf = cpfVerification.convertionCPF(digitedCpf);
                    if(db.foundCPF(cpf)){
                        openAccount(scanner,cpf);
                        System.out.println("Account Opening.");
                    } else {
                        System.out.println("CPF has a account. Please, make a login.");
                    }
                    return;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public static void bankMenu(Scanner scanner, Account account) {
        DBManipulation dbManipulation = new DBManipulation();
        Bank bank = dbManipulation.returnBankAccount(account);
        boolean running = true;
        float amount = 0;

        while (running) {
            System.out.println("\n========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            System.out.print("\n");

            switch (option) {
                case 1:
                    System.out.print("Digit the amount to deposit: ");
                    amount = scanner.nextFloat();
                    if(account.deposit(amount)) {
                        dbManipulation.alterBalanceAccount(account);
                        BankStatement bankStatement = new BankStatement(1,amount);
                        int idBankStatement = dbManipulation.insertBankStatement(bankStatement);
                        dbManipulation.insertTransaction(bank.getId(),idBankStatement);
                    }
                    System.out.println("Deposit ok!");
                    break;
                case 2:
                    System.out.print("Digit the amount to withdraw: ");
                    amount = scanner.nextFloat();
                    if (bank.getAccount().withdraw(amount)){
                        dbManipulation.alterBalanceAccount(account);
                        BankStatement bankStatement = new BankStatement(2,amount);
                        int idBankStatement = dbManipulation.insertBankStatement(bankStatement);
                        dbManipulation.insertTransaction(bank.getId(),idBankStatement);
                        System.out.println("Withdraw ok!");
                    } else {
                        System.out.println("Withdraw fail! Please check your balance" +
                                "\n and try again.");
                    }
                    break;
                case 3:
                    System.out.println("Check Balance.");
                    System.out.println("Actual Balance: R$ " + dbManipulation.returnBalance(account.getId()));
                    break;
                case 4:
                    // ToDo...
                    System.out.println("Transfer.");
                    break;
                case 5:
                    // ToDo...
                    System.out.println("Bank Statement.");
                    break;
                case 0:
                    System.out.println("Exiting...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void openAccount(Scanner scanner, String cpf) throws ParseException {
        AccountConfiguration accountConfiguration = new AccountConfiguration();
        PhoneVerification phoneVerification = new PhoneVerification();
        DateVerification dateVerification = new DateVerification();
        DBManipulation dbManipulation = new DBManipulation();

        scanner.nextLine();

        System.out.print("Complete Name: ");
        String name = scanner.nextLine();

        System.out.print("Phone Number: ");
        String digitedPhone = scanner.next();

        if(!phoneVerification.verificationValidFormat(digitedPhone)){
            System.out.println("Invalid phone number! Please try again.");
        } else {
            String phone = phoneVerification.formatPhone(digitedPhone);
            System.out.print("Birthday Date (dd/MM/yyyy): ");
            String birthdayDate = scanner.next();
            Date birthdaySQL = accountConfiguration.dateFormatStringtoSQL(birthdayDate);
            LocalDateTime today = LocalDateTime.now();

            if(dateVerification.verificationBirthday(birthdaySQL, today)){
                System.out.println("\nSelect type account: ");
                System.out.println("1 - Current Account");
                System.out.println("2 - Savings Account");
                System.out.println("3 - Salary Account");
                System.out.print("Option: ");

                int typeAccount = scanner.nextInt();

                System.out.print("Please, digit a password: ");
                String password = scanner.next();

                System.out.println("\nChecking your password...\n");

                System.out.print("You want to make a initial deposit? (y/n) ");
                char deposit = scanner.next().charAt(0);

                System.out.println("\nCreating your Account...\n");
                int numberAccount = accountConfiguration.createNumberAccount();

                float value = 0;

                if (deposit == 'y') {
                    System.out.print("Enter with a deposit value: ");
                    value = scanner.nextFloat();
                }

                //Inserção do cliente no banco
                BankCustomer bankCustomer = new BankCustomer(name,birthdaySQL,cpf,phone);
                int idBankCustomer = dbManipulation.insertBankCustomer(bankCustomer);
                bankCustomer.setId(idBankCustomer);

                //Inserção da conta no banco
                Account account = new Account(bankCustomer.getName(),bankCustomer.getDate(),bankCustomer.getCpf(),
                        bankCustomer.getPhone(),numberAccount,typeAccount,password,value);
                dbManipulation.insertAccount(account,idBankCustomer);

                //Inserção do banco
                int bankId = dbManipulation.insertBank(account);
            } else {
                System.out.println("Please, check your date birthday and try again.");
            }
        }

    }

    /*private static void loginMenu(Scanner scan, int idBank) {

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
    }*/
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