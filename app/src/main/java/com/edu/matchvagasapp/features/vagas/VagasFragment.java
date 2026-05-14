package com.edu.matchvagasapp.features.vagas;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.local.VagasSalvasManager;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.model.VagaResponse;
import com.edu.matchvagasapp.data.repository.CandidaturaRepository;
import com.edu.matchvagasapp.data.repository.VagaRepository;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class VagasFragment extends Fragment {

    private final CandidaturaRepository candidaturaRepository = new CandidaturaRepository();
    private final VagaRepository vagaRepository = new VagaRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vagas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        View scrollCandidaturas = view.findViewById(R.id.scroll_candidaturas);
        View scrollSalvas = view.findViewById(R.id.scroll_salvas);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    scrollCandidaturas.setVisibility(View.VISIBLE);
                    scrollSalvas.setVisibility(View.GONE);
                } else {
                    scrollCandidaturas.setVisibility(View.GONE);
                    scrollSalvas.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        carregarCandidaturas(view, tabLayout);
        carregarVagasSalvas(view, tabLayout);
    }

    // ── Aba Candidaturas ──────────────────────────────────────────────────────

    private void carregarCandidaturas(View view, TabLayout tabLayout) {
        candidaturaRepository.minhasCandidaturas(new CandidaturaRepository.CandidaturasCallback() {
            @Override
            public void onSuccess(List<CandidaturaResponse> candidaturas) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;
                    popularCandidaturas(root, tabLayout, candidaturas);
                });
            }
            @Override public void onError(String mensagem) {}
        });
    }

    private void popularCandidaturas(View root, TabLayout tabLayout,
                                     List<CandidaturaResponse> candidaturas) {
        LinearLayout layout = root.findViewById(R.id.layout_candidaturas);
        if (layout == null) return;
        layout.removeAllViews();

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) tab.setText("Candidaturas (" + candidaturas.size() + ")");

        if (candidaturas.isEmpty()) {
            layout.addView(textoVazio("Você ainda não se candidatou a nenhuma vaga"));
            return;
        }
        for (CandidaturaResponse c : candidaturas) {
            layout.addView(criarCardCandidatura(c, layout));
        }
    }

    private View criarCardCandidatura(CandidaturaResponse candidatura, LinearLayout parent) {
        View card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_candidatura, parent, false);

        String titulo = candidatura.getTituloVaga() != null ? candidatura.getTituloVaga() : "";
        String inicial = titulo.isEmpty() ? "?" : String.valueOf(titulo.charAt(0)).toUpperCase();
        String status  = candidatura.getStatus() != null ? candidatura.getStatus() : "";
        String data    = formatarData(candidatura.getDataCandidatura());
        long vagaId    = candidatura.getVagaId() != null ? candidatura.getVagaId() : -1L;

        setText(card, R.id.tv_inicial, inicial);
        setText(card, R.id.tv_titulo, titulo);
        setText(card, R.id.tv_data, data.isEmpty() ? "" : "Candidatou em: " + data);
        aplicarStatus(card, status);

        card.findViewById(R.id.btn_detalhes).setOnClickListener(v ->
                navegarParaDetalhes(vagaId, titulo, inicial));
        return card;
    }

    // ── Aba Salvas ────────────────────────────────────────────────────────────

    private void carregarVagasSalvas(View view, TabLayout tabLayout) {
        VagasSalvasManager manager = new VagasSalvasManager(requireContext());
        List<Long> ids = manager.getVagasSalvas();

        View root = getView();
        if (root == null) return;
        LinearLayout layout = root.findViewById(R.id.layout_salvas);
        if (layout == null) return;
        layout.removeAllViews();

        TabLayout.Tab tab = tabLayout.getTabAt(1);

        if (ids.isEmpty()) {
            if (tab != null) tab.setText("Salvas (0)");
            layout.addView(textoVazio("Nenhuma vaga salva ainda.\nSalve vagas nos Detalhes da Vaga."));
            return;
        }

        if (tab != null) tab.setText("Salvas (" + ids.size() + ")");
        layout.addView(textoVazio(getString(R.string.carregando)));

        // Busca cada vaga salva pela API e exibe conforme chegam
        final List<VagaResponse> carregadas = new ArrayList<>();
        final int[] concluidos = {0};
        final int total = ids.size();

        for (Long vagaId : ids) {
            vagaRepository.buscarVagaPorId(vagaId, new VagaRepository.VagaCallback() {
                @Override
                public void onSuccess(VagaResponse vaga) {
                    synchronized (carregadas) {
                        carregadas.add(vaga);
                        concluidos[0]++;
                    }
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        View r = getView();
                        if (r == null) return;
                        adicionarCardSalva(r, vaga);
                        if (concluidos[0] == total) removerLoading(r);
                    });
                }

                @Override
                public void onError(String mensagem) {
                    synchronized (carregadas) { concluidos[0]++; }
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        if (concluidos[0] == total) {
                            View r = getView();
                            if (r != null) removerLoading(r);
                        }
                    });
                }
            });
        }
    }

    private void adicionarCardSalva(View root, VagaResponse vaga) {
        LinearLayout layout = root.findViewById(R.id.layout_salvas);
        if (layout == null) return;

        long vagaId  = vaga.getId() != null ? vaga.getId() : -1L;
        String titulo  = vaga.getTitulo() != null ? vaga.getTitulo() : "";
        String empresa = vaga.getNomeFantasiaEmpresa() != null ? vaga.getNomeFantasiaEmpresa() : "";
        String inicial = vaga.getInicialEmpresa();
        String local   = vaga.getLocalFormatado();
        String tipo    = vaga.getTipoVagaDescricao() != null ? vaga.getTipoVagaDescricao() : "";
        String salario = vaga.getSalarioFormatado();

        View card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_vaga, layout, false);

        setText(card, R.id.tv_inicial, inicial);
        setText(card, R.id.tv_titulo, titulo);
        setText(card, R.id.tv_empresa, empresa);
        setText(card, R.id.tv_local, local);
        setText(card, R.id.tv_tipo, tipo);
        setText(card, R.id.tv_salario, salario);

        card.setOnClickListener(v -> navegarParaDetalhes(vagaId, titulo, inicial));
        card.findViewById(R.id.btn_detalhes).setOnClickListener(v ->
                navegarParaDetalhes(vagaId, titulo, inicial));
        card.findViewById(R.id.btn_candidatar).setOnClickListener(v ->
                navegarParaConfirmar(vagaId, titulo, empresa, inicial));

        layout.addView(card);
    }

    private void removerLoading(View root) {
        LinearLayout layout = root.findViewById(R.id.layout_salvas);
        if (layout == null || layout.getChildCount() == 0) return;
        View primeiro = layout.getChildAt(0);
        if (primeiro instanceof TextView) layout.removeViewAt(0);
    }

    // ── Navegação ─────────────────────────────────────────────────────────────

    private void navegarParaDetalhes(long vagaId, String titulo, String inicial) {
        Bundle args = new Bundle();
        args.putLong(DetalhesVagaFragment.ARG_VAGA_ID, vagaId);
        args.putString(DetalhesVagaFragment.ARG_TITULO, titulo);
        args.putString(DetalhesVagaFragment.ARG_EMPRESA, "");
        args.putString(DetalhesVagaFragment.ARG_INICIAL, inicial);
        args.putString(DetalhesVagaFragment.ARG_LOCAL, "");
        args.putString(DetalhesVagaFragment.ARG_TIPO, "");
        args.putString(DetalhesVagaFragment.ARG_SALARIO, "");
        args.putInt(DetalhesVagaFragment.ARG_MATCH, 0);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_detalhes, args);
    }

    private void navegarParaConfirmar(long vagaId, String titulo, String empresa, String inicial) {
        Bundle args = new Bundle();
        args.putLong(com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment.ARG_VAGA_ID, vagaId);
        args.putString(com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment.ARG_TITULO, titulo);
        args.putString(com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment.ARG_EMPRESA, empresa);
        args.putString(com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment.ARG_INICIAL, inicial);
        args.putInt(com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment.ARG_MATCH, 0);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_confirmar_candidatura, args);
    }

    // ── Status helpers ────────────────────────────────────────────────────────

    private void aplicarStatus(View card, String status) {
        TextView tvStatus = card.findViewById(R.id.tv_status);
        if (tvStatus == null) return;
        tvStatus.setText(formatarStatus(status));
        switch (status.toUpperCase()) {
            case "ENTREVISTA":
                tvStatus.setBackgroundResource(R.drawable.bg_status_entrevista);
                tvStatus.setTextColor(requireContext().getColor(R.color.status_entrevista_text));
                break;
            case "APROVADO":
                tvStatus.setBackgroundResource(R.drawable.bg_status_salvo);
                tvStatus.setTextColor(requireContext().getColor(R.color.status_salvo_text));
                break;
            default:
                tvStatus.setBackgroundResource(R.drawable.bg_status_analise);
                tvStatus.setTextColor(requireContext().getColor(R.color.status_analise_text));
                break;
        }
    }

    private String formatarStatus(String status) {
        if (status == null) return "Em Análise";
        switch (status.toUpperCase()) {
            case "ENTREVISTA": return "Entrevista";
            case "APROVADO":   return "Aprovado";
            case "REPROVADO":  return "Reprovado";
            default:           return "Em Análise";
        }
    }

    private String formatarData(String iso) {
        if (iso == null || iso.length() < 10) return "";
        String[] p = iso.substring(0, 10).split("-");
        return p.length == 3 ? p[2] + "/" + p[1] + "/" + p[0] : iso;
    }

    // ── Util ──────────────────────────────────────────────────────────────────

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
}
