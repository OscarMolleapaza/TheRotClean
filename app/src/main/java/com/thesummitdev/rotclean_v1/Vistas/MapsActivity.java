package com.thesummitdev.rotclean_v1.Vistas;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.thesummitdev.rotclean_v1.Fragments.Comentarios;
import com.thesummitdev.rotclean_v1.Fragments.Distritos;
import com.thesummitdev.rotclean_v1.Fragments.mapa;
import com.thesummitdev.rotclean_v1.R;
import com.thesummitdev.rotclean_v1.Fragments.admin;
import com.thesummitdev.rotclean_v1.Fragments.creditos;
import com.thesummitdev.rotclean_v1.Fragments.sugerencias;

public class MapsActivity extends AppCompatActivity {

    private final int LOCATION = 1;
    //summit123  alias : newkeyrotclean
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Toolbar toolbar;
    Drawer drawer1;

    @SuppressLint("ResourceType")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_maps );


        toolbar = (Toolbar)findViewById(R.id.toolBarMain);


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.grad_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName("Oscar Molleapaza").withEmail("oscarmolleapaza@tecsup.edu.pe").withIcon(getResources().getDrawable(R.drawable.profile))
                ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                }).build();

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN );
        }

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier( 1 ).withIcon( R.drawable.ic_map_ligh ).withName( "Mapa" ); //MAPA
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier( 2 ).withIcon( R.mipmap.district).withName( "Distritos" ); //Distritos
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier( 3 ).withIcon( R.drawable.ic_acerca ).withName( "Sugerencias" ); //Sugerencias
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier( 4 ).withIcon( R.drawable.ic_informacion ).withName( "Creditos" );     //Creditos
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier( 5 ).withIcon( R.mipmap.comments).withName( "Comentarios" ); //Comentarios
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier( 6 ).withIcon( R.drawable.ic_menu_share ).withName( "Admin" ); //ADMIN


         drawer1 = new DrawerBuilder()
                .withActivity( this )
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)

                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6

                )
                .withOnDrawerItemClickListener( new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Fragment fragment = null;
                            FragmentManager fragmentManager = getSupportFragmentManager();

                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:


                                    fragment = new mapa();
                                   // Mapa
                                    break;
                                case 2:

                                    fragment = new Distritos();
                                  //  Distritos
                                    break;
                                case 3:

                                    fragment = new sugerencias();
                                   // Sugerencias
                                    break;
                                case 4:
                                    fragment = new creditos();
                                    //Creditos
                                    break;
                                case 5:
                                    fragment = new Comentarios();
                                   // comentarios
                                    break;
                                case 6:
                                    fragment = new admin();
                                    // admin
                                    break;
                            }

                            if (fragment != null) {
                                fragmentManager.beginTransaction().replace( R.id.content_frame, fragment ).commit();
                            }

                            if (drawerItem instanceof Nameable) {
                                setTitle( ((Nameable) drawerItem).getName().getText( getApplicationContext() ) );
                            }
                        }

                        return false;
                    }
                } )
                .withShowDrawerOnFirstLaunch( false )
                .withFireOnInitialOnClick( true )
                .withShowDrawerUntilDraggedOpened( false )
                .build() ;
    }
}