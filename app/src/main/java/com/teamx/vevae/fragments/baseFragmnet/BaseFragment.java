package com.teamx.vevae.fragments.baseFragmnet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;


import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.Login.Login;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.ShareViewModel.ShareViewModel;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.activities.AuthActivity;
import com.teamx.vevae.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseFragment extends Fragment {

    public KProgressHUD mProgressBar;
    public NavController navController;
    private BottomNavigationView bottomNavigationView;
    private CardView bottomCard;
    public ShareViewModel shareViewModel;
    public NavOptions.Builder navBuilder = new NavOptions.Builder();
    private String userToken;
    private String deviceToken;
    private  BottomSheetDialog bottomSheetDialog;
    public String lang = "en";
//    public String countries[] = {"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Palestine", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);


    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showInternetToast() {
        Toast.makeText(getContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
    }

    public void popUpStack() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
        navController.popBackStack();
    }

    public void clearActivityStack() {
     /*String languageMode = Prefs.getString(LocaleManager.Companion.getLANGUAGE_KEY(), LocaleManager.Companion.getDEFAULT_LAN());
        Prefs.clear();
        Prefs.putString(LocaleManager.Companion.getLANGUAGE_KEY(), languageMode);*/

        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        requireActivity().finish();
    }

    public void clearFragmentsLogout() {
        String languageMode = Prefs.getString(LocaleManager.Companion.getLANGUAGE_KEY(), LocaleManager.Companion.getDEFAULT_LAN());
        Prefs.clear();
        Prefs.putString(LocaleManager.Companion.getLANGUAGE_KEY(), languageMode);
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        requireActivity().finish();
    }


    private void initialize() {

        bottomNavigationView = requireView().findViewById(R.id.bottom_navigation);
        bottomCard = requireView().findViewById(R.id.bottom_card);

    }

   /* public void hideBottomNav() {
        initialize();

        bottomNavigationView.setVisibility(View.GONE);
        bottomCard.setVisibility(View.GONE);
    }

    public void showBottomNav(){

        initialize();

        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomCard.setVisibility(View.VISIBLE);

    }*/


    public void progressbar(String title, String detail) {
        mProgressBar = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(detail)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void loadSVGImage(Context context, String imageUrl, ImageView imageView) {
       /* GlideToVectorYou
                .init()
                .with(context)
                .load(Uri.parse(AppConstants.imagePath(imageUrl)), imageView);*/



      /*  Uri uri = Uri.parse(imageUrl);
        SvgLoader.pluck()
                .with(getActivity())
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(uri, imageView);*/
    }


    public void imageLoaderPicasso(String imageUrl, ImageView imageView) {

        Picasso.get().load(AppConstants.imagePath(imageUrl)).placeholder(R.drawable.icon_placeholder).into(imageView);

    }

    public File getBitMapFile(Bitmap bitmap) {
        String destPath = getContext().getExternalFilesDir(null).getAbsolutePath();

        File file = new File(destPath + File.separator + "reduceImage");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bos);
        byte[] bitmapData = bos.toByteArray();

        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bitmapData);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (Exception e) {
            e.getMessage();
        }
        return file;
    }

    public boolean setNewLocale(String language,Boolean restartProcess){
        App.localeManager.setNewLocale(requireActivity(), language);
        Intent i = new Intent(requireActivity(), AuthActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        if (restartProcess) {
            Intent intent = new  Intent(requireActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return true;
    }


    public void showLoginBottomSheet(int fragmentId, boolean isNavigate) {
        bottomSheetDialog = new BottomSheetDialog(requireContext());

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
                    if (InternetConnection.checkConnection(requireContext())){

                        if (App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {
                            login(userEmail.getText().toString(),userPassword.getText().toString(),deviceToken,LocaleManager.Companion.getLANGUAGE_ENGLISH(),fragmentId,isNavigate);
                        }else{
                            login(userEmail.getText().toString(),userPassword.getText().toString(),deviceToken,LocaleManager.Companion.getLANGUAGE_ARABIC(),fragmentId,isNavigate);

                        }

                    }else{
                        Toast.makeText(requireContext(), "Check Your internet conection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bottomSheetDialog.show();
    }

    private Boolean validate(String email, String password) {

        if (email.isEmpty()){
            Toast.makeText(requireContext(), "Kindly enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()){
            Toast.makeText(requireContext(), "Kindly enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void login(String userEmail, String userPassword, String deviceToken,String lang, int fragmentId, boolean isNavigate) {

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

                    if (isNavigate){
                        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                        navController.navigate(fragmentId, null);
                    }



                }else {
                    Toast.makeText(requireContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                bottomSheetDialog.dismiss();

            }
        });

    }
}
