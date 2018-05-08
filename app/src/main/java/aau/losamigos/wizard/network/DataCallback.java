package aau.losamigos.wizard.network;

import android.util.Log;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutDataCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aau.losamigos.wizard.base.Message;

/**
 * Created by gunmic on 02.05.18.
 */

public class DataCallback implements SalutDataCallback {

    //a list of callbacks that is executed only once after they have been added
    private List<ICallbackAction> fireOnceCallBacks;

    //a list of callbacks that is executed every time a new message arrives
    private List<ICallbackAction> callBacks;

    public DataCallback() {
        fireOnceCallBacks = new ArrayList<>();
        callBacks = new ArrayList<>();
    }

    /**
     * a method to add callback functions that are executed every time a new message arrives
     * @param callback
     */
    public void addCallBackAction(ICallbackAction callback) {
        callBacks.add(callback);
    }

    /**
     * a method to adda call back that is only called once
     * @param callback
     */
    public void addFireOnceCallBackAction(ICallbackAction callback) {
        fireOnceCallBacks.add(callback);
    }


    @Override
    public void onDataReceived(Object message) {

        try
        {
            //Message test = (Message)message;
            Message newMessage = LoganSquare.parse((String) message, Message.class);
            //Log.d("WizardApp - Message:", newMessage.description);  //See you on the other side!


            for (ICallbackAction callback: callBacks) {
                callback.execute(newMessage);
            }

            for (ICallbackAction callback: fireOnceCallBacks) {
                callback.execute(newMessage);
                fireOnceCallBacks.remove(callback);
            }
        }
        catch (IOException ex)
        {
            Log.e("WizardApp", "Failed to parse network data.");
        }
    }
}
