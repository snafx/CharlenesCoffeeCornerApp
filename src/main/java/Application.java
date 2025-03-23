import model.ProductType;
import model.Receipt;
import service.CoffeeShopService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static service.CommandLineOrdering.*;

public class Application {

    public static void main(String[] args) {

        CoffeeShopService service = new CoffeeShopService();
        Scanner scanner = new Scanner(System.in);
        List<ProductType> order = new ArrayList<>();

        System.out.println();
        System.out.println("==================================================");
        System.out.println("|    Welcome to the Charlene's Coffee Corner!    |");
        System.out.println("==================================================");
        System.out.println();

        while (true) {
            printStartingMenu();

            try {
                int startChoice = Integer.parseInt(scanner.nextLine());

                if (startChoice == 1) {
                    processDefaultOrder(order, service);
                    return;
                } else if (startChoice == 2) {
                    break;
                } else {
                    System.out.println("Invalid option. Please select 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1 or 2)!");
            }
        }

        while (true) {
            printCoffeeShopMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> processCoffeeSelection(scanner, order);
                    case 2 -> order.add(ProductType.BACON_ROLL);
                    case 3 -> order.add(ProductType.FRESH_ORANGE_JUICE);
                    case 4 -> {
                        String finalPayment = processPaymentMethodSelection(scanner);
                        Receipt receipt = service.processOrder(order, finalPayment);
                        receipt.print();
                        return;
                    }
                    default -> System.out.println("Invalid option, try again!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

}
