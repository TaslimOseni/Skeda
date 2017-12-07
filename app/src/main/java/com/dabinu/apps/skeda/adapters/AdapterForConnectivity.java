package com.dabinu.apps.skeda.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.activities.ConnectivityOnClick;
import com.dabinu.apps.skeda.templates.ConnectivityTemplate;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class AdapterForConnectivity extends RecyclerView.Adapter<AdapterForConnectivity.ViewHolder>{


    private List<ConnectivityTemplate> list;
    private Context context;


    public AdapterForConnectivity(Context context, List<ConnectivityTemplate> list){
        this.list = list;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.templateconnectivity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final ConnectivityTemplate item = list.get(position);

        holder.textView.setText(item.getName());
        holder.imageView.setImageResource(item.getImage());
        holder.switcher.setChecked(item.isState());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ConnectivityOnClick.class).putExtra("NAME", item.getName()).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }



    @Override
    public int getItemCount(){
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        Switch switcher;
        RelativeLayout card;

        ViewHolder(View itemView){

            super(itemView);

            textView = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.imageItem);
            switcher = itemView.findViewById(R.id.switcher);
            card = itemView.findViewById(R.id.listItemOfList);

        }
    }
}
