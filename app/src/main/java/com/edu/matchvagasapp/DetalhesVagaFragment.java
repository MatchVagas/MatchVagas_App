package com.edu.matchvagasapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class DetalhesVagaFragment extends Fragment {

    public static final String ARG_TITULO = "vagaTitulo";
    public static final String ARG_EMPRESA = "vagaEmpresa";
    public static final String ARG_INICIAL = "vagaInicial";
    public static final String ARG_LOCAL = "vagaLocal";
    public static final String ARG_TIPO = "vagaTipo";
    public static final String ARG_SALARIO = "vagaSalario";
    public static final String ARG_MATCH = "vagaMatch";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detalhes_vagas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        toolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                        .popBackStack());

        Bundle args = getArguments();
        String titulo = "", empresa = "", inicial = "?", local = "", tipo = "", salario = "";
        int match = 0;

        if (args != null) {
            titulo  = args.getString(ARG_TITULO, "");
            empresa = args.getString(ARG_EMPRESA, "");
            inicial = args.getString(ARG_INICIAL, "?");
            local   = args.getString(ARG_LOCAL, "");
            tipo    = args.getString(ARG_TIPO, "");
            salario = args.getString(ARG_SALARIO, "");
            match   = args.getInt(ARG_MATCH, 0);

            setText(view, R.id.tvCompanyInitial, inicial);
            setText(view, R.id.tvJobTitle, titulo);
            setText(view, R.id.tvCompanyName, empresa);
            setText(view, R.id.tvMatchBadge, match + "% Match");
            setText(view, R.id.tvLocation, local);
            setText(view, R.id.tvContractType, tipo);
            setText(view, R.id.tvSalary, salario);
        }

        setText(view, R.id.tvDescription,
                "Estamos buscando um profissional apaixonado por tecnologia para integrar nossa " +
                "equipe de desenvolvimento. Você será responsável pelo desenvolvimento e manutenção " +
                "de funcionalidades, colaborando com times multidisciplinares para entregar produtos " +
                "de alto impacto e qualidade.\n\nO ambiente de trabalho é colaborativo, inovador e " +
                "com foco em crescimento profissional contínuo.");

        setText(view, R.id.tvRequirements,
                "• Graduação em Ciência da Computação, Sistemas de Informação ou áreas relacionadas\n" +
                "• Experiência mínima de 2 anos na área\n" +
                "• Conhecimento sólido em estruturas de dados e algoritmos\n" +
                "• Habilidade para trabalhar em equipe e comunicação clara\n" +
                "• Inglês técnico para leitura de documentação");

        setText(view, R.id.tvBenefits,
                "• Plano de saúde e odontológico\n" +
                "• Vale-refeição e vale-alimentação\n" +
                "• Auxílio home office\n" +
                "• Day off no aniversário\n" +
                "• Stock options\n" +
                "• Cursos e certificações pagos pela empresa");

        final String fTitulo = titulo, fEmpresa = empresa, fInicial = inicial;
        final int fMatch = match;

        MaterialButton btnApply = view.findViewById(R.id.btnApply);
        MaterialButton btnSave = view.findViewById(R.id.btnSave);
        MaterialButton btnShare = view.findViewById(R.id.btnShare);

        btnApply.setOnClickListener(v -> {
            Bundle confirmarArgs = new Bundle();
            confirmarArgs.putString(ConfirmarCandidaturaFragment.ARG_TITULO, fTitulo);
            confirmarArgs.putString(ConfirmarCandidaturaFragment.ARG_EMPRESA, fEmpresa);
            confirmarArgs.putString(ConfirmarCandidaturaFragment.ARG_INICIAL, fInicial);
            confirmarArgs.putInt(ConfirmarCandidaturaFragment.ARG_MATCH, fMatch);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                    .navigate(R.id.action_detalhes_to_confirmar, confirmarArgs);
        });

        btnSave.setOnClickListener(v ->
                Toast.makeText(requireContext(), getString(R.string.vaga_salva), Toast.LENGTH_SHORT).show());

        btnShare.setOnClickListener(v ->
                Toast.makeText(requireContext(), getString(R.string.compartilhando), Toast.LENGTH_SHORT).show());
    }

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }
}