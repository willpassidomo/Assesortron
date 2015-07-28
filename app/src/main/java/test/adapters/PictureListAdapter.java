package test.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import test.assesortron5.R;
import test.persistence.Storage;


/**
 * Created by willpassidomo on 1/21/15.
 */
public class PictureListAdapter implements ListAdapter {
    Context context;
    List<String> pictures;

    public PictureListAdapter(Context context, List<String> pictures) {
        this.pictures = pictures;
        this.context = context;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object getItem(int i) {
        return pictures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return new Long(i);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_walkthrough_pictures, null);
        }

        TextView date = (TextView) view.findViewById(R.id.date_picture_taken);
        ImageView imageView = (ImageView) view.findViewById(R.id.walkthrough_picture);

        date.setText(pictures.size()+"");
        imageView.setImageBitmap(Storage.getPictureByOwnerId(context, pictures.get(i)));
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return pictures.isEmpty();
    }
}
