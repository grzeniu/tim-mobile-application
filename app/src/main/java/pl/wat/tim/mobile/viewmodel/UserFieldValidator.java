package pl.wat.tim.mobile.viewmodel;

import android.text.TextUtils;
import android.util.Patterns;

class UserFieldValidator {

    static boolean isValidEmail(String email) {
        return email != null
                && !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    static boolean isValidPassword(String password) {
        return password != null
                && password.length() >= 6;
    }

}
