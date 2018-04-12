package com.pbus.utility;

import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validation {

    private final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private final String FULLNAME_PATTERN = "^[\\p{L} .'-]+$";
    private final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
    private final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private Pattern pattern;
    private Matcher matcher;

    public String getString(EditText editText){
        String getValue = editText.getText().toString().trim();
        return getValue;
    }
    

    public boolean isUserNameValid(EditText editText){
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(getString(editText));

        return matcher.matches();
    }

    public boolean isFullNameValid(EditText editText){
        Pattern pattern = Pattern.compile(FULLNAME_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(getString(editText));
        return matcher.find();
    }



    public boolean isPasswordValid(EditText editText) {
     return getString(editText).length()>=6;
    }

    public boolean isEmailValid(EditText editText) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(getString(editText));
        return matcher.find();
    }

}
