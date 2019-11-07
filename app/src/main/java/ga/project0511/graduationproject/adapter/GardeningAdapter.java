package ga.project0511.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ga.project0511.graduationproject.R;
import ga.project0511.graduationproject.datatype.Gardening;

public class GardeningAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Gardening> sample;

    public GardeningAdapter(Context context, ArrayList<Gardening> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Gardening getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.activity_listing_plants_listview, null);

        TextView name_activity = view.findViewById(R.id.listing_plants_listView_activityName);
        TextView date_created = view.findViewById(R.id.listing_plants_listView_createdAt);
        TextView id_manager = view.findViewById(R.id.listing_plants_listView_managerID);

        name_activity.setText("활동명: "+sample.get(position).getActivityName());
        date_created.setText("시작 일시: "+sample.get(position).getCreatedAt());
        id_manager.setText("팀장: "+sample.get(position).getManagerID());

        return view;
    }
}
