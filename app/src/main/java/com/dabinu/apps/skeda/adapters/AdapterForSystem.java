package com.dabinu.apps.skeda.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.activities.Profiles;
import com.dabinu.apps.skeda.templates.SystemTemplate;

import java.util.List;


public class AdapterForSystem extends BaseAdapter{

    List<SystemTemplate> list;
    Context context;
    LayoutInflater inflater;

    public AdapterForSystem(List<SystemTemplate> list, Context context){
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = inflater.inflate(R.layout.templatesystem, null);

        final SystemTemplate temp = list.get(position);

        TextView text = convertView.findViewById(R.id.textToBeDisplayed);
        text.setText(temp.getName());
        ImageView image = convertView.findViewById(R.id.imageToBeDisplayed);
        image.setBackgroundResource(temp.getImage());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(temp.getName().trim()){
                    case "Profiles":
                        context.startActivity(new Intent(context, Profiles.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                }
            }
        });

        return convertView;
    }
}
