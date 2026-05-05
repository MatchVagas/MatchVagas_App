package com.edu.matchvagasapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilEmail = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        MaterialButton btnLogin = view.findViewById(R.id.btn_login);
        TextView tvForgotPassword = view.findViewById(R.id.tv_forgot_password);
        TextView tvRegister = view.findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvForgotPassword.setOnClickListener(v ->
            Toast.makeText(requireContext(), "Recuperação de senha em breve", Toast.LENGTH_SHORT).show()
        );

        tvRegister.setOnClickListener(v ->
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_cadastro)
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
        NavHostFragment.findNavController(this).navigate(R.id.action_login_to_dashboard);
    }
}