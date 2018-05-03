package aau.losamigos.wizard.network;

/**
 * Created by flo on 03.05.2018.
 */

import aau.losamigos.wizard.base.Message;

/**
 * defines a callback action that has execute function that can be called from the salutDataCallback
 */
public interface ICallbackAction {
    void execute(Message message);
}
