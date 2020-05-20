package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

/**
 * Represents an investment account
 */
public class Account {

    private static final String FILE_PREFIX = "Portfolio_Position";
    private static final String CONFIG_FILE = "default.conf";
    private static final String[] CASH_CODES = {"SPAXX**", "CORE**"};
    private static final String CASH_SYMBOL = "CASH";

    private String name;
    private String code;
    private double totalValue;

    private List<Position> positions;
    private CashPosition cashPosition;
    private Template template;

    private boolean rebalanceComplete;

    /**
     * Create a new account from file
     * @param name The name of the account.
     * @param code The code for this account in the .csv file.
     */
    public Account(String name, String code, Template template) {
        positions = new ArrayList<>();

        this.name = name;
        this.code = code;
        totalValue = 0;

        rebalanceComplete = false;
        this.setTemplate(template);
    }

    /**
     * Returns the flag that checks if the rebalance is complete in the
     * sortPositions method.
     * @return the rebalanceComplete tag.
     */
    public boolean isRebalanceComplete() {
        return rebalanceComplete;
    }

    /**
     * This method will find the portfolio file in the account directory if it can be found.
     * @return The file if it exists; null if not.
     */
    private static File findInvestmentFile(String accountDir) throws FileNotFoundException {
        File dir = new File(accountDir);
        File[] files = dir.listFiles();

        File portfolioFile = null;
        try {
            for (File file : files) {
                String filename = file.getName();
                if (filename.startsWith(FILE_PREFIX)) {
                    portfolioFile = file;
                    break;

                }
            }

            return portfolioFile;
        }
        catch (NullPointerException npe) {
            throw new FileNotFoundException();
        }
    }


    /**
     * This method shall be able to parse the investment file and create the relevent account files from it.
     * @param accountDir The directory of the account files.
     */
    public static List<Account> readFile(String accountDir) throws FileNotFoundException{

        File investmentFile = findInvestmentFile(accountDir);
        if (investmentFile == null) {
            throw new FileNotFoundException();
        }

        List<Account> portfolio = readConfigFile();

        try {
            Scanner reader = new Scanner(investmentFile);

            while(reader.hasNext()) {
                String Line = reader.nextLine();
                Line = Line.strip();

                if (Line.equals("")) {
                    break;
                }

                String[] tokens = Line.split(",");
                for (int i = 0; i < 3; i++) {
                    tokens[i] = tokens[i].substring(1, tokens[i].length() - 1);
                }

                String accountCode = tokens[0];

                Account thisAccount = null;
                for (Account account : portfolio) {
                    if (account.code.equals(accountCode)) {
                        thisAccount = account;
                        break;
                    }
                }

                // Break out if the account does not exist in file already.
                if (thisAccount == null) {
                    continue;
                }

                String posName = tokens[1];
                for (String str : CASH_CODES) {
                    if (str.equals(posName)) {
                        posName = CASH_SYMBOL;
                        break;
                    }
                }
                double posQuantity = Double.parseDouble(tokens[3]);
                double posValue = Double.parseDouble(tokens[4].substring(1));

                if (posName.equals(CASH_SYMBOL)) {
                    thisAccount.cashPosition = new CashPosition(posName, posValue, posQuantity);
                }
                else {
                    Position position = new Position(posName, posValue, posQuantity);
                    thisAccount.positions.add(position);
                }


            }
        }
        catch (IOException io) {
            System.err.println(io.getMessage());
            exit(1);
        }

        addNewTemplatePositions(portfolio);

        return portfolio;
    }

    /**
     * Read the config file and initialize the accounts as such.
     * @return A list of the accounts to process.
     */
    private static List<Account> readConfigFile() throws FileNotFoundException{
        List<Account> portfolio = new ArrayList<>();

        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            throw new FileNotFoundException();
        }

        Scanner reader = new Scanner(configFile);
        while (reader.hasNext()) {
            String line = reader.nextLine();
            String[] tokens = line.split(" ");

            Template template = new Template(tokens[2]);

            portfolio.add(new Account(tokens[0], tokens[1], template));
        }

        return portfolio;
    }

    /**
     * This method will add any new positions in the template to the account accordingly.
     * @param portfolio the portfolio to add new positions to.
     */
    private static void addNewTemplatePositions(List<Account> portfolio) {
        Map<String, Double> userInputs = new HashMap<>();

        for (Account account : portfolio) {
            Set<String> symbolsInAccount = new HashSet<>();
            for (Position position : account.positions) {
                symbolsInAccount.add(position.getSymbol());
            }
            symbolsInAccount.add(account.cashPosition.getSymbol());

            for (String symbol : account.getTemplate().getPositions().keySet()) {
                if (!symbolsInAccount.contains(symbol)) {
                    if (!userInputs.containsKey(symbol)) {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("What was the most recent price of " + symbol + ": ");
                        double value = sc.nextDouble();

                        userInputs.put(symbol, value);
                    }

                    Position pos = new Position(symbol, userInputs.get(symbol), 0);
                    account.positions.add(pos);
                }
            }
        }
    }

    /**
     * This method will initialize the total value
     */
    private void initializeTotalValue() {
        for (Position pos : positions) {
            totalValue += pos.getTotalValue();
        }
        totalValue += cashPosition.getTotalValue();
    }

    /**
     * Sets the proper template for comparison.
     * @param template the Invest.Template to comapare with
     */
    private void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * Returns the totalValue of the account
     * @return the total value of the account
     */
    double getTotalValue() {
        return totalValue;
    }

    /**
     * Return the template associated with this account.
     * @return the template
     */
    private Template getTemplate() {
        return template;
    }

    /**
     * Returns the name of the account as a String
     * @return the account name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Calculates the percentageOfTotal for all positions
     */
    private void setTotalValueInPos() {
        for (Position position : positions) {
            position.setAccountValue(totalValue);
        }
        cashPosition.setAccountValue(totalValue);
    }

    /**
     * Calculates the percentatgeOfTemplate for all positions in the account
     */
    private void setTemplatePercentage() {
        for (Position position : positions) {
            String symbol = position.getSymbol();
            double templatePercentage;
            if (template.getPositions().containsKey(symbol)) {
                templatePercentage = template.getPositions().get(symbol).getPercentage();
            }
            else {
                templatePercentage = 0;
            }

            position.setTemplatePercentage(templatePercentage);
        }

        String symbol = cashPosition.getSymbol();
        double templatePercentage;
        if (template.getPositions().containsKey(symbol)) {
            templatePercentage = template.getPositions().get(symbol).getPercentage();
        }
        else {
            templatePercentage = 0;
        }

        cashPosition.setTemplatePercentage(templatePercentage);
        cashPosition.setReservedCashValue(this);
    }

    /**
     * Prints out some relevent information for debugging purposes.
     * May make into an official method
     */
    public void debug(boolean afterBuy) {
        System.out.printf(name + "| Value $%8.3f", totalValue);

        System.out.println();

        for (Position pos : positions) {
            System.out.print(pos.toString());
            if (afterBuy) {
                System.out.printf("  toBuy: %3d\n", pos.quantityToBuy);
            }
            else {
                System.out.println();
            }
        }
        System.out.println(cashPosition);

    }

    /**
     * Sorts the Positions by their percentageOfTemplate in ascending order.
     * Also calculates if the rebalance has been completed by if the stock with
     * the lowest percentageOfTemplate is too expensive to buy.
     */
    private void sortPositions() {
        positions.sort((Position o1, Position o2) ->
                (int)(1000 * (o1.calculatePercentageOfTemplate() - o2.calculatePercentageOfTemplate())));
    }

    /**
     * Will imitate the process of buying a stock
     */
    public void buyStock() {

        checkRebalanceComplete();
        if (!rebalanceComplete) {
            positions.get(0).buy();
            cashPosition.removeQuantity(positions.get(0).getValue());
            System.out.printf("%3s should buy +1 share\n", positions.get(0).getSymbol());
            checkRebalanceComplete();
        }

    }

    /**
     * Will recalculate to see if the rebalance algorithm is complete
     */
    private void checkRebalanceComplete() {
        sortPositions();
        double cashValue = cashPosition.getFreeCashValue();
        if (cashValue < positions.get(0).getValue()) {
            rebalanceComplete = true;
        }
    }

    public void initializeAccount() {
        initializeTotalValue();
        setTemplatePercentage();
        setTotalValueInPos();
        sortPositions();
    }

}
