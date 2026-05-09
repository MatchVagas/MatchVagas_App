package com.edu.matchvagasapp.features.empresa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.edu.matchvagasapp.R;

public class EmpresaFragment extends Fragment {

    private TextView txtNome;
    private TextView txtDescricao;
    private TextView txtTelefone;
    private TextView txtSite;

    public EmpresaFragment() {
        // Construtor vazio obrigatório
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_empresa, container, false);

        txtNome = view.findViewById(R.id.txtNomeEmpresa);
        txtDescricao = view.findViewById(R.id.txtDescricaoEmpresa);
        txtTelefone = view.findViewById(R.id.txtTelefoneEmpresa);
        txtSite = view.findViewById(R.id.txtSiteEmpresa);

        txtNome.setText("Tech Solutions");
        txtDescricao.setText("Empresa especializada em desenvolvimento de software.");
        txtTelefone.setText("(11) 99999-9999");
        txtSite.setText("www.techsolutions.com");

        return view;
    }
}