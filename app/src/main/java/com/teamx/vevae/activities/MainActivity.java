package com.teamx.vevae.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.Login.Login;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.databinding.ActivityMainBinding;
import com.teamx.vevae.fragments.bottomLogin.BottomSheetLoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    NavController navController;
    private String userToken;
    private String deviceToken;
    private  BottomSheetDialog bottomSheetDialog;




    public static final String Channel_ID  = "Adroit_Solution";
    private static final String Channel_NAME= "eMan_Transport";
    private static final String Channel_DESC = "Taxi_App_Notification";
    String lang = "en";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());



        navController = Navigation.findNavController(this,R.id.nav_host_Fragmnet);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigation,navController);

        if (App.localeManager.getLanguage() != LocaleManager.Companion.getLANGUAGE_ENGLISH()) {

            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();
        }
        else {

            lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel   channel = new NotificationChannel(Channel_ID,Channel_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Channel_DESC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        activityMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id){
                case R.id.profileFragment:
                    navController = Navigation.findNavController(this, R.id.nav_host_Fragmnet);
                    navController.navigate(R.id.profileFragment, null);
                    break;
                case R.id.homeFragment:
                    navController = Navigation.findNavController(this, R.id.nav_host_Fragmnet);
                    navController.navigate(R.id.homeFragment, null);
                    break;

                case R.id.searchFragment:
                    navController = Navigation.findNavController(this, R.id.nav_host_Fragmnet);
                    navController.navigate(R.id.searchFragment, null);
                    break;

                case R.id.cartFragment:
                    userToken= Prefs.getString("userToken","");
                    if (!userToken.isEmpty()){
                        navController = Navigation.findNavController(this, R.id.nav_host_Fragmnet);
                        navController.navigate(R.id.cartFragment, null);

                    }else {
                        showLoginBottomSheet();

                    }

                    break;
            }
            return true;
        });

    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(App.localeManager.setLocale(newBase));
    }



    public void hideNavBar(){
        activityMainBinding.bottomCard.setVisibility(View.GONE);
    }

    public void showNavBar(){
        activityMainBinding.bottomCard.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showLoginBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_login);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        EditText userEmail = bottomSheetDialog.findViewById(R.id.edit_email);
        EditText userPassword = bottomSheetDialog.findViewById(R.id.edit_password);
        CardView btnLogin = bottomSheetDialog.findViewById(R.id.btnLogin);

        ImageView btnCross = bottomSheetDialog.findViewById(R.id.btnCross);

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        deviceToken = task.getResult();
                    }
                });




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate(userEmail.getText().toString(),userPassword.getText().toString())){
                    if (InternetConnection.checkConnection(MainActivity.this)){

                        login(userEmail.getText().toString(),userPassword.getText().toString(),deviceToken,lang);
                    }else{
                        Toast.makeText(MainActivity.this, "Check Your internet conection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });






        bottomSheetDialog.show();
    }

    private Boolean validate(String email, String password) {

        if (email.isEmpty()){
            Toast.makeText(MainActivity.this, "Kindly enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()){
            Toast.makeText(MainActivity.this, "Kindly enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void login(String userEmail, String userPassword, String deviceToken,String lang) {

        WebServiceFactory.getInstance().loginUser(userEmail,userPassword,deviceToken,lang).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.body().getFlag()==1){

                    Prefs.putString("userToken", response.body().getData().getToken());
                    Prefs.putString("userName", response.body().getData().getUser().getName());
                    Prefs.putString("userEmail", response.body().getData().getUser().getEmail());
                    Prefs.putString("userPhone", response.body().getData().getUser().getPhone());
                    Prefs.putString("userImage", response.body().getData().getUser().getImage());
                    Prefs.putInt("userId", response.body().getData().getUser().getId());

                    bottomSheetDialog.dismiss();

                    navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_Fragmnet);
                    navController.navigate(R.id.cartFragment, null);


                }else {
                    Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                bottomSheetDialog.dismiss();

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*navController = Navigation.findNavController(this, R.id.nav_host_Fragmnet);
        navController.navigate(R.id.homeFragment, null);*/



    }
}