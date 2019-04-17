package pl.wat.tim.mobile.user;

import android.text.TextUtils;
import android.util.Patterns;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class User implements Serializable {

    private String email;
    private String password;

    public void setUserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isValidEmail() {
        return this.email != null && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword() {
        return this.password != null && this.password.length() >= 6;
    }

    //TODO use retrofit instead of this method
    public boolean isValidCredential() {
        return this.email.equalsIgnoreCase("bardhan.jit@gmail.com") && this.password.equals("123456");
    }
}
