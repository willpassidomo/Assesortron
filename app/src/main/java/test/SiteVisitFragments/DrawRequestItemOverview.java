package test.SiteVisitFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import test.assesortron5.R;
import test.objects.DrawRequest;
import test.objects.DrawRequestItem;

/**
 * Created by otf on 7/26/15.
 */
public class DrawRequestItemOverview extends Fragment {
    ListView listView;
    DrawRequestItemOverviewListner parentListener;
    DrawRequest drawRequest;

    public DrawRequestItemOverview() {}

    public static DrawRequestItemOverview newInstance(DrawRequestItemOverviewListner parentListener, DrawRequest drawRequest) {
        DrawRequestItemOverview drawRequestItemOverview = new DrawRequestItemOverview();
        drawRequestItemOverview.setParentListener(parentListener);
        drawRequestItemOverview.setDrawRequest(drawRequest);

        return drawRequestItemOverview;
    }

    private void setParentListener(DrawRequestItemOverviewListner parentListener) {
        this.parentListener = parentListener;
    }

    private void setDrawRequest(DrawRequest drawRequest) {
        if (drawRequest != null) {
            this.drawRequest = drawRequest;
        } else {
            this.drawRequest = new DrawRequest();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);

        View view = inflater.inflate(R.layout.fragment_draw_request_items_overview, null);
        listView = (ListView) view.findViewById(R.id.draw_request_manage_item_list);
        listView.setAdapter(new DrawRequestItemManagerListAdapter(getActivity(),
                DrawRequestItem.DRAW_REQUEST_ITEM_TYPES,
                drawRequest.getDrawRequestItemLists()));
        return view;
    }

    public class DrawRequestItemManagerListAdapter extends BaseAdapter {
        Context context;
        Map<String, List<DrawRequestItem>> items;
        String[] categories;


        public DrawRequestItemManagerListAdapter(Context context, String[] categories, Map<String, List<DrawRequestItem>> items) {
            this.context = context;
            this.items = items;
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public Object getItem(int i) {
            return categories[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_draw_request_item_categories, null);
            }
            LinearLayout viewItemsAction = (LinearLayout) view.findViewById(R.id.list_draw_items_view_list);
            Button addItem = (Button) view.findViewById(R.id.list_draw_item_display_add);
            TextView name = (TextView) view.findViewById(R.id.list_draw_item_display_type);
            TextView count = (TextView) view.findViewById(R.id.list_draw_item_display_count);
            TextView total = (TextView) view.findViewById(R.id.list_draw_item_display_total);

            final String NAME = DrawRequestItem.getTitle(categories[i]);
            List<DrawRequestItem> ITEMS = items.get(NAME);
            if (ITEMS == null) {
                ITEMS = new ArrayList<>();
            }


            double itotal = 0;
            for (DrawRequestItem dri : ITEMS) {
                itotal += dri.getAmount();
            }

            name.setText(NAME);
            count.setText(ITEMS.size() + "");
            total.setText(itotal + "");

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItem(categories[i]);
                }
            });

            viewItemsAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewItemsListener(categories[i]);
                }
            });

            return view;
        }
    }

    private void addItem(String type) {
        parentListener.addItem(type);
    }
    private void viewItemsListener(String type) {
        parentListener.listType(type);
    }

    public interface DrawRequestItemOverviewListner{
        public void addItem(String type);
        public void listType(String type);
    }
}
