package test.drawers;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import test.assesortron5.R;

/**
 * Created by otf on 7/15/15.
 */
public class IconHeaderRecyclerAdapter extends RecyclerView.Adapter<IconHeaderRecyclerAdapter.Holder> {
    public static final int TYPE_FRAGMENT = 1;
    public static final int TYPE_INTENT = 2;
    public static final int TYPE_LISTENER = 3;

    IconHeaderObject[] items;
    DrawerActivtyListener parent;
    int headerView;
    int HEADER = 0;
    int ITEM = 1;


    public IconHeaderRecyclerAdapter(int headerView, DrawerActivtyListener parent) {
        this.headerView = headerView;
        this.parent = parent;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (i == HEADER) {
            View view =inflater.inflate(headerView, parent, false);
            return new Holder(view, i);
        } else {
            View view = inflater.inflate(R.layout.drawer_list_item, parent, false);
            return new Holder(view, i);
        }
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int i) {
        if (viewHolder.VIEW_TYPE == ITEM) {
            IconHeaderObject item = items[i - 1];
            viewHolder.imageView.setImageDrawable(item.drawable);
            viewHolder.textView.setText(item.title);
            switch (item.type) {
                case TYPE_FRAGMENT:
                    viewHolder.background.setOnClickListener(itemClickListener(item.fragement));
                    break;
                case TYPE_INTENT:
                    viewHolder.background.setOnClickListener(itemClickListener(item.intent));
                    break;
                case TYPE_LISTENER:
                    viewHolder.background.setOnClickListener(item.listener);
                    break;
            }
        }
        if (viewHolder.VIEW_TYPE == HEADER) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return HEADER;
        } else {
            return ITEM;
        }
    }

    public IconHeaderObject newItem(Context context, String title, int drawable, Fragment fragment) {
        return new IconHeaderObject(title, context.getResources().getDrawable(drawable), fragment);
    }

    public IconHeaderObject newItem(Context context, String title, int drawable, Intent intent) {
        return new IconHeaderObject(title, context.getResources().getDrawable(drawable), intent);
    }

    public IconHeaderObject newItem(Context context, String title, int drawable, View.OnClickListener listener) {
        return new IconHeaderObject(title, context.getResources().getDrawable(drawable), listener);
    }

    public void setItems(IconHeaderObject[] items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.length + 1;
    }

    public boolean isHeader(int i) {
        return i == 0;
    }

    private View.OnClickListener itemClickListener(final Fragment fragment) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.displayFragment(fragment);
            }
        };
        return listener;
    }

    private View.OnClickListener itemClickListener(final Intent intent) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.startIntent(intent);
            }
        };
        return listener;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout background;
        View view;
        int VIEW_TYPE;

        public Holder(View view, int position) {
            super(view);
            if (isHeader(position)) {
                VIEW_TYPE = HEADER;
            } else {
                VIEW_TYPE = ITEM;
                this.view = view;
                this.background = (LinearLayout)view.findViewById(R.id.drawer_list_background);
                this.textView = (TextView) view.findViewById(R.id.drawer_list_text);
                this.imageView = (ImageView) view.findViewById(R.id.drawer_list_image);
            }
        }


    }

    public class IconHeaderObject {
        int type;
        Drawable drawable;
        String title;
        Fragment fragement;
        Intent intent;
        View.OnClickListener listener;

        public IconHeaderObject(String title, Drawable drawable, Fragment fragement) {
            type = TYPE_FRAGMENT;
            this.title = title;
            this.drawable = drawable;
            this.fragement = fragement;
        }

        public IconHeaderObject(String title, Drawable drawable, Intent intent) {
            type = TYPE_INTENT;
            this.title = title;
            this.drawable = drawable;
            this.intent = intent;
        }

        public IconHeaderObject(String title, Drawable drawable, View.OnClickListener listener) {
            type = TYPE_LISTENER;
            this.title = title;
            this.drawable = drawable;
            this.listener = listener;
        }
    }
}