package com.edu.matchvagasapp.features.perfil;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.model.DadosPessoaisRequest;
import com.edu.matchvagasapp.data.model.DadosPessoaisResponse;
import com.edu.matchvagasapp.data.repository.PerfilRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditarDadosPessoaisFragment extends Fragment {

    private TextInputLayout tilNome, tilEmail, tilTelefone, tilNascimento;
    private TextInputLayout tilCpf, tilCep, tilCidade;
    private TextInputEditText etNome, etEmail, etTelefone, etNascimento;
    private TextInputEditText etCpf, etCep, etCidade, etLinkedin, etPortfolio;
    private AutoCompleteTextView etGenero, etEstado;
    private MaterialButton btnSalvar;

    private final PerfilRepository perfilRepository = new PerfilRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_dados_pessoais, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyWindowInsets(view);
        initViews(view);
        setupDropdowns();
        setupDatePicker();
        setupButtons(view);
        carregarDados();
    }

    private void applyWindowInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.header_editar), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), bars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.footer_salvar), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bars.bottom);
            return insets;
        });
    }

    private void initViews(View view) {
        tilNome = view.findViewById(R.id.til_nome);
        tilEmail = view.findViewById(R.id.til_email);
        tilTelefone = view.findViewById(R.id.til_telefone);
        tilNascimento = view.findViewById(R.id.til_nascimento);
        tilCpf = view.findViewById(R.id.til_cpf);
        tilCep = view.findViewById(R.id.til_cep);
        tilCidade = view.findViewById(R.id.til_cidade);

        etNome = view.findViewById(R.id.et_nome);
        etEmail = view.findViewById(R.id.et_email);
        etTelefone = view.findViewById(R.id.et_telefone);
        etNascimento = view.findViewById(R.id.et_nascimento);
        etCpf = view.findViewById(R.id.et_cpf);
        etGenero = view.findViewById(R.id.et_genero);
        etCep = view.findViewById(R.id.et_cep);
        etCidade = view.findViewById(R.id.et_cidade);
        etEstado = view.findViewById(R.id.et_estado);
        etLinkedin = view.findViewById(R.id.et_linkedin);
        etPortfolio = view.findViewById(R.id.et_portfolio);
    }

    private void setupDropdowns() {
        String[] generos = {
                getString(R.string.genero_masculino),
                getString(R.string.genero_feminino),
                getString(R.string.genero_nao_binario),
                getString(R.string.genero_prefiro_nao_informar)
        };
        etGenero.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, generos));

        String[] estados = {
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
                "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
                "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };
        etEstado.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, estados));
    }

    private void setupDatePicker() {
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.hint_data_nascimento))
                .setCalendarConstraints(constraints)
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            String formatted = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(selection));
            etNascimento.setText(formatted);
        });

        etNascimento.setOnClickListener(v ->
                datePicker.show(getParentFragmentManager(), "DATE_PICKER_NASCIMENTO"));
        tilNascimento.setEndIconOnClickListener(v ->
                datePicker.show(getParentFragmentManager(), "DATE_PICKER_NASCIMENTO"));
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        btnSalvar = view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(v -> salvarDados(view));
    }

    private void carregarDados() {
        setCarregando(true);
        perfilRepository.buscarDadosPessoais(new PerfilRepository.DadosPessoaisCallback() {
            @Override
            public void onSucesso(DadosPessoaisResponse dados) {
                if (!isAdded()) return;
                preencherCampos(dados);
                setCarregando(false);
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                setCarregando(false);
            }
        });
    }

    private void preencherCampos(DadosPessoaisResponse d) {
        if (d.getNome() != null)           etNome.setText(d.getNome());
        if (d.getEmail() != null)          etEmail.setText(d.getEmail());
        if (d.getTelefone() != null)       etTelefone.setText(d.getTelefone());
        if (d.getDataNascimento() != null) etNascimento.setText(d.getDataNascimento());
        if (d.getCpf() != null)            etCpf.setText(d.getCpf());
        if (d.getGenero() != null)         etGenero.setText(d.getGenero(), false);
        if (d.getCep() != null)            etCep.setText(d.getCep());
        if (d.getCidade() != null)         etCidade.setText(d.getCidade());
        if (d.getEstado() != null)         etEstado.setText(d.getEstado(), false);
        if (d.getLinkedin() != null)       etLinkedin.setText(d.getLinkedin());
        if (d.getPortfolio() != null)      etPortfolio.setText(d.getPortfolio());
    }

    private void salvarDados(View rootView) {
        String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";

        tilNome.setError(null);
        tilEmail.setError(null);

        boolean valido = true;

        if (nome.isEmpty()) {
            tilNome.setError(getString(R.string.erro_nome_vazio));
            valido = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.erro_email_invalido));
            valido = false;
        }

        if (!valido) return;

        String telefone = etTelefone.getText() != null ? etTelefone.getText().toString().trim() : "";
        String nascimento = etNascimento.getText() != null ? etNascimento.getText().toString().trim() : "";
        String cpf = etCpf.getText() != null ? etCpf.getText().toString().trim() : "";
        String genero = etGenero.getText() != null ? etGenero.getText().toString().trim() : "";
        String cep = etCep.getText() != null ? etCep.getText().toString().trim() : "";
        String cidade = etCidade.getText() != null ? etCidade.getText().toString().trim() : "";
        String estado = etEstado.getText() != null ? etEstado.getText().toString().trim() : "";
        String linkedin = etLinkedin.getText() != null ? etLinkedin.getText().toString().trim() : "";
        String portfolio = etPortfolio.getText() != null ? etPortfolio.getText().toString().trim() : "";

        setLoading(true);

        DadosPessoaisRequest request = new DadosPessoaisRequest(
                nome, email, telefone, nascimento, cpf,
                genero, cep, cidade, estado, linkedin, portfolio);

        perfilRepository.atualizarDadosPessoais(request, new PerfilRepository.PerfilCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(rootView, getString(R.string.sucesso_dados_salvos), Snackbar.LENGTH_SHORT).show();
                rootView.postDelayed(
                        () -> NavHostFragment.findNavController(EditarDadosPessoaisFragment.this).navigateUp(),
                        1200);
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(rootView, mensagem, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setCarregando(boolean carregando) {
        if (btnSalvar == null) return;
        btnSalvar.setEnabled(!carregando);
        btnSalvar.setText(carregando
                ? getString(R.string.carregando)
                : getString(R.string.btn_salvar_alteracoes));
    }

    private void setLoading(boolean loading) {
        if (btnSalvar == null) return;
        btnSalvar.setEnabled(!loading);
        btnSalvar.setText(loading
                ? getString(R.string.salvando)
                : getString(R.string.btn_salvar_alteracoes));
    }
}
