package aau.losamigos.wizard;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.peak.salut.Salut;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.network.DataCallback;

public class TableActivity extends AppCompatActivity implements View.OnClickListener {

    public Salut network;

    ImageView pCard1;
    ImageView pCard2;
    ImageView pCard3;
    ImageView pCard4;
    ImageView pCard5;

    ImageView playedCard1;

    Drawable img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

        network = GameConfig.getInstance().getSalut();

        DataCallback callback = GameConfig.getInstance().getCallBack();
        pCard1 = findViewById(R.id.PCard1);
        pCard2 = findViewById(R.id.PCard2);
        pCard3 = findViewById(R.id.PCard3);
        pCard4 = findViewById(R.id.PCard4);
        pCard5 = findViewById(R.id.PCard5);

        pCard1.setOnClickListener(this);
        pCard2.setOnClickListener(this);
        pCard3.setOnClickListener(this);
        pCard4.setOnClickListener(this);
        pCard5.setOnClickListener(this);

        playedCard1 = findViewById(R.id.playedCard1);

    }

    public void onClick(View v) {
        ImageView card = (ImageView)v;
        if(v.getId() == R.id.PCard1){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard2){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard3){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard4){
            playCard1(card);
        }
        if(v.getId() == R.id.PCard5){
            playCard1(card);
        }




        }

   public void playCard1(ImageView card){
        img = card.getDrawable();
        playedCard1.setImageDrawable(img);
        playedCard1.setVisibility(View.VISIBLE);

   }

    public void resetGame(View view) {
        setContentView(R.layout.activity_table);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Salut network = GameConfig.getInstance().getSalut();
        if(network != null) {
            if( network.isRunningAsHost) {
                try {
                    network.stopNetworkService(true);

                } catch (Exception e) {

                }
            } else {
                try {
                    network.unregisterClient(true);
                } catch (Exception e) {

                }
            }
        }
    }
}
