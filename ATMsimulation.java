import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

class User {
    private int account_balance;

    public int setAccountBalance(int max, int min){
        return (int) Math.floor(Math.random() * (max - min + 1) + min); 
    }
    public int getBalance(){
        return account_balance;
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

        // getter for card number
        public int getCardNumber() {
            return card_number;
        }
        // getter for card pin
        public int getCardPin() {
            return card_pin;
        }
        // updating pin of user
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

    // verifying if the user is accessing the ATM as admin or not
    private static boolean verifyAdmin(String code) {
        if(code.equalsIgnoreCase("admin"))
            return true;
        else
            return false;
    }

    // setting machine balance initially to a random value
    private static int setMachineBalance(int max, int min){
        return (int) Math.floor(Math.random() * (max - min + 1) + min); 
    }

    // verifying card number entered with current users card number in the database
    public static boolean verifyCardNumber(int enteredCardNumber, int cardNumber){
        if(enteredCardNumber == cardNumber) 
            return true;
        else 
            return false;
    }
    
    // verifying card pin entered with current users card pin in the database
    public static boolean verifyCardPin(int enteredCardPin, int cardPin){
        if(enteredCardPin == cardPin) 
            return true;
        else 
            return false;
    }
    
    // verifying if the amount input is in multiples of 100 and 500 or not
    public static boolean checkMultiples(int amount){
        if(amount%100 == 0 || amount%500 == 0)
            return true;
        else
            return false;
    }

    // implementing TIMEOUT library that makes output delayed
    public static void printWithDelays(String data, TimeUnit unit, long delay) throws InterruptedException {
        for (char ch:data.toCharArray()) {
            System.out.print(ch);
            unit.sleep(delay);
        }
    }

    // check the new pin entered in the required range of input
    public static boolean checkNewPin(int newCardPin){
        if(newCardPin > 99 && newCardPin < 1000)
            return true;
        else
            return false;
    }

    public static void writeObjectToFile(File file, int data_base_size, User[] user, User.Card[] card, Date date) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(date+"\n");
            for(int i=0; i<data_base_size; i++){
                bw.write("User:"+ (i+1) + "\n");
                bw.write("Account Balance: " + user[i].getBalance() + "\n");
                bw.write("Card Number: " + card[i].getCardNumber() + "\n");
                bw.write("Card Pin: " + card[i].getCardPin() + "\n\n"); 
            }
            bw.close();
        }
        catch (Exception e){
            return;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        Date date = java.util.Calendar.getInstance().getTime(); 
        File file = new File("users.txt"); 
        
        //since ATM can't generate new users and only works with the existing ones
        //generating a database with random values of PIN NUMBER and BALANCE
        final int data_base_size = 3;

        // generating database
        User[] user = new User[data_base_size];
        User.Account[] account = new User.Account[data_base_size];
        User.Card[] card = new User.Card[data_base_size];
        for (int i = 0; i < data_base_size; i++) {
            user[i] = new User();
            account[i] = user[i].new Account();
            card[i] = user[i].new Card();
        }
        writeObjectToFile(file, data_base_size, user, card, date);

        // setting machine balance
        int machine_balance = setMachineBalance(100000, 0);

        System.out.println("\nType 'start' to start the simulation");
        String helperString = scan.next();
        boolean simulation_status = false;
        // ! DON'T IGNORE !
        // *SMALL AUTHENTICATION SYSTEM, IN ORDER TO KNOW THE DETAILS, TYPE 'admin' (not case sensitive)*

        // printing database if the user was admin, else not printing
        while (!simulation_status) {
            if (helperString.equalsIgnoreCase("start")) {
                printWithDelays("Welcome to ATM Simulation    \n", TimeUnit.MILLISECONDS, 100);
                simulation_status = true;
            } else if (verifyAdmin(helperString)) {
                System.out.print("\nFetching Userbase Data");
                printWithDelays("....\n", TimeUnit.MILLISECONDS, 800);
                System.out.println("");
                for (int i = 0; i < 3; i++) {
                    System.out.println("User" + (i + 1) + " account details:");
                    account[i].display_balance();
                    
                    System.out.println("Card Number: " + card[i].getCardNumber());
                    System.out.println("Card Pin: " + card[i].getCardPin());
                    printWithDelays("\n", TimeUnit.MILLISECONDS, 1000);
                }
                System.out.println("Machine starting Balance: " + machine_balance);
                System.out.println("");
                printWithDelays("Welcome to ATM Simulation    \n", TimeUnit.MILLISECONDS, 100);
                simulation_status = true;
            }
            else {
                System.out.println("Try typing again buddy!\n");
                helperString = scan.next();
            }
        }

        
        int currentUser = -1;
        int enteredCardNumber;
        int enteredCardPin;

        // verification system for the user starts from here
        if (simulation_status) { 
            // input for the card number
            System.out.print("Please enter your Card Number: ");
            enteredCardNumber = Math.abs(scan.nextInt());

            for (int i = 0; i < 3; i++) {
                //verifying card number
                if (verifyCardNumber(enteredCardNumber, card[i].getCardNumber())) {
                    System.out.print("Please enter your Pin Number: ");
                    enteredCardPin = Math.abs(scan.nextInt());

                    // since the card number is correct, checking corresponding pin number for same card
                    if (verifyCardPin(enteredCardPin, card[i].getCardPin())) {
                        currentUser = i;
                        System.out.print("\nVerifying Credentials");
                        printWithDelays("...\n", TimeUnit.MILLISECONDS, 800);
                        System.out.println("\nUser" + (i + 1) + " verified, Welcome!");
                        // saving the index of user perfectly identified
                        break;
                    }
                    // if the corresponding pin is wrong
                    else {
                        while (!verifyCardPin(enteredCardPin, card[i].getCardPin())) {
                            // we'll exit if the user doesn't want ot continue with the simulation 
                            System.out.println("\nPlease re-enter a valid Pin Number or -1 to Quit Simulation");
                            enteredCardPin = Math.abs(scan.nextInt());
                            if (enteredCardPin == -1) {
                                simulation_status = false;
                                System.out.print("\nEnding Session");
                                printWithDelays(" ", TimeUnit.MILLISECONDS, 800);
                                System.out.println("Thank You!\nHave a nice day.");
                                System.exit(0);
                            }
                        }
                        if (verifyCardPin(enteredCardPin, card[i].getCardPin())) {
                            currentUser = i;
                            System.out.print("\nVerifying Credentials");
                            printWithDelays("...\n", TimeUnit.MILLISECONDS, 800);
                            System.out.println("\nUser" + (i + 1) + " verified, Welcome!");
                            // saving the index of user perfectly identified
                            break;
                        }
                    }
                }
                // if the card number is false
                else if(i == data_base_size - 1){
                    System.out.println("\nThere is no such user with card number '" + enteredCardNumber + "' in the data base.");
                    System.out.println("Enter 1 to continue with new card number or any other to exit simulator");
                    int choice = Math.abs(scan.nextInt());
                    if(choice == 1){
                        System.out.print("\nPlease re-enter a valid Card Number: ");
                        enteredCardNumber = Math.abs(scan.nextInt());
                        i = -1;
                    }
                    else {
                        // we'll exit if the user doesn't want ot continue with the simulation 
                        simulation_status = false;
                        System.out.print("\nEnding Session");
                        printWithDelays(" ", TimeUnit.MILLISECONDS, 800);
                        System.out.println("Thank You!\nHave a nice day.");
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
        
        // menu of inputs user will see as an interface
        while (simulation_status) {
            
            System.out.println("0 = Exit ATM");
            System.out.println("1 = Cash Withdraw");
            System.out.println("2 = Cash Deposit");
            System.out.println("3 = View Balance");
            System.out.println("4 = Change Pin\n");

            // scanning the choice of user
            int choice = Math.abs(scan.nextInt());
            String isReceipt;
            int amount;

            switch (choice) {
                case 0:
                    // we'll exit if the user doesn't want ot continue with the simulation 
                    simulation_status = false;
                    System.out.print("\nEnding Session");
                    printWithDelays("..\n", TimeUnit.MILLISECONDS, 800);
                    System.out.println("Thank You!\nHave a nice day.");
                    System.exit(0);
                    break;

                
                case 1:
                    System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                    amount = Math.abs(scan.nextInt());
                    // denomination of 100 and 500 only
                    while(checkMultiples(amount) != true){
                        System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                        amount = Math.abs(scan.nextInt());
                    }
                    // checking if machnie balance is lower than what user wants to withdraw
                    // giving user the chance to input by stating the available balance 
                    while (machine_balance < amount) {
                        System.out.println("Machine doesn't have sufficient cash to be withdrawn");
                        System.out.println("Please enter a value, less than " + machine_balance);
                        amount = Math.abs(scan.nextInt());
                        while(checkMultiples(amount) != true){
                            System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                            amount = Math.abs(scan.nextInt());
                        }
                    }
                    // if the entered amount was perfectly fine, we'll move further with the transaction
                    if(account[currentUser].withdraw(amount)){
                        System.out.print("\nTypical Machine Sounds");
                        printWithDelays("...\n", TimeUnit.MILLISECONDS, 1000);
                        
                        // printing receipt with date and time if required by user
                        System.out.println("Do you want to print a receipt (Yes/No)?\n(Save Trees).\n");
                        isReceipt = scan.next();
                        if(isReceipt.equalsIgnoreCase("No")){
                            
                            System.out.println("Your withdrawl of amount " + amount + " was successful!"); 
                            System.out.println("Thank You\n");
                        }
                        else if(isReceipt.equalsIgnoreCase("Yes")){
                            printWithDelays("\nPrinting..\n", TimeUnit.MILLISECONDS, 100);
                            System.out.println("Receipt");  
                            System.out.println(date);  
                            System.out.println("Your withdrawl of amount " + amount + " was successful!\n");
                        }
                        else {
                            System.out.println("Invalid Input");
                            printWithDelays("\nPrinting..\n", TimeUnit.MILLISECONDS, 100);
                            System.out.println("Receipt");  
                            System.out.println(date);  
                            System.out.println("Your withdrawl of amount " + amount + " was successful!\n");
                        }
                        // updating machine balance
                        writeObjectToFile(file, data_base_size, user, card, date);
                        machine_balance = machine_balance - amount;
                    }
                    break;

                case 2:
                    System.out.println("Please enter an amount to deposit");
                    amount = Math.abs(scan.nextInt());
                    while(checkMultiples(amount) != true){
                        System.out.println("Please enter an amount to be withdrawn in multiples of 100 or 500 only");
                        amount = Math.abs(scan.nextInt());
                    }
                    printWithDelays("", TimeUnit.MILLISECONDS, 1000);
                    account[currentUser].deposit(amount);

                    System.out.println("Do you want to print a receipt (Yes/No)?\n(Save Trees).\n");
                    isReceipt = scan.next();
                    if(isReceipt.equalsIgnoreCase("No")){
                        System.out.println("Your deposit of amount " + amount + " was succesful!");
                        System.out.println("Thank You\n");
                    }
                    else if(isReceipt.equalsIgnoreCase("Yes")){
                        printWithDelays("\nPrinting..\n", TimeUnit.MILLISECONDS, 100);
                        System.out.println("\nReceipt");  
                    System.out.println(date);
                    System.out.println("Your deposit of amount " + amount + " was succesful!\n");
                    }
                    else {
                        System.out.println("Invalid Input");
                        printWithDelays("\nPrinting..\n", TimeUnit.MILLISECONDS, 100);
                        System.out.println("\nReceipt");  
                        System.out.println(date);
                        System.out.println("Your deposit of amount " + amount + " was succesful!\n");
                    }
                        
                    writeObjectToFile(file, data_base_size, user, card, date);
                    machine_balance = machine_balance + amount;
                    break;

                case 3:
                    System.out.print("Fetching User Balance");
                    printWithDelays("...\n", TimeUnit.MILLISECONDS, 800);
                    System.out.println(date);
                    account[currentUser].display_balance();
                    System.out.println("");
                    break;

                case 4:
                    System.out.println("\nFor double verification, entering wrong credentials will result in error.");
                    System.out.print("Enter current card number: ");
                    enteredCardNumber = Math.abs(scan.nextInt());
                    System.out.print("Enter current card pin: ");
                    enteredCardPin = Math.abs(scan.nextInt());
                    System.out.print("Enter new card pin: ");
                    int newCardPin = Math.abs(scan.nextInt());

                    while(!checkNewPin(newCardPin)){
                        System.out.print("Enter 3 digit card pin: ");
                        newCardPin = Math.abs(scan.nextInt());
                    }
                    while(enteredCardPin == newCardPin){
                        System.out.print("Enter new card pin: ");
                        newCardPin = Math.abs(scan.nextInt());
                    }

                    System.out.println("");

                    if(card[currentUser].updatePin(enteredCardNumber, enteredCardPin, newCardPin)){
                        System.out.print("\nUpdating User Database");
                        printWithDelays("...", TimeUnit.MILLISECONDS, 1000);                        
                        printWithDelays("\nIntentional delay to imply its important process    ", TimeUnit.MILLISECONDS, 100);
                        System.out.println("\nSuccessfully Updated\n");
                    }
                    else{
                        printWithDelays("", TimeUnit.MILLISECONDS, 1000);
                        System.out.print("Error, please try again\n");
                    }
                    writeObjectToFile(file, data_base_size, user, card, date);
                    break;

                default:
                    System.out.println("Please enter a valid choice!");
                    break;
            }
        }

        scan.close();
    }
}