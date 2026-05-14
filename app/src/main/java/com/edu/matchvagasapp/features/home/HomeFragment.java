package com.edu.matchvagasapp.features.home;

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

import com.edu.matchvagasapp.MatchVagasApp;
import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.local.TokenManager;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.model.SugestaoVagaResponse;
import com.edu.matchvagasapp.data.model.VagaResponse;
import com.edu.matchvagasapp.data.repository.CandidaturaRepository;
import com.edu.matchvagasapp.data.repository.VagaRepository;
import com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment;
import com.edu.matchvagasapp.features.vagas.DetalhesVagaFragment;

import java.util.List;

public class HomeFragment extends Fragment {

    private final VagaRepository vagaRepository = new VagaRepository();
    private final CandidaturaRepository candidaturaRepository = new CandidaturaRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preencherUsuario(view);
        mostrarCarregandoVagas(view);
        carregarCandidaturas();
        carregarVagas();
    }

    // ── Usuário ───────────────────────────────────────────────────────────────

    private void preencherUsuario(View view) {
        TokenManager tm = new TokenManager(MatchVagasApp.getAppContext());
        String nome = tm.getNome();
        if (nome == null || nome.isEmpty()) return;

        String primeiroNome = nome.split(" ")[0];
        String inicial = String.valueOf(nome.charAt(0)).toUpperCase();

        setText(view, R.id.tv_saudacao, "Olá, " + primeiroNome + "!");
        setText(view, R.id.tv_avatar_inicial, inicial);
    }

    // ── Candidaturas (stat) ───────────────────────────────────────────────────

    private void carregarCandidaturas() {
        candidaturaRepository.minhasCandidaturas(new CandidaturaRepository.CandidaturasCallback() {
            @Override
            public void onSuccess(List<CandidaturaResponse> candidaturas) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;
                    setText(root, R.id.tv_stat_candidaturas, String.valueOf(candidaturas.size()));
                });
            }

            @Override
            public void onError(String mensagem) { /* mantém valor padrão */ }
        });
    }

    // ── Vagas recomendadas ────────────────────────────────────────────────────

    private void mostrarCarregandoVagas(View view) {
        LinearLayout container = view.findViewById(R.id.layout_vagas_recomendadas);
        if (container == null) return;

        TextView tvLoading = new TextView(requireContext());
        tvLoading.setText(R.string.carregando);
        tvLoading.setTextColor(requireContext().getColor(R.color.text_secondary));
        tvLoading.setGravity(Gravity.CENTER);
        tvLoading.setPadding(48, 48, 48, 48);
        container.addView(tvLoading);
    }

    private void carregarVagas() {
        vagaRepository.buscarSugestoes(new VagaRepository.SugestoesCallback() {
            @Override
            public void onSuccess(List<SugestaoVagaResponse> sugestoes) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;
                    popularSugestoes(root, sugestoes);
                });
            }

            @Override
            public void onError(String mensagem) {
                // Fallback para vagas genéricas quando candidato não tem perfil ainda
                vagaRepository.buscarVagas(new VagaRepository.VagasCallback() {
                    @Override
                    public void onSuccess(List<VagaResponse> vagas) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
                            View root = getView();
                            if (root == null) return;
                            popularVagasGenericas(root, vagas);
                        });
                    }

                    @Override
                    public void onError(String mensagemFallback) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
                            View root = getView();
                            if (root == null) return;
                            mostrarErroVagas(root, mensagemFallback);
                        });
                    }
                });
            }
        });
    }

    private void popularSugestoes(View root, List<SugestaoVagaResponse> sugestoes) {
        LinearLayout container = root.findViewById(R.id.layout_vagas_recomendadas);
        if (container == null) return;

        container.removeAllViews();

        setText(root, R.id.tv_stat_matches, String.valueOf(sugestoes.size()));

        if (sugestoes.isEmpty()) {
            TextView tvVazio = new TextView(requireContext());
            tvVazio.setText("Nenhuma sugestão disponível no momento");
            tvVazio.setTextColor(requireContext().getColor(R.color.text_secondary));
            tvVazio.setGravity(Gravity.CENTER);
            tvVazio.setPadding(48, 48, 48, 48);
            container.addView(tvVazio);
            return;
        }

        int limite = Math.min(sugestoes.size(), 3);
        for (int i = 0; i < limite; i++) {
            SugestaoVagaResponse sugestao = sugestoes.get(i);
            container.addView(criarCardVaga(sugestao.getVaga(), sugestao.getPontuacao(), container));
        }
    }

    private void popularVagasGenericas(View root, List<VagaResponse> vagas) {
        LinearLayout container = root.findViewById(R.id.layout_vagas_recomendadas);
        if (container == null) return;

        container.removeAllViews();

        setText(root, R.id.tv_stat_matches, String.valueOf(vagas.size()));

        if (vagas.isEmpty()) {
            TextView tvVazio = new TextView(requireContext());
            tvVazio.setText("Nenhuma vaga disponível no momento");
            tvVazio.setTextColor(requireContext().getColor(R.color.text_secondary));
            tvVazio.setGravity(Gravity.CENTER);
            tvVazio.setPadding(48, 48, 48, 48);
            container.addView(tvVazio);
            return;
        }

        int limite = Math.min(vagas.size(), 3);
        for (int i = 0; i < limite; i++) {
            container.addView(criarCardVaga(vagas.get(i), 0, container));
        }
    }

    private void mostrarErroVagas(View root, String mensagem) {
        LinearLayout container = root.findViewById(R.id.layout_vagas_recomendadas);
        if (container == null) return;

        container.removeAllViews();

        TextView tvErro = new TextView(requireContext());
        tvErro.setText("Não foi possível carregar as vagas");
        tvErro.setTextColor(requireContext().getColor(R.color.text_secondary));
        tvErro.setGravity(Gravity.CENTER);
        tvErro.setPadding(48, 48, 48, 48);
        container.addView(tvErro);
    }

    private View criarCardVaga(VagaResponse vaga, int match, LinearLayout container) {
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

        TextView tvMatch = card.findViewById(R.id.tv_match);
        if (tvMatch != null) {
            if (match > 0) {
                tvMatch.setText(match + "% Match");
                tvMatch.setVisibility(View.VISIBLE);
            } else {
                tvMatch.setVisibility(View.GONE);
            }
        }

        card.setOnClickListener(v ->
                navegarParaDetalhes(vagaId, titulo, empresa, inicial, local, tipo, salario, match));
        card.findViewById(R.id.btn_detalhes).setOnClickListener(v ->
                navegarParaDetalhes(vagaId, titulo, empresa, inicial, local, tipo, salario, match));
        card.findViewById(R.id.btn_candidatar).setOnClickListener(v ->
                navegarParaConfirmar(vagaId, titulo, empresa, inicial, match));

        return card;
    }

    // ── Navegação ─────────────────────────────────────────────────────────────

    private void navegarParaDetalhes(long vagaId, String titulo, String empresa, String inicial,
                                     String local, String tipo, String salario, int match) {
        Bundle args = new Bundle();
        args.putLong(DetalhesVagaFragment.ARG_VAGA_ID, vagaId);
        args.putString(DetalhesVagaFragment.ARG_TITULO, titulo);
        args.putString(DetalhesVagaFragment.ARG_EMPRESA, empresa);
        args.putString(DetalhesVagaFragment.ARG_INICIAL, inicial);
        args.putString(DetalhesVagaFragment.ARG_LOCAL, local);
        args.putString(DetalhesVagaFragment.ARG_TIPO, tipo);
        args.putString(DetalhesVagaFragment.ARG_SALARIO, salario);
        args.putInt(DetalhesVagaFragment.ARG_MATCH, match);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_detalhes, args);
    }

    private void navegarParaConfirmar(long vagaId, String titulo, String empresa, String inicial,
                                      int match) {
        Bundle args = new Bundle();
        args.putLong(ConfirmarCandidaturaFragment.ARG_VAGA_ID, vagaId);
        args.putString(ConfirmarCandidaturaFragment.ARG_TITULO, titulo);
        args.putString(ConfirmarCandidaturaFragment.ARG_EMPRESA, empresa);
        args.putString(ConfirmarCandidaturaFragment.ARG_INICIAL, inicial);
        args.putInt(ConfirmarCandidaturaFragment.ARG_MATCH, match);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_confirmar_candidatura, args);
    }

    // ── Util ──────────────────────────────────────────────────────────────────

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }
}
