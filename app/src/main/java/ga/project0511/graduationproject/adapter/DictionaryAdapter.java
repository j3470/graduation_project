package ga.project0511.graduationproject.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ga.project0511.graduationproject.R;
import ga.project0511.graduationproject.datatype.Plant;

public class DictionaryAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Plant> sample;
    HashMap<String, BitmapDrawable> bitmap;


    public DictionaryAdapter(Context context, ArrayList<Plant> data){
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        bitmap = new HashMap<String, BitmapDrawable>();
    }

    @Override
    public  int getCount() { return sample.size(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Plant getItem(int position) {
        return sample.get(position);
    }


    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.activity_dictionary_listview, null);

        TextView plant_name = view.findViewById(R.id.dictionary_listView_plantName);
        TextView plant_habitat = view.findViewById(R.id.dictionary_listView_habitat);
        TextView plant_difficulty = view.findViewById(R.id.dictionary_listView_difficulty);
        ImageView imageView = view.findViewById(R.id.dictionary_listView_image);

        plant_name.setText(" 식물명: "+sample.get(position).getName());
        plant_habitat.setText(" 원산지: "+sample.get(position).getInformation().getHabitat());
        plant_difficulty.setText(" 난이도: "+sample.get(position).getRequirement().getDifficulty());

        String id = sample.get(position).getId();

        BitmapDrawable bitmapDrawable = bitmap.get(id);
        //Bitmap origBm = bitmapDrawable.getBitmap();
        //Bitmap resizeBm = Bitmap.createScaledBitmap(, 80, 80, true);
        imageView.setImageDrawable(bitmapDrawable);

        return view;
    }

    public void setBitmapDrawable(String name, BitmapDrawable drawable) { bitmap.put(name, drawable); }

}
