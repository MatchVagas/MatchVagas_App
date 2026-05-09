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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditarFormacaoFragment extends Fragment {

    private TextInputLayout tilInstituicao, tilCurso, tilInicioAno, tilConclusaoAno;
    private TextInputEditText etInstituicao, etCurso, etInicioAno, etConclusaoAno;
    private AutoCompleteTextView etGrau, etInicioMes, etConclusaoMes;
    private MaterialSwitch switchCursando;
    private View containerConclusao;

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
        prefillData();
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
        // Oculta o bloco de conclusão quando "Ainda cursando" está ativo
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

        MaterialButton btnSalvar = view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(v -> salvarFormacao(view));
    }

    private void prefillData() {
        etInstituicao.setText("FATEC São Paulo");
        etCurso.setText("Análise e Desenvolvimento de Sistemas");
        etGrau.setText(getString(R.string.grau_graduacao), false);
        etInicioMes.setText(getString(R.string.mes_fev), false);
        etInicioAno.setText("2022");
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

        if (!switchCursando.isChecked()) {
            String anoConclusao = etConclusaoAno.getText() != null
                    ? etConclusaoAno.getText().toString().trim() : "";
            if (anoConclusao.isEmpty() || anoConclusao.length() != 4) {
                tilConclusaoAno.setError(getString(R.string.erro_ano_invalido));
                valido = false;
            } else {
                tilConclusaoAno.setError(null);
            }
        }

        if (!valido) return;

        Snackbar.make(rootView, getString(R.string.sucesso_formacao_salva), Snackbar.LENGTH_SHORT).show();
        rootView.postDelayed(() -> NavHostFragment.findNavController(this).navigateUp(), 1200);
    }
}
