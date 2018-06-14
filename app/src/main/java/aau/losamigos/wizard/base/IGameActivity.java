package aau.losamigos.wizard.base;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by flo on 12.06.2018.
 * clears table
 */

public interface IGameActivity {
    void clearTable();
    void sendCardsToDevice(Player player);
    void setCardsForHost();
}
