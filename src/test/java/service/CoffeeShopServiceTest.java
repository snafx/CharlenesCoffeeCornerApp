package service;

import model.ProductType;
import model.Receipt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Constants.PAYMENT_CARD;
import static util.Constants.PAYMENT_CASH;

class CoffeeShopServiceTest {

    private final CoffeeShopService service = new CoffeeShopService();

    @ParameterizedTest
    @CsvSource({
            "SMALL_COFFEE, 2.50",
            "MEDIUM_COFFEE, 3.00",
            "LARGE_COFFEE, 3.50",
            "FRESH_ORANGE_JUICE, 3.95",
            "BACON_ROLL, 4.50"
    })
    void testProductPrices(String productType, double expectedPrice) {
        ProductType product = ProductType.valueOf(productType);
        assertEquals(expectedPrice, product.getPrice(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("provideOrdersWithExpectedTotals")
    void testOrderTotal(List<ProductType> order, double expectedTotal) {
        Receipt receipt = service.processOrder(order, PAYMENT_CARD);
        assertEquals(expectedTotal, receipt.total(), 0.01);
        receipt.print();
    }

    private static Object[][] provideOrdersWithExpectedTotals() {
        return new Object[][]{
                {List.of(ProductType.SMALL_COFFEE), 2.50},
                {List.of(ProductType.LARGE_COFFEE, ProductType.BACON_ROLL), 8.00},
                {List.of(ProductType.MEDIUM_COFFEE, ProductType.SPECIAL_ROAST_COFFEE), 3.90},
                {List.of(ProductType.MEDIUM_COFFEE, ProductType.BACON_ROLL, ProductType.EXTRA_MILK), 7.50}, // free cheapest extra
                {List.of(ProductType.SMALL_COFFEE, ProductType.SMALL_COFFEE, ProductType.SMALL_COFFEE, ProductType.SMALL_COFFEE, ProductType.LARGE_COFFEE), 10.00}, // 5th beverage is free
                {List.of(ProductType.SMALL_COFFEE, ProductType.FRESH_ORANGE_JUICE, ProductType.SMALL_COFFEE, ProductType.SMALL_COFFEE, ProductType.LARGE_COFFEE, ProductType.LARGE_COFFEE), 14.95} // 5th beverage is free, 6th is paid
        };
    }

    @Test
    void testFreeExtraApplied() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.LARGE_COFFEE,
                ProductType.BACON_ROLL,
                ProductType.EXTRA_MILK,
                ProductType.FOAMED_MILK
        ), PAYMENT_CARD);
        assertTrue(receipt.items().stream().anyMatch(item -> item.price() == 0.0 && item.name().equals("Extra Milk")));
        receipt.print();
    }

    @Test
    void testOnlyExtraOrdered() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.EXTRA_MILK,
                ProductType.FOAMED_MILK,
                ProductType.SPECIAL_ROAST_COFFEE,
                ProductType.FOAMED_MILK,
                ProductType.EXTRA_MILK,
                ProductType.EXTRA_MILK
        ), PAYMENT_CASH);
        assertEquals(6, receipt.items().size());
        assertEquals(2.80, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testFifthBeverageIsFree() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.SMALL_COFFEE,
                ProductType.LARGE_COFFEE,
                ProductType.FRESH_ORANGE_JUICE,
                ProductType.SMALL_COFFEE,
                ProductType.MEDIUM_COFFEE // 5th beverage is free
        ), PAYMENT_CASH);

        assertTrue(receipt.items().get(4).price() == 0.0 && receipt.items().get(4).name().equals("Medium Coffee"));
        assertTrue(receipt.items().stream().anyMatch(item -> item.price() == 0.0 && item.name().contains("Medium Coffee")));
        assertEquals(12.45, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testFifthBeverageIsFreeWhenOrderedMultipleBeverages() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.SMALL_COFFEE,
                ProductType.LARGE_COFFEE,
                ProductType.FRESH_ORANGE_JUICE,
                ProductType.SMALL_COFFEE,
                ProductType.MEDIUM_COFFEE, // 5th beverage is free
                ProductType.LARGE_COFFEE,
                ProductType.EXTRA_MILK,
                ProductType.FRESH_ORANGE_JUICE
        ), PAYMENT_CARD);
        assertTrue(receipt.items().stream().anyMatch(item -> item.price() == 0.0 && item.name().contains("Medium Coffee")));
        assertEquals(20.20, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testOnlySnacksOrdered() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL
        ), PAYMENT_CASH);
        assertEquals(6, receipt.items().size());
        assertEquals(27.00, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testBeverageAndSnackSetWithManySnacksOrdered() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.LARGE_COFFEE,
                ProductType.SPECIAL_ROAST_COFFEE // free extra
        ), PAYMENT_CARD);
        assertEquals(8, receipt.items().size());
        assertEquals(30.50, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testManySnacksSetsWithManySingleSnacksOrdered() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.BACON_ROLL,
                ProductType.LARGE_COFFEE,
                ProductType.LARGE_COFFEE,
                ProductType.LARGE_COFFEE,
                ProductType.SPECIAL_ROAST_COFFEE,
                ProductType.FOAMED_MILK,
                ProductType.FOAMED_MILK
        ), PAYMENT_CARD);
        assertEquals(12, receipt.items().size());
        assertEquals(37.50, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testExactMatchingPromoSetsAndExtras() {
        Receipt receipt = service.processOrder(List.of(
                ProductType.BACON_ROLL,
                ProductType.LARGE_COFFEE,
                ProductType.EXTRA_MILK, // free cheapest extra
                ProductType.BACON_ROLL,
                ProductType.MEDIUM_COFFEE,
                ProductType.SPECIAL_ROAST_COFFEE, // free extra
                ProductType.BACON_ROLL,
                ProductType.MEDIUM_COFFEE,
                ProductType.FOAMED_MILK, // free cheapest extra
                ProductType.FRESH_ORANGE_JUICE,
                ProductType.BACON_ROLL,
                ProductType.SMALL_COFFEE, // 5th beverage is free
                ProductType.FOAMED_MILK, // free cheapest extra
                ProductType.FRESH_ORANGE_JUICE,
                ProductType.FRESH_ORANGE_JUICE,
                ProductType.LARGE_COFFEE,
                ProductType.SPECIAL_ROAST_COFFEE
        ), PAYMENT_CARD);
        assertEquals(17, receipt.items().size());
        assertEquals(43.75, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testEmptyOrderPrintReceipt() {
        Receipt receipt = service.processOrder(List.of(), PAYMENT_CARD);
        assertEquals(0, receipt.items().size());
        assertEquals(0.00, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testSingleBeverageOrder() {
        List<ProductType> order = List.of(ProductType.SMALL_COFFEE);
        Receipt receipt = service.processOrder(order, PAYMENT_CARD);
        assertEquals(1, receipt.items().size());
        assertEquals("Small Coffee", receipt.items().getFirst().name());
        assertEquals(2.50, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testBeverageWithExtra() {
        List<ProductType> order = Arrays.asList(
                ProductType.LARGE_COFFEE,
                ProductType.FOAMED_MILK
        );
        Receipt receipt = service.processOrder(order, PAYMENT_CASH);
        assertEquals(2, receipt.items().size());
        assertEquals(4.00, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void test5thBeverageFree() {
        for (int i = 0; i < 4; i++) {
            service.processOrder(List.of(ProductType.SMALL_COFFEE), PAYMENT_CARD);
        }
        Receipt receipt = service.processOrder(List.of(ProductType.LARGE_COFFEE), PAYMENT_CASH);
        assertEquals(1, receipt.items().size());
        assertTrue(receipt.items().getFirst().isFree());
        assertEquals(0.0, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testOrderBeverageAndSnackWithFreeExtra() {
        List<ProductType> order = Arrays.asList(
                ProductType.LARGE_COFFEE,
                ProductType.BACON_ROLL,
                ProductType.SPECIAL_ROAST_COFFEE,
                ProductType.EXTRA_MILK //free cheapest extra
        );
        Receipt receipt = service.processOrder(order, PAYMENT_CARD);
        assertEquals(4, receipt.items().size());
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Extra Milk") && item.isFree()));
        assertEquals(8.90, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testOrderDoubleBeverageAndSnackWithTwoFreeExtra() {
        List<ProductType> order = Arrays.asList(
                ProductType.LARGE_COFFEE,
                ProductType.BACON_ROLL,
                ProductType.SPECIAL_ROAST_COFFEE,
                ProductType.LARGE_COFFEE,
                ProductType.BACON_ROLL,
                ProductType.EXTRA_MILK, //free cheapest extra
                ProductType.FOAMED_MILK //free cheapest extra
        );
        Receipt receipt = service.processOrder(order, PAYMENT_CASH);
        assertEquals(7, receipt.items().size());
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Extra Milk") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Foamed Milk") && item.isFree()));
        assertEquals(16.90, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testMixedOrderWithFreeBeverageAndFreeExtras() {
        for (int i = 0; i < 3; i++) {
            service.processOrder(List.of(ProductType.MEDIUM_COFFEE), PAYMENT_CARD);
        }
        List<ProductType> order = Arrays.asList(
                ProductType.LARGE_COFFEE,
                ProductType.FRESH_ORANGE_JUICE, //5th beverage is free
                ProductType.BACON_ROLL,
                ProductType.SPECIAL_ROAST_COFFEE, //free extra
                ProductType.EXTRA_MILK, //free cheapest extra
                ProductType.MEDIUM_COFFEE,
                ProductType.BACON_ROLL
        );
        Receipt receipt = service.processOrder(order, PAYMENT_CARD);
        assertEquals(7, receipt.items().size());
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Freshly squeezed orange juice (0.25l)") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Extra Milk") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Special Roast Coffee") && item.isFree()));
        assertEquals(15.50, receipt.total(), 0.01);
        receipt.print();
    }

    @Test
    void testMixedLargeOrderWithFreeBeveragesAndFreeExtras() {
        for (int i = 0; i < 3; i++) {
            service.processOrder(List.of(ProductType.MEDIUM_COFFEE), PAYMENT_CARD);
        }
        List<ProductType> order = Arrays.asList(
                ProductType.LARGE_COFFEE,
                ProductType.FRESH_ORANGE_JUICE, //5th beverage is free
                ProductType.BACON_ROLL,
                ProductType.SPECIAL_ROAST_COFFEE,
                ProductType.EXTRA_MILK, //free cheapest extra
                ProductType.SMALL_COFFEE,
                ProductType.BACON_ROLL,
                ProductType.FOAMED_MILK, //free cheapest extra
                ProductType.LARGE_COFFEE,
                ProductType.LARGE_COFFEE,
                ProductType.SMALL_COFFEE,
                ProductType.BACON_ROLL,
                ProductType.SPECIAL_ROAST_COFFEE, //free extra
                ProductType.SPECIAL_ROAST_COFFEE,
                ProductType.MEDIUM_COFFEE, //10th beverage is free
                ProductType.SMALL_COFFEE
        );
        Receipt receipt = service.processOrder(order, PAYMENT_CASH);
        assertEquals(16, receipt.items().size());
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Freshly squeezed orange juice (0.25l)") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Medium Coffee") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Extra Milk") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Foamed Milk") && item.isFree()));
        assertTrue(receipt.items().stream().anyMatch(item -> item.name().equals("Special Roast Coffee") && item.isFree()));
        assertEquals(33.30, receipt.total(), 0.01);
        receipt.print();
    }
}
