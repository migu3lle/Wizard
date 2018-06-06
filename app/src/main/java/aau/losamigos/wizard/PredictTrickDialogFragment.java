package aau.losamigos.wizard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

/**
 * Created by gunmic on 23.05.18.
 * Alert Dialog with NumberPicker to predict tricks per Round
 */

public class PredictTrickDialogFragment extends DialogFragment{

    private NumberPicker.OnValueChangeListener valueChangeListener;
    private int notAllowed;
    private String[] numbersArray;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Set up String array with allowed tricks to play (e.g. all except the notAllowed one)
        notAllowed = this.getArguments().getInt("forbiddenTricks");
        Log.d("WizardApp", "DialogFragment: Number of forbiddenTricks: " + notAllowed);
        if(notAllowed == -1){
            //All numbers of tricks are allowed: 0, 1, .... 20
            numbersArray = new String[21];
        }
        else{
            //All numbers of tricks are allowed except one number
            numbersArray = new String[20];
        }
        for (int i = 0, j = 0; j < numbersArray.length; i++) {
            if(i == notAllowed)
                continue;
            numbersArray[j] = String.valueOf(i);
            Log.d("WizardApp", "DialogFragment: numbersArray[" + j + "] = " + numbersArray[j]);
            j++;
        }

        for (int i = 0; i < numbersArray.length; i++) {
            Log.d("WizardApp", "DialogFragment: numbersArray[" + i + "] = " + numbersArray[i]);
        }

        final NumberPicker numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(numbersArray.length-1);
        numberPicker.setDisplayedValues(numbersArray);
        numberPicker.setWrapSelectorWheel(false);

        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.AlertPredictRoundNo)
                .setMessage(R.string.AlertPredictText)
                .setPositiveButton(R.string.AlertPredictSetButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        valueChangeListener.onValueChange(numberPicker, numberPicker.getValue(), numberPicker.getValue());
                    }
                });
        // Create the AlertDialog object and return it

        builder.setView(numberPicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
