package com.example.tallnow.Fragments;

import com.example.tallnow.Notification.Myresponse;
import com.example.tallnow.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface  APIService
{
    @Headers({
                    "Content-Type:application/json",
                    "Authorization:key=AAAAp9P4TFc:APA91bGx0N8IGvfnZGKkENhd5_EQ7qNqETjNVyQkdqtEZ-npue8-l6Ne_R_HXUfqxbEh9CNxtmBNTGywXHp2j1RYe1TCfKi6pYCW0AzfjCwoHBRk0pISDyk8mJDVd-ElcV65U5RDxNE6"
    })
    @POST("fcm/send")
    Call<Myresponse> sendNotification(@Body Sender body);
}
