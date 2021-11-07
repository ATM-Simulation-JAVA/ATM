import java.io.*;
import java.util.*;

class User {
    int account_balance;

    User() {
        // Math.floor(Math.random()*(max-min+1)+min)
        // Minimum balance = 1000, Maximum = 10000
        account_balance = (int) Math.floor(Math.random() * (10000 - 1000 + 1) + 1000);
    }

    class Account {
        // withdrawing amount from account balance
        public boolean withdraw(int amount) {
            if (account_balance >= amount) {
                account_balance = account_balance - amount;
                System.out.println("Your withdrawl of amount " + amount + " was succesful!");
                return true;
            } else {
                System.out.println("Your current balance is lower than the amount to be withdrawn!");
                return false;
            }
            
        }
        // displaying current account balance
        public void display_balance() {
            System.out.println("Your current account balance is " + account_balance);
        }
        // depositing amount to account balance
        public void deposit(int amount) {
            account_balance = account_balance + amount;
            System.out.println("Your deposit of amount " + amount + " was succesful!");
        }
    }

    class Card {
        int card_number;
        int card_pin;

        Card() {
            // 5 digit card number
            card_number = (int) Math.floor(Math.random() * (99999 - 10000 + 1) + 10000);
            // 3 digit card pin
            card_pin = (int) Math.floor(Math.random() * (999 - 100 + 1) + 100);
        }

        public boolean verifyCardNumber(int cardNumber) {
            if (card_number == cardNumber) {
                return true;
            } else {
                return false;
            }
        }
        public boolean verifyCardPin(int pinNumber) {
            if (card_pin == pinNumber) {
                return true;
            } else {
                return false;
            }
        }
    }
}


class ATM {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        // syntax to create an array of objects and link it to the internal classes simultaneously
        // automating class generation and one to one mapping
        // to replace HARDCODED logic

        int data_base_size = 3;

        User[] user = new User[data_base_size];
        User.Account[] account = new User.Account[data_base_size];
        User.Card[] card = new User.Card[data_base_size];
        for (int i = 0; i < data_base_size; i++) {
            user[i] = new User();
            account[i] = user[i].new Account();
            card[i] = user[i].new Card();
        }

        int machine_balance = (int) Math.floor(Math.random() * (100000 - 0 + 1) + 0);


        System.out.println("Type 'start' to start the simulation");
        String start = scan.next();
        boolean simulation_status = false;

        // ! DON'T IGNORE !
        // *SMALL AUTHENTICATION SYSTEM, IN ORDER TO KNOW THE DETAILS, TYPE 'admin' *

        while (!simulation_status) {
            if (start.equalsIgnoreCase("start")) {
                System.out.println("Welcome to ATM Simulation");
                simulation_status = true;
            } else if (start.equalsIgnoreCase("admin")) {
                for (int i = 0; i < 3; i++) {
                    System.out.println("User" + (i + 1) + " account details:");
                    System.out.println("Account Balance: " + user[i].account_balance);
                    System.out.println("Card Number: " + card[i].card_number);
                    System.out.println("Pin Number: " + card[i].card_pin);
                    System.out.println("");
                }
                System.out.println("Machine Balance: " + machine_balance);
                System.out.println("");
                System.out.println("Welcome to ATM Simulation");
                simulation_status = true;
            }
        }

        // verification system starts
        int userNumber = -1;
        if (simulation_status) {
            
            System.out.println("Please enter your Card Number");
            int cardNumber = scan.nextInt();

            for (int i = 0; i < 3; i++) {
                //verifying card number
                if (card[i].verifyCardNumber(cardNumber)) {
                    System.out.println("Please enter your Pin Number");
                    int pinNumber = scan.nextInt();

                    // since the card number is correct, checking corresponding pin number for same card
                    if (card[i].verifyCardPin(pinNumber)) {
                        userNumber = i;
                        System.out.println("User" + (i + 1) + " verified, Welcome!");
                        // saving the index of user perfectly identified
                        break;
                    }
                    // if the corresponding pin is wrong
                    else {
                        while (!card[i].verifyCardPin(pinNumber)) {
                            System.out.println("Please re-enter a valid Pin Number or 0 to Quit Simulation");
                            pinNumber = scan.nextInt();
                            if (pinNumber == 0) {
                                simulation_status = false;
                                System.out.println("Thank You! Have a nice day.");
                                System.exit(0);
                            }
                        }
                        if (card[i].verifyCardPin(pinNumber)) {
                            userNumber = i;
                            System.out.println("User" + (i + 1) + " verified, Welcome!");
                            // saving the index of user perfectly identified
                            break;
                        }
                    }
                }
                // if the card number is false
                else if(i == data_base_size - 1){
                    System.out.println("There is no such user with card number: " + cardNumber + " in the data base.");
                    System.out.println("Enter 1 to continue with new card number or any other to exit simulator");
                    int choice = scan.nextInt();
                    if(choice == 1){
                        System.out.println("Please re-enter a valid Card Number");
                        cardNumber = scan.nextInt();
                        i = 0;
                    }
                    else {
                        simulation_status = false;
                        System.out.println("Thank You! Have a nice day.");
                        System.exit(0);
                    }
                }
            }
            // in case of unexpected error which even I don't know
            if (userNumber == -1) {
                System.out.println("Unexpected Error!");
                simulation_status = false;
                System.exit(0);
            }
        }
        // verification system ends

        while (simulation_status) {
            System.out.println("0 = Exit ATM");
            System.out.println("1 = Cash Withdraw");
            System.out.println("2 = Cash Deposit");
            System.out.println("3 = View Balance");

            int choice = scan.nextInt();
            int amount;

            switch (choice) {
                case 0:
                    simulation_status = false;
                    System.out.println("Thank You! Have a nice day.");
                    System.exit(0);
                    break;

                case 1:
                    System.out.println("Please enter an amount to be withdrawn");
                    amount = scan.nextInt();

                    while (machine_balance < amount) {
                        System.out.println("Machine doesn't have sufficient cash to be withdrawn");
                        System.out.println("Please enter a value, less than " + machine_balance);
                        amount = scan.nextInt();
                    }

                    if(account[userNumber].withdraw(amount)){
                        machine_balance = machine_balance - amount;
                    }
                    break;

                case 2:
                    System.out.println("Please enter an amount to deposit");
                    amount = scan.nextInt();

                    account[userNumber].deposit(amount);

                    machine_balance = machine_balance + amount;
                    break;

                case 3:
                    account[userNumber].display_balance();
                    break;

                default:
                    System.out.println("Please enter a valid choice!");
                    break;
            }
        }

        scan.close();
    }
}