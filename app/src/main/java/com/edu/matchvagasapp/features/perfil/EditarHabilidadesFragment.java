package com.edu.matchvagasapp.features.perfil;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.data.model.HabilidadesRequest;
import com.edu.matchvagasapp.data.model.HabilidadesResponse;
import com.edu.matchvagasapp.data.repository.PerfilRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EditarHabilidadesFragment extends Fragment {

    private static final int LIMITE_HABILIDADES = 20;

    private static final List<String> SUGESTOES_TECNICAS = Arrays.asList(
            "Java", "Kotlin", "Android", "Python", "JavaScript",
            "TypeScript", "SQL", "Git", "REST APIs", "Flutter",
            "React Native", "Docker", "Firebase", "AWS", "SOLID"
    );

    private static final List<String> SUGESTOES_COMPORTAMENTAIS = Arrays.asList(
            "Comunicação", "Trabalho em equipe", "Proatividade",
            "Resolução de problemas", "Liderança", "Adaptabilidade",
            "Organização", "Pensamento crítico"
    );

    private TextInputLayout tilHabilidade;
    private TextInputEditText etHabilidade;
    private ChipGroup chipGroupHabilidades;
    private ChipGroup chipGroupSugestoesTecnicas;
    private ChipGroup chipGroupSugestoesComportamentais;
    private View layoutVazio;
    private MaterialButton btnSalvar;

    /** Nomes normalizados (lowercase) das habilidades atualmente exibidas em chips. */
    private final Set<String> habilidadesAdicionadas = new HashSet<>();
    private final PerfilRepository perfilRepository = new PerfilRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_habilidades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyWindowInsets(view);
        initViews(view);
        setupCampoAdicionar(view);
        setupSugestoes();
        setupButtons(view);
        carregarHabilidades();
    }

    private void applyWindowInsets(View view) {
        View header = view.findViewById(R.id.header_habilidades);
        if (header != null) {
            ViewCompat.setOnApplyWindowInsetsListener(header, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), bars.top, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }
        View footer = view.findViewById(R.id.footer_salvar);
        if (footer != null) {
            ViewCompat.setOnApplyWindowInsetsListener(footer, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bars.bottom);
                return insets;
            });
        }
    }

    private void initViews(View view) {
        tilHabilidade = view.findViewById(R.id.til_habilidade);
        etHabilidade = view.findViewById(R.id.et_habilidade);
        chipGroupHabilidades = view.findViewById(R.id.chipgroup_habilidades);
        chipGroupSugestoesTecnicas = view.findViewById(R.id.chipgroup_sugestoes_tecnicas);
        chipGroupSugestoesComportamentais = view.findViewById(R.id.chipgroup_sugestoes_comportamentais);
        layoutVazio = view.findViewById(R.id.layout_vazio);
    }

    // ── Carregar habilidades do backend ───────────────────────────────────────

    private void carregarHabilidades() {
        setCarregando(true);
        perfilRepository.buscarHabilidades(new PerfilRepository.HabilidadesCallback() {
            @Override
            public void onSucesso(List<HabilidadesResponse> lista) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    chipGroupHabilidades.removeAllViews();
                    habilidadesAdicionadas.clear();
                    for (HabilidadesResponse item : lista) {
                        if (item.getNome() != null) {
                            adicionarChipLocal(item.getNome());
                            sincronizarSugestoes(item.getNome(), true);
                        }
                    }
                    atualizarContadorEVazio();
                    setCarregando(false);
                });
            }

            @Override
            public void onVazio() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    chipGroupHabilidades.removeAllViews();
                    habilidadesAdicionadas.clear();
                    atualizarContadorEVazio();
                    setCarregando(false);
                });
            }
        });
    }

    // ── Campo de input e botão adicionar ─────────────────────────────────────

    private void setupCampoAdicionar(View rootView) {
        MaterialButton btnAdicionar = rootView.findViewById(R.id.btn_adicionar);
        btnAdicionar.setOnClickListener(v -> adicionarDoInput(rootView));

        etHabilidade.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                adicionarDoInput(rootView);
                return true;
            }
            return false;
        });
    }

    private void adicionarDoInput(View rootView) {
        String texto = etHabilidade.getText() != null
                ? etHabilidade.getText().toString().trim() : "";

        tilHabilidade.setError(null);

        if (texto.isEmpty()) {
            tilHabilidade.setError(getString(R.string.erro_habilidade_vazia));
            return;
        }

        if (habilidadesAdicionadas.size() >= LIMITE_HABILIDADES) {
            tilHabilidade.setError(getString(R.string.erro_habilidade_limite));
            return;
        }

        String normalizado = capitalizar(texto);
        String chave = normalizado.toLowerCase(Locale.getDefault());

        if (habilidadesAdicionadas.contains(chave)) {
            tilHabilidade.setError(getString(R.string.erro_habilidade_duplicada));
            return;
        }

        etHabilidade.setText("");
        esconderTeclado(rootView);

        // Optimistic: adiciona chip localmente antes da resposta
        adicionarChipLocal(normalizado);
        sincronizarSugestoes(normalizado, true);
        atualizarContadorEVazio();

        // Persiste no backend
        perfilRepository.adicionarHabilidade(
                new HabilidadesRequest(normalizado, null),
                new PerfilRepository.PerfilCallback() {
                    @Override
                    public void onSuccess() {
                        // já refletido localmente — nada a fazer
                    }

                    @Override
                    public void onError(String mensagem) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
                            // Desfaz optimistic update
                            removerChipLocal(normalizado);
                            sincronizarSugestoes(normalizado, false);
                            atualizarContadorEVazio();
                            if (getView() != null) {
                                Snackbar.make(requireView(), mensagem, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

    // ── Gestão local de chips ─────────────────────────────────────────────────

    private void adicionarChipLocal(String nome) {
        String chave = nome.toLowerCase(Locale.getDefault());
        if (habilidadesAdicionadas.contains(chave)) return;

        habilidadesAdicionadas.add(chave);

        Chip chip = criarChipSelecionada(nome);
        chip.setOnCloseIconClickListener(v -> removerHabilidadeComAPI(chip, nome));
        chipGroupHabilidades.addView(chip);
    }

    private void removerChipLocal(String nome) {
        String chave = nome.toLowerCase(Locale.getDefault());
        habilidadesAdicionadas.remove(chave);

        for (int i = 0; i < chipGroupHabilidades.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupHabilidades.getChildAt(i);
            if (chip.getText().toString().toLowerCase(Locale.getDefault()).equals(chave)) {
                chipGroupHabilidades.removeView(chip);
                break;
            }
        }
    }

    private void removerHabilidadeComAPI(Chip chip, String nome) {
        // Optimistic: remove chip imediatamente
        String chave = nome.toLowerCase(Locale.getDefault());
        chipGroupHabilidades.removeView(chip);
        habilidadesAdicionadas.remove(chave);
        sincronizarSugestoes(nome, false);
        atualizarContadorEVazio();

        perfilRepository.removerHabilidade(nome, new PerfilRepository.PerfilCallback() {
            @Override
            public void onSuccess() {
                // já refletido localmente — nada a fazer
            }

            @Override
            public void onError(String mensagem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    // Desfaz: recarrega lista do servidor
                    carregarHabilidades();
                    if (getView() != null) {
                        Snackbar.make(requireView(), mensagem, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // ── Criação de chips ──────────────────────────────────────────────────────

    private Chip criarChipSelecionada(String nome) {
        Chip chip = new Chip(requireContext());
        chip.setText(nome);
        chip.setCloseIconVisible(true);
        chip.setChipBackgroundColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.primary_light)));
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
        chip.setCloseIconTint(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.primary)));
        chip.setChipStrokeColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.primary)));
        chip.setChipStrokeWidth(dpParaPx(1f));
        chip.setCheckedIconVisible(false);
        return chip;
    }

    // ── Sugestões ─────────────────────────────────────────────────────────────

    private void setupSugestoes() {
        for (String sugestao : SUGESTOES_TECNICAS) {
            chipGroupSugestoesTecnicas.addView(criarChipSugestao(sugestao));
        }
        for (String sugestao : SUGESTOES_COMPORTAMENTAIS) {
            chipGroupSugestoesComportamentais.addView(criarChipSugestao(sugestao));
        }
    }

    private Chip criarChipSugestao(String nome) {
        Chip chip = new Chip(requireContext());
        chip.setText(nome);
        chip.setChipBackgroundColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), android.R.color.transparent)));
        chip.setChipStrokeColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.divider)));
        chip.setChipStrokeWidth(dpParaPx(1.5f));
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
        chip.setCheckedIconVisible(false);
        chip.setCloseIconVisible(false);

        chip.setOnClickListener(v -> {
            if (!chip.isEnabled()) return;
            if (habilidadesAdicionadas.size() >= LIMITE_HABILIDADES) {
                Snackbar.make(requireView(),
                        getString(R.string.erro_habilidade_limite), Snackbar.LENGTH_SHORT).show();
                return;
            }
            String chave = nome.toLowerCase(Locale.getDefault());
            if (habilidadesAdicionadas.contains(chave)) return;

            // Optimistic
            adicionarChipLocal(nome);
            marcarSugestaoComoAdicionada(chip);
            atualizarContadorEVazio();

            perfilRepository.adicionarHabilidade(
                    new HabilidadesRequest(nome, null),
                    new PerfilRepository.PerfilCallback() {
                        @Override
                        public void onSuccess() { /* refletido localmente */ }

                        @Override
                        public void onError(String mensagem) {
                            if (!isAdded()) return;
                            requireActivity().runOnUiThread(() -> {
                                removerChipLocal(nome);
                                desmarcarSugestao(chip);
                                atualizarContadorEVazio();
                                if (getView() != null) {
                                    Snackbar.make(requireView(), mensagem,
                                            Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
        });
        return chip;
    }

    private void marcarSugestaoComoAdicionada(Chip chip) {
        chip.setEnabled(false);
        chip.setChipBackgroundColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.primary_light)));
        chip.setChipStrokeColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.primary)));
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
    }

    private void desmarcarSugestao(Chip chip) {
        chip.setEnabled(true);
        chip.setChipBackgroundColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), android.R.color.transparent)));
        chip.setChipStrokeColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.divider)));
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
    }

    private void sincronizarSugestoes(String nome, boolean adicionado) {
        String chave = nome.toLowerCase(Locale.getDefault());
        sincronizarEmGrupo(chipGroupSugestoesTecnicas, SUGESTOES_TECNICAS, chave, adicionado);
        sincronizarEmGrupo(chipGroupSugestoesComportamentais, SUGESTOES_COMPORTAMENTAIS, chave, adicionado);
    }

    private void sincronizarEmGrupo(ChipGroup group, List<String> lista,
                                    String chave, boolean adicionado) {
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.getText().toString().toLowerCase(Locale.getDefault()).equals(chave)) {
                if (adicionado) marcarSugestaoComoAdicionada(chip);
                else desmarcarSugestao(chip);
                break;
            }
        }
    }

    // ── Botão voltar (cada operação já persiste individualmente) ─────────────

    private void setupButtons(View view) {
        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        btnSalvar = view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());
    }

    // ── Contador e estado vazio ───────────────────────────────────────────────

    private void setCarregando(boolean carregando) {
        if (btnSalvar != null) btnSalvar.setEnabled(!carregando);
    }

    private void atualizarContadorEVazio() {
        int total = habilidadesAdicionadas.size();
        View root = getView();
        if (root == null) return;

        ((android.widget.TextView) root.findViewById(R.id.tv_contador))
                .setText(total + "/" + LIMITE_HABILIDADES);

        layoutVazio.setVisibility(total == 0 ? View.VISIBLE : View.GONE);
        chipGroupHabilidades.setVisibility(total == 0 ? View.GONE : View.VISIBLE);
    }

    // ── Utilitários ───────────────────────────────────────────────────────────

    private void esconderTeclado(View view) {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String capitalizar(String texto) {
        if (texto.isEmpty()) return texto;
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private float dpParaPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}
