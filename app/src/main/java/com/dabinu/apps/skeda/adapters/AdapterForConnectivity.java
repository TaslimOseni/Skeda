package com.dabinu.apps.skeda.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.templates.ConnectivityRally;
import java.util.List;



public class AdapterForConnectivity extends RecyclerView.Adapter<AdapterForConnectivity.ViewHolder>{


    private List<ConnectivityRally> list;


    public AdapterForConnectivity(List<ConnectivityRally> list) {
        this.list = list;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsguy, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConnectivityRally item = list.get(position);

        holder.textView.setText(item.getName());
        holder.imageView.setImageResource(item.getImage());
    }

    @Override
    public int getItemCount(){
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        ViewHolder(View itemView){

            super(itemView);

            textView = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.imageItem);
        }
    }
}
