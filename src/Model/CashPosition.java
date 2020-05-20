package Model;

/**
 * This class is like a position, but it has some additional functionality specific to the CASH allocation for a given
 * account.
 */
class CashPosition extends Position{

    private double reservedCashValue;

    CashPosition(String symbol, double value, double quantity) {
        super(symbol, value, quantity);
    }

    /**
     * Set the percentage of the template
     * @param percent the percentage
     */
    @Override
    void setTemplatePercentage(double percent) {
        templatePercent = percent;
    }

    /**
     * This method will initialize the reservedCashValue and freeCashValue member variables
     * @param account The account in which this position lies.
     */
    void setReservedCashValue(Account account) {
        reservedCashValue = templatePercent * account.getTotalValue();
    }

    /**
     * Return the freeCashValue of the CASH position
     * @return double.  freeCashValue
     */
    double getFreeCashValue() {
        return this.getTotalValue() - reservedCashValue;
    }
}
