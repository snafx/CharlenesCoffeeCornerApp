package service;

import model.ProductType;

import java.util.List;
import java.util.Scanner;

import static util.Constants.*;

public class CommandLineOrdering {

    private CommandLineOrdering() {
    }

    public static void processCoffeeSelection(Scanner scanner, List<ProductType> order) {
        printCoffeeSizes();
        int size = Integer.parseInt(scanner.nextLine());
        try {
            switch (size) {
                case 1 -> order.add(ProductType.valueOf("SMALL_COFFEE"));
                case 2 -> order.add(ProductType.valueOf("MEDIUM_COFFEE"));
                case 3 -> order.add(ProductType.valueOf("LARGE_COFFEE"));
                default -> System.out.println("Invalid coffee size option, try again!");
            }
            while (true) {
                printCoffeeExtras();
                int extra = Integer.parseInt(scanner.nextLine());

                switch (extra) {
                    case 1 -> order.add(ProductType.valueOf("EXTRA_MILK"));
                    case 2 -> order.add(ProductType.valueOf("FOAMED_MILK"));
                    case 3 -> order.add(ProductType.valueOf("SPECIAL_ROAST_COFFEE"));
                    case 4 -> System.out.println("No more extras added.");
                    default -> System.out.println("Invalid extra option, try again!");
                }
                if (extra == 4) {
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid size or extra! Please try again.");
        }
    }

    public static String processPaymentMethodSelection(Scanner scanner) {
        printPaymentMethods();
        int paymentMethod = Integer.parseInt(scanner.nextLine());
        String finalPayment = "";
        switch (paymentMethod) {
            case 1 -> finalPayment = PAYMENT_CASH;
            case 2 -> finalPayment = PAYMENT_CARD;
            default -> System.out.println("Invalid payment method, please try again!");
        }
        return finalPayment;
    }

    public static void printCoffeeShopMenu() {
        System.out.println(SEPARATION_LINE_EQUALS_SIGN_MENU);
        System.out.println("| Please select a product from our delicious menu: |");
        System.out.println(SEPARATION_LINE_EQUALS_SIGN_MENU);
        System.out.println("\nType ONLY menu item numbers!");
        System.out.println(SEPARATION_LINE_EQUALS_SIGN_MENU);
        System.out.printf(MENU_FORMATTING_PATTERN, "| Menu:", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| 1 - Coffee (small, medium, large)", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| Extras:", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "|  * extra milk", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "|  * foamed milk", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "|  * special roast coffee", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| 2 - Bacon Roll, 4.50 CHF", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| 3 - Fresh Orange Juice, 3.95 CHF", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| ", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| 4 - Finish the order", " |");
        System.out.printf(MENU_FORMATTING_PATTERN, "| ", " |");
        System.out.println(SEPARATION_LINE_EQUALS_SIGN_MENU);
        System.out.print("\nChoose an option: ");
    }

    public static void printCoffeeSizes() {
        System.out.println("\nChoose coffee size:");
        System.out.println("1 - Small, 2.50 CHF");
        System.out.println("2 - Medium, 3.00 CHF");
        System.out.println("3 - Large, 3.50 CHF");
        System.out.print("Coffee size selected: ");
    }

    public static void printCoffeeExtras() {
        System.out.println("\nAny extras?");
        System.out.println("1 - Extra Milk, 0.30 CHF");
        System.out.println("2 - Foamed Milk, 0.50 CHF");
        System.out.println("3 - Special Roast Coffee, 0.90 CHF");
        System.out.println("4 - None");
        System.out.print("Extra selected: ");
    }

    public static void printPaymentMethods() {
        System.out.println("\nPlease select payment method: ");
        System.out.println("1 - Payment by cash.");
        System.out.println("2 - Payment by credit card.");
        System.out.print("Payment method selected: ");
    }
}
