import classes.*;
import control.AccountConfiguration;
import db.DBManipulation;
import verifications.CPFVerification;
import verifications.DateVerification;
import verifications.PhoneVerification;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws ParseException {

        Scanner scanner = new Scanner(System.in);
        mainMenu(scanner);
        scanner.close();
        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner) throws ParseException {
        CPFVerification cpfVerification = new CPFVerification();
        DBManipulation db = new DBManipulation();
        String digitedCpf, cpf;
        boolean running = true;

        while (running) {
            try {
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
                        BankCustomer bankCustomer = db.returnBankCustomerCPF(cpf);
                        if (bankCustomer == null) {
                            System.out.println("CPF does not account. Please open your account first.");
                        } else {
                            System.out.print("Password: ");
                            String password = scanner.next();
                            Account account = db.returnAccount(bankCustomer);
                            if (account == null) {
                                System.out.println("Account doesn't localizated. Please try again.");
                            } else {
                                if (account.getPassword().equals(password)) {
                                    bankMenu(scanner, account);
                                }
                            }
                        }
                        return;
                    case 2:
                        System.out.print("Digit your CPF: ");
                        digitedCpf = scanner.next();
                        cpf = cpfVerification.convertionCPF(digitedCpf);
                        if (db.foundCPF(cpf)) {
                            openAccount(scanner, cpf);
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
            } catch (InputMismatchException e) {
                System.out.println("You digit a letter! Please try again.");
                return;
            }
        }
    }

    public static void bankMenu(Scanner scanner, Account account) {
        DBManipulation dbManipulation = new DBManipulation();
        Bank bank = dbManipulation.returnBankAccount(account);
        CPFVerification cpfVerification = new CPFVerification();
        boolean running = true;
        float amount = 0;

        while (running) {
            try {
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
                        System.out.println("Deposit.");
                        System.out.print("Digit the amount to deposit: ");
                        amount = scanner.nextFloat();
                        if (account.deposit(amount)) {
                            dbManipulation.alterBalanceAccount(account);
                            BankStatement bankStatement = new BankStatement(1, amount);
                            int idBankStatement = dbManipulation.insertBankStatement(bankStatement);
                            dbManipulation.insertTransaction(bank.getId(), idBankStatement);
                        }
                        System.out.println("Deposit ok!");
                        break;
                    case 2:
                        System.out.println("Withdraw.");
                        System.out.print("Digit the amount to withdraw: ");
                        amount = scanner.nextFloat();
                        if (bank.getAccount().withdraw(amount)) {
                            dbManipulation.alterBalanceAccount(account);
                            BankStatement bankStatement = new BankStatement(2, amount);
                            int idBankStatement = dbManipulation.insertBankStatement(bankStatement);
                            dbManipulation.insertTransaction(bank.getId(), idBankStatement);
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
                        System.out.println("Transfer.");
                        System.out.println("Type of transfer: ");
                        System.out.println("1 - Bank Account to Bank Account");
                        System.out.println("2 - Bank Account to CPF (external account)");
                        System.out.print("Choose an option: ");
                        int optionTransfer = scanner.nextInt();

                        if (optionTransfer == 1) {
                            System.out.print("Enter with a account number to transfer: ");
                            int numberAccountTransfer = scanner.nextInt();

                            if (dbManipulation.verificationAccount(numberAccountTransfer)) {
                                System.out.println("Digit the amount to transfer: ");
                                amount = scanner.nextFloat();
                                if (bank.getAccount().getBalance() >= amount) {
                                    int bankCustomerId = dbManipulation.returnBankCustomerId(numberAccountTransfer);
                                    BankCustomer bankCustomerTransfer = dbManipulation.returnBankCustomer(bankCustomerId);
                                    Account accountTransfer = dbManipulation.returnAccount(bankCustomerTransfer);
                                    Bank bankTransfer = dbManipulation.returnBankAccount(accountTransfer);
                                    System.out.print("Confirm the transfer: \n" +
                                            "R$ " + amount +
                                            bank.getAccount().getName() + " to " + accountTransfer.getName() + "\n (y/n): ");
                                    char confirm = scanner.next().charAt(0);
                                    if (confirm == 'y') {
                                        if (bank.getAccount().internalTransfer(bank, bankTransfer, amount)) {
                                            dbManipulation.alterBalanceAccount(bank.getAccount());
                                            dbManipulation.alterBalanceAccount(accountTransfer);
                                            BankStatement bankStatement = new BankStatement(3, amount);
                                            int idBankStatement = dbManipulation.insertBankStatement(bankStatement);
                                            dbManipulation.insertTransaction(bank.getId(), idBankStatement);
                                            BankStatement bankStatementTransfer = new BankStatement(4, amount);
                                            int idBankStatementTransfer = dbManipulation.insertBankStatement(bankStatementTransfer);
                                            dbManipulation.insertTransaction(bankTransfer.getId(), idBankStatementTransfer);
                                            System.out.println("Bank Account transfer ok!");
                                        } else {
                                            System.out.println("\nSomenthing wrong. Please try again.");
                                        }
                                    }
                                } else {
                                    System.out.print("\nSomenthing wrong. Check your balance and try again!\n");
                                }
                            } else {
                                System.out.println("\nAccount not found. Please try again.");
                            }
                        } else {
                            System.out.print("Enter CPF to transfer: ");
                            String cpf = scanner.next();

                            if (cpfVerification.verification(cpf)) {
                                String cpfConverted = cpfVerification.convertionCPF(cpf);

                                System.out.print("Digit the amount to transfer: ");
                                amount = scanner.nextFloat();

                                if (bank.getAccount().getBalance() >= amount) {
                                    System.out.print("Confirm you transfer with \n" +
                                            cpf + " of value R$ " + amount + "\n(y/n) ");
                                    char confirm = scanner.next().charAt(0);
                                    if (confirm == 'y') {
                                        if (bank.getAccount().externalTransfer(bank, amount)) {
                                            dbManipulation.alterBalanceAccount(bank.getAccount());
                                            BankStatement bankStatement = new BankStatement(3, amount);
                                            int idBankStatement = dbManipulation.insertBankStatement(bankStatement);
                                            dbManipulation.insertTransaction(bank.getId(), idBankStatement);
                                            System.out.println("Bank Account transfer ok!");
                                        } else {
                                            System.out.println("\nSomenthing wrong. Please try again.");
                                        }
                                    }
                                }
                            } else {
                                System.out.println("CPF was incorrect. Please try again.");
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Bank Statement.");
                        ArrayList<Transitions> transitions = dbManipulation.returnTransitions(bank.getId());
                        ArrayList<BankStatement> bankStatements = new ArrayList<>();
                        if (!transitions.isEmpty()) {
                            for (Transitions transition : transitions) {
                                BankStatement bankStatement = dbManipulation.returnBankStatement(transition.getIdBankStatement());
                                bankStatements.add(bankStatement);
                            }

                            for (BankStatement bankStatement : bankStatements) {
                                System.out.println(bankStatement.extract(bankStatement));
                            }
                        } else {
                            System.out.println("Any transitions not found.");
                        }
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        running = false;
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (InputMismatchException e){
                System.out.println("You digit a letter! Please try again.");
                return;
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

}