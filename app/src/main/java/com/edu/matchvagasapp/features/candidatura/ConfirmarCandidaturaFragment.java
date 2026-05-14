package com.edu.matchvagasapp.features.candidatura;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.model.CandidaturaRequest;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.repository.CandidaturaRepository;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;

public class ConfirmarCandidaturaFragment extends Fragment {

    public static final String ARG_TITULO  = "vagaTitulo";
    public static final String ARG_EMPRESA = "vagaEmpresa";
    public static final String ARG_INICIAL = "vagaInicial";
    public static final String ARG_MATCH   = "vagaMatch";
    public static final String ARG_VAGA_ID = "vagaId";

    private final CandidaturaRepository candidaturaRepository = new CandidaturaRepository();
    private MaterialButton btnConfirmar;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirmar_candidatura, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, bars.top, 0, 0);
            return insets;
        });

        NestedScrollView scrollView = view.findViewById(R.id.scrollView);
        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, bars.bottom);
            return insets;
        });

        toolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                        .popBackStack());

        Bundle args = getArguments();
        if (args != null) {
            setText(view, R.id.tvCompanyInitial, args.getString(ARG_INICIAL, "?"));
            setText(view, R.id.tvJobTitle, args.getString(ARG_TITULO, ""));
            setText(view, R.id.tvCompanyName, args.getString(ARG_EMPRESA, ""));
            int match = args.getInt(ARG_MATCH, 0);
            setText(view, R.id.tvMatchBadge, match + "% Match");
        }

        btnConfirmar = view.findViewById(R.id.btnConfirmar);
        MaterialCheckBox checkboxTermos = view.findViewById(R.id.checkboxTermos);

        btnConfirmar.setOnClickListener(v -> {
            if (!checkboxTermos.isChecked()) {
                checkboxTermos.setError(getString(R.string.erro_termos));
                return;
            }
            checkboxTermos.setError(null);
            enviarCandidatura(view);
        });

        checkboxTermos.setOnCheckedChangeListener((cb, checked) -> {
            if (checked) cb.setError(null);
        });
    }

    private void enviarCandidatura(View view) {
        Bundle args = getArguments();
        long vagaId = args != null ? args.getLong(ARG_VAGA_ID, -1) : -1;

        if (vagaId == -1) {
            Snackbar.make(view, "Não foi possível identificar a vaga. Tente novamente.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Lê apenas os campos suportados pelo backend CandidaturaRequestDTO
        boolean compartilharTelefone    = ((MaterialSwitch) view.findViewById(R.id.switchTelefone)).isChecked();
        boolean compartilharCurriculo   = ((MaterialSwitch) view.findViewById(R.id.switchCurriculo)).isChecked();
        boolean compartilharFormacao    = ((MaterialSwitch) view.findViewById(R.id.switchFormacao)).isChecked();
        boolean compartilharExperiencia = ((MaterialSwitch) view.findViewById(R.id.switchExperiencia)).isChecked();

        CandidaturaRequest request = new CandidaturaRequest(
                vagaId,
                true,   // compartilharObjetivoProfissional
                true,   // compartilharDisponibilidade
                true,   // compartilharPretensaoSalarial
                compartilharCurriculo,
                compartilharExperiencia,
                compartilharFormacao,
                compartilharTelefone,
                false   // compartilharEndereco (padrão privado)
        );

        setEnvioLoading(true);

        candidaturaRepository.candidatar(request, new CandidaturaRepository.CandidaturaCallback() {
            @Override
            public void onSuccess(CandidaturaResponse response) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setEnvioLoading(false);
                    mostrarDialogSucesso();
                });
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setEnvioLoading(false);
                    Snackbar.make(rootView, mensagem, Snackbar.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarDialogSucesso() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_sucesso_title))
                .setMessage(getString(R.string.dialog_sucesso_msg))
                .setPositiveButton(getString(R.string.dialog_ok), (dialog, which) ->
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                                .popBackStack(R.id.dashboardFragment, false))
                .setCancelable(false)
                .show();
    }

    private void setEnvioLoading(boolean loading) {
        if (btnConfirmar != null) {
            btnConfirmar.setEnabled(!loading);
            btnConfirmar.setText(loading ? "Enviando..." : "Confirmar candidatura");
        }
    }

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }
}
