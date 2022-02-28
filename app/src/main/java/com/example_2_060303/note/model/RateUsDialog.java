package com.example_2_060303.note.model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example_2_060303.note.R;
import com.example_2_060303.note.helper.SharedPref;

public class RateUsDialog extends Dialog {

    private float userRate = 0;
    private SharedPref sharedPref;
    private AppCompatButton rateNowBtn;
    private AppCompatButton laterBtn;
    private AppCompatButton neverBtn;
    private RatingBar ratingBar;
    private ImageView ratingImage;

    private static final String ARQUIVO_PREFERENCIA = "Arquivo Preferencia";
    private Context c;

    public RateUsDialog(@NonNull Context context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rate_us_dialog_layout);

        rateNowBtn = findViewById(R.id.rateNowBtn);
        laterBtn = findViewById(R.id.laterBtn);
        neverBtn = findViewById(R.id.neverBtn);
        ratingBar = findViewById(R.id.ratingBar);
        ratingImage = findViewById(R.id.ratingImage);

        SharedPreferences preferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if( preferences.contains("avaliacao") ){ // entao nao é vazia

            sharedPref = new SharedPref( c.getApplicationContext() );
            String resp = sharedPref.verificarAvaliacao();
            if( resp.equals("maisTarde15") ){
                laterBtn.setVisibility(View.GONE);
            }

        }

        rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // enviar avaliacao ( variável 'userRate' ) para play store

                 Uri uri = Uri.parse("market://details?id=com.example_2_060303.note");
                 Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                goToMarket.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
                try {
                    getContext().startActivity( goToMarket );

                } catch (Exception e) {

                    final String appPackageName = getContext().getPackageName();
                    try {
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                }

                sharedPref = new SharedPref( c.getApplicationContext() );
                sharedPref.salvarAvaliacao( "true" );
                dismiss();
            }
        });

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref = new SharedPref( c.getApplicationContext() );

                String respostaAvaliacao = "";
                respostaAvaliacao = sharedPref.verificarAvaliacao();

                if( respostaAvaliacao.equals("false") ){
                    sharedPref.salvarAvaliacao( "maisTarde7" );
                }else {
                    if( respostaAvaliacao.equals("maisTarde7") ){
                        sharedPref.salvarAvaliacao( "maisTarde10" );
                    }else if( respostaAvaliacao.equals("maisTarde10") ){
                        sharedPref.salvarAvaliacao( "maisTarde15" );
                    }
                }

                dismiss();
            }
        });

        neverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref = new SharedPref( c.getApplicationContext() );
                sharedPref.salvarAvaliacao( "true" );

                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if ( rating <= 1 ){
                    ratingImage.setImageResource( R.drawable.one_star );
                }else if(  rating <= 2 ){
                    ratingImage.setImageResource( R.drawable.two_star );
                }else if(  rating <= 3 ){
                    ratingImage.setImageResource( R.drawable.three_star );
                }else if(  rating <= 4 ){
                    ratingImage.setImageResource( R.drawable.four_star );
                }else if(  rating <= 5 ){
                    ratingImage.setImageResource( R.drawable.five_star );
                }

                // animation
                animateImage( ratingImage );

                userRate = rating;

            }
        });

    }

    public void animateImage( ImageView ratingImage){

        ScaleAnimation scaleAnimation = new ScaleAnimation( 0, 1f, 0 ,1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation( scaleAnimation );

    }

}
