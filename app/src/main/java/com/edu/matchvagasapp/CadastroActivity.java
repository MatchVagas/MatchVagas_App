package com.edu.matchvagasapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CadastroActivity extends AppCompatActivity {

    private int currentStep = 0;

    private ViewFlipper viewFlipper;
    private MaterialButton btnVoltar, btnProximo;
    private TextView tvStepCounter;
    private View stepBar1, stepBar2, stepBar3;
    private TextView tvLabel1, tvLabel2, tvLabel3;

    // Etapa 1
    private TextInputLayout tilNome, tilEmail, tilSenha, tilConfirmarSenha;
    private TextInputEditText etNome, etEmail, etSenha, etConfirmarSenha;
    private CheckBox cbTermos;

    // Etapa 2
    private TextInputLayout tilArea;
    private AutoCompleteTextView etArea;
    private ChipGroup chipGroupNivel, chipGroupContrato;

    // Etapa 3
    private TextInputLayout tilCidade, tilEstado;
    private TextInputEditText etCidade;
    private AutoCompleteTextView etEstado;
    private MaterialSwitch switchRemoto, switchRelocacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_cadastro);

        applyWindowInsets();
        initViews();
        setupDropdowns();
        setupNavigation();
    }

    private void applyWindowInsets() {
        View header = findViewById(R.id.header_layout);
        View footer = findViewById(R.id.footer_buttons);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            header.setPadding(
                header.getPaddingLeft(),
                bars.top + 4,
                header.getPaddingRight(),
                header.getPaddingBottom()
            );
            footer.setPadding(
                footer.getPaddingLeft(),
                footer.getPaddingTop(),
                footer.getPaddingRight(),
                bars.bottom + 16
            );
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void initViews() {
        viewFlipper = findViewById(R.id.view_flipper);
        btnVoltar = findViewById(R.id.btn_voltar);
        btnProximo = findViewById(R.id.btn_proximo);
        tvStepCounter = findViewById(R.id.tv_step_counter);
        stepBar1 = findViewById(R.id.step_bar_1);
        stepBar2 = findViewById(R.id.step_bar_2);
        stepBar3 = findViewById(R.id.step_bar_3);
        tvLabel1 = findViewById(R.id.tv_label_1);
        tvLabel2 = findViewById(R.id.tv_label_2);
        tvLabel3 = findViewById(R.id.tv_label_3);

        tilNome = findViewById(R.id.til_nome);
        tilEmail = findViewById(R.id.til_email);
        tilSenha = findViewById(R.id.til_senha);
        tilConfirmarSenha = findViewById(R.id.til_confirmar_senha);
        etNome = findViewById(R.id.et_nome);
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        etConfirmarSenha = findViewById(R.id.et_confirmar_senha);
        cbTermos = findViewById(R.id.cb_termos);

        tilArea = findViewById(R.id.til_area);
        etArea = findViewById(R.id.et_area);
        chipGroupNivel = findViewById(R.id.chip_group_nivel);
        chipGroupContrato = findViewById(R.id.chip_group_contrato);

        tilCidade = findViewById(R.id.til_cidade);
        tilEstado = findViewById(R.id.til_estado);
        etCidade = findViewById(R.id.et_cidade);
        etEstado = findViewById(R.id.et_estado);
        switchRemoto = findViewById(R.id.switch_remoto);
        switchRelocacao = findViewById(R.id.switch_relocacao);

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (currentStep > 0) {
                goToPreviousStep();
            } else {
                finish();
            }
        });
    }

    private void setupDropdowns() {
        String[] areas = {
            "Tecnologia", "Design", "Marketing", "Finanças",
            "Recursos Humanos", "Vendas", "Jurídico", "Educação",
            "Saúde", "Engenharia", "Administração", "Outro"
        };
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, areas
        );
        etArea.setAdapter(areaAdapter);

        String[] estados = {
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, estados
        );
        etEstado.setAdapter(estadoAdapter);
    }

    private void setupNavigation() {
        btnVoltar.setOnClickListener(v -> goToPreviousStep());
        btnProximo.setOnClickListener(v -> {
            if (validateCurrentStep()) {
                if (currentStep < 2) {
                    goToNextStep();
                } else {
                    finalizarCadastro();
                }
            }
        });
    }

    private void goToNextStep() {
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
        currentStep++;
        viewFlipper.showNext();
        updateStepUI();
    }

    private void goToPreviousStep() {
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
        currentStep--;
        viewFlipper.showPrevious();
        updateStepUI();
    }

    private void updateStepUI() {
        int activeColor = ContextCompat.getColor(this, R.color.accent);
        int inactiveColor = ContextCompat.getColor(this, R.color.step_inactive);
        int activeLabelColor = ContextCompat.getColor(this, R.color.accent);
        int inactiveLabelColor = ContextCompat.getColor(this, R.color.step_inactive);

        stepBar1.setBackgroundColor(currentStep >= 0 ? activeColor : inactiveColor);
        stepBar2.setBackgroundColor(currentStep >= 1 ? activeColor : inactiveColor);
        stepBar3.setBackgroundColor(currentStep >= 2 ? activeColor : inactiveColor);

        tvLabel1.setTextColor(currentStep >= 0 ? activeLabelColor : inactiveLabelColor);
        tvLabel2.setTextColor(currentStep >= 1 ? activeLabelColor : inactiveLabelColor);
        tvLabel3.setTextColor(currentStep >= 2 ? activeLabelColor : inactiveLabelColor);

        tvStepCounter.setText("Passo " + (currentStep + 1) + " de 3");

        btnVoltar.setVisibility(currentStep > 0 ? View.VISIBLE : View.GONE);
        btnProximo.setText(currentStep == 2 ? "Finalizar" : "Próximo");
    }

    private boolean validateCurrentStep() {
        switch (currentStep) {
            case 0: return validateEtapa1();
            case 1: return validateEtapa2();
            case 2: return validateEtapa3();
        }
        return true;
    }

    private boolean validateEtapa1() {
        boolean valid = true;

        String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";
        if (nome.isEmpty()) {
            tilNome.setError("Nome é obrigatório");
            valid = false;
        } else if (nome.length() < 3) {
            tilNome.setError("Informe o nome completo");
            valid = false;
        } else {
            tilNome.setError(null);
        }

        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        if (email.isEmpty()) {
            tilEmail.setError("E-mail é obrigatório");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("E-mail inválido");
            valid = false;
        } else {
            tilEmail.setError(null);
        }

        String senha = etSenha.getText() != null ? etSenha.getText().toString() : "";
        if (senha.isEmpty()) {
            tilSenha.setError("Senha é obrigatória");
            valid = false;
        } else if (senha.length() < 6) {
            tilSenha.setError("Mínimo de 6 caracteres");
            valid = false;
        } else {
            tilSenha.setError(null);
        }

        String confirmar = etConfirmarSenha.getText() != null ? etConfirmarSenha.getText().toString() : "";
        if (confirmar.isEmpty()) {
            tilConfirmarSenha.setError("Confirme sua senha");
            valid = false;
        } else if (!confirmar.equals(senha)) {
            tilConfirmarSenha.setError("Senhas não conferem");
            valid = false;
        } else {
            tilConfirmarSenha.setError(null);
        }

        if (valid && !cbTermos.isChecked()) {
            Snackbar.make(viewFlipper, "Aceite os Termos de Uso para continuar", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return valid;
    }

    private boolean validateEtapa2() {
        boolean valid = true;

        String area = etArea.getText() != null ? etArea.getText().toString().trim() : "";
        if (area.isEmpty()) {
            tilArea.setError("Selecione uma área de atuação");
            valid = false;
        } else {
            tilArea.setError(null);
        }

        if (chipGroupNivel.getCheckedChipId() == View.NO_ID) {
            Snackbar.make(viewFlipper, "Selecione seu nível de experiência", Snackbar.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private boolean validateEtapa3() {
        boolean valid = true;

        String cidade = etCidade.getText() != null ? etCidade.getText().toString().trim() : "";
        if (cidade.isEmpty()) {
            tilCidade.setError("Cidade é obrigatória");
            valid = false;
        } else {
            tilCidade.setError(null);
        }

        String estado = etEstado.getText() != null ? etEstado.getText().toString().trim() : "";
        if (estado.isEmpty()) {
            tilEstado.setError("Selecione o estado");
            valid = false;
        } else {
            tilEstado.setError(null);
        }

        return valid;
    }

    private void finalizarCadastro() {
        // TODO: integrar com backend de autenticação
        Intent intent = new Intent(this, DashbordCadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}