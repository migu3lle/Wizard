package aau.losamigos.wizard.elements;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Created by flo on 12.06.2018.
 */

public class PlayerRoundStateParser  {
    public static PlayerRoundState parse(String obj) {
        String[] splits =  obj.split(";");
        int actual = Integer.parseInt(splits[0]);
        int estimated = Integer.parseInt(splits[1]);
        int points = Integer.parseInt(splits[2]);
        return new PlayerRoundState(actual, estimated, points);
    }

    public static String convert(PlayerRoundState obj) {
        return obj.getActualStiches() + ";" + obj.getCalledStiches() + ";" + obj.getPoints();
    }
}
