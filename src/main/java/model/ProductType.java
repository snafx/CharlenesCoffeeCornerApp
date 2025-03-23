package model;

public enum ProductType {

    SMALL_COFFEE("Small Coffee", 2.50, true, false),
    MEDIUM_COFFEE("Medium Coffee", 3.00, true, false),
    LARGE_COFFEE("Large Coffee", 3.50, true, false),
    FRESH_ORANGE_JUICE("Freshly squeezed orange juice (0.25l)", 3.95, true, false),
    BACON_ROLL("Bacon Roll", 4.50, false, false),
    EXTRA_MILK("Extra Milk", 0.30, false, true),
    FOAMED_MILK("Foamed Milk", 0.50, false, true),
    SPECIAL_ROAST_COFFEE("Special Roast Coffee", 0.90, false, true);

    private final String productName;
    private final double price;
    private final boolean isBeverage;
    private final boolean isExtra;

    ProductType(String productName, double price, boolean isBeverage, boolean isExtra) {
        this.productName = productName;
        this.price = price;
        this.isBeverage = isBeverage;
        this.isExtra = isExtra;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBeverage() {
        return isBeverage;
    }

    public boolean isExtra() {
        return isExtra;
    }
}
