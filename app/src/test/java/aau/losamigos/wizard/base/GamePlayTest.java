package aau.losamigos.wizard.base;

import com.peak.salut.SalutDevice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GamePlayTest.FalseNumberOfPlayersTests.class,GamePlayTest.RightNumberOfPlayerTests.class})
public class GamePlayTest {


    /*
    New GamePlay class with PlayerArray = Null
     */
    public static class FalseNumberOfPlayersTests{

        @Test(expected = NullPointerException.class)
        public void playerArrayIsNull(){
            GamePlay gp = new GamePlay(null);
        }

    }
    public static class RightNumberOfPlayerTests{
        @Before
        public void setUp(){
            GamePlay gp = new GamePlay(null);
        }

        @Test(expected = NullPointerException.class)
        public void playerArrayIsNull(){
            GamePlay gp = new GamePlay(null);
        }

    }



}
