package com.edu.matchvagasapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {

    private static final String TAG = "TokenManager";
    private static final String PREFS_NAME = "matchvagas_secure_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USUARIO_ID = "usuario_id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_PERFIL = "perfil";

    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = buildPrefs(context.getApplicationContext());
    }

    private SharedPreferences buildPrefs(Context appContext) {
        try {
            MasterKey masterKey = new MasterKey.Builder(appContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    appContext,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Fallback para SharedPreferences simples caso o Keystore falhe
            Log.e(TAG, "EncryptedSharedPreferences indisponível, usando fallback", e);
            return appContext.getSharedPreferences(PREFS_NAME + "_fallback", Context.MODE_PRIVATE);
        }
    }

    public void salvar(String token, Long usuarioId, String nome, String perfil) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putLong(KEY_USUARIO_ID, usuarioId)
                .putString(KEY_NOME, nome)
                .putString(KEY_PERFIL, perfil)
                .apply();
    }

    public String getToken() { return prefs.getString(KEY_TOKEN, null); }
    public long getUsuarioId() { return prefs.getLong(KEY_USUARIO_ID, -1); }
    public String getNome() { return prefs.getString(KEY_NOME, null); }
    public String getPerfil() { return prefs.getString(KEY_PERFIL, null); }
    public boolean isLogado() { return getToken() != null; }

    public void limpar() {
        prefs.edit().clear().apply();
    }
}
