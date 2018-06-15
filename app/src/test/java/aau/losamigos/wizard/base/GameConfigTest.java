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

import java.util.ArrayList;
import java.util.HashMap;

import aau.losamigos.wizard.elements.Player;

/**
 * Created by gunmic on 15.06.18.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ GameConfigTest.EmptySingletonTests.class, GameConfigTest.SingletonTests.class, GameConfigTest.SetPlayersTests.class })
public class GameConfigTest {

    public static class EmptySingletonTests{
        GameConfig gcfg;

        /******************** SETUP ********************/
        @Before
        public void setUp(){
            //Generate GameConfig object (Singleton)
            gcfg = GameConfig.getInstance();
        }
        @After
        public void reset(){
            //Reset GameConfig object to null (Singleton)
            gcfg.reset();
        }
        /******************** TESTS ********************/
        @Test
        public void testSingletonReset(){
            gcfg.reset();
            Assert.assertNotEquals(gcfg, GameConfig.getInstance());
        }
        @Test
        public void testEmptySingletonGeneration(){
            gcfg = GameConfig.getInstance(1, 1, false, false);
            Assert.assertEquals(0, gcfg.getMinPlayer());
            Assert.assertEquals(0, gcfg.getMaxPlayer());
        }
        @Test
        public void testEmptySingletonGeneration2(){
            gcfg.setMinPlayer(1);
            gcfg.setMaxPlayer(1);
            Assert.assertEquals(1, gcfg.getMinPlayer());
            Assert.assertEquals(1, gcfg.getMaxPlayer());
        }
    }

    public static class SingletonTests {
        GameConfig gcfg;

        /******************** SETUP ********************/
        @Before
        public void setUp(){
            //Generate GameConfig object (Singleton)
            gcfg = GameConfig.getInstance(1, 1, false, false);
        }
        @After
        public void reset(){
            //Reset GameConfig object to null (Singleton)
            gcfg.reset();
        }
        /******************** TESTS ********************/
        @Test
        public void testSingletonReset(){
            gcfg.reset();
            Assert.assertNotEquals(gcfg, GameConfig.getInstance());
        }
        @Test
        public void testSingletonReset2(){
            gcfg.reset();
            gcfg = GameConfig.getInstance();
            Assert.assertEquals(gcfg, GameConfig.getInstance());
        }
        @Test
        public void testSingletonWithValues(){
            Assert.assertEquals(1, gcfg.getMinPlayer());
            Assert.assertEquals(1, gcfg.getMaxPlayer());
        }
    }

    public static class SetPlayersTests{
        GameConfig gcfg;
        ArrayList<SalutDevice> players0;
        ArrayList<SalutDevice> players1;
        ArrayList<SalutDevice> players2;
        SalutDevice device1;
        SalutDevice device2;
        HashMap<Player, SalutDevice> playerDeviceMap;
        Player testPlayer;

        /******************** SETUP ********************/
        @Before
        public void setUp(){
            //Generate GameConfig object (Singleton)
            gcfg = GameConfig.getInstance(0, 1, false, false);
            //Generate Salut Device
            device1 = new SalutDevice();
            device1.deviceName = "device1";
            device2 = new SalutDevice();
            device2.deviceName = "device2";
            //Generate player lists
            players0 = new ArrayList<>();
            players1 = new ArrayList<>();
            players2 = new ArrayList<>();
            players1.add(device1);
            players2.add(device1);
            players2.add(device2);
            //Generate PlayerDeviceMap
            playerDeviceMap = new HashMap<>();
        }
        @Rule
        public ExpectedException exception = ExpectedException.none();

        @After
        public void reset(){
            //Reset GameConfig object to null (Singleton)
            gcfg.reset();
        }
        /******************** TESTS ********************/
        @Test(expected = IllegalStateException.class)
        public void testAddPlayersTwiceException(){
            gcfg.setPlayers(players1);
            gcfg.setPlayers(players1);
        }
        @Test(expected = IllegalStateException.class)
        public void testAddPlayersTwiceException2(){
            gcfg.setPlayers(players1);
            gcfg.setPlayers(players1);
        }
        @Test
        public void testAddPlayersTwiceExceptionMessage(){
            exception.expect(IllegalStateException.class);
            exception.expectMessage("GameConfig: Players may only be instantiated once.");
            gcfg.setPlayers(players1);
            gcfg.setPlayers(players1);
        }
        @Test
        public void testAddZeroPlayersValid(){
            Assert.assertFalse(gcfg.setPlayers(players0));
        }
        @Test
        public void testAddOnePlayerValid(){
            Assert.assertTrue(gcfg.setPlayers(players1));
        }
        @Test
        public void testAddOnePlayerValid2(){
            gcfg.setPlayers(players1);
            Assert.assertEquals(1, gcfg.getPlayers().length);
            Assert.assertEquals(device1, gcfg.getPlayers()[0].getSalutDevice());
        }
        @Test
        public void testAddTwoPlayersValid(){
            Assert.assertFalse(gcfg.setPlayers(players2));
        }
        @Test
        public void testAddTwoPlayersValid2(){
            gcfg.setPlayers(players2);
            Assert.assertNull(gcfg.getPlayers());
        }
        @Test
        public void testAddTwoPlayersValid3(){
            Assert.assertFalse(gcfg.setPlayers(players0));
            //In this case no Exception, just false to be returned
            Assert.assertFalse(gcfg.setPlayers(players2));
        }
        @Test
        public void testAddOnePlayerGetPlayerDeviceMapValid(){
            gcfg.setPlayers(players1);
            playerDeviceMap.put(gcfg.getPlayers()[0], device1);
            Assert.assertEquals(playerDeviceMap, gcfg.getPlayerDeviceMap());
        }
    }
}
