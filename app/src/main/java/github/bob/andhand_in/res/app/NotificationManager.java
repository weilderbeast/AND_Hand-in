package github.bob.andhand_in.res.app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import github.bob.andhand_in.R;
import github.bob.andhand_in.ui.fragments.friend_requests.FriendRequests;
import github.bob.andhand_in.ui.fragments.friend_requests.FriendRequestsViewModel;

public class NotificationManager {
    private static NotificationManager instance;

    private NotificationManager(){}

    public static NotificationManager getInstance() {
        if(instance == null){
            instance = new NotificationManager();
        }
        return instance;
    }

    public void createSimpleNotification(Context context, String title, String content, int iconId){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Telegraph")
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NotificationID.getID(), builder.build());
    }

    public void createSimpleNotification(Context context, String title, String content, int iconId, int priority){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Telegraph")
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(priority);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NotificationID.getID(), builder.build());
    }

    //TODO add buttons that do something
    public void createFriendRequestNotification(Context context, String title, String content, int iconId){
        int notifId = NotificationID.getID();

        Intent addFriend = new Intent(context, FriendRequestsViewModel.class);
        addFriend.setAction("add_friend");
        addFriend.putExtra("notification_id", notifId);



        Intent removeFriend = new Intent(context, FriendRequestsViewModel.class);
        removeFriend.setAction("remove_friend");
        removeFriend.putExtra("notification_id", notifId);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Telegraph")
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notifId, builder.build());
    }
}
