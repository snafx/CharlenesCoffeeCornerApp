package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {

    private Constants() {
    }

    public static final String LINE_BREAK = "\n";
    public static final String MENU_FORMATTING_PATTERN = "%-48s %s %n";
    public static final String SEPARATION_LINE_EQUALS_SIGN_MENU = "==================================================";
    public static final String SEPARATION_LINE_EQUALS_SIGN_RECEIPT = "|======================================================|";
    public static final String SEPARATION_LINE_DASH = "|------------------------------------------------------|";
    public static final String EMPTY_LINE_WITH_PIPES = "|                                                      |";

    public static final String ORDER_DATE = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    public static final String ORDER_TIME = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

    public static final String PAYMENT_CASH = "Paid in full with cash";
    public static final String PAYMENT_CARD = "Paid with credit card";
}
