package com.hanaa.confessions;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;/*
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FrameLayout fragment_container;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String Email = currentUser.getEmail();
        Toolbar toolbar = findViewById( R.id.toolBBar );
        setSupportActionBar( toolbar );


        drawer = findViewById( R.id.drawer_layout );
        NavigationView navigationView = findViewById( R.id.nav );
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        View view = navigationView.getHeaderView( 0 );
        TextView userEmail = view.findViewById( R.id.email );
        userEmail.setText( Email );
        ImageView imageView = view.findViewById( R.id.img );
        imageView.setImageResource( R.drawable.coco );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( MainActivity.this, drawer, toolbar ,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close
                );
        toggle.syncState();

        BottomNavigationView bottomNav = findViewById( R.id.nav_view );
        bottomNav.setOnNavigationItemSelectedListener( navListener );
        fragment_container = findViewById( R.id.fragment_container );



        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, selectedFragment).commit();
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen( GravityCompat.START )){
            drawer.closeDrawer( GravityCompat.START );
        }else {
            //which means it will remain closed
            super.onBackPressed();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        // item is the item that was selected
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){

                case R.id.nav_home:

                    selectedFragment = new HomeFragment();
                  //  cvIntro.setVisibility( View.GONE );
                    fragment_container.setVisibility( View.VISIBLE );

                    break;

                case R.id.nav_favourites:

                    selectedFragment = new FavouriteFragment();
                  //  cvIntro.setVisibility( View.GONE );
                    fragment_container.setVisibility( View.VISIBLE );
                    break;

                case R.id.nav_write:

                    selectedFragment = new WriteFragment();
              //      cvIntro.setVisibility( View.GONE );
                    fragment_container.setVisibility( View.VISIBLE );
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, selectedFragment).commit();
            return true;
        }};

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.myConfessions:
                Intent intent = new Intent( MainActivity.this, MyConfessions.class );
                startActivity( intent );
                break;

            case R.id.aboutUs:
                final AlertDialog.Builder myAlert = new AlertDialog.Builder( MainActivity.this );
                myAlert.setMessage( R.string.about_us )
                        .setPositiveButton( "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle( "About Us.." )
                        .create();
                myAlert.show();

                break;


            case R.id.rate:

                String url = "https://play.google.com/store/apps/details?id=com.hanaa.confessions";
                Intent i = new Intent( Intent.ACTION_VIEW );
                i.setData( Uri.parse(url) );
                startActivity( i );

                break;


            case R.id.share:

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Confessions Application");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.hanaa.confessions" + "\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share App through.."));
                } catch(Exception e) {
                    //e.toString();
                }
                break;

            case R.id.logOut:

                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent( MainActivity.this, Login.class );
                startActivity( intent1 );
                MainActivity.this.finish();
                break;

        }
        return true;
    }

}
