package Invest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Represents an investment account
 */
public class Account {

    private static final String FILE_PREFIX = "Portfolio_Position";

    private String name;
    private double totalValue;

    private List<Position> positions;
    private Template template;

    private boolean rebalanceComplete;

    /**
     * Create a new account from file
     * @param name The name of the account.
     */
    public Account(String name) {
        positions = new ArrayList<>();

        totalValue = 0;
        for (Position pos : positions) {
            totalValue += pos.getValue() * pos.getQuantity();
        }

        rebalanceComplete = false;
    }

    /**
     * Returns the flag that checks if the rebalance is complete in the
     * sortPositions method.
     * @return
     */
    public boolean isRebalanceComplete() {
        return rebalanceComplete;
    }

    /**
     * Will read the information from a file to initialize an account
     * @param filename the file to read from

    private void readFile(String filename) {
        try {
            Scanner reader = new Scanner(new File(filename));

            name = reader.nextLine();

            while(reader.hasNext()) {
                String Line = reader.nextLine();
                String[] tokens = Line.split(" ");
                Position position = new Position(tokens[0],
                        Double.parseDouble(tokens[1]),
                                Double.parseDouble(tokens[2]));
                positions.add(position);
            }
        }
        catch(IOException io) {
            System.err.println(io.getMessage());
            exit(1);
        }

    }
    */

    /**
     * This method will find the portfolio file in the account directory if it can be found.
     * @return The file if it exists; null if not.
     */
    private static File findInvestmentFile(String accountDir) {
        File dir = new File(accountDir);
        File[] files = dir.listFiles();

        File portfolioFile = null;
        for (File file : files) {
            String filename = file.getName();
            if (filename.startsWith(FILE_PREFIX)) {
                portfolioFile = file;
                break;
            }
        }

        return portfolioFile;
    }


    /**
     * This method shall be able to parse the investment file and create the relevent account files from it.
     * @param accountDir The directory of the account files.
     */
    public static void readFile(String accountDir) throws FileNotFoundException{

        File investmentFile = findInvestmentFile(accountDir);
        if (investmentFile == null) {
            throw new FileNotFoundException();
        }

        System.out.println(investmentFile.getName());
    }

    /**
     * Sets the proper template for comparison.
     * @param template the Invest.Template to comapare with
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * Returns the totalValue of the account
     * @return
     */
    public double getTotalValue() {
        return totalValue;
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
    public void setTotalValue() {
        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setAccountValue(totalValue);
        }
    }

    /**
     * Calculates the percentatgeOfTemplate for all positions in the account
     */
    public void setTemplatePercentage() {
        for (int i = 0; i < positions.size(); i++) {
            int index = template.indexOf(positions.get(i).getSymbol());
            double templatePecentage = template.positions.get(index).getPercentage();

            positions.get(i).setTemplatePercentage(templatePecentage);
        }
    }

    private double getValueAfterBuy() {
        double value = 0;
        for (Position pos : positions) {
            value += pos.getValueAfterBuy();
        }
        return value;
    }

    /**
     * Prints out some relevent information for debugging purposes.
     * May make into an official method
     */
    public void debug(boolean afterBuy) {
        System.out.printf(name + "| Original Value $%8.3f", totalValue);
        if (afterBuy) {
            System.out.printf("  Value after puchases $%8.3f\n", getValueAfterBuy());
        }
        else {
            System.out.println();
        }
        for (Position pos : positions) {
            System.out.print(pos.toString());
            if (afterBuy) {
                System.out.printf("  toBuy: %3d\n", pos.quantityToBuy);
            }
            else {
                System.out.println();
            }
        }

        //TODO
        //Give better symbol
    }

    /**
     * Sorts the Positions by their percentageOfTemplate in ascending order.
     * Also calculates if the rebalance has been completed by if the stock with
     * the lowest percentageOfTemplate is too expensive to buy.
     */
    public void sortPositions() {
        positions.sort((Position o1, Position o2) ->
                (int)(1000 * (o1.calculatePercentageOfTemplate() - o2.calculatePercentageOfTemplate())));
    }


    /**
     * Performs a linear search and returns the index of the Invest.Position with the symbol specified.
     * @param symbol the symbol to check
     * @return its index
     */
    public int indexOfSymbol(String symbol) {
        for (int i = 0; i < positions.size(); i++ ) {
            if (positions.get(i).getSymbol().equals(symbol)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Will imitate the process of buying a stock
     */
    public void buyStock() {
        //TODO
        //Add logic for buying stock and ending program.
        checkRebalanceComplete();
        if (!rebalanceComplete) {
            positions.get(0).buy();
            positions.get(indexOfSymbol("CASH")).removeQuantity(positions.get(0).getValue());
            System.out.printf("%3s should buy +1 share\n", positions.get(0).getSymbol());
            checkRebalanceComplete();
        }

    }

    /**
     * Will recalculate to see if the rebalance algorithm is complete
     */
    public void checkRebalanceComplete() {
        sortPositions();
        double cashValue = positions.get(indexOfSymbol("CASH")).getTotalValue();
        if (cashValue < positions.get(0).getValue()) {
            rebalanceComplete = true;
        }
    }
}
