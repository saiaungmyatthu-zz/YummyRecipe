package com.mm.saiaumain.yummyrecipe;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        doRecipeList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            doRecipeList();
        } else if (id == R.id.nav_cookbook) {
            doRecipeBook();
        } else if (id == R.id.nav_newrecipe) {
            doNewRecipe();
        } else if (id == R.id.nav_buylist) {
            doRecipeList();
        } else if (id == R.id.nav_settings) {
            doRecipeList();
        } else if(id == R.id.nav_facebook) {
            CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            Snackbar snackbar = Snackbar.make(layout, "Posted to Yummy Recipe facebook page.", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else if (id == R.id.nav_twitter) {
            CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            Snackbar snackbar = Snackbar.make(layout, "Tweeted to Yummy Recipe.", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        setTitle((id == R.id.nav_home || id == R.id.nav_facebook || id == R.id.nav_twitter)? getString(R.string.app_name) : item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doNewRecipe(){
        Intent newRecipeIntent = new Intent(this, NewRecipeActivity.class);
        this.startActivity(newRecipeIntent);
    }

    private void doRecipeList(){
        fragment = new HomeScreen();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    private void doRecipeBook(){
        fragment = new RecipeListScreen();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }
}
