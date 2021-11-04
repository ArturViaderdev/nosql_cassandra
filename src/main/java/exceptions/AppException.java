/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import java.util.Arrays;
import java.util.List;

/**
 * Exceptions from the Application
 *
 * @author alu2014080
 */
public class AppException extends CodeException {

    // Exception codes
    public static final int EXISTING_USER = 0;
    public static final int USER_NOT_FOUND = 1;
    public static final int NO_USERS_FOUND = 2;
    public static final int INCORRECT_CURRENT_PASSWORD = 3;
    public static final int SAME_CURRENT_AND_NEW_PASSWORD = 4;
    public static final int DIFFERENT_NEW_AND_VERIFIED_PASSWORD = 5;
    public static final int NO_INCIDENCES_FOUND = 6;
    public static final int NO_EVENTS_FOUND = 7;
    public static final int NO_LOGIN_REGISTERED = 8;
    public static final int ERROR = 9;

    public AppException(int code) {
        super(code);
    }

    private final List<String> messages = Arrays.asList(
            "USERNAME ALREADY EXISTS",
            "USER NOT FOUND",
            "THERE AREN'T ANY USERS CREATED",
            "INCORRECT PASSWORD",
            "NEW PASSWORD CANNOT BE THE SAME AS THE CURRENT ONE",
            "NEW PASSWORD AND THE VERIFYING ONE ARE NOT THE SAME",
            "NO INCIDENCES FOUND FOR THIS EMPLOYEE",
            "NO EVENTS FOUND FOR THIS EMPLOYEE",
            "THERE ISN'T ANY LOGIN REGISTERED",
            "ERROR IN THE APP. TRY AGAIN"
    );

    @Override
    public String getMessage() {
        return messages.get(getCode());
    }
}
