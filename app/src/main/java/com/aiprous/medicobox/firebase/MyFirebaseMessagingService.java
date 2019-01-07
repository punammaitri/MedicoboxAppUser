package com.aiprous.medicobox.firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Collection;
import java.util.StringTokenizer;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String getTitle, getData, getPrice, getOrderId, Price;
    private String OrderId, PickUpAddress, DropAddress, getPriceText, getOrderText, PickUp, Drop;
    private String TotalDistance;
    private String DistanceText;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("Msg From :", remoteMessage.getFrom());

        getTitle = String.valueOf(remoteMessage.getNotification().getBody());

        try {
            if (getTitle.equals("Your Booking is accepted")) {
                //For New Booking
                Collection<String> getMessage = remoteMessage.getData().values();
                Object[] ff = getMessage.toArray();
                getData = (String) ff[2];

                StringTokenizer part = new StringTokenizer(getData, "=");
                Price = part.nextToken();
                OrderId = part.nextToken();
                PickUp = part.nextToken();
                PickUpAddress = part.nextToken();
                Drop = part.nextToken();
                DropAddress = part.nextToken();
                DistanceText=part.nextToken();
                TotalDistance=part.nextToken();

                StringTokenizer price = new StringTokenizer(Price, " ");
                getPriceText = price.nextToken();
                getPrice = price.nextToken();

                StringTokenizer orderId = new StringTokenizer(OrderId, " ");
                getOrderText = orderId.nextToken();
                getOrderId = orderId.nextToken();

                //Adding click event to the notificaton
         /*       Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("price", getPrice);
                intent.putExtra("order_id", getOrderId);
                intent.putExtra("pickup", PickUpAddress);
                intent.putExtra("drop", DropAddress);
                intent.putExtra("distance", TotalDistance);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}