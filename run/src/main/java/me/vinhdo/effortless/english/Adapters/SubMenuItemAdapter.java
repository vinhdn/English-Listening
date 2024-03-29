package me.vinhdo.effortless.english.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.vinhdo.effortless.english.Models.Models;
import me.vinhdo.effortless.english.R;

/**
 * Created by Vinh on 1/10/15.
 */
public class SubMenuItemAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Models> data;
    private ArrayList<Models> data2;
    public SubMenuItemAdapter(Context context, ArrayList<Models> data,ArrayList<Models> data2){
        this.mContext = context;
        this.data = data;
        this.data2 = data2;
    }

    @Override
    public int getCount() {
        if(data != null)
            return data.size();
        if(data2 != null)
            return data2.size();
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        if(data != null)
        return data.get(arg0);
        if(data2 != null)
            return data2.get(arg0);
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        if(data != null)
        return data.get(arg0).getId();
        if(data2 != null)
            return data2.get(arg0).getId();
        return 0;
    }

    @Override
    public View getView(int p, View view, ViewGroup vg) {
        MenuHolder holder;
        if(view == null){
            holder = new MenuHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_menu, null);
            holder.image = (ImageView)view.findViewById(R.id.menu_item_image);
            holder.title = (TextView)view.findViewById(R.id.menu_item_title);
            view.setTag(holder);
        }else{
            holder = (MenuHolder)view.getTag();
        }
        if(data != null) {
            holder.title.setText(data.get(p).getName());
            if (data.get(p).getId() > 0)
                holder.image.setImageResource(data.get(p).getImage());
        }
        if(data2 != null) {
            holder.title.setText(data2.get(p).getName());
            if(data2.get(p).getId() > 0)
                holder.image.setImageResource(data2.get(p).getImage());
        }
        return view;
    }

    public class MenuHolder{
        ImageView image;
        TextView title;
    }
}
