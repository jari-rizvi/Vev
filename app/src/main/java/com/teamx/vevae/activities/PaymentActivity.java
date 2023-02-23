package com.teamx.vevae.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.teamx.vevae.App.App;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.Utils.localization.LocaleManager;

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

public class PaymentActivity extends AppCompatActivity {

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

    String lang = "en";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment);
        amountText = findViewById(R.id.amount);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        payButton = findViewById(R.id.payButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Transaction in progress");
        progressDialog.setCancelable(false);
        httpClient = new OkHttpClient();


        totalPrice=  Prefs.getDouble("totalPrice",0);
        userEmail = Prefs.getString("userEmail","");
        userToken = Prefs.getString("userToken","");


        if (App.localeManager.getLanguage() != LocaleManager.Companion.getLANGUAGE_ENGLISH()) {

            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();
        }
        else {

            lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();
        }

        //Initialize
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull(AppConstants.STRIPE__PUBLISH_KEY)
        );
        amount=  Prefs.getInt("amount",500);

        amountText.setText(amount+"");


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Amount
                totalPrice = Double.valueOf(amountText.getText().toString());
                //call checkout to get paymentIntentClientSecret key


                if (InternetConnection.checkConnection(getApplicationContext())) {

                    if (cardInputWidget.getPaymentMethodCreateParams() != null){

                        progressDialog.show();
                        String storeType = Prefs.getString("storeType","");
                        if (storeType.equalsIgnoreCase("product")){
                            startCheckout("actions/product/stripe_paymentintent.php?email="+userEmail+"&token="+userToken);
                        }
                        else if (storeType.equalsIgnoreCase("service")){
                            int serviceId = Prefs.getInt("serviceId",0);
                            int duration_id = Prefs.getInt("durationId",0);
                            int serviceDateId = Prefs.getInt("serviceDateId",0);

                            startCheckout("actions/service/stripe_paymentintent.php?service_id="+serviceId+"&date_id="+serviceDateId+"&duration_id="+duration_id +"&token="+userToken+"&email="+userEmail);
                        }


                    }else {
                        Toast.makeText(PaymentActivity.this, "Empty fields or Invalid details", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(PaymentActivity.this, "Check Your internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(App.localeManager.setLocale(newBase));
    }


    private void startCheckout(String endPoint) {
        {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\"usd\","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                + "}";
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
                    .url(BACKEND_URL + endPoint)
                    .post(body)
                    .build();
            httpClient.newCall(request).enqueue(new PayCallback(this));

        }
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<PaymentActivity> activityRef;
        PayCallback(@NonNull PaymentActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            progressDialog.dismiss();
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() -> Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show());
            progressDialog.dismiss();
            Log.e("Error",  e.toString() );
            progressDialog.dismiss();
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                progressDialog.dismiss();
                Log.e("Transaction", response.toString() );

                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
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
            stripe.confirmPayment(PaymentActivity.this, confirmParams);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(PaymentActivity.this));

    }

    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<PaymentActivity> activityRef;
        PaymentResultCallback(@NonNull PaymentActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        //If Payment is successful
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            progressDialog.dismiss();
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();

            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Prefs.putString("transactionId",paymentIntent.getId());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast toast =Toast.makeText(activity, "Payment Successful", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Prefs.putString("transactionId",paymentIntent.getId());

                finish();

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert("Payment failed", Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage());

            }
        }
        //If Payment is not successful
        @Override
        public void onError(@NonNull Exception e) {
            progressDialog.dismiss();
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());


        }
    }
    private void displayAlert(@NonNull String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

}