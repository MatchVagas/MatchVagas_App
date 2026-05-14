package com.edu.matchvagasapp.features.buscar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.model.VagaResponse;
import com.edu.matchvagasapp.data.repository.VagaRepository;
import com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment;
import com.edu.matchvagasapp.features.vagas.DetalhesVagaFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class BuscarFragment extends Fragment {

    private final VagaRepository vagaRepository = new VagaRepository();
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private String queryAtual = "";
    private String areaAtual = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configurarBusca(view);
        configurarFiltros(view);
        buscar(null, null);
    }

    // ── Busca com debounce ────────────────────────────────────────────────────

    private void configurarBusca(View view) {
        TextInputEditText etSearch = view.findViewById(R.id.et_search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                queryAtual = s.toString().trim();
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> buscar(queryAtual.isEmpty() ? null : queryAtual, areaAtual);
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchHandler.removeCallbacks(searchRunnable);
                buscar(queryAtual.isEmpty() ? null : queryAtual, areaAtual);
                return true;
            }
            return false;
        });
    }

    // ── Filtros por chips ─────────────────────────────────────────────────────

    private void configurarFiltros(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_filter);
        String chipTodos = getString(R.string.chip_todos);

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            Chip chip = group.findViewById(checkedIds.get(0));
            if (chip == null) return;
            String texto = chip.getText().toString();
            areaAtual = chipTodos.equals(texto) ? null : texto;
            buscar(queryAtual.isEmpty() ? null : queryAtual, areaAtual);
        });
    }

    // ── Chamada à API ─────────────────────────────────────────────────────────

    private void buscar(String titulo, String area) {
        setResultadosCount(getString(R.string.carregando));

        vagaRepository.buscarVagasFiltradas(titulo, area, new VagaRepository.VagasCallback() {
            @Override
            public void onSuccess(List<VagaResponse> vagas) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root != null) popularResultados(root, vagas);
                });
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setResultadosCount("Erro ao buscar vagas");
                    View root = getView();
                    if (root != null) limparResultados(root);
                });
            }
        });
    }

    // ── Popular resultados ────────────────────────────────────────────────────

    private void popularResultados(View root, List<VagaResponse> vagas) {
        LinearLayout container = root.findViewById(R.id.layout_resultados);
        if (container == null) return;

        container.removeAllViews();
        setResultadosCount(vagas.size() + " " + getString(R.string.resultados));

        if (vagas.isEmpty()) {
            container.addView(textoVazio(getString(R.string.buscar_hint)
                    .contains("Buscar") ? "Nenhuma vaga encontrada" : "Nenhuma vaga encontrada"));
            return;
        }

        for (VagaResponse vaga : vagas) {
            container.addView(criarCardVaga(vaga, container));
        }
    }

    private void limparResultados(View root) {
        LinearLayout container = root.findViewById(R.id.layout_resultados);
        if (container != null) container.removeAllViews();
    }

    private View criarCardVaga(VagaResponse vaga, LinearLayout container) {
        View card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_vaga, container, false);

        long vagaId  = vaga.getId() != null ? vaga.getId() : -1L;
        String titulo  = vaga.getTitulo() != null ? vaga.getTitulo() : "";
        String empresa = vaga.getNomeFantasiaEmpresa() != null ? vaga.getNomeFantasiaEmpresa() : "";
        String inicial = vaga.getInicialEmpresa();
        String local   = vaga.getLocalFormatado();
        String tipo    = vaga.getTipoVagaDescricao() != null ? vaga.getTipoVagaDescricao() : "";
        String salario = vaga.getSalarioFormatado();

        setText(card, R.id.tv_inicial, inicial);
        setText(card, R.id.tv_titulo, titulo);
        setText(card, R.id.tv_empresa, empresa);
        setText(card, R.id.tv_local, local);
        setText(card, R.id.tv_tipo, tipo);
        setText(card, R.id.tv_salario, salario);

        card.setOnClickListener(v ->
                navegarParaDetalhes(vagaId, titulo, empresa, inicial, local, tipo, salario));
        card.findViewById(R.id.btn_detalhes).setOnClickListener(v ->
                navegarParaDetalhes(vagaId, titulo, empresa, inicial, local, tipo, salario));
        card.findViewById(R.id.btn_candidatar).setOnClickListener(v ->
                navegarParaConfirmar(vagaId, titulo, empresa, inicial));

        return card;
    }

    // ── Navegação ─────────────────────────────────────────────────────────────

    private void navegarParaDetalhes(long vagaId, String titulo, String empresa,
                                     String inicial, String local, String tipo, String salario) {
        Bundle args = new Bundle();
        args.putLong(DetalhesVagaFragment.ARG_VAGA_ID, vagaId);
        args.putString(DetalhesVagaFragment.ARG_TITULO, titulo);
        args.putString(DetalhesVagaFragment.ARG_EMPRESA, empresa);
        args.putString(DetalhesVagaFragment.ARG_INICIAL, inicial);
        args.putString(DetalhesVagaFragment.ARG_LOCAL, local);
        args.putString(DetalhesVagaFragment.ARG_TIPO, tipo);
        args.putString(DetalhesVagaFragment.ARG_SALARIO, salario);
        args.putInt(DetalhesVagaFragment.ARG_MATCH, 0);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_detalhes, args);
    }

    private void navegarParaConfirmar(long vagaId, String titulo, String empresa, String inicial) {
        Bundle args = new Bundle();
        args.putLong(ConfirmarCandidaturaFragment.ARG_VAGA_ID, vagaId);
        args.putString(ConfirmarCandidaturaFragment.ARG_TITULO, titulo);
        args.putString(ConfirmarCandidaturaFragment.ARG_EMPRESA, empresa);
        args.putString(ConfirmarCandidaturaFragment.ARG_INICIAL, inicial);
        args.putInt(ConfirmarCandidaturaFragment.ARG_MATCH, 0);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_confirmar_candidatura, args);
    }

    // ── Util ──────────────────────────────────────────────────────────────────

    private void setResultadosCount(String texto) {
        View root = getView();
        if (root == null) return;
        TextView tv = root.findViewById(R.id.tv_resultados_count);
        if (tv != null) tv.setText(texto);
    }

    private TextView textoVazio(String msg) {
        TextView tv = new TextView(requireContext());
        tv.setText(msg);
        tv.setTextColor(requireContext().getColor(R.color.text_secondary));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(48, 80, 48, 80);
        return tv;
    }

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchHandler.removeCallbacks(searchRunnable);
    }
}
