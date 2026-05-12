package com.edu.matchvagasapp.features.vagas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.edu.matchvagasapp.R;
import com.google.android.material.tabs.TabLayout;

public class VagasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vagas, container, false);

        View scrollCandidaturas = view.findViewById(R.id.scroll_candidaturas);
        View scrollSalvas = view.findViewById(R.id.scroll_salvas);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

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

        view.findViewById(R.id.btn_detalhes_1).setOnClickListener(v ->
                navigateToDetalhes(-1, "Desenvolvedor Android", "TechCorp Brasil", "T",
                        "São Paulo, SP", "CLT", "R$ 6.000 – R$ 9.000", 92));

        view.findViewById(R.id.btn_detalhes_2).setOnClickListener(v ->
                navigateToDetalhes(-1, "Engenheiro Backend", "Startup XYZ", "S",
                        "Remoto", "CLT", "R$ 8.000 – R$ 12.000", 87));

        view.findViewById(R.id.btn_detalhes_3).setOnClickListener(v ->
                navigateToDetalhes(-1, "UX Designer", "Design Studio", "D",
                        "São Paulo, SP", "PJ", "R$ 7.000 – R$ 10.000", 79));

        return view;
    }

    private void navigateToDetalhes(long vagaId, String titulo, String empresa, String inicial,
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