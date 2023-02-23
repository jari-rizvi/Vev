package com.teamx.vevae.fragments.auth;

import android.os.Bundle;

import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.easyprefs.library.Prefs;
import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.Verification.EmailVerification;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.databinding.FragmentEmailOTPBinding;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailOTPFragment extends BaseFragment {


    FragmentEmailOTPBinding fragmentEmailOTPBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_email_o_t_p, container, false);

        fragmentEmailOTPBinding = FragmentEmailOTPBinding.inflate(getLayoutInflater(),container,false);


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {
            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();
        }else{
          lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();

        }

        fragmentEmailOTPBinding.btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fragmentEmailOTPBinding.pinView.getText().toString().isEmpty()){
                    if (InternetConnection.checkConnection(requireContext())){
                        verify();
                    }else {
                        showInternetToast();
                    }
                }else {
                    showToast(getString(R.string.complete_verification_code));
                }
            }
        });




        return fragmentEmailOTPBinding.getRoot();
    }

    private void verify() {

        progressbar("Verify Code", "Please wait...");

        String email = Prefs.getString("emailPhone","");
        WebServiceFactory.getInstance().verifyUserByEmail(fragmentEmailOTPBinding.pinView.getText().toString(),email,lang).enqueue(new Callback<EmailVerification>() {
            @Override
            public void onResponse(Call<EmailVerification> call, Response<EmailVerification> response) {
                if (response.body().getFlag()==1){
                    mProgressBar.dismiss();

                    navBuilder.setEnterAnim(R.anim.slide_in);
                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                    navController.navigate(R.id.resetPasswordFragment,null,navBuilder.build());

                }else {
                    showToast(response.body().getMessage());
                    mProgressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<EmailVerification> call, Throwable t) {
                mProgressBar.dismiss();
            }
        });

    }
}