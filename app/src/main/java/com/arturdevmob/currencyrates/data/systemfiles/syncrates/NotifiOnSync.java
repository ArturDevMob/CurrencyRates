package com.arturdevmob.currencyrates.data.systemfiles.syncrates;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.presentation.SingleActivity;
import androidx.core.app.NotificationCompat;

public class NotifiOnSync {
    private static final String CHANNEL_ID = "channel_sync_currency_rates";
    private Context context;

    public NotifiOnSync(Context context) {
        this.context = context;
    }

    public void showNotifiSuccessSyncCurrencyRate() {
        String textDescriptionNotifi = getString(R.string.update_currency_rates_done);
        this.showNotifi(textDescriptionNotifi);
    }

    public void showNotifiErrorSyncCurrencyRate() {
        String textDescriptionNotifi = getString(R.string.update_currency_rates_error);
        this.showNotifi(textDescriptionNotifi);
    }

    private void showNotifi(String description) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_euro_symbol_white)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(description)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.auto_update_currency_rates),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(getString(R.string.desc_channel));
            channel.enableLights(true);
            channel.enableVibration(true);

            manager.createNotificationChannel(channel);
        }

        manager.notify(1, notification);
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(context, SingleActivity.class);

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getString(int resId) {
        return context.getResources().getString(resId);
    }
}
