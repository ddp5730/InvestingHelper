package Model;

/**
 * This class will represent a single position.
 */
public class Position implements Comparable<Position>{

    private final String symbol;
    private final double value;
    private double quantity;
    private double accountValue;

    double templatePercent;

    int quantityToBuy;

    /**
     * Create a new Invest.Position
     * @param symbol the symbol for the position
     * @param value its current value
     * @param quantity the current quanity
     */
    Position(String symbol, double value, double quantity) {
        this.symbol = symbol;
        this.value = value;
        this.quantity = quantity;

        templatePercent = 0;
        accountValue = 0;
        quantityToBuy = 0;
    }

    /**
     * Returns the quantity
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    private double getTotalQuantity() {
        return quantity + quantityToBuy;
    }

    /**
     * Returns the value
     * @return the value
     */
    double getValue() {
        return value;
    }

    /**
     * Returns the symbol
     * @return the symbol
     */
    String getSymbol() {
        return symbol;
    }

    /**
     * Returns the PercentageOfTotal
     * @return the percentage of total.
     */
    private double getPercentageOfTotal() {
        return getValueAfterBuy() / accountValue;
    }

    /**
     * Returns the percentageOfTemplate
     * @return the percentageOfTemplate
     */
    private double getPercentageOfTemplate() {
        return calculatePercentageOfTemplate();
    }

    double getTotalValue() {
        return quantity * value;
    }

    private double getValueAfterBuy() {
        return getTotalQuantity() * value;
    }
    /**
     * Calculates the percentage value of a total that this Invest.Position is worth.
     * @return the percentage value.
     */
    private double calculatePercentageOfTotal() {

        return (getTotalQuantity() * value) / accountValue;
    }

    void setAccountValue(double value) {
        accountValue = value;
    }

    void setTemplatePercentage(double percent) {
        templatePercent = percent;
    }

    /**
     * Calculates the percentageOfTemplate for this Position and assigns it
     */
    double calculatePercentageOfTemplate() {
        return calculatePercentageOfTotal() / templatePercent;
    }

    /**
     * Prints out the toString for this position including the:
     * symbol | Quantity Value/Share PercentOfTemplate
     * @return the string
     */
    @Override
    public String toString() {
        return String.format("%4s | TotalValue: $%8.3f Quanity: %8.2f  Value/Share: $%8.3f   PercentOfAccount:  %4.1f%%   " +
                        "/ %4.1f%%   PercentOfTemplate: %8.1f%%",
                symbol, getValueAfterBuy(), quantity, value, getPercentageOfTotal() * 100,
                templatePercent * 100, getPercentageOfTemplate() * 100);
    }

    /**
     * Compares instances of Positions, allows natural ordering.
     * @param other The Position to be compared to.
     * @return a negative if this is less, 0 if equal, and positive if this is less.
     */
    public int compareTo(Position other) {
        return (int)(1000*(other.calculatePercentageOfTemplate() - this.calculatePercentageOfTemplate()));
    }

    double buy() {
        quantityToBuy++;
        return value;
    }

    void removeQuantity(double value) {
        quantity -= value;
    }
}
