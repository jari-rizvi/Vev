package com.teamx.vevae.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.Login.Login;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.databinding.FragmentOTPBinding;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OTPFragment extends BaseFragment {


    FragmentOTPBinding fragmentOTPBinding;

    String phoneNumber;
    private String codeBySystem;
    private FirebaseAuth mAuth;

    String username,password,email,forgot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_o_t_p, container, false);

        fragmentOTPBinding = FragmentOTPBinding.inflate(inflater,container,false);


        if (App.localeManager.getLanguage() != LocaleManager.Companion.getLANGUAGE_ENGLISH()) {

            fragmentOTPBinding.buttonBackImage.setImageDrawable(
                    getResources().getDrawable(
                            R.drawable.arabic_button_back,
                            requireActivity().getTheme()
                    )
            );

            lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();
        }
        else {
            fragmentOTPBinding.buttonBackImage.setImageDrawable(
                    getResources().getDrawable(
                            R.drawable.icon_arrow,
                            requireActivity().getTheme()
                    )
            );

            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();
        }


        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();

        if (bundle!=null){

            username = bundle.getString("name");
            phoneNumber = bundle.getString("phone");
            email = bundle.getString("email");
            password = bundle.getString("password");
            forgot = bundle.getString("forgot");

            sendVerificationCodeToUser(phoneNumber);

        }



        fragmentOTPBinding.resentOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCodeToUser(phoneNumber);
                showToast(getString(R.string.code_is_send_to_your_number));
            }
        });




        fragmentOTPBinding.btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = fragmentOTPBinding.pinView.getText().toString();
                if (!code.isEmpty()){
                    progressbar("Verifing","Please wait");
                    verifyCode(code);
                }else {
                    showToast(getString(R.string.please_enter_code));
                }
            }
        });



        return fragmentOTPBinding.getRoot();
    }


    private void sendVerificationCodeToUser(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);

            //get the code to match with user entered otp code
            codeBySystem = verificationId;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                fragmentOTPBinding.pinView.setText(code);
                progressbar("Verifing","Please wait");
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            showToast(e.getMessage());
            Log.e("Failed",e.getMessage() );
        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        SignInUsingCredentials(credential);
    }

    private void SignInUsingCredentials(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    showToast(getString(R.string.oTP_verified));
                    mProgressBar.dismiss();

                    if (forgot !=null){
                        navBuilder.setEnterAnim(R.anim.slide_in);
                        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                        navController.navigate(R.id.resetPasswordFragment,null,navBuilder.build());
                    }else {
                        getRegister();
                    }


                }else {
                    Log.e("TAG", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        mProgressBar.dismiss();
                        // The verification code entered was invalid
                    }
                }
            }
        });

    }

    private void getRegister() {
        String deviceToken = Prefs.getString("deviceToken","");
        progressbar("Signing Up","Please wait...");
        WebServiceFactory.getInstance().registerUser(username,phoneNumber,email,password, deviceToken, lang).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.body().getFlag() ==1){
                    mProgressBar.dismiss();

                    Prefs.putString("userToken", response.body().getData().getToken());
                    Prefs.putString("userName", response.body().getData().getUser().getName());
                    Prefs.putString("userEmail", response.body().getData().getUser().getEmail());
                    Prefs.putString("userPhone", response.body().getData().getUser().getPhone());
                    Prefs.putString("userImage",response.body().getData().getUser().getImage());
                    Prefs.putInt("userId", response.body().getData().getUser().getId());

                    clearActivityStack();

                }else{
                    mProgressBar.dismiss();
                    showToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });

    }
}