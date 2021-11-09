import java.io.*;
import java.util.*;

class User {
    private int account_balance;

    public int setAccountBalance(int max, int min){
        return (int) Math.floor(Math.random() * (max - min + 1) + min); 
    }

    User() {
        // Math.floor(Math.random()*(max-min+1)+min)
        // Minimum balance = 1000, Maximum = 10000
        account_balance =  setAccountBalance(10000, 1000);
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
            System.out.println("Current account balance: " + account_balance);
        }
        // depositing amount to account balance
        public void deposit(int amount) {
            account_balance = account_balance + amount;
            System.out.println("Your deposit of amount " + amount + " was succesful!");
        }
    }

    class Card {
        private int card_number;
        private int card_pin;

        public int setCardCredentials(int max, int min){
            return (int) Math.floor(Math.random() * (max - min + 1) + min); 
        }

        Card() {
            // 5 digit card number
            card_number = setCardCredentials(99999, 10000);
            // 3 digit card pin
            card_pin = setCardCredentials(999, 100);
        }

        public int getCarNumber() {
            return card_number;
        }
        public int getCardPin() {
            return card_pin;
        }
        public boolean updatePin(int enteredCardNumber, int enteredCardPin, int newCardPin){
            if(enteredCardNumber == card_number && enteredCardPin == card_pin){
                card_pin = newCardPin;
                return true;
            }
            else
                return false;      
        }
        
    }
}


class ATM {

    private static boolean verifyAdmin(String code) {
        if(code.equalsIgnoreCase("admin"))
            return true;
        else
            return false;
    }

    private static int setMachineBalance(int max, int min){
        return (int) Math.floor(Math.random() * (max - min + 1) + min); 
    }

    public static boolean verifyCardNumber(int enteredCardNumber, int cardNumber){
        if(enteredCardNumber == cardNumber) 
            return true;
        else 
            return false;
    }
    
    public static boolean verifyCardPin(int enteredCardPin, int cardPin){
        if(enteredCardPin == cardPin) 
            return true;
        else 
            return false;
    }
    
    public static boolean checkMutiplesOf100or500(int amount){
        if(amount%100 == 0 || amount%500 == 0)
            return true;
        else
            return false;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        
        //since ATM can't generate new users and only works with the existing ones
        //generating a database with random values of PIN NUMBER and BALANCE
        final int data_base_size = 3;

        User[] user = new User[data_base_size];
        User.Account[] account = new User.Account[data_base_size];
        User.Card[] card = new User.Card[data_base_size];
        for (int i = 0; i < data_base_size; i++) {
            user[i] = new User();
            account[i] = user[i].new Account();
            card[i] = user[i].new Card();
        }

        int machine_balance = setMachineBalance(100000, 0);

        System.out.println("Type 'start' to start the simulation");
        String helperString = scan.next();
        boolean simulation_status = false;

        // ! DON'T IGNORE !
        // *SMALL AUTHENTICATION SYSTEM, IN ORDER TO KNOW THE DETAILS, TYPE 'admin' (not case sensitive)*

        while (!simulation_status) {
            if (helperString.equalsIgnoreCase("start")) {
                System.out.println("Welcome to ATM Simulation");
                simulation_status = true;
            } else if (verifyAdmin(helperString)) {
                for (int i = 0; i < 3; i++) {
                    System.out.println("User" + (i + 1) + " account details:");
                    account[i].display_balance();
                    
                    System.out.println("Card Number: " + card[i].getCarNumber());
                    System.out.println("Card Pin: " + card[i].getCardPin());
                    System.out.println("");
                }
                System.out.println("Machine starting Balance: " + machine_balance);
                System.out.println("");
                System.out.println("Welcome to ATM Simulation");
                simulation_status = true;
            }
            else {
                System.out.println("Try typing again buddy!");
                helperString = scan.next();
            }
        }

        // verification system starts
        int currentUser = -1;
        int enteredCardNumber;
        int enteredCardPin;

        if (simulation_status) { 
            System.out.print("Please enter your Card Number: ");
            enteredCardNumber = scan.nextInt();

            for (int i = 0; i < 3; i++) {
                //verifying card number
                if (verifyCardNumber(enteredCardNumber, card[i].getCarNumber())) {
                    System.out.print("Please enter your Pin Number: ");
                    enteredCardPin = scan.nextInt();

                    // since the card number is correct, checking corresponding pin number for same card
                    if (verifyCardPin(enteredCardPin, card[i].getCardPin())) {
                        currentUser = i;
                        System.out.println("User" + (i + 1) + " verified, Welcome!");
                        // saving the index of user perfectly identified
                        break;
                    }
                    // if the corresponding pin is wrong
                    else {
                        while (!verifyCardPin(enteredCardPin, card[i].getCardPin())) {
                            System.out.println("Please re-enter a valid Pin Number or -1 to Quit Simulation");
                            enteredCardPin = scan.nextInt();
                            if (enteredCardPin == -1) {
                                simulation_status = false;
                                System.out.println("Thank You! Have a nice day.");
                                System.exit(0);
                            }
                        }
                        if (verifyCardPin(enteredCardPin, card[i].getCardPin())) {
                            currentUser = i;
                            System.out.println("User" + (i + 1) + " verified, Welcome!");
                            // saving the index of user perfectly identified
                            break;
                        }
                    }
                }
                // if the card number is false
                else if(i == data_base_size - 1){
                    System.out.println("There is no such user with card number: " + enteredCardNumber + " in the data base.");
                    System.out.println("Enter 1 to continue with new card number or any other to exit simulator");
                    int choice = scan.nextInt();
                    if(choice == 1){
                        System.out.print("Please re-enter a valid Card Number: ");
                        enteredCardNumber = scan.nextInt();
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
            if (currentUser == -1) {
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
            System.out.println("4 = Change Pin");

            int choice = scan.nextInt();
            int amount;

            switch (choice) {
                case 0:
                    simulation_status = false;
                    System.out.println("Thank You! Have a nice day.");
                    System.exit(0);
                    break;

                // denomination of 100 and 500 only
                case 1:
                    System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                    amount = scan.nextInt();
                    while(checkMutiplesOf100or500(amount) != true){
                        System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                        amount = scan.nextInt();
                    }

                    while (machine_balance < amount) {
                        System.out.println("Machine doesn't have sufficient cash to be withdrawn");
                        System.out.println("Please enter a value, less than " + machine_balance);
                        amount = scan.nextInt();
                        while(checkMutiplesOf100or500(amount) != true){
                            System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                            amount = scan.nextInt();
                        }
                    }

                    if(account[currentUser].withdraw(amount)){
                        machine_balance = machine_balance - amount;
                    }
                    break;

                case 2:
                    System.out.println("Please enter an amount to deposit");
                    amount = scan.nextInt();

                    account[currentUser].deposit(amount);

                    machine_balance = machine_balance + amount;
                    break;

                case 3:
                    account[currentUser].display_balance();
                    break;

                case 4:
                    System.out.println("For double verification");
                    System.out.print("Enter current card number: ");
                    enteredCardNumber = scan.nextInt();
                    System.out.print("Enter current card pin: ");
                    enteredCardPin = scan.nextInt();
                    System.out.print("Enter new card pin: ");
                    int newCardPin = scan.nextInt();
                    System.out.println("");

                    if(card[currentUser].updatePin(enteredCardNumber, enteredCardPin, newCardPin)){
                        System.out.println("Successfully Updated");
                    }
                    else{
                        System.out.print("Error");
                    }

                default:
                    System.out.println("Please enter a valid choice!");
                    break;
            }
        }

        scan.close();
    }
}