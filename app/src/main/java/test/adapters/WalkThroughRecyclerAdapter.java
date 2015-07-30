package test.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.SimpleDateFormat;
import java.util.List;

import test.assesortron5.R;
import test.SiteVisitFragments.SiteWalkthrough;
import test.persistence.Constants;
import test.persistence.Storage;
import test.objects.WalkThrough;

/**
 * Created by willpassidomo on 1/27/15.
 */
public class WalkThroughRecyclerAdapter extends RecyclerView.Adapter<WalkThroughRecyclerAdapter.WalkThroughViewHolder> {
    Context context;
    List<WalkThrough> walkThroughs;
    String siteVisitId;

    public WalkThroughRecyclerAdapter(Context context, List<WalkThrough> walkThroughs, String siteVisitId) {
        this.walkThroughs = walkThroughs;
        this.context = context;
        this.siteVisitId = siteVisitId;
    }



    @Override
    public WalkThroughViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_walk_throughs, parent, false);
        return new WalkThroughViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WalkThroughViewHolder holder, int position) {
        WalkThrough walkThrough = walkThroughs.get(position);
        holder.floor.setText(walkThrough.getFloor());
        holder.subContractor.setText(walkThrough.getTrade());
        holder.numPictures.setText(walkThrough.getPictures().size() + "");
        holder.date.setText(new SimpleDateFormat("MM/dd/yyyy").format(walkThrough.getDate()));

        AsyncTask<String, Void, Bitmap> getImage = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                return Storage.getPictureByOwnerId(null, strings[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 72, 72, false);
                holder.imageView.setImageBitmap(scaled);
            }
        };
        if (walkThrough.getPictures().size() > 0) {
            getImage.execute(walkThrough.getPicture(0));
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_idk));
        }

    }

    @Override
    public int getItemViewType(int i) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return walkThroughs.size();
    }

    public class WalkThroughViewHolder extends RecyclerView.ViewHolder {
        TextView floor;
        TextView subContractor;
        TextView numPictures;
        TextView date;
        ImageView imageView;

        public WalkThroughViewHolder(View view) {
            super(view);

            floor = (TextView) view.findViewById(R.id.walk_through_floor);
            subContractor = (TextView) view.findViewById(R.id.walk_through_contractor);
            numPictures = (TextView) view.findViewById(R.id.walk_through_num_pics);
            date = (TextView) view.findViewById(R.id.walk_through_list_date);
            imageView = (ImageView) view.findViewById(R.id.list_walkthrough_image);
        }
    }
}
