import java.util.*;
import java.io.*;
import java.lang.*;


class User {
    int account_balance;

    User() {
        // Math.floor(Math.random()*(max-min+1)+min)
        // Minimum balance = 1000, Maximum = 10000
        account_balance = (int) Math.floor(Math.random() * (10000 - 1000 + 1) + 1000);
    }

    class Account {
        // withdrawing amount from account balance
        public void withdraw(int amount) {
            if (account_balance >= amount) {
                account_balance = account_balance - amount;
                System.out.println("Your withdrawl of amount " + amount + " was succesful!");
            } else {
                System.out.println("Your current balance is lower than the amount to be withdrawn!");
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

        public boolean verification(int cardNumber, int pinNumber) {
            if (cardNumber == card_number && pinNumber == card_pin) {
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
        User[] user = new User[3];
        User.Account[] account = new User.Account[3];
        for(int i=0; i<3; i++){
            user[i] = new User();
            account[i] = user[i].new Account();
        }
        ////

        // TWO INSTANCES FOR TESTING HARDCODED 
        User u1 = new User();
        User u2 = new User();

        User.Account a1 = u1.new Account();
        User.Account a2 = u2.new Account();

        User.Card c1 = u1.new Card();
        User.Card c2 = u2.new Card();

        System.out.println("user1 created, balance in account: " + u1.account_balance);
        System.out.println("Card Number for user1: " + c1.card_number);
        System.out.println("Card Pin for user1: " + c1.card_pin);
        System.out.println("");
        System.out.println("user2 created, balance in account: " + u2.account_balance);
        System.out.println("Card Number for user2: " + c2.card_number);
        System.out.println("Card Pin for user2: " + c2.card_pin);
        System.out.println("");
        System.out.println("");



        System.out.println("To begin ATM Simulation, enter 420 or any other to exit");
        int start = scan.nextInt();


        int machine_balance = (int) Math.floor(Math.random() * (100000 - 0 + 1) + 0);
        boolean simulation_status = true;

        System.out.println("Welcome to ATM Simulation");

        // verification of card number entered
        System.out.println("Please enter your Card Number");
        int cardNumber = scan.nextInt();
        while (!(c1.verifyCardNumber(cardNumber) || c2.verifyCardNumber(cardNumber)) || !simulation_status) {
            System.out.println("Please enter a valid Card Number or 0 to Quit Simulation");
            cardNumber = scan.nextInt();
            if (cardNumber == 0) {
                simulation_status = false;
                System.out.println("Thank You! Have a nice day.");
                System.exit(0);
            }
        }
        // verification of pin corresponding to the card entered
        System.out.println("Please enter your Card Pin");
        int pinNumber = scan.nextInt();
        if (c1.verifyCardNumber(cardNumber) || !simulation_status) {
            while (!(c1.verifyCardPin(pinNumber)) || !simulation_status) {
                System.out.println("Please enter a valid Card Pin or 0 to Quit Simulation");
                pinNumber = scan.nextInt();
                if (pinNumber == 0) {
                    simulation_status = false;
                    System.out.println("Thank You! Have a nice day.");
                    System.exit(0);
                }
            }
        } else if (c2.verifyCardNumber(cardNumber) || !simulation_status) {
            while (!(c2.verifyCardPin(pinNumber)) || !simulation_status) {
                System.out.println("Please enter a valid Card Pin or 0 to Quit Simulation");
                pinNumber = scan.nextInt();
                if (pinNumber == 0) {
                    simulation_status = false;
                    System.out.println("Thank You! Have a nice day.");
                    System.exit(0);
                }
            }
        }

        boolean user1 = false;
        boolean user2 = false;

        // double verification of credentials
        if (c1.verification(cardNumber, pinNumber)) {
            System.out.println("Hi user1, select one of the choice below");
            user1 = true;
        } else if (c2.verification(cardNumber, pinNumber)) {
            System.out.println("Hi user2, select one of the choice below");
            user2 = true;
        } else {
            System.out.println("Unexpected Error while double verification");
            System.exit(0);
        }

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
                    ////
                    while (machine_balance < amount) {
                        System.out.println("Machine doesn't have sufficient cash to be withdrawn");
                        System.out.println("Please enter a value, less than " + machine_balance);
                        amount = scan.nextInt();
                    }
                    ////
                    if (user1) {
                        a1.withdraw(amount);
                    } else if (user2) {
                        a2.withdraw(amount);
                    }
                    ////
                    machine_balance = machine_balance - amount;
                    break;

                case 2:
                    System.out.println("Please enter an amount to deposit");
                    amount = scan.nextInt();
                    ////
                    if (user1) {
                        a1.deposit(amount);
                    } else if (user2) {
                        a2.deposit(amount);
                    }
                    ////
                    machine_balance = machine_balance + amount;
                    break;

                case 3:
                    if (user1) {
                        a1.display_balance();
                    } else if (user2) {
                        a2.display_balance();
                    }
                    break;

                default:
                    System.out.println("Please enter a valid choice!");
                    break;
            }
        }

        scan.close();
    }
}