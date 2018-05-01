package aau.losamigos.wizard;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TableActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getSupportActionBar().hide();

    }

   public void playCard1(View card){
        card.setX(850);
        card.setY(500);
   }
   public void playCard2(View card){
       card.setX(700);
       card.setY(350);
   }
    public void playCard3(View card){
        card.setX(800);
        card.setY(300);
    }
    public void playCard4(View card){
        card.setX(900);
        card.setY(250);
    }
    public void playCard5(View card){
        card.setX(1000);
        card.setY(300);
    }
    public void playCard6(View card){
        card.setX(1100);
        card.setY(350);
    }
}
