package Invest;

/**
 * A class to represent a TemplatePosition within a Template (account).
 */
public class TemplatePosition {

    private String symbol;
    private double percentage;

    /**
     * Create a new template from a given symbol and percentage(OfTotal).
     * @param symbol the symbol for the position template
     * @param percentage the percentage(OfTotal) hoped to achieve.
     */
    public TemplatePosition(String symbol, double percentage) {
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
