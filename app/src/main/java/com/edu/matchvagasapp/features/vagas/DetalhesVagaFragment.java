package com.edu.matchvagasapp.features.vagas;

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

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.local.VagasSalvasManager;
import com.edu.matchvagasapp.data.model.VagaResponse;
import com.edu.matchvagasapp.data.repository.VagaRepository;
import com.edu.matchvagasapp.features.candidatura.ConfirmarCandidaturaFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class DetalhesVagaFragment extends Fragment {

    public static final String ARG_TITULO   = "vagaTitulo";
    public static final String ARG_EMPRESA  = "vagaEmpresa";
    public static final String ARG_INICIAL  = "vagaInicial";
    public static final String ARG_LOCAL    = "vagaLocal";
    public static final String ARG_TIPO     = "vagaTipo";
    public static final String ARG_SALARIO  = "vagaSalario";
    public static final String ARG_MATCH    = "vagaMatch";
    public static final String ARG_VAGA_ID  = "vagaId";

    private final VagaRepository vagaRepository = new VagaRepository();

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
        long vagaId = -1;

        if (args != null) {
            titulo  = args.getString(ARG_TITULO, "");
            empresa = args.getString(ARG_EMPRESA, "");
            inicial = args.getString(ARG_INICIAL, "?");
            local   = args.getString(ARG_LOCAL, "");
            tipo    = args.getString(ARG_TIPO, "");
            salario = args.getString(ARG_SALARIO, "");
            match   = args.getInt(ARG_MATCH, 0);
            vagaId  = args.getLong(ARG_VAGA_ID, -1);
        }

        setText(view, R.id.tvCompanyInitial, inicial);
        setText(view, R.id.tvJobTitle, titulo);
        setText(view, R.id.tvCompanyName, empresa);
        setText(view, R.id.tvMatchBadge, match > 0 ? match + "% Match" : "");
        setText(view, R.id.tvLocation, local);
        setText(view, R.id.tvContractType, tipo);
        setText(view, R.id.tvSalary, salario);

        // Texto padrão enquanto carrega da API
        setText(view, R.id.tvDescription, getString(R.string.carregando));
        setText(view, R.id.tvRequirements, getString(R.string.carregando));
        setText(view, R.id.tvBenefits, getString(R.string.carregando));

        if (vagaId != -1) {
            carregarDetalhesVaga(view, vagaId);
        } else {
            // Sem vagaId — exibe texto genérico
            setText(view, R.id.tvDescription, "Detalhes da vaga não disponíveis.");
            setText(view, R.id.tvRequirements, "—");
            setText(view, R.id.tvBenefits, "—");
        }

        final String fTitulo = titulo, fEmpresa = empresa, fInicial = inicial;
        final int fMatch = match;
        final long fVagaId = vagaId;

        MaterialButton btnApply  = view.findViewById(R.id.btnApply);
        MaterialButton btnSave   = view.findViewById(R.id.btnSave);
        MaterialButton btnShare  = view.findViewById(R.id.btnShare);

        btnApply.setOnClickListener(v -> {
            Bundle confirmarArgs = new Bundle();
            confirmarArgs.putString(ConfirmarCandidaturaFragment.ARG_TITULO, fTitulo);
            confirmarArgs.putString(ConfirmarCandidaturaFragment.ARG_EMPRESA, fEmpresa);
            confirmarArgs.putString(ConfirmarCandidaturaFragment.ARG_INICIAL, fInicial);
            confirmarArgs.putInt(ConfirmarCandidaturaFragment.ARG_MATCH, fMatch);
            confirmarArgs.putLong(ConfirmarCandidaturaFragment.ARG_VAGA_ID, fVagaId);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)
                    .navigate(R.id.action_detalhes_to_confirmar, confirmarArgs);
        });

        // Botão Salvar — persiste localmente via VagasSalvasManager
        if (fVagaId != -1) {
            VagasSalvasManager salvasManager = new VagasSalvasManager(requireContext());
            atualizarBotaoSalvar(btnSave, salvasManager.estaSalva(fVagaId));
            btnSave.setOnClickListener(v -> {
                boolean salvarAgora = !salvasManager.estaSalva(fVagaId);
                if (salvarAgora) salvasManager.salvar(fVagaId);
                else salvasManager.remover(fVagaId);
                atualizarBotaoSalvar(btnSave, salvarAgora);
                String msg = salvarAgora ? "Vaga salva!" : "Vaga removida das salvas";
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            });
        } else {
            btnSave.setEnabled(false);
        }

        btnShare.setOnClickListener(v ->
                Toast.makeText(requireContext(), getString(R.string.compartilhando), Toast.LENGTH_SHORT).show());
    }

    private void carregarDetalhesVaga(View view, long vagaId) {
        vagaRepository.buscarVagaPorId(vagaId, new VagaRepository.VagaCallback() {
            @Override
            public void onSuccess(VagaResponse vaga) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> preencherComVaga(view, vaga));
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setText(view, R.id.tvDescription, "Não foi possível carregar os detalhes.");
                    setText(view, R.id.tvRequirements, "—");
                    setText(view, R.id.tvBenefits, "—");
                });
            }
        });
    }

    private void preencherComVaga(View view, VagaResponse vaga) {
        // Atualiza campos que podem vir mais completos da API
        if (vaga.getNomeFantasiaEmpresa() != null && !vaga.getNomeFantasiaEmpresa().isEmpty()) {
            setText(view, R.id.tvCompanyInitial, vaga.getInicialEmpresa());
            setText(view, R.id.tvCompanyName, vaga.getNomeFantasiaEmpresa());
        }
        if (vaga.getTitulo() != null) setText(view, R.id.tvJobTitle, vaga.getTitulo());

        String local = vaga.getLocalFormatado();
        if (!local.isEmpty()) setText(view, R.id.tvLocation, local);

        String tipo = vaga.getTipoVagaDescricao();
        if (tipo != null) setText(view, R.id.tvContractType, tipo);

        String salario = vaga.getSalarioFormatado();
        if (!"A negociar".equals(salario) || true) setText(view, R.id.tvSalary, salario);

        // Descrição, requisitos e benefícios
        String desc = vaga.getDescricao();
        setText(view, R.id.tvDescription,
                desc != null && !desc.isEmpty() ? desc : "Descrição não informada.");

        String req = vaga.getRequisitos();
        setText(view, R.id.tvRequirements,
                req != null && !req.isEmpty() ? req : "Requisitos não informados.");

        String ben = vaga.getBeneficios();
        setText(view, R.id.tvBenefits,
                ben != null && !ben.isEmpty() ? ben : "Benefícios não informados.");
    }

    private void atualizarBotaoSalvar(MaterialButton btn, boolean salva) {
        btn.setText(salva ? "Salvo ✓" : "Salvar");
        btn.setAlpha(salva ? 0.7f : 1f);
    }

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }
}
