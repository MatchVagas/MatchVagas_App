package com.edu.matchvagasapp.features.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.model.VagaResponse;
import com.edu.matchvagasapp.data.repository.VagaRepository;
import com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment;
import com.edu.matchvagasapp.features.vagas.DetalhesVagaFragment;

import java.util.List;

public class HomeFragment extends Fragment {

    private final VagaRepository vagaRepository = new VagaRepository();

    // IDs das vagas carregadas da API (fallback = -1 enquanto não carregado)
    private long vagaId1 = -1;
    private long vagaId2 = -1;
    private long vagaId3 = -1;

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

        carregarVagas(view);

        view.findViewById(R.id.card_vaga_android).setOnClickListener(v ->
                openDetalhes(vagaId1, "Desenvolvedor Android", "TechCorp Brasil", "T",
                        "São Paulo, SP", "CLT", "R$ 6.000 – R$ 9.000", 92));

        view.findViewById(R.id.card_vaga_backend).setOnClickListener(v ->
                openDetalhes(vagaId2, "Engenheiro Backend", "Startup XYZ", "S",
                        "Remoto", "PJ", "R$ 8.000 – R$ 12.000", 87));

        view.findViewById(R.id.card_vaga_fullstack).setOnClickListener(v ->
                openDetalhes(vagaId3, "Desenvolvedor Full Stack", "Banco Digital", "B",
                        "Rio de Janeiro, RJ", "CLT", "R$ 7.000 – R$ 10.000", 79));

        view.findViewById(R.id.btnCandidatarAndroid).setOnClickListener(v ->
                openConfirmar(vagaId1, "Desenvolvedor Android", "TechCorp Brasil", "T", 92));

        view.findViewById(R.id.btnCandidatarBackend).setOnClickListener(v ->
                openConfirmar(vagaId2, "Engenheiro Backend", "Startup XYZ", "S", 87));

        view.findViewById(R.id.btnCandidatarFullstack).setOnClickListener(v ->
                openConfirmar(vagaId3, "Desenvolvedor Full Stack", "Banco Digital", "B", 79));
    }

    private void carregarVagas(View view) {
        vagaRepository.buscarVagas(new VagaRepository.VagasCallback() {
            @Override
            public void onSuccess(List<VagaResponse> vagas) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    if (vagas.size() > 0) {
                        VagaResponse v1 = vagas.get(0);
                        vagaId1 = v1.getId() != null ? v1.getId() : -1;
                        atualizarCard(view, R.id.card_vaga_android, R.id.btnCandidatarAndroid, v1);
                    }
                    if (vagas.size() > 1) {
                        VagaResponse v2 = vagas.get(1);
                        vagaId2 = v2.getId() != null ? v2.getId() : -1;
                        atualizarCard(view, R.id.card_vaga_backend, R.id.btnCandidatarBackend, v2);
                    }
                    if (vagas.size() > 2) {
                        VagaResponse v3 = vagas.get(2);
                        vagaId3 = v3.getId() != null ? v3.getId() : -1;
                        atualizarCard(view, R.id.card_vaga_fullstack, R.id.btnCandidatarFullstack, v3);
                    }
                    // Reassigna cliques com os IDs reais depois de carregar
                    reassignarCliques(view, vagas);
                });
            }

            @Override
            public void onError(String mensagem) {
                // Falha silenciosa — os cards ficam com o conteúdo hardcoded do layout
            }
        });
    }

    private void reassignarCliques(View view, List<VagaResponse> vagas) {
        if (vagas.size() > 0) {
            VagaResponse v = vagas.get(0);
            view.findViewById(R.id.card_vaga_android).setOnClickListener(cv ->
                    openDetalhesFromVaga(vagaId1, v));
            view.findViewById(R.id.btnCandidatarAndroid).setOnClickListener(cv ->
                    openConfirmarFromVaga(vagaId1, v));
        }
        if (vagas.size() > 1) {
            VagaResponse v = vagas.get(1);
            view.findViewById(R.id.card_vaga_backend).setOnClickListener(cv ->
                    openDetalhesFromVaga(vagaId2, v));
            view.findViewById(R.id.btnCandidatarBackend).setOnClickListener(cv ->
                    openConfirmarFromVaga(vagaId2, v));
        }
        if (vagas.size() > 2) {
            VagaResponse v = vagas.get(2);
            view.findViewById(R.id.card_vaga_fullstack).setOnClickListener(cv ->
                    openDetalhesFromVaga(vagaId3, v));
            view.findViewById(R.id.btnCandidatarFullstack).setOnClickListener(cv ->
                    openConfirmarFromVaga(vagaId3, v));
        }
    }

    private void atualizarCard(View root, int cardId, int btnId, VagaResponse vaga) {
        // Atualiza os listeners do botão "Candidatar" dentro do card via tag
        View card = root.findViewById(cardId);
        if (card == null) return;
        card.setTag(vaga);
    }

    private void openDetalhesFromVaga(long vagaId, VagaResponse vaga) {
        openDetalhes(vagaId,
                vaga.getTitulo() != null ? vaga.getTitulo() : "",
                vaga.getNomeFantasiaEmpresa() != null ? vaga.getNomeFantasiaEmpresa() : "",
                vaga.getInicialEmpresa(),
                vaga.getLocalFormatado(),
                vaga.getModalidadeDescricao() != null ? vaga.getModalidadeDescricao() : "",
                vaga.getSalarioFormatado(),
                0);
    }

    private void openConfirmarFromVaga(long vagaId, VagaResponse vaga) {
        openConfirmar(vagaId,
                vaga.getTitulo() != null ? vaga.getTitulo() : "",
                vaga.getNomeFantasiaEmpresa() != null ? vaga.getNomeFantasiaEmpresa() : "",
                vaga.getInicialEmpresa(),
                0);
    }

    private void openConfirmar(long vagaId, String titulo, String empresa, String inicial, int match) {
        Bundle args = new Bundle();
        args.putString(ConfirmarCandidaturaFragment.ARG_TITULO, titulo);
        args.putString(ConfirmarCandidaturaFragment.ARG_EMPRESA, empresa);
        args.putString(ConfirmarCandidaturaFragment.ARG_INICIAL, inicial);
        args.putInt(ConfirmarCandidaturaFragment.ARG_MATCH, match);
        args.putLong(ConfirmarCandidaturaFragment.ARG_VAGA_ID, vagaId);

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_confirmar_candidatura, args);
    }

    private void openDetalhes(long vagaId, String titulo, String empresa, String inicial,
                               String local, String tipo, String salario, int match) {
        Bundle args = new Bundle();
        args.putString(DetalhesVagaFragment.ARG_TITULO, titulo);
        args.putString(DetalhesVagaFragment.ARG_EMPRESA, empresa);
        args.putString(DetalhesVagaFragment.ARG_INICIAL, inicial);
        args.putString(DetalhesVagaFragment.ARG_LOCAL, local);
        args.putString(DetalhesVagaFragment.ARG_TIPO, tipo);
        args.putString(DetalhesVagaFragment.ARG_SALARIO, salario);
        args.putInt(DetalhesVagaFragment.ARG_MATCH, match);
        args.putLong(DetalhesVagaFragment.ARG_VAGA_ID, vagaId);

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                .navigate(R.id.action_dashboard_to_detalhes, args);
    }
}
