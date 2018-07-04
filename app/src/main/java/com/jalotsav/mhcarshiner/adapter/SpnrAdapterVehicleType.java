package com.jalotsav.mhcarshiner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jalotsav.mhcarshiner.models.MdlVehicleType;

import java.util.ArrayList;

/**
 * Created by Jalotsav on 6/29/2018.
 */
public class SpnrAdapterVehicleType extends ArrayAdapter<MdlVehicleType> {

    private ArrayList<MdlVehicleType> arrylstVehicleType;

    public SpnrAdapterVehicleType(@NonNull Context context, int resource, @NonNull ArrayList<MdlVehicleType> objects) {
        super(context, resource, objects);
        arrylstVehicleType = new ArrayList<>();
        arrylstVehicleType.addAll(objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView tvTitle = (TextView) super.getDropDownView(position, convertView, parent);
        tvTitle.setText(arrylstVehicleType.get(position).getVehicleType());
        return tvTitle;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView tvTitle = (TextView) super.getView(position, convertView, parent);
        tvTitle.setText(arrylstVehicleType.get(position).getVehicleType());
        return tvTitle;
    }
}
