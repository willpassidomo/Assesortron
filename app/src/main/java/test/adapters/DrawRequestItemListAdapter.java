package test.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

import test.assesortron3.MakeDrawRequestField;
import test.assesortron3.R;
import test.objects.DrawRequestItem;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by willpassidomo on 3/18/15.
 */
public class DrawRequestItemListAdapter implements ListAdapter {
    private Context context;
    private List<DrawRequestItem> drawRequestItems;


    public DrawRequestItemListAdapter(Context context, List<DrawRequestItem> drawRequestItems) {
        this.context = context;
        this.drawRequestItems = drawRequestItems;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        if (drawRequestItems.isEmpty()) {
            return 1;
        } else {
            return drawRequestItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return drawRequestItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_draw_request_item_list, null);
        }

        TextView subcontractor = (TextView) view.findViewById(R.id.draw_request_item_list_subcontractor);
        TextView amount = (TextView) view.findViewById(R.id.draw_request_item_list_amount);

        Button deleteButton = (Button) view.findViewById(R.id.list_draw_request_delete_button);
        Button editButton = (Button) view.findViewById(R.id.list_draw_request_edit_button);

        final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.list_draw_request_switcher);

        if (drawRequestItems.isEmpty()) {
            subcontractor.setText("no items");
            amount.setText("0");
        } else {
            subcontractor.setText(drawRequestItems.get(position).getSubContractor());
            amount.setText(drawRequestItems.get(position).getAmount() + "");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vs.showNext();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawRequestItem co = drawRequestItems.get(position);
                    Storage.deleteDrawRequestItem(context, co.getId());
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawRequestItem co = drawRequestItems.get(position);

                    Intent intent = new Intent(context, MakeDrawRequestField.class);
                    intent.putExtra(Constants.NEW_OR_EDIT, Constants.EDIT);
                    intent.putExtra(Constants.DRAW_REQUEST_ITEM_ID, co.getId());
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
