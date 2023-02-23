package com.teamx.vevae.Utils;


import com.teamx.vevae.DummyModel.PaymentMethod;
import com.teamx.vevae.Models.preview.Modifiers1;
import com.teamx.vevae.Models.preview.Modifiers2;
import com.teamx.vevae.Models.services.ServicesDate;
import com.teamx.vevae.Models.services.ServicesDatum;

public class AppConstants {
//    public static  String SERVER_BASE_URL = "http://192.168.0.191:8000/api/";
//    public static final String IMAGE_BASE_URL = "http://192.168.0.191:8000";
//    public static boolean active = false;

    public static final String SERVER_BASE_URL = "https://vevadmins.vevuae.com/api/";
    public static final String IMAGE_BASE_URL = "https://vevadmins.vevuae.com/";
    public static final String STRIPE__PUBLISH_KEY = "pk_test_51DxDBcACqLSpVnkChYNe1cMPpjZ5G6KObaoyO451c5lOIHu16rH6eTiZ2LBROAwhH2III8JbIKExHjdHhtTW6D6M007PQu4Aks";



    public static PaymentMethod SELECTED_METHOD = null;
    public static ServicesDatum SELECTED_SERVICE = null;
    public static Modifiers1 SELECTED_MODIFIER_1 = null;
    public static Modifiers2 SELECTED_MODIFIER_2 = null;

    public static ServicesDate Current_Item = null;



    public static String imagePath(String path) {
        if (path.contains("http")) {
            return path;
        } else
            return AppConstants.IMAGE_BASE_URL + path;
    }

    public static String firebaseImagePath(String path) {
        if (path.contains("https")) {
            return path;
        } else
            return AppConstants.IMAGE_BASE_URL+path;

    }



}
