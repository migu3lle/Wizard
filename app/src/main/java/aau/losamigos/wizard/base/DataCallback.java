package aau.losamigos.wizard.base;

import android.util.Log;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutDataCallback;

import java.io.IOException;

/**
 * Created by gunmic on 02.05.18.
 */

public class DataCallback implements SalutDataCallback {
    @Override
    public void onDataReceived(Object message) {

        try
        {
            //Message test = (Message)message;
            Message newMessage = LoganSquare.parse((String) message, Message.class);
            Log.d("WizardApp", newMessage.description);  //See you on the other side!
            //Log.d("WizardApp", test.description);  //See you on the other side!
            //Do other stuff with data.
        }
        catch (IOException ex)
        {
            Log.e("WizardApp", "Failed to parse network data.");
        }
    }
}
