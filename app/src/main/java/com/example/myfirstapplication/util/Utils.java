package com.example.myfirstapplication.util;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Utils {


    public static boolean validateTextField(EditText editText, String displayName) {
        String data = editText.getText().toString().trim();
        if (data != null && data.isEmpty()) {
            editText.setError(displayName + " cannot be empty.");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public static boolean validateTextFieldPassword(EditText editText, String displayName) {
        String data = editText.getText().toString().trim();
        if (data != null && data.isEmpty()) {
            editText.setError(displayName + " cannot be empty.");
            return false;
        } else if (data.length() < 5) {
            editText.setError(displayName + " must greater than 5 characters.");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

}
