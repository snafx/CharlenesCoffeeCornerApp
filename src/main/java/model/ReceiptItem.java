package model;

public record ReceiptItem(String name, double price, boolean isFree) {

    @Override
    public String toString() {
        if (isFree) {
            if (name.equals(ProductType.SMALL_COFFEE.getProductName()) || name.equals(ProductType.MEDIUM_COFFEE.getProductName()) || name.equals(ProductType.LARGE_COFFEE.getProductName()) || name.equals(ProductType.FRESH_ORANGE_JUICE.getProductName())) {
                return String.format("%-42s (5th Free) %s", "| " + name, " |");
            }
            return String.format("%-40s (Free Extra) %s", "| " + name, " |");
        } else {
            return String.format("%-44s %.2f CHF %s", "| " + name, price, " |");
        }
    }
}
