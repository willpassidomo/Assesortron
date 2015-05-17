package test.assesortron3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import test.adapters.DrawRequestItemListAdapter;
import test.objects.DrawRequestItem;


public class DrawRequestItemList extends Fragment {

    List<DrawRequestItem> drawRequestItems;

    public static DrawRequestItemList newInstance(List<DrawRequestItem> drawRequestItems) {
        DrawRequestItemList fragment = new DrawRequestItemList();
        fragment.setDrawRequestItems(drawRequestItems);
        return fragment;
    }

    public DrawRequestItemList() {

    }

    public void setDrawRequestItems(List<DrawRequestItem> drawRequestItems) {
        this.drawRequestItems = drawRequestItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draw_request_item_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            setList();
        }
    }

    private void setList() {
        ListView listView = (ListView) getView().findViewById(R.id.draw_request_item_list);
        DrawRequestItemListAdapter drila = new DrawRequestItemListAdapter(getActivity(), drawRequestItems);
        listView.setAdapter(drila);
    }

}