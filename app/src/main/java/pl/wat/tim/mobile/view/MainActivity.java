package pl.wat.tim.mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.ActivityMainBinding;
import pl.wat.tim.mobile.databinding.NavHeaderMainBinding;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.viewmodel.FinancesViewModel;
import pl.wat.tim.mobile.viewmodel.factory.FinancesViewModelFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentProvider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = (User) this.getIntent().getExtras().getSerializable("USER_OBJ");
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        FinancesViewModel viewModel = ViewModelProviders.of(this, new FinancesViewModelFactory(this, user, this)).get(FinancesViewModel.class);
        binding.setFinancesViewModel(viewModel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUsernameInHeader(binding, user.getUsername());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FinanceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_finances);
        }
    }

    private void setUsernameInHeader(ActivityMainBinding binding, String username) {

        NavHeaderMainBinding bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, binding
                .navView, false);
        binding.navView.addHeaderView(bind.getRoot());
        bind.setUsername(username);
    }

    @Override
    public <T extends Fragment> void showFragment(T fragment, int containerViewId) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_finances) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FinanceFragment()).commit();
        } else if (id == R.id.nav_report) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).commit();
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
