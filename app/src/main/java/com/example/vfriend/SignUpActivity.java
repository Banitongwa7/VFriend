package com.example.vfriend;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUp;
    private EditText edtEmail, edtPassword, confirmPassword;
    private TextView tvSignIn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // hide toolbar
        getSupportActionBar().hide();

        btnSignUp = findViewById(R.id.signup_button);
        edtEmail = findViewById(R.id.signup_email);
        edtPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_comfirm);
        tvSignIn = findViewById(R.id.signin_text);

        dbHelper = new DatabaseHelper(this);

        btnSignUp.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String cfmPassword = confirmPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty() || cfmPassword.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            }else {
                if (password.equals(cfmPassword)) {
                    // chech if email exist
                    if (dbHelper.checkEmail(email)) {
                        Toast.makeText(this, "Cet email existe déjà. Connectez-vous !", Toast.LENGTH_SHORT).show();
                    } else {
                        if(dbHelper.insertUser(email, password)) {
                            Toast.makeText(this, "Vous êtes inscrit.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent); // go to sign in activity
                        }else {
                            Toast.makeText(this, "Erreur lors de l'inscription.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    confirmPassword.setError("Le mot de passe ne correspond pas.");
                    //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
            }


        });


        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });

    }
}