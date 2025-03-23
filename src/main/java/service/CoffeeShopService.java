package service;

import model.ProductType;
import model.Receipt;
import model.ReceiptItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CoffeeShopService {
    private int beverageCount = 0;

    /**
     * Process user order considering user choices, selected promos and loyalty program.
     */
    public Receipt processOrder(List<ProductType> order, String paymentMethod) {
        double total = 0.0;
        int beverageCountInOrder = 0;
        int snackCountInOrder = 0;
        List<ReceiptItem> items = new ArrayList<>();
        List<ProductType> extras = new ArrayList<>();
        List<ProductType> beverages = new ArrayList<>();
        List<ProductType> snacks = new ArrayList<>();

        for (ProductType product : order) {
            if (product == null) continue;

            if (product.isExtra()) {
                extras.add(product);
            } else if (product.isBeverage()) {
                beverages.add(product);
                beverageCountInOrder++;
            } else {
                snacks.add(product);
                snackCountInOrder++;
            }
        }

        total = addBeveragesToTheReceipt(beverages, items, total);
        total = addRemainingProductTypeToTheReceipt(snacks, items, total);
        applyFreeExtras(beverageCountInOrder, snackCountInOrder, extras, items);
        total = addRemainingProductTypeToTheReceipt(extras, items, total);

        return new Receipt(items, total, paymentMethod);
    }

    /**
     * Add beverages to the receipt and validate every 5th beverage is free of charge
     */
    private double addBeveragesToTheReceipt(List<ProductType> beverages, List<ReceiptItem> items, double total) {
        for (ProductType beverage : beverages) {
            beverageCount++;

            boolean isFree = beverageCount % 5 == 0;

            items.add(new ReceiptItem(beverage.getProductName(), isFree ? 0.0 : beverage.getPrice(), isFree));
            if (!isFree) total += beverage.getPrice();
        }
        return total;
    }

    /**
     * Add remaining product (snack, extra) to the receipt
     */
    private static double addRemainingProductTypeToTheReceipt(List<ProductType> remainingProductType, List<ReceiptItem> items, double total) {
        for (ProductType productType : remainingProductType) {
            items.add(new ReceiptItem(productType.getProductName(), productType.getPrice(), false));
            total += productType.getPrice();
        }
        return total;
    }

    /**
     * Apply free extras — one for each beverage-snack pair
     */
    private static void applyFreeExtras(int beverageCountInOrder, int snackCountInOrder, List<ProductType> extras, List<ReceiptItem> items) {
        int promoSets = Math.min(beverageCountInOrder, snackCountInOrder);
        for (int i = 0; i < promoSets && !extras.isEmpty(); i++) {
            ProductType freeExtra = extras.stream().min(Comparator.comparingDouble(ProductType::getPrice)).get();
            extras.remove(freeExtra);
            items.add(new ReceiptItem(freeExtra.getProductName(), 0.0, true));
        }
    }
}
