package test.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import test.assesortron5.R;
import test.objects.DrawRequestItem;

/**
 * Created by otf on 7/15/15.
 */
public class DrawRequestItemManagerListAdapter extends BaseAdapter {
    Context context;
    Map<String, List<DrawRequestItem>> items;
    String[] categories;


    public DrawRequestItemManagerListAdapter (Context context, String[] categories, Map<String, List<DrawRequestItem>> items) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater =  (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_draw_request_item_categories, viewGroup);
        }
        LinearLayout viewItemsAction = (LinearLayout)view.findViewById(R.id.list_draw_items_view_list);
        Button addItem = (Button)view.findViewById(R.id.list_draw_item_display_add);
        TextView name = (TextView)view.findViewById(R.id.list_draw_item_display_type);
        TextView count = (TextView)view.findViewById(R.id.list_draw_item_display_count);
        TextView total = (TextView)view.findViewById(R.id.list_draw_item_display_total);

        final String NAME = categories[i];
        List<DrawRequestItem> ITEMS = items.get(NAME);


        double itotal = 0;
        for (DrawRequestItem dri: ITEMS) {
           itotal += dri.getAmount();
        }

        name.setText(NAME);
        count.setText(ITEMS.size());
        total.setText(itotal + "");

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "TODO add new Item" + NAME, Toast.LENGTH_SHORT).show();
            }
        });

        viewItemsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "TODO display list of  items for" + NAME, Toast.LENGTH_SHORT).show();
            }
        });









        return view;
    }
}
