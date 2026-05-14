package com.edu.matchvagasapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VagasSalvasManager {

    private static final String PREFS = "vagas_salvas";
    private static final String KEY_IDS = "vaga_ids";

    private final SharedPreferences prefs;

    public VagasSalvasManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void salvar(long vagaId) {
        Set<String> ids = new HashSet<>(getIdsSet());
        ids.add(String.valueOf(vagaId));
        prefs.edit().putStringSet(KEY_IDS, ids).apply();
    }

    public void remover(long vagaId) {
        Set<String> ids = new HashSet<>(getIdsSet());
        ids.remove(String.valueOf(vagaId));
        prefs.edit().putStringSet(KEY_IDS, ids).apply();
    }

    public boolean estaSalva(long vagaId) {
        return getIdsSet().contains(String.valueOf(vagaId));
    }

    public List<Long> getVagasSalvas() {
        List<Long> result = new ArrayList<>();
        for (String id : getIdsSet()) {
            try { result.add(Long.parseLong(id)); }
            catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private Set<String> getIdsSet() {
        Set<String> stored = prefs.getStringSet(KEY_IDS, null);
        return stored != null ? stored : new HashSet<>();
    }
}
