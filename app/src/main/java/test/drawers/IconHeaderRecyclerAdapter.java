package test.drawers;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public abstract class IconHeaderRecyclerAdapter extends RecyclerView.Adapter<IconHeaderRecyclerAdapter.Holder> {
    public static final int TYPE_FRAGMENT = 1;
    public static final int TYPE_INTENT = 2;
    public static final int TYPE_LISTENER = 3;
    public static final int TYPE_FRAGMENT_SUPPORT = 4;
    private View CURRENT_CLICKED;

    ListItem[] items;
    DrawerActivtyListener parent;
    int HEADER = 0;
    int ITEM = 1;


    public IconHeaderRecyclerAdapter(DrawerActivtyListener parent) {
        this.parent = parent;
    }

    public void displayFirst() {
        for (ListItem item: items) {
            if (item.type == TYPE_FRAGMENT) {
                parent.displayFragment(item.fragement, item.title);
                parent.setTitle(item.title);
                break;
            }
        }
    }

    public void display(String name) {
        for (ListItem item: items) {
            if (item.title.equals(name)) {
                parent.displayFragment(item.fragement, item.title);
                parent.setTitle(item.title);
                break;
            }
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (i == HEADER) {
            View headerView = inflateHeader(inflater, parent);
            return new Holder(headerView, i);
        } else {
            View view = inflater.inflate(R.layout.drawer_list_item, parent, false);
            return new Holder(view, i);
        }
    }

    abstract public View inflateHeader(LayoutInflater inflater, ViewGroup parent);

    @Override
    public void onBindViewHolder(Holder viewHolder, int i) {
        if (viewHolder.VIEW_TYPE == ITEM) {
            ListItem item = items[i - 1];
            viewHolder.imageView.setImageDrawable(item.drawable);
            viewHolder.textView.setText(item.title);
            switch (item.type) {
                case TYPE_FRAGMENT:
                    LinearLayout self = viewHolder.background;
                    viewHolder.background.setOnClickListener(itemClickListener(self, item.fragement, item.title));
                    break;
                case TYPE_LISTENER:
                    LinearLayout self1 = viewHolder.background;
                    viewHolder.background.setOnClickListener(itemClickListener(self1, item.listener));
                    break;
                case TYPE_FRAGMENT_SUPPORT:
                    LinearLayout self2 = viewHolder.background;
                    viewHolder.background.setOnClickListener(itemClickListener(self2, item.fragmentSupported, item.title));
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

    public ListItem newItem(Context context, String title, int drawable, Fragment fragment) {
        return new ListItem(title, context.getResources().getDrawable(drawable), fragment);
    }

    public ListItem newItem(Context context, String title, int drawable, android.support.v4.app.Fragment fragment) {
        return new ListItem(title, context.getResources().getDrawable(drawable), fragment);
    }

    public ListItem newItem(Context context, String title, int drawable, Intent intent) {
        return new ListItem(title, context.getResources().getDrawable(drawable), intent);
    }

    public ListItem newItem(Context context, String title, int drawable, View.OnClickListener listener) {
        return new ListItem(title, context.getResources().getDrawable(drawable), listener);
    }

    public void setItems(ListItem[] items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.length + 1;
    }

    public boolean isHeader(int i) {
        return i == 0;
    }

    private View.OnClickListener itemClickListener(final LinearLayout background, final Fragment fragment, final String title) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                setClicked(view);
                parent.displayFragment(fragment, title);
                parent.setTitle(title);
            }
        };
        return listener;
    }

    private View.OnClickListener itemClickListener(final LinearLayout background, final android.support.v4.app.Fragment fragment, final String title) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                setClicked(view);
                parent.displayFragment(fragment, title);
                parent.setTitle(title);
            }
        };
        return listener;
    }

    private View.OnClickListener itemClickListener(final LinearLayout background, final View.OnClickListener listener) {
        View.OnClickListener wrapperListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                setClicked(view);
                listener.onClick(view);
            }
        };
        return wrapperListener;
    }

    private void setClicked(View background) {
        if (CURRENT_CLICKED != null) {
            CURRENT_CLICKED.setSelected(false);
        }
        CURRENT_CLICKED = background;
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
                this.background = (LinearLayout) view.findViewById(R.id.drawer_list_background);
                this.textView = (TextView) view.findViewById(R.id.drawer_list_text);
                this.imageView = (ImageView) view.findViewById(R.id.drawer_list_image);
            }
        }
    }

    public class ListItem {
        int type;
        Drawable drawable;
        String title;
        Fragment fragement;
        android.support.v4.app.Fragment fragmentSupported;
        Intent intent;
        View.OnClickListener listener;

        public ListItem(String title, Drawable drawable, Fragment fragement) {
            type = TYPE_FRAGMENT;
            this.title = title;
            this.drawable = drawable;
            this.fragement = fragement;
        }

        public ListItem(String title, Drawable drawable, android.support.v4.app.Fragment fragment) {
            type = TYPE_FRAGMENT_SUPPORT;
            this.title = title;
            this.drawable = drawable;
            this.fragmentSupported = fragment;
        }

        public ListItem(String title, Drawable drawable, Intent intent) {
            type = TYPE_INTENT;
            this.title = title;
            this.drawable = drawable;
            this.intent = intent;
        }

        public ListItem(String title, Drawable drawable, View.OnClickListener listener) {
            type = TYPE_LISTENER;
            this.title = title;
            this.drawable = drawable;
            this.listener = listener;
        }
    }
}
