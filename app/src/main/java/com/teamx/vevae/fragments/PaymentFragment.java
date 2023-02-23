package com.teamx.vevae.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamx.vevae.Models.ClientSecretModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PaymentFragment extends BaseFragment {


    // 10.0.2.2 is the Android emulator's alias to localhost
    // 192.168.1.6 If you are testing in real device with usb connected to same network then use your IP address
    private static final String BACKEND_URL = "https://vevuae.com/"; //4242 is port mentioned in server i.e index.js
    TextView amountText;
    CardInputWidget cardInputWidget;
    Button payButton;
    String secretKey;
    int amount = 500 ;
    // we need paymentIntentClientSecret to start transaction
    private String paymentIntentClientSecret;
    //declare stripe
    private Stripe stripe;

    Double totalPrice=null;

    private OkHttpClient httpClient;

    static ProgressDialog progressDialog;
    private String userEmail,userToken;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_payment, container, false);

        amountText = view.findViewById(R.id.amount);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);
        payButton = view.findViewById(R.id.payButton);
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setTitle("Transaction in progress");
        progressDialog.setCancelable(false);
        httpClient = new OkHttpClient();


        //Initialize
        stripe = new Stripe(
               requireContext(),AppConstants.STRIPE__PUBLISH_KEY
        );

        totalPrice=  Prefs.getDouble("totalPrice",0);
        userEmail = Prefs.getString("userEmail","");
        userToken = Prefs.getString("userToken","");

        amountText.setText(amount+"");



        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Amount
                totalPrice = Double.valueOf(amountText.getText().toString());
                //call checkout to get paymentIntentClientSecret key


                if (InternetConnection.checkConnection(requireContext())) {

                    if (cardInputWidget.getPaymentMethodCreateParams() != null){

                        progressDialog.show();
                        startCheckout();

                    }else {
                        Toast.makeText(requireContext(), "Empty fields or Invalid details", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(requireContext(), getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void startCheckout() {
        {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/text; charset=utf-8");
            double amount=totalPrice;
            Map<String,Object> payMap=new HashMap<>();
            Map<String,Object> itemMap=new HashMap<>();
            List<Map<String,Object>> itemList =new ArrayList<>();
            payMap.put("currency","AED");
            itemMap.put("amount",amount);
            itemList.add(itemMap);
            payMap.put("items",itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "actions/product/stripe_paymentintent.php?email="+userEmail+"&token="+userToken)
                    .post(body)
                    .build();


            httpClient.newCall(request).enqueue(new PayCallback(this));

        }
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<PaymentFragment> activityRef;
        PayCallback(@NonNull PaymentFragment activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            progressDialog.dismiss();
            final PaymentFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }

            Toast.makeText(activity.requireContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
//            activity.runOnUiThread(() -> Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show());

            Log.e("Error",  e.toString() );
            progressDialog.dismiss();
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
            final PaymentFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(activity.requireContext(), "Error: " + response.toString(), Toast.LENGTH_LONG).show();
                });

            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
       /* Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
        */
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response.body().string());

            paymentIntentClientSecret = jsonObject.getString("clientSecret");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //once you get the payment client secret start transaction
        //get card detail
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            //now use paymentIntentClientSecret to start transaction
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
            //start payment
            stripe.confirmPayment(requireActivity(), confirmParams);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<PaymentFragment> activityRef;
        PaymentResultCallback(@NonNull PaymentFragment activity) {
            activityRef = new WeakReference<>(activity);
        }
        //If Payment is successful
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            progressDialog.dismiss();
            final PaymentFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();

            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Prefs.putString("transactionId",paymentIntent.getId());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast toast =Toast.makeText(requireContext(), "Ordered Successful", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Prefs.putString("transactionId",paymentIntent.getId());


            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert("Payment failed", Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage());

            }
        }
        //If Payment is not successful
        @Override
        public void onError(@NonNull Exception e) {
            progressDialog.dismiss();
            final PaymentFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());


        }
    }
    private void displayAlert(@NonNull String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

}