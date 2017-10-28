package com.dabinu.apps.skeda;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


class AdapterForExpandableListView extends BaseExpandableListAdapter{


    private Context context;
    private ArrayList<Group> groups;


    AdapterForExpandableListView(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View theViewWeWant, ViewGroup parent) {

        Child child = (Child) getChild(groupPosition, childPosition);
        if (theViewWeWant == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            theViewWeWant = infalInflater.inflate(R.layout.itemsguy, null);
        }
        TextView tv = (TextView) theViewWeWant.findViewById(R.id.textItem);
        ImageView iv = (ImageView) theViewWeWant.findViewById(R.id.imageItem);


        tv.setText(child.getName());
        iv.setImageResource(child.getImage());

        final Intent intent = new Intent(context, AllNetworks.class);
        final Intent intentSms = new Intent(context, Esemes.class);
        final Intent intentCalls = new Intent(context, CallsActivity.class);


        theViewWeWant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupPosition == 1){
                    switch(childPosition){
                        case 0:
                            intent.putExtra("NAME", "WiFi");
                            context.startActivity(intent);
                            break;
                        case 1:
                            intent.putExtra("NAME", "Bluetooth");
                            context.startActivity(intent);
                            break;
                        case 2:
                            intent.putExtra("NAME", "Flight mode");
                            context.startActivity(intent);
                            break;
                        case 3:
                            intent.putExtra("NAME", "Hotspot");
                            context.startActivity(intent);
                            break;
                        case 4:
                            intent.putExtra("NAME", "Data Conn.");
                            context.startActivity(intent);
                            break;

                    }
                }
                else if(groupPosition == 0){
                        switch(childPosition){
                            case 1:
                                context.startActivity(intentSms);
                                break;
                            case 0:
                                context.startActivity(intentCalls);
                                break;
                        }
                }
            }
        });


        return theViewWeWant;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition){
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount(){
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.groupsguy, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.headerOfList);
        tv.setText(group.getName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}