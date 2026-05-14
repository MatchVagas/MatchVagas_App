package com.edu.matchvagasapp.features.perfil;

import android.os.Bundle;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

public class EditarPerfilProfissionalFragment extends Fragment {

    private TextInputLayout tilObjetivo;
    private TextInputEditText etObjetivo, etPretensao;
    private AutoCompleteTextView etDisponibilidade;
    private MaterialButton btnSalvar;

    // Campos do perfil carregados da API para preservar ao salvar
    private DadosPessoaisResponse dadosAtuais = null;

    private final PerfilRepository perfilRepository = new PerfilRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_perfil_profissional, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyWindowInsets(view);
        initViews(view);
        setupDropdown();
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
        tilObjetivo = view.findViewById(R.id.til_objetivo);
        etObjetivo = view.findViewById(R.id.et_objetivo);
        etDisponibilidade = view.findViewById(R.id.et_disponibilidade);
        etPretensao = view.findViewById(R.id.et_pretensao);
        btnSalvar = view.findViewById(R.id.btn_salvar);
    }

    private void setupDropdown() {
        String[] opcoes = {
                getString(R.string.disponibilidade_imediata),
                getString(R.string.disponibilidade_2_semanas),
                getString(R.string.disponibilidade_1_mes),
                getString(R.string.disponibilidade_a_combinar)
        };
        etDisponibilidade.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, opcoes));
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());
        btnSalvar.setOnClickListener(v -> salvarDados(view));
    }

    private void carregarDados() {
        setCarregando(true);
        perfilRepository.buscarDadosPessoais(new PerfilRepository.DadosPessoaisCallback() {
            @Override
            public void onSucesso(DadosPessoaisResponse dados) {
                if (!isAdded()) return;
                dadosAtuais = dados;
                if (dados.getObjetivoProfissional() != null)
                    etObjetivo.setText(dados.getObjetivoProfissional());
                if (dados.getDisponibilidade() != null)
                    etDisponibilidade.setText(dados.getDisponibilidade(), false);
                if (dados.getPretensaoSalarial() != null)
                    etPretensao.setText(dados.getPretensaoSalarial().toPlainString());
                setCarregando(false);
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                setCarregando(false);
            }
        });
    }

    private void salvarDados(View rootView) {
        String objetivo = etObjetivo.getText() != null ? etObjetivo.getText().toString().trim() : "";
        tilObjetivo.setError(null);

        if (objetivo.isEmpty()) {
            tilObjetivo.setError(getString(R.string.erro_objetivo_vazio));
            return;
        }

        String disponibilidade = etDisponibilidade.getText() != null
                ? etDisponibilidade.getText().toString().trim() : "";
        String pretensaoStr = etPretensao.getText() != null
                ? etPretensao.getText().toString().trim() : "";

        BigDecimal pretensao = null;
        if (!pretensaoStr.isEmpty()) {
            try {
                pretensao = new BigDecimal(pretensaoStr.replace(",", "."));
            } catch (NumberFormatException ignored) { }
        }

        setLoading(true);

        // Preserva todos os outros campos do perfil que não editamos nesta tela
        String cpf = dadosAtuais != null && dadosAtuais.getCpf() != null
                ? dadosAtuais.getCpf() : "";
        String nomeCompleto = dadosAtuais != null ? dadosAtuais.getNome() : null;
        String email = dadosAtuais != null ? dadosAtuais.getEmail() : null;
        String dataNasc = dadosAtuais != null ? dadosAtuais.getDataNascimento() : null;

        DadosPessoaisRequest.LocalizacaoRequest localizacao = buildLocalizacao(dadosAtuais);
        DadosPessoaisRequest.TelefoneRequest telefone = buildTelefone(dadosAtuais);

        DadosPessoaisRequest request = new DadosPessoaisRequest(
                cpf, nomeCompleto, email, dataNasc,
                objetivo,
                disponibilidade.isEmpty() ? null : disponibilidade,
                pretensao,
                telefone,
                localizacao);

        perfilRepository.atualizarPerfilProfissional(request, new PerfilRepository.PerfilCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(rootView, getString(R.string.sucesso_perfil_profissional_salvo),
                        Snackbar.LENGTH_SHORT).show();
                rootView.postDelayed(
                        () -> NavHostFragment.findNavController(EditarPerfilProfissionalFragment.this)
                                .navigateUp(),
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

    private DadosPessoaisRequest.LocalizacaoRequest buildLocalizacao(DadosPessoaisResponse d) {
        if (d == null) return null;
        String logradouro = d.getLogradouro();
        String numero = d.getNumeroEnd();
        String bairro = d.getBairro();
        String cep = d.getCep();
        String cidade = d.getCidade();
        String estado = d.getEstado();
        if (logradouro == null || numero == null || bairro == null
                || cep == null || cidade == null || estado == null) return null;
        return new DadosPessoaisRequest.LocalizacaoRequest(
                logradouro, numero, d.getComplemento(), bairro, cep, cidade, estado);
    }

    private DadosPessoaisRequest.TelefoneRequest buildTelefone(DadosPessoaisResponse d) {
        if (d == null || d.getTelefone() == null) return null;
        DadosPessoaisResponse.TelefoneInfo t = d.getTelefone();
        if (t.getNumero() == null || t.getTipoTelefoneId() == null) return null;
        return new DadosPessoaisRequest.TelefoneRequest(
                t.getNumero(), t.getTipoTelefoneId(), t.isWpp());
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
