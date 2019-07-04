import Invest.Account;
import Invest.Template;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Will handle the running of the program
 *
 *
 * Possible Changes:
 * !!!!Fix to work if CASH Template Percentage not 0
 *
 * Make Invest.Account and Invest.Position Abstract Classes
 * Use Maps instead of Lists
 * Add GUI
 * Make Price a separate file from account quantities
 *
 */
public class Program {

    private static final String ROTHIRA_NAME="229366432";
    private static final String REF_FILE_PATH="AccountFiles/";

    private static List<Account> accounts;

    /**
     * Main is entry point for program.  Serves to initialize accounts and
     * call mainLoop
     * @param args command line arguments; ignored.
     */
    public static void main(String[] args) {

        try {
            Account.readFile(REF_FILE_PATH);
        }
        catch (FileNotFoundException fileException) {
            System.err.println("The file could not be found in '" + REF_FILE_PATH + "'");
            exit(1);
        }

    }

    /**
     * Carries out the main program loop
     */
    public static void mainLoop() {
        System.out.println("Program Start");
        System.out.println("--------------------------");

        prompt();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        do {
            if (input.equals("Q")) {
                break;
            }

            int choice = Integer.parseInt(input) - 1;

            calculateReBalance(accounts.get(choice));

            prompt();
            input = sc.nextLine();

        }while(!input.equals("Q"));


    }

    /**
     * Displays a prompt for the next action
     */
    public static void prompt() {
        System.out.println("Select an account to run program on");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println("\t" + (i + 1) + ")\t " + accounts.get(i));
        }
        System.out.println("Enter \"Q\" to quit");
        System.out.print("> ");
    }

    /**
     * Calculates the rebalance
     * @param account the account to rebalance
     */
    public static void calculateReBalance(Account account) {
        account.setTemplate(Template.TEMPLATE);
        account.setTemplatePercentage();
        account.setTotalValue();
        account.sortPositions();
        System.out.println("\nBefore Rebalance");
        System.out.println("----------------------------------------");
        account.debug(false);
        System.out.println();

        while(!account.isRebalanceComplete()) {
            account.buyStock();
        }
        System.out.println("\nAfter Rebalance");
        System.out.println("---------------------------------------");
        account.debug(true);
        System.out.println();
    }

}
