package ga.project0511.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ga.project0511.graduationproject.R;
import ga.project0511.graduationproject.datatype.PlantComments;

public class CommentAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInfatler = null;
    ArrayList<PlantComments> comments;

    TextView author;
    TextView date;
    TextView content;

    public CommentAdapter(Context context, ArrayList<PlantComments> comments) {
        this.mContext = context;
        this.comments = comments;
        this.mLayoutInfatler = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() { return comments.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public PlantComments getItem(int position) { return comments.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View init
        View view = mLayoutInfatler.inflate(R.layout.comments_listitem_layout, null);

        author = view.findViewById(R.id.comments_listitem_layout_author_text);
        date = view.findViewById(R.id.comments_listitem_layout_date_text);
        content = view.findViewById(R.id.comments_listitem_layout_content);

        // Insert value into TextView

        if(!comments.isEmpty()) {
            author.setText(comments.get(position).getAuthor());
            date.setText(comments.get(position).getDate());
            content.setText(comments.get(position).getContent());
        }

        return view;
    }
}
