package com.edu.matchvagasapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashbordCadActivity extends AppCompatActivity {

    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private BuscarFragment buscarFragment;
    private VagasFragment vagasFragment;
    private PerfilFragment perfilFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard_can);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setPadding(0, 0, 0, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        if (savedInstanceState == null) {
            setupFragments();
        }
        setupNavigation();
    }

    private void setupFragments() {
        homeFragment = new HomeFragment();
        buscarFragment = new BuscarFragment();
        vagasFragment = new VagasFragment();
        perfilFragment = new PerfilFragment();
        activeFragment = homeFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_host_fragment, perfilFragment, "perfil")
                .add(R.id.nav_host_fragment, vagasFragment, "vagas")
                .add(R.id.nav_host_fragment, buscarFragment, "buscar")
                .add(R.id.nav_host_fragment, homeFragment, "home")
                .hide(perfilFragment)
                .hide(vagasFragment)
                .hide(buscarFragment)
                .commit();
    }

    private void setupNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            if (homeFragment == null) return false;

            Fragment selected;
            int id = item.getItemId();
            if (id == R.id.item_1) selected = homeFragment;
            else if (id == R.id.item_2) selected = buscarFragment;
            else if (id == R.id.item_3) selected = vagasFragment;
            else selected = perfilFragment;

            if (selected == activeFragment) return true;

            getSupportFragmentManager().beginTransaction()
                    .hide(activeFragment)
                    .show(selected)
                    .commit();
            activeFragment = selected;
            return true;
        });
    }
}