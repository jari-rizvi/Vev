package com.teamx.vevae.Networking;


import com.teamx.vevae.Models.ClientSecretModel;
import com.teamx.vevae.Models.AddAddress;
import com.teamx.vevae.Models.addreview.AddReview;
import com.teamx.vevae.Models.addtocart.AddToCart;
import com.teamx.vevae.Models.DefaultAddress;
import com.teamx.vevae.Models.DeleteCartItem;
import com.teamx.vevae.Models.DeleteSearch;
import com.teamx.vevae.Models.Login.Login;
import com.teamx.vevae.Models.Logout;
import com.teamx.vevae.Models.Password.ChangePassword;
import com.teamx.vevae.Models.PlaceOrder;
import com.teamx.vevae.Models.SignUp.ValidateUser;
import com.teamx.vevae.Models.SignUp.VerifyUser;
import com.teamx.vevae.Models.UpdateProfile.UpdateProfile;
import com.teamx.vevae.Models.UpdateQuantity;
import com.teamx.vevae.Models.UserDetails.UserDetails;
import com.teamx.vevae.Models.Verification.EmailVerification;
import com.teamx.vevae.Models.address.ShowAllAddress;
import com.teamx.vevae.Models.cart.Cart;
import com.teamx.vevae.Models.checkout.CheckOut;
import com.teamx.vevae.Models.home.Home;
import com.teamx.vevae.Models.orderDetails.OrderDetails;
import com.teamx.vevae.Models.orderList.OrderList;
import com.teamx.vevae.Models.preview.ProductPreview;
import com.teamx.vevae.Models.recentsearch.RecentSearch;
import com.teamx.vevae.Models.search.SearchResult;
import com.teamx.vevae.Models.services.BuyServices;
import com.teamx.vevae.Models.AllCategories.AllCategories;
import com.teamx.vevae.Models.storeprofile.StoreProfile;
import com.teamx.vevae.Models.updatednotification.Notifications;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface WebService {



    //Register user
    @FormUrlEncoded
    @POST("api/user/register")
    Call<Login> registerUser(@Field("name") String name,
                             @Field("phone") String phone,
                             @Field("email") String email,
                             @Field("password") String password,
                             @Field("deviceToken") String deviceToken, @Field("lang") String lang );


    //verify user
    @FormUrlEncoded
    @POST("api/user/verify_user")
    Call<VerifyUser> verifyUser(@Field("email") String email, @Field("phone") String phone,  @Field("lang") String lang );


    @FormUrlEncoded
    @POST("api/user/validate_user")
    Call<ValidateUser> validateUserForgotPassword(@Field("email_phone") String email, @Field("lang") String lang );

    @FormUrlEncoded
    @POST("api/user/login")
    Call<Login> loginUser(@Field("email") String email, @Field("password") String password, @Field("deviceToken") String deviceToken, @Field("lang") String lang );


    @DELETE("api/user/logout")
    Call<Logout> logout();


    @FormUrlEncoded
    @POST("api/user/verify_code")
    Call<EmailVerification> verifyUserByEmail(@Field("code") String code,@Field("email") String emailPhone, @Field("lang") String lang );


    @FormUrlEncoded
    @PUT("api/user/change_forget_password")
    Call<ChangePassword> changePassword(@Field("password") String Password,@Field("email_phone") String emailPhone);


    @GET("api/user/edit_profile")
    Call<UserDetails> getUserDetail(@Query("lang") String lang);


    @POST("api/user/update_profile")
    Call<UpdateProfile> updateProfile(@Body RequestBody body );


    @FormUrlEncoded
    @POST("api/user/user_change_password_wa")
    Call<ChangePassword> updatePassword(@Field("currentPassword") String Password, @Field("newPassword") String newPassword,  @Field("confirmPassword") String confirmPassword );


    @GET("api/Stores/storesList")
    Call<Home> getAllStore(@Query("latitude") double latitude , @Query("longitude") double longitude, @Query("lang") String lang );

    @FormUrlEncoded
    @POST("api/ratings/addReview")
    Call<AddReview> addReview(@Field("storeId") int storeId, @Field("ratings") float ratings, @Field("comments") String comment,  @Field("lang") String lang );

    @GET("api/Products/storeProfile")
    Call<StoreProfile> getStoreDetails(@Query("storeId") int storeId, @Query("storetype") String storetype, @Query("lang") String lang );



    @GET("api/Products/ProductPreview")
    Call<ProductPreview> getProduct(@Query("ProductId") int storeId, @Query("lang") String lang );


    @FormUrlEncoded
    @POST("api/Products/addtoCart")
    Call<AddToCart> addToCart(@Field("modifier1") String modifier1,
                              @Field("modifier2") String modifier2, @Field("ProductId") int productId,@Field("quantity") int quantity,  @Field("lang") String lang );



    @GET("api/Products/showCartData")
    Call<Cart> getCartData(@Query("lang") String lang);


    @FormUrlEncoded
    @PATCH("api/Products/updateCart")
    Call<UpdateQuantity> updateQuantity(@Field("cartId") int cartId,@Field("quantity") int quantity);


    @FormUrlEncoded
    @POST("api/Address/AddShippingaddress")
    Call<AddAddress> addAddress(@Field("HouseNo") String HouseNo,
                                @Field("streetNo") String streetNo,
                                @Field("postalCode") String postalCode,
                                @Field("city") String city,
                                @Field("state") String state,
                                @Field("Isdefault") int Isdefault,
                                @Field("lang") String lang );



    @PUT("api/Address/isDefaultAddress")
    Call<DefaultAddress> setDefaultAddress(@Query("AddressId") int AddressId,@Query("Isdefault") int Isdefault);


    @GET("api/Address/ShowAllAddress")
    Call<ShowAllAddress> getAllAddress(@Query("lang") String lang);


    @DELETE("api/Products/itemRemove")
    Call<DeleteCartItem> deleteCartItem(@Query("cartId") int cartId);

    @GET("api/Products/ProceedCheckout")
    Call<CheckOut> getCheckOutData(@Query("lang") String lang);



    @GET("api/Services/previewServices")
    Call<BuyServices> getServices(@Query("serviceId") int serviceId, @Query("lang") String lang);



    @GET("api/Order/orderList")
    Call<OrderList> getOrderList(@Query("lang") String lang);



    @GET("api/Order/orderDetail")
    Call<OrderDetails> getOrderDetails(@Query("orderId") int orderId,@Query("lang") String lang );


    @GET("api/Search/getSearchResults")
    Call<SearchResult> getSearchResult(@Query("SearchKeyword") String SearchKeyword, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("lang") String lang);



    @GET("api/Search/getSearch")
    Call<RecentSearch> getRecentSearch(@Query("lang") String lang);

    @POST("api/Products/orderPlaced")
    Call<PlaceOrder> proceedOrder(@Body String params);

    @GET("api/Products/ListByCategories")
    Call<AllCategories> getAllItemsByCategory(@Query("categoryId") int categoryId,@Query("store_id") int storeId, @Query("type") String  type,@Query("lang") String lang);



    @POST("api/Services/Buyservice")
    Call<PlaceOrder> buyService(@Body String params );



    @GET("api/Notification/getNotifications")
    Call<Notifications> getAllNotification(@Query("lang") String lang);


    @DELETE("api/Search/clearSearch")
    Call<DeleteSearch> deleteSearchHistory();

    @FormUrlEncoded
    @POST("api/Search/addSearch")
    Call<Logout> addSearch(@Field("SearchKeyword") String SearchKeyword, @Field("SearchType") String SearchType, @Field("SearchTypeId") int SearchTypeId,  @Field("lang") String lang );


}



