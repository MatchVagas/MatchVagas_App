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
import com.edu.matchvagasapp.data.model.FormacaoRequest;
import com.edu.matchvagasapp.data.model.FormacaoResponse;
import com.edu.matchvagasapp.data.repository.PerfilRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class EditarFormacaoFragment extends Fragment {

    private TextInputLayout tilInstituicao, tilCurso, tilInicioAno, tilConclusaoAno;
    private TextInputEditText etInstituicao, etCurso, etInicioAno, etConclusaoAno;
    private AutoCompleteTextView etGrau, etInicioMes, etConclusaoMes;
    private MaterialSwitch switchCursando;
    private View containerConclusao;
    private MaterialButton btnSalvar;

    private final PerfilRepository perfilRepository = new PerfilRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_formacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyWindowInsets(view);
        initViews(view);
        setupDropdowns();
        setupSwitch();
        setupButtons(view);
        carregarDados();
    }

    private void applyWindowInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.header_formacao), (v, insets) -> {
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
        tilInstituicao = view.findViewById(R.id.til_instituicao);
        tilCurso = view.findViewById(R.id.til_curso);
        tilInicioAno = view.findViewById(R.id.til_inicio_ano);
        tilConclusaoAno = view.findViewById(R.id.til_conclusao_ano);

        etInstituicao = view.findViewById(R.id.et_instituicao);
        etCurso = view.findViewById(R.id.et_curso);
        etGrau = view.findViewById(R.id.et_grau);
        etInicioMes = view.findViewById(R.id.et_inicio_mes);
        etInicioAno = view.findViewById(R.id.et_inicio_ano);
        etConclusaoMes = view.findViewById(R.id.et_conclusao_mes);
        etConclusaoAno = view.findViewById(R.id.et_conclusao_ano);

        switchCursando = view.findViewById(R.id.switch_cursando);
        containerConclusao = view.findViewById(R.id.container_conclusao);
    }

    private void setupDropdowns() {
        String[] graus = {
                getString(R.string.grau_ensino_medio),
                getString(R.string.grau_tecnico),
                getString(R.string.grau_graduacao),
                getString(R.string.grau_posgraduacao),
                getString(R.string.grau_mba),
                getString(R.string.grau_mestrado),
                getString(R.string.grau_doutorado)
        };
        etGrau.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, graus));

        String[] meses = {
                getString(R.string.mes_jan), getString(R.string.mes_fev),
                getString(R.string.mes_mar), getString(R.string.mes_abr),
                getString(R.string.mes_mai), getString(R.string.mes_jun),
                getString(R.string.mes_jul), getString(R.string.mes_ago),
                getString(R.string.mes_set), getString(R.string.mes_out),
                getString(R.string.mes_nov), getString(R.string.mes_dez)
        };
        ArrayAdapter<String> mesAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, meses);
        etInicioMes.setAdapter(mesAdapter);
        etConclusaoMes.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, meses));
    }

    private void setupSwitch() {
        atualizarVisibilidadeConclusao(switchCursando.isChecked());
        switchCursando.setOnCheckedChangeListener((btn, isChecked) ->
                atualizarVisibilidadeConclusao(isChecked));
    }

    private void atualizarVisibilidadeConclusao(boolean cursando) {
        containerConclusao.setVisibility(cursando ? View.GONE : View.VISIBLE);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        btnSalvar = view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(v -> salvarFormacao(view));
    }

    private void carregarDados() {
        setCarregando(true);
        perfilRepository.buscarFormacoes(new PerfilRepository.FormacoesCallback() {
            @Override
            public void onSucesso(List<FormacaoResponse> lista) {
                if (!isAdded()) return;
                preencherCampos(lista.get(0));
                setCarregando(false);
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                setCarregando(false);
            }
        });
    }

    private void preencherCampos(FormacaoResponse f) {
        if (f.getInstituicao() != null)  etInstituicao.setText(f.getInstituicao());
        if (f.getCurso() != null)        etCurso.setText(f.getCurso());
        if (f.getGrau() != null)         etGrau.setText(f.getGrau(), false);
        if (f.getMesInicio() != null)    etInicioMes.setText(f.getMesInicio(), false);
        if (f.getAnoInicio() != null)    etInicioAno.setText(f.getAnoInicio());

        switchCursando.setChecked(f.isAindaCursando());
        if (!f.isAindaCursando()) {
            if (f.getMesConclusao() != null) etConclusaoMes.setText(f.getMesConclusao(), false);
            if (f.getAnoConclusao() != null) etConclusaoAno.setText(f.getAnoConclusao());
        }
    }

    private void salvarFormacao(View rootView) {
        String instituicao = etInstituicao.getText() != null
                ? etInstituicao.getText().toString().trim() : "";
        String curso = etCurso.getText() != null
                ? etCurso.getText().toString().trim() : "";
        String anoInicio = etInicioAno.getText() != null
                ? etInicioAno.getText().toString().trim() : "";

        tilInstituicao.setError(null);
        tilCurso.setError(null);
        tilInicioAno.setError(null);

        boolean valido = true;

        if (instituicao.isEmpty()) {
            tilInstituicao.setError(getString(R.string.erro_instituicao_vazia));
            valido = false;
        }

        if (curso.isEmpty()) {
            tilCurso.setError(getString(R.string.erro_curso_vazio));
            valido = false;
        }

        if (anoInicio.isEmpty() || anoInicio.length() != 4) {
            tilInicioAno.setError(getString(R.string.erro_ano_invalido));
            valido = false;
        }

        String mesConclusao = null;
        String anoConclusao = null;
        boolean aindaCursando = switchCursando.isChecked();

        if (!aindaCursando) {
            String anoConclusaoStr = etConclusaoAno.getText() != null
                    ? etConclusaoAno.getText().toString().trim() : "";
            if (anoConclusaoStr.isEmpty() || anoConclusaoStr.length() != 4) {
                tilConclusaoAno.setError(getString(R.string.erro_ano_invalido));
                valido = false;
            } else {
                tilConclusaoAno.setError(null);
                anoConclusao = anoConclusaoStr;
                mesConclusao = etConclusaoMes.getText() != null
                        ? etConclusaoMes.getText().toString().trim() : "";
            }
        }

        if (!valido) return;

        String grau = etGrau.getText() != null ? etGrau.getText().toString().trim() : "";
        String mesInicio = etInicioMes.getText() != null ? etInicioMes.getText().toString().trim() : "";

        setLoading(true);

        FormacaoRequest request = new FormacaoRequest(
                instituicao, curso, grau, mesInicio, anoInicio,
                mesConclusao, anoConclusao, aindaCursando);

        perfilRepository.adicionarFormacao(request, new PerfilRepository.PerfilCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(rootView, getString(R.string.sucesso_formacao_salva), Snackbar.LENGTH_SHORT).show();
                rootView.postDelayed(
                        () -> NavHostFragment.findNavController(EditarFormacaoFragment.this).navigateUp(),
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
        btnSalvar.setText(carregando ? getString(R.string.carregando) : getString(R.string.salvar));
    }

    private void setLoading(boolean loading) {
        if (btnSalvar == null) return;
        btnSalvar.setEnabled(!loading);
        btnSalvar.setText(loading ? getString(R.string.salvando) : getString(R.string.salvar));
    }
}
