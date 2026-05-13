package com.edu.matchvagasapp.features.cadastro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.edu.matchvagasapp.data.local.TokenManager;
import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.CandidatoPerfilRequest;
import com.edu.matchvagasapp.data.model.LocalizacaoRequest;
import com.edu.matchvagasapp.data.model.LoginResponse;
import com.edu.matchvagasapp.data.model.TelefoneRequest;
import com.edu.matchvagasapp.data.model.UsuarioResponse;
import com.edu.matchvagasapp.data.repository.AuthRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroFragment extends Fragment {

    private int currentStep = 0;
    private Long selectedDateMillis = null;
    private final AuthRepository authRepository = new AuthRepository();

    private ViewFlipper viewFlipper;
    private MaterialButton btnVoltar, btnProximo;
    private TextView tvStepCounter;
    private View stepBar1, stepBar2, stepBar3;
    private TextView tvLabel1, tvLabel2, tvLabel3;

    // Etapa 1 — Dados Pessoais
    private TextInputLayout tilNome, tilCpf, tilEmail, tilSenha, tilConfirmarSenha, tilDataNascimento;
    private TextInputEditText etNome, etCpf, etEmail, etSenha, etConfirmarSenha, etDataNascimento;
    private CheckBox cbTermos;

    // Etapa 2 — Endereço
    private TextInputLayout tilCep, tilLogradouro, tilNumeroEnd, tilComplemento, tilBairro, tilCidade, tilEstado;
    private TextInputEditText etCep, etLogradouro, etNumeroEnd, etComplemento, etBairro, etCidade;
    private AutoCompleteTextView etEstado;

    // Etapa 3 — Contato
    private TextInputLayout tilTelefone;
    private TextInputEditText etTelefone;
    private MaterialSwitch switchWhatsapp;

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
        setupMasks();
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
        viewFlipper    = view.findViewById(R.id.view_flipper);
        btnVoltar      = view.findViewById(R.id.btn_voltar);
        btnProximo     = view.findViewById(R.id.btn_proximo);
        tvStepCounter  = view.findViewById(R.id.tv_step_counter);
        stepBar1       = view.findViewById(R.id.step_bar_1);
        stepBar2       = view.findViewById(R.id.step_bar_2);
        stepBar3       = view.findViewById(R.id.step_bar_3);
        tvLabel1       = view.findViewById(R.id.tv_label_1);
        tvLabel2       = view.findViewById(R.id.tv_label_2);
        tvLabel3       = view.findViewById(R.id.tv_label_3);

        // Etapa 1
        tilNome           = view.findViewById(R.id.til_nome);
        tilCpf            = view.findViewById(R.id.til_cpf);
        tilEmail          = view.findViewById(R.id.til_email);
        tilSenha          = view.findViewById(R.id.til_senha);
        tilConfirmarSenha = view.findViewById(R.id.til_confirmar_senha);
        tilDataNascimento = view.findViewById(R.id.til_data_nascimento);
        etNome            = view.findViewById(R.id.et_nome);
        etCpf             = view.findViewById(R.id.et_cpf);
        etEmail           = view.findViewById(R.id.et_email);
        etSenha           = view.findViewById(R.id.et_senha);
        etConfirmarSenha  = view.findViewById(R.id.et_confirmar_senha);
        etDataNascimento  = view.findViewById(R.id.et_data_nascimento);
        cbTermos          = view.findViewById(R.id.cb_termos);

        // Etapa 2
        tilCep        = view.findViewById(R.id.til_cep);
        tilLogradouro = view.findViewById(R.id.til_logradouro);
        tilNumeroEnd  = view.findViewById(R.id.til_numero_end);
        tilComplemento = view.findViewById(R.id.til_complemento);
        tilBairro     = view.findViewById(R.id.til_bairro);
        tilCidade     = view.findViewById(R.id.til_cidade);
        tilEstado     = view.findViewById(R.id.til_estado);
        etCep         = view.findViewById(R.id.et_cep);
        etLogradouro  = view.findViewById(R.id.et_logradouro);
        etNumeroEnd   = view.findViewById(R.id.et_numero_end);
        etComplemento = view.findViewById(R.id.et_complemento);
        etBairro      = view.findViewById(R.id.et_bairro);
        etCidade      = view.findViewById(R.id.et_cidade);
        etEstado      = view.findViewById(R.id.et_estado);

        // Etapa 3
        tilTelefone    = view.findViewById(R.id.til_telefone);
        etTelefone     = view.findViewById(R.id.et_telefone);
        switchWhatsapp = view.findViewById(R.id.switch_whatsapp);

        etDataNascimento.setOnClickListener(v -> showDatePicker());

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (currentStep > 0) {
                goToPreviousStep();
            } else {
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    private void showDatePicker() {
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Data de nascimento")
                .setCalendarConstraints(constraints)
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            selectedDateMillis = selection;
            String formatted = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(selection));
            etDataNascimento.setText(formatted);
            tilDataNascimento.setError(null);
        });

        picker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void setupDropdowns() {
        String[] estados = {
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };
        etEstado.setAdapter(new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_dropdown_item_1line, estados
        ));
    }

    private void setupMasks() {
        etCpf.addTextChangedListener(cpfMask());
        etCep.addTextChangedListener(cepMask());
    }

    private TextWatcher cpfMask() {
        return new TextWatcher() {
            boolean updating = false;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (updating) return;
                updating = true;
                String raw = s.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 11) raw = raw.substring(0, 11);
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 3 || i == 6) formatted.append('.');
                    if (i == 9) formatted.append('-');
                    formatted.append(raw.charAt(i));
                }
                s.replace(0, s.length(), formatted.toString());
                updating = false;
            }
        };
    }

    private TextWatcher cepMask() {
        return new TextWatcher() {
            boolean updating = false;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (updating) return;
                updating = true;
                String raw = s.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 8) raw = raw.substring(0, 8);
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 5) formatted.append('-');
                    formatted.append(raw.charAt(i));
                }
                s.replace(0, s.length(), formatted.toString());
                updating = false;
            }
        };
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
        int activeColor   = ContextCompat.getColor(requireContext(), R.color.accent);
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

        String nome = str(etNome);
        if (nome.isEmpty()) {
            tilNome.setError("Nome é obrigatório");
            valid = false;
        } else if (nome.length() < 3) {
            tilNome.setError("Informe o nome completo");
            valid = false;
        } else {
            tilNome.setError(null);
        }

        String cpfRaw = str(etCpf).replaceAll("[^0-9]", "");
        if (cpfRaw.isEmpty()) {
            tilCpf.setError("CPF é obrigatório");
            valid = false;
        } else if (cpfRaw.length() != 11) {
            tilCpf.setError("CPF deve ter 11 dígitos");
            valid = false;
        } else {
            tilCpf.setError(null);
        }

        String email = str(etEmail);
        if (email.isEmpty()) {
            tilEmail.setError("E-mail é obrigatório");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("E-mail inválido");
            valid = false;
        } else {
            tilEmail.setError(null);
        }

        String senha = str(etSenha);
        if (senha.isEmpty()) {
            tilSenha.setError("Senha é obrigatória");
            valid = false;
        } else if (senha.length() < 6) {
            tilSenha.setError("Mínimo de 6 caracteres");
            valid = false;
        } else {
            tilSenha.setError(null);
        }

        String confirmar = str(etConfirmarSenha);
        if (confirmar.isEmpty()) {
            tilConfirmarSenha.setError("Confirme sua senha");
            valid = false;
        } else if (!confirmar.equals(senha)) {
            tilConfirmarSenha.setError("Senhas não conferem");
            valid = false;
        } else {
            tilConfirmarSenha.setError(null);
        }

        if (selectedDateMillis == null) {
            tilDataNascimento.setError("Data de nascimento é obrigatória");
            valid = false;
        } else {
            tilDataNascimento.setError(null);
        }

        if (valid && !cbTermos.isChecked()) {
            Snackbar.make(viewFlipper, "Aceite os Termos de Uso para continuar", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return valid;
    }

    private boolean validateEtapa2() {
        boolean valid = true;

        String cepRaw = str(etCep).replaceAll("[^0-9]", "");
        if (cepRaw.isEmpty()) {
            tilCep.setError("CEP é obrigatório");
            valid = false;
        } else if (cepRaw.length() != 8) {
            tilCep.setError("CEP deve ter 8 dígitos");
            valid = false;
        } else {
            tilCep.setError(null);
        }

        String logradouro = str(etLogradouro);
        if (logradouro.isEmpty()) {
            tilLogradouro.setError("Logradouro é obrigatório");
            valid = false;
        } else {
            tilLogradouro.setError(null);
        }

        String numeroEnd = str(etNumeroEnd);
        if (numeroEnd.isEmpty()) {
            tilNumeroEnd.setError("Número é obrigatório");
            valid = false;
        } else {
            tilNumeroEnd.setError(null);
        }

        String bairro = str(etBairro);
        if (bairro.isEmpty()) {
            tilBairro.setError("Bairro é obrigatório");
            valid = false;
        } else {
            tilBairro.setError(null);
        }

        String cidade = str(etCidade);
        if (cidade.isEmpty()) {
            tilCidade.setError("Cidade é obrigatória");
            valid = false;
        } else {
            tilCidade.setError(null);
        }

        String estado = str(etEstado);
        if (estado.isEmpty()) {
            tilEstado.setError("Selecione o estado");
            valid = false;
        } else {
            tilEstado.setError(null);
        }

        return valid;
    }

    private boolean validateEtapa3() {
        String telefone = str(etTelefone).replaceAll("[^0-9]", "");
        if (!telefone.isEmpty() && telefone.length() < 10) {
            tilTelefone.setError("Número de telefone inválido");
            return false;
        }
        tilTelefone.setError(null);
        return true;
    }

    private void finalizarCadastro() {
        String nome = str(etNome);
        String email = str(etEmail);
        String senha = str(etSenha);
        String dataNascimento = formatarDataNascimento(selectedDateMillis);

        CadastroRequest request = new CadastroRequest(nome, email, senha, dataNascimento, "CANDIDATO", true);

        setCadastroLoading(true);

        authRepository.cadastrar(request, new AuthRepository.CadastroCallback() {
            @Override
            public void onSuccess(UsuarioResponse response) {
                if (!isAdded()) return;
                authRepository.login(email, senha, new AuthRepository.LoginCallback() {
                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
                            new TokenManager(requireContext()).salvar(
                                    loginResponse.getToken(),
                                    loginResponse.getUsuarioId(),
                                    loginResponse.getNome(),
                                    loginResponse.getPerfil()
                            );
                            criarPerfilCandidato();
                        });
                    }

                    @Override
                    public void onError(String mensagem) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
                            setCadastroLoading(false);
                            NavHostFragment.findNavController(CadastroFragment.this).popBackStack();
                        });
                    }
                });
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setCadastroLoading(false);
                    Snackbar.make(viewFlipper, mensagem, Snackbar.LENGTH_LONG).show();
                });
            }
        });
    }

    private void criarPerfilCandidato() {
        String cpf = str(etCpf).replaceAll("[^0-9]", "");
        String cep = str(etCep).replaceAll("[^0-9]", "");
        String cepFormatado = cep.length() == 8 ? cep.substring(0, 5) + "-" + cep.substring(5) : cep;

        LocalizacaoRequest localizacao = new LocalizacaoRequest(
                str(etLogradouro),
                str(etNumeroEnd),
                str(etComplemento),
                str(etBairro),
                cepFormatado,
                str(etCidade),
                str(etEstado)
        );

        String telefoneRaw = str(etTelefone).replaceAll("[^0-9]", "");
        TelefoneRequest telefone = telefoneRaw.isEmpty()
                ? null
                : new TelefoneRequest(str(etTelefone), switchWhatsapp.isChecked());

        CandidatoPerfilRequest perfilRequest = new CandidatoPerfilRequest(cpf, telefone, localizacao);

        authRepository.criarPerfilCandidato(perfilRequest, new AuthRepository.CriarPerfilCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setCadastroLoading(false);
                    NavHostFragment.findNavController(CadastroFragment.this)
                            .navigate(R.id.action_cadastro_to_dashboard);
                });
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setCadastroLoading(false);
                    NavHostFragment.findNavController(CadastroFragment.this)
                            .navigate(R.id.action_cadastro_to_dashboard);
                });
            }
        });
    }

    private String formatarDataNascimento(Long millis) {
        if (millis == null) return null;
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(millis);
        return String.format(java.util.Locale.US, "%04d-%02d-%02dT00:00:00",
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH));
    }

    private String str(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private String str(AutoCompleteTextView et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setCadastroLoading(boolean loading) {
        btnProximo.setEnabled(!loading);
        btnProximo.setText(loading ? "Aguarde..." : "Finalizar");
    }
}
