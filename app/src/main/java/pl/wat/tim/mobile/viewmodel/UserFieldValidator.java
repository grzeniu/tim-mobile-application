package pl.wat.tim.mobile.viewmodel;

import android.text.TextUtils;
import android.util.Patterns;

public class UserFieldValidator {

    public static boolean isValidEmail(String email) {
        return email != null
                && !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null
                && password.length() >= 6;
    }

}
