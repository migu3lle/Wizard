package aau.losamigos.wizard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.peak.salut.SalutDevice;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by gunmic on 04.05.18.
 */

public class SalutListViewAdapter extends ArrayAdapter<SalutDevice> {
    private final Context context;
    private final ArrayList<SalutDevice> values;
    private final int resource;

    public SalutListViewAdapter(Context context, int resource, ArrayList<SalutDevice> values){
        super(context, resource, values);
        this.context = context;
        this.resource = resource;
        this.values = values;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
        SalutDevice salutDevice = values.get(position);
        textView.setText(salutDevice.readableName);

        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));



        return rowView;
    }
}
