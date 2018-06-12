package aau.losamigos.wizard.base;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aau.losamigos.wizard.elements.PlayerRoundState;
import aau.losamigos.wizard.elements.PlayerRoundStateParser;
import aau.losamigos.wizard.rules.Actions;

/**
 * Created by flo on 12.06.2018.
 */

public class LoganSquareTest {

    @Test
    public void TestSerializeDeserializeMessageSimple() throws IOException {
        Message message = new Message();
        message.action = Actions.INITIAL_CARD_GIVING;

        String serialized = LoganSquare.serialize(message);

        Message deserialized = LoganSquare.parse(serialized, Message.class);

        Assert.assertEquals(deserialized.action, message.action);
    }

    @Test
    public void TestSerializeDeserializeMessageComplex() throws IOException {

        Message message = new Message();
        Map<String, List<String>> playerStates = new HashMap<>();
        List<String> states = new ArrayList<>();
        states.add(PlayerRoundStateParser.convert(new PlayerRoundState(1,1,1)));
        playerStates.put("asdf", states);
        message.playerStates = playerStates;

        String serialized = LoganSquare.serialize(message);

        Message deserialized = LoganSquare.parse(serialized, Message.class);

        Assert.assertNotNull(deserialized.playerStates);
    }
}
