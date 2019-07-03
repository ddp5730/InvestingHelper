package Invest;

/**
 * A class to represent a PositionTemplate within a Template (account).
 */
public class PositionTemplate {

    private String symbol;
    private double percentage;

    /**
     * Create a new template from a given symbol and percentage(OfTotal).
     * @param symbol the symbol for the position template
     * @param percentage the percentage(OfTotal) hoped to achieve.
     */
    public PositionTemplate(String symbol, double percentage) {
        this.symbol = symbol;
        this.percentage = percentage;
    }

    /**
     * Returns the symbol
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the percentage
     * @return
     */
    public double getPercentage() {
        return percentage;
    }
}
