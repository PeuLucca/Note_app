package com.example_2_060303.note.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example_2_060303.note.R;
import com.example_2_060303.note.activity.MainActivity;

import java.util.Random;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        notificacao(context);
    }

    public void notificacao(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            NotificationChannel channel = new NotificationChannel("Minha notificação",
                    "Minha notificacao Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Minha notificação");

        String[] arrayFrases = {
                String.valueOf(context.getApplicationContext().getResources().getString(R.string.naoPercaNadaAnoteECompartilhePT)),
                String.valueOf(context.getApplicationContext().getResources().getString(R.string.vcJaFezTudoOqPlanejouHjPT)),
                        context.getApplicationContext().getResources().getString(R.string.comeceSeu1RascunhoPT) + "\uD83D\uDE09",
                                context.getApplicationContext().getResources().getString(R.string.lembreDeOrganizarNotasPT) + "\uD83D\uDCDD"
        };
        int numeroAleatorio = new Random().nextInt(4);

        builder.setContentTitle(String.valueOf(context.getApplicationContext().getResources().getString(R.string.nEsquecaDasSuasTarefasPT)));
        builder.setContentIntent( resultPendingIntent );
        builder.setContentText( arrayFrases[numeroAleatorio] );
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.logo_transparent); // colocar icone transparente
            builder.setColor(context.getResources().getColor(R.color.backgroundIcon));
        } else {
            builder.setSmallIcon(R.drawable.logo_popup);
        }
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1,builder.build());
    }

}