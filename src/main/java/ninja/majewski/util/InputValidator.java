package ninja.majewski.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    public static boolean isStringOk(String s) {
        return !StringUtils.isEmpty(s);
    }

    public static boolean isPhoneOk(String phone) {
        // TODO Regex for phone validation ??
        return isStringOk(phone);
    }

    public static boolean isEmailOk(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
