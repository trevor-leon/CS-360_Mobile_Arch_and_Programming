package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    // Controller

    private LoginDatabase loginDB;
    private Button loginButton;
    private Button createAccountButton;
    private EditText usernameText;
    private EditText passwordText;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enable a background thread on a later version
        // Get a one-of of the LoginDatabase, and then initialize logins.
        loginDB = LoginDatabase.getInstance(getApplicationContext());

        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        forgotPassword = findViewById(R.id.forgotPassword);

        loginButton.setEnabled(false);
        createAccountButton.setEnabled(false);
        forgotPassword.setEnabled(false);

        usernameText = findViewById(R.id.editTextTextUsername);
        passwordText = findViewById(R.id.editTextTextPassword);

        loginButton.setOnClickListener(view -> {
            /*
              If the user tries to login and the username and password are in the database,
              then they should be redirected into the inventory activity. If they are not in
              the database, they should instead be shown a toast explaining what went wrong.
              Username/password verification: If the logins HashMap's get function returns
              the password for the specified, and that password matches the provided password; open
              the inventory activity. Else, show "Invalid Email or password." toast.
             */
            boolean passwordVerified = Objects.equals(loginDB.getLogins().get(usernameText.getText().toString()),
                    passwordText.getText().toString());
            if (passwordVerified) {
                // Open the onHand inventory screen
                Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(intent);
            } else {
                // Else, show a toast explaining "Invalid Email or password"
                Toast.makeText(getApplicationContext(),
                        R.string.invalid_username_password_toast, Toast.LENGTH_LONG).show();
                usernameText.setText("");
                passwordText.setText("");
            }
        });

        // Set the OnClickListener for the createAccountButton
        createAccountButton.setOnClickListener(view -> {
            // TODO: Sanitize the Email and passwords before creation (NOT REQUIRED)
            /*
              If the user tries to create an account, and the login is not already in the data-
              base, then the login should be added to the database. If the login is in the
              database, then a toast should explain that there is already an account with
              the associated email address.
             */
            boolean loginAdded = loginDB.addLogin(usernameText.getText().toString(),
                    passwordText.getText().toString());
            if (loginAdded) {
                // Open the onHand inventory screen
                Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(intent);
            } else {
                // Else, show a toast explaining "Account not created"
                Toast.makeText(getApplicationContext(),
                        R.string.account_not_created, Toast.LENGTH_LONG).show();
                usernameText.setText("");
                passwordText.setText("");
            }
        });

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the new CharSequence's trimmed length is greater than 0, turn the buttons on.
                boolean sLengthGTZero = s.toString().trim().length() > 0;
                forgotPassword.setEnabled(sLengthGTZero);
                if (sLengthGTZero &&
                        passwordText.getText().toString().trim().length() > 0) {
                    loginButton.setEnabled(true);
                    createAccountButton.setEnabled(true);
                } else {
                    // Else, turn them off.
                    loginButton.setEnabled(false);
                    createAccountButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the new CharSequence's trimmed length
                if (s.toString().trim().length() > 0 &&
                        usernameText.getText().toString().trim().length() > 0) {
                    loginButton.setEnabled(true);
                    createAccountButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                    createAccountButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}