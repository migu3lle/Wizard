package aau.losamigos.wizard;

import org.junit.Assert;
import org.junit.Test;

import aau.losamigos.wizard.elements.Player;
import aau.losamigos.wizard.elements.PlayerRoundState;
import aau.losamigos.wizard.elements.PlayerRoundStateParser;

/**
 * Created by flo on 25.06.2018.
 */

public class PlayerRoundStateParserTest {

    @Test
    public void TestSerlializePlayerRoundState() {
        PlayerRoundState prs = new PlayerRoundState(1, 2, 3);
        String serialized = PlayerRoundStateParser.convert(prs);
        Assert.assertEquals("1;2;3", serialized);
    }

    @Test
    public void TestDeserializePlayerRoundState() {
        String serilized = "1;2;3";
        PlayerRoundState prs = PlayerRoundStateParser.parse(serilized);
        Assert.assertEquals(1, prs.getActualStiches());
        Assert.assertEquals(2, prs.getCalledStiches());
        Assert.assertEquals(3, prs.getPoints());
    }

}
