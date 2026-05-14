package com.edu.matchvagasapp.features.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.edu.matchvagasapp.MatchVagasApp;
import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.local.TokenManager;
import com.edu.matchvagasapp.data.model.CandidatoPerfilResponse;
import com.edu.matchvagasapp.data.model.DadosPessoaisResponse;
import com.edu.matchvagasapp.data.model.ExperienciaResponse;
import com.edu.matchvagasapp.data.model.FormacaoResponse;
import com.edu.matchvagasapp.data.model.HabilidadesResponse;
import com.edu.matchvagasapp.data.repository.PerfilRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PerfilFragment extends Fragment {

    private final PerfilRepository perfilRepository = new PerfilRepository();

    // Progresso: 5 seções, cada uma vale 20 pontos
    // índices: 0=dados pessoais, 1=formação, 2=experiência, 3=habilidades, 4=perfil profissional
    private final boolean[] secoesConcluidas = {false, false, false, false, false};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configurarNavegacao(view);
        preencherUsuario(view);
        carregarDadosPerfil(view);
    }

    // ── Navegação ─────────────────────────────────────────────────────────────

    private void configurarNavegacao(View view) {
        view.findViewById(R.id.item_dados_pessoais).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_editar_dados));

        view.findViewById(R.id.item_formacao).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_editar_formacao));

        view.findViewById(R.id.item_experiencia).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_editar_experiencia));

        view.findViewById(R.id.item_habilidades).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_editar_habilidades));

        view.findViewById(R.id.item_perfil_profissional).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_editar_perfil_profissional));

        view.findViewById(R.id.btn_completar_agora).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_cadastro_curriculo));

        view.findViewById(R.id.btn_editar_perfil).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_editar_dados));

        view.findViewById(R.id.btn_sair).setOnClickListener(v -> logout(view));
    }

    // ── Usuário (TokenManager) ────────────────────────────────────────────────

    private void preencherUsuario(View view) {
        TokenManager tm = new TokenManager(MatchVagasApp.getAppContext());
        String nome = tm.getNome();
        if (nome == null || nome.isEmpty()) return;

        String inicial = String.valueOf(nome.charAt(0)).toUpperCase();
        setText(view, R.id.tv_avatar_inicial, inicial);
        setText(view, R.id.tv_perfil_nome, nome);
    }

    // ── Dados do perfil (API) ─────────────────────────────────────────────────

    private void carregarDadosPerfil(View view) {
        // Todas as 5 chamadas são feitas em paralelo; o progresso é recalculado
        // toda vez que uma delas conclui (total = 5 chamadas independentes)
        AtomicInteger pendentes = new AtomicInteger(5);

        carregarDadosPessoais(view, pendentes);
        carregarFormacoes(view, pendentes);
        carregarExperiencias(view, pendentes);
        carregarHabilidades(view, pendentes);
        carregarPerfilProfissional(view, pendentes);
    }

    private void carregarDadosPessoais(View view, AtomicInteger pendentes) {
        perfilRepository.buscarDadosPessoais(new PerfilRepository.DadosPessoaisCallback() {
            @Override
            public void onSucesso(DadosPessoaisResponse dados) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;

                    // Subtítulo: usar cidade/estado do perfil como cargo se disponível
                    if (dados.getCidade() != null && !dados.getCidade().isEmpty()) {
                        String local = dados.getCidade()
                                + (dados.getEstado() != null ? ", " + dados.getEstado() : "");
                        setText(root, R.id.tv_perfil_cargo, local);
                    }

                    atualizarSecao(root, R.id.tv_dados_sub, R.id.tv_dados_status,
                            "Nome, e-mail, localização", true, 0);
                    atualizarProgresso(root, pendentes);
                });
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root != null) {
                        atualizarSecao(root, R.id.tv_dados_sub, R.id.tv_dados_status,
                                "Não preenchido", false, 0);
                        atualizarProgresso(root, pendentes);
                    }
                });
            }
        });
    }

    private void carregarFormacoes(View view, AtomicInteger pendentes) {
        perfilRepository.buscarFormacoes(new PerfilRepository.FormacoesCallback() {
            @Override
            public void onSucesso(List<FormacaoResponse> lista) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;

                    String sub = lista.size() == 1
                            ? "1 formação adicionada"
                            : lista.size() + " formações adicionadas";
                    atualizarSecao(root, R.id.tv_formacao_sub, R.id.tv_formacao_status,
                            sub, true, 1);
                    atualizarProgresso(root, pendentes);
                });
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root != null) {
                        atualizarSecao(root, R.id.tv_formacao_sub, R.id.tv_formacao_status,
                                "Nenhuma formação adicionada", false, 1);
                        atualizarProgresso(root, pendentes);
                    }
                });
            }
        });
    }

    private void carregarExperiencias(View view, AtomicInteger pendentes) {
        perfilRepository.buscarExperiencias(new PerfilRepository.ExperienciasCallback() {
            @Override
            public void onSucesso(List<ExperienciaResponse> lista) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;

                    // Primeiro cargo como subtítulo no header
                    ExperienciaResponse primeira = lista.get(0);
                    if (primeira.getCargo() != null) {
                        String cargo = primeira.getCargo()
                                + (primeira.getEmpresa() != null
                                        ? " · " + primeira.getEmpresa() : "");
                        setText(root, R.id.tv_perfil_cargo, cargo);
                    }

                    String sub = lista.size() == 1
                            ? "1 experiência adicionada"
                            : lista.size() + " experiências adicionadas";
                    atualizarSecao(root, R.id.tv_experiencia_sub, R.id.tv_experiencia_status,
                            sub, true, 2);
                    atualizarProgresso(root, pendentes);
                });
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root != null) {
                        atualizarSecao(root, R.id.tv_experiencia_sub, R.id.tv_experiencia_status,
                                "Nenhuma experiência adicionada", false, 2);
                        atualizarProgresso(root, pendentes);
                    }
                });
            }
        });
    }

    private void carregarHabilidades(View view, AtomicInteger pendentes) {
        perfilRepository.buscarHabilidades(new PerfilRepository.HabilidadesCallback() {
            @Override
            public void onSucesso(List<HabilidadesResponse> lista) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;

                    int n = lista.size();
                    String sub = n == 0 ? "Nenhuma habilidade adicionada"
                            : n == 1 ? "1 habilidade adicionada"
                            : n + " habilidades adicionadas";
                    boolean completo = n > 0;

                    atualizarSecao(root, R.id.tv_habilidades_sub, R.id.tv_habilidades_status,
                            sub, completo, 3);
                    atualizarProgresso(root, pendentes);
                });
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root != null) {
                        atualizarSecao(root, R.id.tv_habilidades_sub, R.id.tv_habilidades_status,
                                "Nenhuma habilidade adicionada", false, 3);
                        atualizarProgresso(root, pendentes);
                    }
                });
            }
        });
    }

    private void carregarPerfilProfissional(View view, AtomicInteger pendentes) {
        perfilRepository.buscarPerfilProfissional(new PerfilRepository.PerfilProfissionalCallback() {
            @Override
            public void onSucesso(CandidatoPerfilResponse dados) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root == null) return;

                    String objetivo = dados.getObjetivoProfissional();
                    boolean temObjetivo = objetivo != null && !objetivo.isEmpty();

                    String sub = temObjetivo ? objetivo : "Objetivo, disponibilidade e pretensão";
                    if (temObjetivo && sub.length() > 50) sub = sub.substring(0, 47) + "…";

                    boolean completo = temObjetivo
                            || dados.getDisponibilidade() != null
                            || dados.getPretensaoSalarial() != null;

                    atualizarSecao(root, R.id.tv_objetivo_sub, R.id.tv_objetivo_status,
                            sub, completo, 4);
                    atualizarProgresso(root, pendentes);
                });
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    View root = getView();
                    if (root != null) {
                        atualizarSecao(root, R.id.tv_objetivo_sub, R.id.tv_objetivo_status,
                                "Objetivo, disponibilidade e pretensão", false, 4);
                        atualizarProgresso(root, pendentes);
                    }
                });
            }
        });
    }

    // ── Seção e progresso ─────────────────────────────────────────────────────

    private void atualizarSecao(View root, int subId, int statusId,
                                String subtexto, boolean completo, int indice) {
        setText(root, subId, subtexto);

        secoesConcluidas[indice] = completo;

        TextView tvStatus = root.findViewById(statusId);
        if (tvStatus == null) return;

        if (completo) {
            tvStatus.setText("Completo");
            tvStatus.setBackgroundResource(R.drawable.bg_status_entrevista);
            tvStatus.setTextColor(requireContext().getColor(R.color.status_entrevista_text));
        } else {
            tvStatus.setText("Incompleto");
            tvStatus.setBackgroundResource(R.drawable.bg_status_analise);
            tvStatus.setTextColor(requireContext().getColor(R.color.status_analise_text));
        }
    }

    private void atualizarProgresso(View root, AtomicInteger pendentes) {
        pendentes.decrementAndGet();

        int concluidas = 0;
        for (boolean ok : secoesConcluidas) if (ok) concluidas++;
        int pct = concluidas * 20;

        TextView tvPct = root.findViewById(R.id.tv_perfil_progresso_pct);
        if (tvPct != null) tvPct.setText(pct + "%");

        ProgressBar pb = root.findViewById(R.id.progressbar_perfil);
        if (pb != null) pb.setProgress(pct);
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    private void logout(View view) {
        new TokenManager(MatchVagasApp.getAppContext()).limpar();
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.loginFragment,
                        null,
                        new androidx.navigation.NavOptions.Builder()
                                .setPopUpTo(R.id.nav_graph, true)
                                .build());
    }

    // ── Util ──────────────────────────────────────────────────────────────────

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }
}
