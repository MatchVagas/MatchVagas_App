package com.edu.matchvagasapp.features.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.edu.matchvagasapp.features.home.HomeFragment;
import com.edu.matchvagasapp.features.perfil.PerfilFragment;
import com.edu.matchvagasapp.R;
import com.edu.matchvagasapp.features.buscar.BuscarFragment;
import com.edu.matchvagasapp.features.vagas.VagasFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardFragment extends Fragment {

    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private BuscarFragment buscarFragment;
    private VagasFragment vagasFragment;
    private PerfilFragment perfilFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_can, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
            bottomNav.setPadding(0, 0, 0, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        if (savedInstanceState == null) {
            setupFragments();
        } else {
            homeFragment = (HomeFragment) getChildFragmentManager().findFragmentByTag("home");
            buscarFragment = (BuscarFragment) getChildFragmentManager().findFragmentByTag("buscar");
            vagasFragment = (VagasFragment) getChildFragmentManager().findFragmentByTag("vagas");
            perfilFragment = (PerfilFragment) getChildFragmentManager().findFragmentByTag("perfil");
            activeFragment = homeFragment;
        }

        setupNavigation(view);
    }

    private void setupFragments() {
        homeFragment = new HomeFragment();
        buscarFragment = new BuscarFragment();
        vagasFragment = new VagasFragment();
        perfilFragment = new PerfilFragment();
        activeFragment = homeFragment;

        getChildFragmentManager().beginTransaction()
                .add(R.id.nav_host_fragment, perfilFragment, "perfil")
                .add(R.id.nav_host_fragment, vagasFragment, "vagas")
                .add(R.id.nav_host_fragment, buscarFragment, "buscar")
                .add(R.id.nav_host_fragment, homeFragment, "home")
                .hide(perfilFragment)
                .hide(vagasFragment)
                .hide(buscarFragment)
                .commit();
    }

    private void setupNavigation(View view) {
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            if (homeFragment == null) return false;

            Fragment selected;
            int id = item.getItemId();
            if (id == R.id.item_1) selected = homeFragment;
            else if (id == R.id.item_2) selected = buscarFragment;
            else if (id == R.id.item_3) selected = vagasFragment;
            else selected = perfilFragment;

            if (selected == activeFragment) return true;

            getChildFragmentManager().beginTransaction()
                    .hide(activeFragment)
                    .show(selected)
                    .commit();
            activeFragment = selected;
            return true;
        });
    }
}