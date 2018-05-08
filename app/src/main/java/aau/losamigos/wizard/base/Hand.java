package aau.losamigos.wizard.base;

import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.cards.JesterCard;
import aau.losamigos.wizard.elements.cards.WizardCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by Andreas.Mairitsch on 16.04.2018.
 */

public class Hand {

    private List<AbstractCard> handCards;
    private Player handOwner;

    public Hand(List<AbstractCard> handCards) {
        this.handCards = handCards;
    }
    public Hand(List<AbstractCard> handCards, Player handOwner) {
        this.handCards = handCards;
        this.handOwner = handOwner;
    }

    public Player getHandOwner() {
        return handOwner;
    }

    public void setHandOwner(Player handOwner) {
        this.handOwner = handOwner;
    }


    public List<AbstractCard> getHandCards() {

        return handCards;
    }
    public void removeCard(AbstractCard card) {

        handCards.remove(card);
    }

    public List<AbstractCard> getAllowedCards(List<AbstractCard> table) {

        AbstractCard cardTyp = null; //gespielter Kartentyp
        Fractions fraction = null; //gespielte Fraktion

        /*
        Wenn noch keine Karte gespielt, alle Karten erlaubt
        sonst prüfe ausgespielten Kartentyp und Fraktion
         */
        if(table.isEmpty()){
            for (AbstractCard cardHand:handCards)
                cardHand.setAllowedToPlay(true);
        }
        else{
            /* Fraktionsermittlung

            Prüft table welche Fraktion gespielt werden darf
            Wenn Zaubererkarte sind alle Handkarten erlaubt
            Wenn Narr wird nächste Tablekarte geprüft, wenn keine weitere Karte vorhanden
            sind alle Karten erlaubt.
            Wenn Fraktion sind nur Zauberer, Narren und Karten der Fraktion erlaubt
ception arts            */
            for (AbstractCard cardTable:table) {
                cardTyp = cardTable;
                if(cardTyp instanceof WizardCard)
                    break;
                else if(cardTyp instanceof JesterCard){

                }else if(cardTyp instanceof FractionCard){
                    fraction = ((FractionCard) cardTyp).getFraction();
                    break;
                }
                else{
                    //TODO Exception werfen da kein richtiger Typ
                }
            }
            /*
            Aufgrund der Fraktioninfo werden die Handkarten jetzt einzeln auf allowedToPlay = true
            oder false gesetzt.
            Zauberer und Narren immer erlaubt, Fraktion wird eingeschränkt
            */
            if(cardTyp instanceof WizardCard){
                for (AbstractCard cardHand:handCards)
                    cardHand.setAllowedToPlay(true);
            }
            else if(cardTyp instanceof JesterCard){
                for (AbstractCard cardHand:handCards)
                    cardHand.setAllowedToPlay(true);

            }else if(cardTyp instanceof FractionCard){
                for (AbstractCard cardHand:handCards) {
                    if(cardHand instanceof WizardCard)
                        cardHand.setAllowedToPlay(true);
                    else if(cardHand instanceof JesterCard){
                        cardHand.setAllowedToPlay(true);
                    }else if(cardHand instanceof FractionCard){
                        if(((FractionCard) cardHand).getFraction().equals(fraction))
                            cardHand.setAllowedToPlay(true);
                        else
                            cardHand.setAllowedToPlay(false);
                    }
                }
            }
            else{
                //TODO Exception werfen da kein richtiger Typ
            }
        }
        return handCards;
    }

}
