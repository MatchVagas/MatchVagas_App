package com.edu.matchvagasapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvForgotPassword.setOnClickListener(v ->
            Toast.makeText(this, "Recuperação de senha em breve", Toast.LENGTH_SHORT).show()
        );

        tvRegister.setOnClickListener(v ->
            startActivity(new Intent(this, CadastroActivity.class))
        );
    }

    private void attemptLogin() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Informe o e-mail");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("E-mail inválido");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Informe a senha");
            valid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("A senha deve ter pelo menos 6 caracteres");
            valid = false;
        }

        if (!valid) return;

        // TODO: integrar com backend de autenticação
        Intent intent = new Intent(this, DashbordCadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}