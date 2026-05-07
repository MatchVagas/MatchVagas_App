package com.edu.matchvagasapp.features.cadastro;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.edu.matchvagasapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CadastroFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cadastro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyWindowInsets(view);
        initViews(view);
        setupDropdowns();
        setupNavigation();
    }

    private void applyWindowInsets(View rootView) {
        View header = rootView.findViewById(R.id.header_layout);
        View footer = rootView.findViewById(R.id.footer_buttons);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
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

    private void initViews(View view) {
        viewFlipper = view.findViewById(R.id.view_flipper);
        btnVoltar = view.findViewById(R.id.btn_voltar);
        btnProximo = view.findViewById(R.id.btn_proximo);
        tvStepCounter = view.findViewById(R.id.tv_step_counter);
        stepBar1 = view.findViewById(R.id.step_bar_1);
        stepBar2 = view.findViewById(R.id.step_bar_2);
        stepBar3 = view.findViewById(R.id.step_bar_3);
        tvLabel1 = view.findViewById(R.id.tv_label_1);
        tvLabel2 = view.findViewById(R.id.tv_label_2);
        tvLabel3 = view.findViewById(R.id.tv_label_3);

        tilNome = view.findViewById(R.id.til_nome);
        tilEmail = view.findViewById(R.id.til_email);
        tilSenha = view.findViewById(R.id.til_senha);
        tilConfirmarSenha = view.findViewById(R.id.til_confirmar_senha);
        etNome = view.findViewById(R.id.et_nome);
        etEmail = view.findViewById(R.id.et_email);
        etSenha = view.findViewById(R.id.et_senha);
        etConfirmarSenha = view.findViewById(R.id.et_confirmar_senha);
        cbTermos = view.findViewById(R.id.cb_termos);

        tilArea = view.findViewById(R.id.til_area);
        etArea = view.findViewById(R.id.et_area);
        chipGroupNivel = view.findViewById(R.id.chip_group_nivel);
        chipGroupContrato = view.findViewById(R.id.chip_group_contrato);

        tilCidade = view.findViewById(R.id.til_cidade);
        tilEstado = view.findViewById(R.id.til_estado);
        etCidade = view.findViewById(R.id.et_cidade);
        etEstado = view.findViewById(R.id.et_estado);
        switchRemoto = view.findViewById(R.id.switch_remoto);
        switchRelocacao = view.findViewById(R.id.switch_relocacao);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (currentStep > 0) {
                goToPreviousStep();
            } else {
                NavHostFragment.findNavController(this).popBackStack();
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
            requireContext(), android.R.layout.simple_dropdown_item_1line, areas
        );
        etArea.setAdapter(areaAdapter);

        String[] estados = {
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_dropdown_item_1line, estados
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
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left));
        currentStep++;
        viewFlipper.showNext();
        updateStepUI();
    }

    private void goToPreviousStep() {
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right));
        currentStep--;
        viewFlipper.showPrevious();
        updateStepUI();
    }

    private void updateStepUI() {
        int activeColor = ContextCompat.getColor(requireContext(), R.color.accent);
        int inactiveColor = ContextCompat.getColor(requireContext(), R.color.step_inactive);

        stepBar1.setBackgroundColor(currentStep >= 0 ? activeColor : inactiveColor);
        stepBar2.setBackgroundColor(currentStep >= 1 ? activeColor : inactiveColor);
        stepBar3.setBackgroundColor(currentStep >= 2 ? activeColor : inactiveColor);

        tvLabel1.setTextColor(currentStep >= 0 ? activeColor : inactiveColor);
        tvLabel2.setTextColor(currentStep >= 1 ? activeColor : inactiveColor);
        tvLabel3.setTextColor(currentStep >= 2 ? activeColor : inactiveColor);

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
        NavHostFragment.findNavController(this).navigate(R.id.action_cadastro_to_dashboard);
    }
}