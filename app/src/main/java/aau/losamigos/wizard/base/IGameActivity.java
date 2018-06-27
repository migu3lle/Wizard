package aau.losamigos.wizard.base;


import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by flo on 12.06.2018.
 * clears table
 */

public interface IGameActivity {
    void clearTable();
    void sendCardsToDevice(Player player);
    void setCardsForHost();
    void hostStiches(int newForbiddenTricks);
    void hostPickCard(List<Integer> cards);
    void setInitialPrediction(boolean predict);
    void setMiddleCards(List<AbstractCard> playedCards);
}
