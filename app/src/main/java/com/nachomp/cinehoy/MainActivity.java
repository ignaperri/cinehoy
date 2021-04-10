package com.nachomp.cinehoy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nachomp.cinehoy.common.Constantes;
import com.nachomp.cinehoy.common.SharedPreferencesManager;
import com.nachomp.cinehoy.data.admob.AdMobConstants;
import com.nachomp.cinehoy.firebase.FirebaseUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private ImageView ivAvatar;
    private AdView mAdView;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //AdMob
        mAdView = findViewById(R.id.adView);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //mostrar aca el banner del anuncio
                Log.d(TAG, "admob onInitializationComplete");
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                /**
                 * El método onAdLoaded() se ejecuta cuando el anuncio termina de cargarse.
                 * Por ejemplo, te sirve si quieres que el objeto AdView no se añada a tu actividad
                 * o fragmento hasta que sepas con seguridad que se cargará un anuncio.
                 */
                mAdView.setVisibility(View.VISIBLE);
                Log.d(TAG, "admob onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                /**
                 * El método onAdFailedToLoad() es el único que incluye un parámetro. El parámetro errorCode indica qué tipo de fallo se ha producido. Los valores posibles son constantes que se especifican en la clase AdRequest:
                 * ERROR_CODE_INTERNAL_ERROR: se ha producido un error en los sistemas internos (por ejemplo, el servidor de anuncios ha enviado una respuesta no válida).
                 * ERROR_CODE_INVALID_REQUEST: la solicitud de anuncio no era válida (por ejemplo, el ID del bloque de anuncios era incorrecto).
                 * ERROR_CODE_NETWORK_ERROR: no se ha podido realizar la solicitud de anuncio debido a problemas con la conexión de red.
                 * ERROR_CODE_NO_FILL: la solicitud de anuncio se ha realizado correctamente, pero no se ha devuelto ningún anuncio por falta de inventario publicitario.
                 */
                Log.d(TAG, "admob onAdFailedToLoad: Error = " + errorCode);
            }

            @Override
            public void onAdOpened() {
                /**
                 * Este método se invoca cuando el usuario toca un anuncio.
                 */
                Log.d(TAG, "admob onAdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "admob onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                /**
                 * Este método se invoca después de onAdOpened(), cuando un usuario hace clic y
                 * abre otra aplicación (como Google Play), y deja en segundo plano la aplicación actual.
                 */
                Log.d(TAG, "admob onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                /**
                 * Cuando un usuario regresa a la aplicación después de ver la URL de destino de un anuncio,
                 * se invoca este método. La aplicación puede utilizarlo para reanudar las actividades suspendidas
                 * o hacer cualquier otra tarea necesaria para prepararse para la interacción. Consulta el ejemplo
                 * de AdListener de AdMob para ver cómo se implementan los métodos del procesador de anuncios en la aplicación API Demo de Android.
                 */
                Log.d(TAG, "admob onAdClosed");
            }
        });
        ///////


        ivAvatar = findViewById(R.id.imageViewToolbarPhoto);
        Glide.with(this)
                .load(getResources().getDrawable(R.mipmap.ic_launcher))
                .centerCrop()
                .skipMemoryCache(true)
                .into(ivAvatar);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_popular, R.id.navigation_toprated, R.id.navigation_upcoming)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_FIREBASE_USUARIO_TOKEN, instanceIdResult.getToken());
                        FirebaseUtils.addOrUpdateUserInRealtimeDatabase(getApplicationContext());
                        Log.i("MainActivity", "Firebase token actual: " + instanceIdResult.getToken());
                    }
                });

    }

}
