package model;

import java.util.List;
import java.util.logging.Logger;

import static util.Constants.*;

public record Receipt(List<ReceiptItem> items, double total, String paymentMethod) {

    private static final Logger logger = Logger.getLogger(Receipt.class.getName());

    private String getUniqueReceiptNumber() {
        return Integer.toHexString((int) System.currentTimeMillis()).toUpperCase();
    }

    public void print() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(LINE_BREAK).append(LINE_BREAK)
                .append(SEPARATION_LINE_EQUALS_SIGN_RECEIPT).append(LINE_BREAK)
                .append("|               Charlene's Coffee Corner               |").append(LINE_BREAK)
                .append("|                    Swiss Re Office                   |").append(LINE_BREAK)
                .append("|             Soodring 6, 8134 Adliswil, ZH            |").append(LINE_BREAK)
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append(SEPARATION_LINE_DASH).append(LINE_BREAK);
        items.forEach(item -> sb.append(item).append(LINE_BREAK));
        sb
                .append(SEPARATION_LINE_DASH).append(LINE_BREAK)
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append(String.format("%-42s %6.2f CHF  |%n", "| Total:", total))
                .append(SEPARATION_LINE_EQUALS_SIGN_RECEIPT).append(LINE_BREAK)
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append("|               *** Client's receipt ***               |").append(LINE_BREAK)
                .append(String.format("%-44s %s %n", "| Time:", ORDER_TIME + "  |"))
                .append(String.format("%-42s %s %n", "| Date:", ORDER_DATE + "  |"))
                .append(String.format("%-44s %s %n", "| Receipt #:", getUniqueReceiptNumber() + "  |"))
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append("|                   *** Payment ***                    |").append(LINE_BREAK);

        if (paymentMethod.equals(PAYMENT_CARD)) {
            sb.append("|                ").append(PAYMENT_CARD).append("                 |").append(LINE_BREAK);
        } else {
            sb.append("|                ").append(PAYMENT_CASH).append("                |").append(LINE_BREAK);
        }

        sb
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append("|                Thank you and goodbye!                |").append(LINE_BREAK)
                .append(EMPTY_LINE_WITH_PIPES).append(LINE_BREAK)
                .append(SEPARATION_LINE_EQUALS_SIGN_RECEIPT).append(LINE_BREAK);

        logger.info(sb.toString());
    }
}
