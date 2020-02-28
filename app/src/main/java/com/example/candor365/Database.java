package com.example.candor365;




import android.nfc.NfcAdapter;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Api;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

class Database {
    static String TAG = "DataBase";
    private static FirebaseFirestore db=null;
    private static Map data;



    static void initializeDb() {
        db = FirebaseFirestore.getInstance();
    }

    static void writeClassDb(Map docData, String date){
        db.collection("classesByDate").document(date).collection("6:30").document("ClassInfo").set(docData);
        //need error checking
    }

    static void readClassDb(final String date, final readCallBack reader){


        db.collection("classesByDate").document(date).collection("6:30").document("ClassInfo")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map data = documentSnapshot.getData();
                        if (data != null)
                            Log.d(TAG, "Document data => " + data.toString());
                        reader.onCallBack(data);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Map emptyData=null;
                reader.onCallBack(emptyData);
            }
        });
    }
    //This method reads a specific item from a specific category from the respective parameters
    static void readShopDb(final String category, final String item, final readCallBack reader){
        db.collection("store").document("category").collection(category).document(item)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map Item = documentSnapshot.getData();
                        if (Item != null){
                            Log.d(TAG, "Item data => " + Item.toString());
                        }
                        reader.onCallBack(Item);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Map noItem = null;
                reader.onCallBack(noItem);
            }
        });
    }
    //this method reads all items from a specific category
//    static void readShopDb(final String category, final readCallBack reader){
//        db.collection("store").document("category").collection(category).
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Map Item = documentSnapshot.getData();
//                        if (Item != null){
//                            Log.d(TAG, "Item data => " + Item.toString());
//                        }
//                        reader.onCallBack(Item);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Map noItem = null;
//                reader.onCallBack(noItem);
//            }
//        });
//    }

}
