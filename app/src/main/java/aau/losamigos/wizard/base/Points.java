package aau.losamigos.wizard.base;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by zork1_000 on 14.05.2018.
 */

public class Points {

    private int scores;
    private int stitch;

    public static int getScore(Player player){
        int score = 0;

        if (player.getCalledStiches()== player.getActualStiches()){
            score = 20 + (player.getCalledStiches()*10);
        }else{
            if ((player.getCalledStiches()- player.getActualStiches())<0){
                score = 10*(player.getCalledStiches()-player.getActualStiches());
            }else{
                score = 10*(player.getActualStiches()-player.getCalledStiches());
            }
        }
        return score;
    }


}
