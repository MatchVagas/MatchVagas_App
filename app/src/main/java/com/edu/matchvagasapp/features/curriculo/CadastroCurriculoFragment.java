package com.edu.matchvagasapp.features.curriculo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.edu.matchvagasapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class CadastroCurriculoFragment extends Fragment {

    private static final long TAMANHO_MAXIMO_BYTES = 5L * 1024 * 1024; // 5 MB

    private static final String[] MIME_ACEITOS = {
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    };

    private View layoutSemArquivo;
    private View cardComArquivo;
    private TextView tvNomeArquivo, tvTamanhoArquivo, tvExtensaoBadge;
    private View btnTrocarArquivo, btnRemoverArquivo;
    private MaterialButton btnSalvar;

    private Uri arquivoUri = null;

    private final ActivityResultLauncher<String[]> selecionarArquivo =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) processarArquivoSelecionado(uri);
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_curriculo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyWindowInsets(view);
        initViews(view);
        setupCliques(view);
    }

    // ─────────── Setup ───────────

    private void applyWindowInsets(View rootView) {
        View header = rootView.findViewById(R.id.header_layout);
        View footer = rootView.findViewById(R.id.footer_buttons);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            header.setPadding(header.getPaddingLeft(), bars.top + 8,
                    header.getPaddingRight(), header.getPaddingBottom());
            footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(),
                    footer.getPaddingRight(), bars.bottom + 16);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void initViews(View view) {
        layoutSemArquivo = view.findViewById(R.id.layout_sem_arquivo);
        cardComArquivo = view.findViewById(R.id.card_com_arquivo);
        tvNomeArquivo = view.findViewById(R.id.tv_nome_arquivo);
        tvTamanhoArquivo = view.findViewById(R.id.tv_tamanho_arquivo);
        tvExtensaoBadge = view.findViewById(R.id.tv_extensao_badge);
        btnTrocarArquivo = view.findViewById(R.id.btn_trocar_arquivo);
        btnRemoverArquivo = view.findViewById(R.id.btn_remover_arquivo);
        btnSalvar = view.findViewById(R.id.btn_salvar_curriculo);
    }

    private void setupCliques(View view) {
        view.findViewById(R.id.btn_fechar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        layoutSemArquivo.setOnClickListener(v -> abrirSeletor());
        btnTrocarArquivo.setOnClickListener(v -> abrirSeletor());
        btnRemoverArquivo.setOnClickListener(v -> removerArquivo());
        btnSalvar.setOnClickListener(v -> salvarCurriculo(view));
    }

    // ─────────── Seleção de arquivo ───────────

    private void abrirSeletor() {
        selecionarArquivo.launch(MIME_ACEITOS);
    }

    private void processarArquivoSelecionado(Uri uri) {
        InfoArquivo info = obterInfoArquivo(uri);
        if (info == null) {
            mostrarErro(getString(R.string.erro_arquivo_leitura));
            return;
        }

        if (!tipoValido(info.mimeType)) {
            mostrarErro(getString(R.string.erro_tipo_invalido));
            return;
        }

        if (info.tamanho > TAMANHO_MAXIMO_BYTES) {
            mostrarErro(getString(R.string.erro_arquivo_grande));
            return;
        }

        arquivoUri = uri;
        exibirArquivoSelecionado(info.nome, info.tamanho, info.mimeType);
    }

    private InfoArquivo obterInfoArquivo(Uri uri) {
        try (Cursor cursor = requireContext().getContentResolver()
                .query(uri, null, null, null, null)) {
            if (cursor == null || !cursor.moveToFirst()) return null;

            int nomeIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int tamanhoIdx = cursor.getColumnIndex(OpenableColumns.SIZE);

            String nome = nomeIdx >= 0 ? cursor.getString(nomeIdx) : "curriculo";
            long tamanho = tamanhoIdx >= 0 ? cursor.getLong(tamanhoIdx) : 0;
            String mimeType = requireContext().getContentResolver().getType(uri);

            return new InfoArquivo(nome, tamanho, mimeType != null ? mimeType : "");
        } catch (Exception e) {
            return null;
        }
    }

    private boolean tipoValido(String mimeType) {
        for (String aceito : MIME_ACEITOS) {
            if (aceito.equals(mimeType)) return true;
        }
        return false;
    }

    // ─────────── UI de arquivo ───────────

    private void exibirArquivoSelecionado(String nome, long tamanho, String mimeType) {
        tvNomeArquivo.setText(nome);
        tvTamanhoArquivo.setText(formatarTamanho(tamanho));
        tvExtensaoBadge.setText(extrairExtensao(nome, mimeType));

        layoutSemArquivo.setVisibility(View.GONE);
        cardComArquivo.setVisibility(View.VISIBLE);

        btnSalvar.setEnabled(true);
        btnSalvar.setAlpha(1f);
    }

    private void removerArquivo() {
        arquivoUri = null;

        layoutSemArquivo.setVisibility(View.VISIBLE);
        cardComArquivo.setVisibility(View.GONE);

        btnSalvar.setEnabled(false);
        btnSalvar.setAlpha(0.5f);
    }

    // ─────────── Salvar ───────────

    private void salvarCurriculo(View rootView) {
        if (arquivoUri == null) {
            mostrarErro(getString(R.string.erro_selecione_arquivo));
            return;
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_curriculo_titulo))
                .setMessage(getString(R.string.dialog_curriculo_msg))
                .setPositiveButton(getString(R.string.dialog_ok), (d, w) ->
                        NavHostFragment.findNavController(this).navigateUp())
                .setCancelable(false)
                .show();
    }

    // ─────────── Helpers ───────────

    private void mostrarErro(String mensagem) {
        View view = getView();
        if (view != null) Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG).show();
    }

    private String formatarTamanho(long bytes) {
        if (bytes <= 0) return "–";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024));
    }

    private String extrairExtensao(String nome, String mimeType) {
        if (nome != null && nome.contains(".")) {
            return nome.substring(nome.lastIndexOf('.') + 1).toUpperCase();
        }
        if (mimeType.contains("pdf")) return "PDF";
        if (mimeType.contains("wordprocessingml")) return "DOCX";
        if (mimeType.contains("msword")) return "DOC";
        return "DOC";
    }

    // ─────────── DTO interno ───────────

    private static class InfoArquivo {
        final String nome;
        final long tamanho;
        final String mimeType;

        InfoArquivo(String nome, long tamanho, String mimeType) {
            this.nome = nome;
            this.tamanho = tamanho;
            this.mimeType = mimeType;
        }
    }
}
