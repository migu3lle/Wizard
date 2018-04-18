package aau.losamigos.wizard.base;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.cards.FractionCard;
import aau.losamigos.wizard.elements.cards.JesterCard;
import aau.losamigos.wizard.elements.cards.WizardCard;
import aau.losamigos.wizard.types.Fractions;

/**
 * Created by Andreas.Mairitsch on 18.04.2018.
 */

public class HandTest {

    private Player p[] = new Player[3];

    private ArrayList<AbstractCard> table;
    private ArrayList<AbstractCard> handcards = new ArrayList<AbstractCard>();
    private Hand h[] = new Hand[3];

    @Before
    public void setUp() {
        p[0] = new Player(1,"Player1");


        handcards.add(new WizardCard(3));
        handcards.add(new JesterCard(4));
        handcards.add(new FractionCard(5,11,Fractions.Dwarf));
        handcards.add(new FractionCard(6,5,Fractions.Dwarf));
        handcards.add(new FractionCard(7,8,Fractions.Giant));
        handcards.add(new FractionCard(8,13,Fractions.Elb));
        h[0] = new Hand(handcards,p[0]);

       table = new ArrayList<AbstractCard>();
    }
    @After
    public void reset() {
       table = null;
    }

    @Test
    public void testWizardFirst() {
        table.add(new WizardCard(2));
        table.add(new FractionCard(1,10, Fractions.Dwarf));

        int count = 0;
        for (AbstractCard card: h[0].getAllowedCards(table)) {
            if(card.isAllowedToPlay()==true)
                count++;
        }
        Assert.assertEquals(6,count);
    }
    @Test
    public void testFractionFirst() {
        table.add(new FractionCard(1,10, Fractions.Dwarf));


        int count = 0;
        for (AbstractCard card: h[0].getAllowedCards(table)) {
            if(card.isAllowedToPlay()==true)
                count++;
        }
        Assert.assertEquals(4,count);
    }
    @Test
    public void testOnlyJester() {
        table.add(new JesterCard(20));

        int count = 0;
        for (AbstractCard card: h[0].getAllowedCards(table)) {
            if(card.isAllowedToPlay()==true)
                count++;
        }
        Assert.assertEquals(6,count);
    }
    @Test
    public void testJesterFraction() {
        table.add(new JesterCard(20));
        table.add(new FractionCard(1,10, Fractions.Giant));

        int count = 0;
        for (AbstractCard card: h[0].getAllowedCards(table)) {
            if(card.isAllowedToPlay()==true)
                count++;
        }
        Assert.assertEquals(3,count);
    }

    @Test
    public void testTableNull() {


        int count = 0;
        for (AbstractCard card: h[0].getAllowedCards(table)) {
            if(card.isAllowedToPlay()==true)
                count++;
        }
        Assert.assertEquals(6,count);
    }
}

/*
 public void setUp() {

        for (int i = 0; i < 3; i++) {
            ArrayList<AbstractCard> newHand =new ArrayList<AbstractCard>();
            for (int j = 0; j < 20; j++) {
                AbstractCard card = new AbstractCard(j+i*20);
                newHand.add(card);
            }
            p[i] = new Player(i+1,"Player"+(i+1));
            hand[i]= new Hand(newHand,p[i]);
        }
    }
 */