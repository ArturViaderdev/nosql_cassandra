/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import java.util.Arrays;
import java.util.List;

/**
 * Exception from the command/input
 *
 * @author alu2014080
 */
public class InputException extends CodeException {

    // Exception codes
    public static final int FILL_SCORE = 0;
    public static final int INCORRECT_CHOICE = 1;
    public static final int EXCEED_CHARACTERS = 2;
    public static final int INCORRECT_PASSWORD = 3;
    public static final int SAME_PASSWORDS = 4;
    public static final int NEW_AND_CHECK_DIFFERENT_PASSWORDS = 5;
    public static final int VALUE_LENGTH = 6;
    public static final int NEGATIVE_VALUE = 7;
    public static final int MINIMUM_VALUE = 8;

    // Exception messages
    private final List<String> messages = Arrays.asList(
            "COMPLETE THE INFORMATION",
            "CHOOSE BETWEEN THE CHOICES GIVEN",
            "DO NOT WRITE MORE THAN THE LIMIT OF CHARACTERS REQUESTED",
            "INCORRECT WRITTEN PASSWORD",
            "CURRENT AND NEW PASSWORD CANNOT BE THE SAME",
            "NEW AND VERIFYING PASSWORDS ARE NOT THE SAME",
            "THIS INFORMATION HAS TO HAVE THE SAME DIGITS AS REQUESTED",
            "YOU CANNOT INSERT NEGATIVE OR ZERO VALUE",
            "YOU HAVE TO INSERT A VALUE BEYOND THE MINIMUM VALUE"
    );

    public InputException(int code) {
        super(code);
    }

    @Override
    public String getMessage() {
        return messages.get(getCode());
    }

}
