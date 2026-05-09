package com.edu.matchvagasapp.features.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.edu.matchvagasapp.R;

public class PerfilFragment extends Fragment {

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

        view.findViewById(R.id.btn_completar_agora).setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment())
                        .navigate(R.id.action_dashboard_to_cadastro_curriculo));
    }
}
