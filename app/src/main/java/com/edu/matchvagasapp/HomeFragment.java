package com.edu.matchvagasapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

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

        view.findViewById(R.id.card_vaga_android).setOnClickListener(v ->
                openDetalhes("Desenvolvedor Android", "TechCorp Brasil", "T",
                        "São Paulo, SP", "CLT", "R$ 6.000 – R$ 9.000", 92));

        view.findViewById(R.id.card_vaga_backend).setOnClickListener(v ->
                openDetalhes("Engenheiro Backend", "Startup XYZ", "S",
                        "Remoto", "PJ", "R$ 8.000 – R$ 12.000", 87));

        view.findViewById(R.id.card_vaga_fullstack).setOnClickListener(v ->
                openDetalhes("Desenvolvedor Full Stack", "Banco Digital", "B",
                        "Rio de Janeiro, RJ", "CLT", "R$ 7.000 – R$ 10.000", 79));
    }

    private void openDetalhes(String titulo, String empresa, String inicial,
                               String local, String tipo, String salario, int match) {
        Bundle args = new Bundle();
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
}