package com.example.vfriend;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.vfriend.dao.UserDAO;

public class SignInActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private EditText edtEmail, edtPassword;
    private TextView tvSignUp;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // hide toolbar
        getSupportActionBar().hide();

        edtEmail = findViewById(R.id.login_email);
        edtPassword = findViewById(R.id.login_password);
        btnSignIn = findViewById(R.id.login_button);
        tvSignUp = findViewById(R.id.signup_text);

        userDAO = new UserDAO(SignInActivity.this);

        btnSignIn.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            } else {
                // check if email is valid
                if (!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
                    Toast.makeText(this, "Email invalide.", Toast.LENGTH_SHORT).show();
                }else{
                    User user = userDAO.checkUser(email, password);
                    if (user != null) {
                        Toast.makeText(this, "Vous êtes connecté.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }else{
                        Toast.makeText(this, "Email ou mot de passe invalide.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}