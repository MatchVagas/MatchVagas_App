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
import com.edu.matchvagasapp.data.model.ExperienciaRequest;
import com.edu.matchvagasapp.data.model.ExperienciaResponse;
import com.edu.matchvagasapp.data.repository.PerfilRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class EditarExperienciaFragment extends Fragment {

    private TextInputLayout tilCargo, tilEmpresa, tilInicioAno, tilSaidaAno;
    private TextInputEditText etCargo, etEmpresa, etCidadeExp, etInicioAno, etSaidaAno;
    private AutoCompleteTextView etModalidade, etVinculo, etInicioMes, etSaidaMes;
    private MaterialSwitch switchAtual;
    private View containerSaida;
    private MaterialButton btnSalvar;

    private final PerfilRepository perfilRepository = new PerfilRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_experiencia, container, false);
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
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.header_experiencia), (v, insets) -> {
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
        tilCargo = view.findViewById(R.id.til_cargo);
        tilEmpresa = view.findViewById(R.id.til_empresa);
        tilInicioAno = view.findViewById(R.id.til_inicio_ano);
        tilSaidaAno = view.findViewById(R.id.til_saida_ano);

        etCargo = view.findViewById(R.id.et_cargo);
        etEmpresa = view.findViewById(R.id.et_empresa);
        etModalidade = view.findViewById(R.id.et_modalidade);
        etVinculo = view.findViewById(R.id.et_vinculo);
        etCidadeExp = view.findViewById(R.id.et_cidade_exp);
        etInicioMes = view.findViewById(R.id.et_inicio_mes);
        etInicioAno = view.findViewById(R.id.et_inicio_ano);
        etSaidaMes = view.findViewById(R.id.et_saida_mes);
        etSaidaAno = view.findViewById(R.id.et_saida_ano);

        switchAtual = view.findViewById(R.id.switch_atual);
        containerSaida = view.findViewById(R.id.container_saida);
    }

    private void setupDropdowns() {
        String[] modalidades = {
                getString(R.string.modalidade_presencial),
                getString(R.string.modalidade_remoto),
                getString(R.string.modalidade_hibrido)
        };
        etModalidade.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, modalidades));

        String[] vinculos = {
                getString(R.string.vinculo_clt),
                getString(R.string.vinculo_pj),
                getString(R.string.vinculo_estagio),
                getString(R.string.vinculo_freelance),
                getString(R.string.vinculo_temporario)
        };
        etVinculo.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, vinculos));

        String[] meses = {
                getString(R.string.mes_jan), getString(R.string.mes_fev),
                getString(R.string.mes_mar), getString(R.string.mes_abr),
                getString(R.string.mes_mai), getString(R.string.mes_jun),
                getString(R.string.mes_jul), getString(R.string.mes_ago),
                getString(R.string.mes_set), getString(R.string.mes_out),
                getString(R.string.mes_nov), getString(R.string.mes_dez)
        };
        etInicioMes.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, meses));
        etSaidaMes.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, meses));
    }

    private void setupSwitch() {
        atualizarVisibilidadeSaida(switchAtual.isChecked());
        switchAtual.setOnCheckedChangeListener((btn, isChecked) ->
                atualizarVisibilidadeSaida(isChecked));
    }

    private void atualizarVisibilidadeSaida(boolean trabalhoAtual) {
        containerSaida.setVisibility(trabalhoAtual ? View.GONE : View.VISIBLE);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        btnSalvar = view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(v -> salvarExperiencia(view));
    }

    private void carregarDados() {
        setCarregando(true);
        perfilRepository.buscarExperiencias(new PerfilRepository.ExperienciasCallback() {
            @Override
            public void onSucesso(List<ExperienciaResponse> lista) {
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

    private void preencherCampos(ExperienciaResponse e) {
        if (e.getCargo() != null)      etCargo.setText(e.getCargo());
        if (e.getEmpresa() != null)    etEmpresa.setText(e.getEmpresa());
        if (e.getModalidade() != null) etModalidade.setText(e.getModalidade(), false);
        if (e.getVinculo() != null)    etVinculo.setText(e.getVinculo(), false);
        if (e.getCidade() != null)     etCidadeExp.setText(e.getCidade());
        if (e.getMesInicio() != null)  etInicioMes.setText(e.getMesInicio(), false);
        if (e.getAnoInicio() != null)  etInicioAno.setText(e.getAnoInicio());

        switchAtual.setChecked(e.isEmpregoAtual());
        if (!e.isEmpregoAtual()) {
            if (e.getMesSaida() != null) etSaidaMes.setText(e.getMesSaida(), false);
            if (e.getAnoSaida() != null) etSaidaAno.setText(e.getAnoSaida());
        }
    }

    private void salvarExperiencia(View rootView) {
        String cargo = etCargo.getText() != null ? etCargo.getText().toString().trim() : "";
        String empresa = etEmpresa.getText() != null ? etEmpresa.getText().toString().trim() : "";
        String anoInicio = etInicioAno.getText() != null ? etInicioAno.getText().toString().trim() : "";

        tilCargo.setError(null);
        tilEmpresa.setError(null);
        tilInicioAno.setError(null);

        boolean valido = true;

        if (cargo.isEmpty()) {
            tilCargo.setError(getString(R.string.erro_cargo_vazio));
            valido = false;
        }

        if (empresa.isEmpty()) {
            tilEmpresa.setError(getString(R.string.erro_empresa_vazia));
            valido = false;
        }

        if (anoInicio.isEmpty() || anoInicio.length() != 4) {
            tilInicioAno.setError(getString(R.string.erro_ano_invalido));
            valido = false;
        }

        String mesSaida = null;
        String anoSaida = null;
        boolean empregoAtual = switchAtual.isChecked();

        if (!empregoAtual) {
            String anoSaidaStr = etSaidaAno.getText() != null
                    ? etSaidaAno.getText().toString().trim() : "";
            if (anoSaidaStr.isEmpty() || anoSaidaStr.length() != 4) {
                tilSaidaAno.setError(getString(R.string.erro_ano_invalido));
                valido = false;
            } else {
                tilSaidaAno.setError(null);
                anoSaida = anoSaidaStr;
                mesSaida = etSaidaMes.getText() != null
                        ? etSaidaMes.getText().toString().trim() : "";
            }
        }

        if (!valido) return;

        String modalidade = etModalidade.getText() != null ? etModalidade.getText().toString().trim() : "";
        String vinculo = etVinculo.getText() != null ? etVinculo.getText().toString().trim() : "";
        String cidade = etCidadeExp.getText() != null ? etCidadeExp.getText().toString().trim() : "";
        String mesInicio = etInicioMes.getText() != null ? etInicioMes.getText().toString().trim() : "";

        setLoading(true);

        ExperienciaRequest request = new ExperienciaRequest(
                cargo, empresa, modalidade, vinculo, cidade,
                mesInicio, anoInicio, mesSaida, anoSaida, empregoAtual);

        perfilRepository.adicionarExperiencia(request, new PerfilRepository.PerfilCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(rootView, getString(R.string.sucesso_experiencia_salva), Snackbar.LENGTH_SHORT).show();
                rootView.postDelayed(
                        () -> NavHostFragment.findNavController(EditarExperienciaFragment.this).navigateUp(),
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
