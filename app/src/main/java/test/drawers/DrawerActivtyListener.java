package test.drawers;

import android.app.Fragment;
import android.content.Intent;
import android.view.View;

/**
 * Created by otf on 7/15/15.
 */
public interface DrawerActivtyListener {
    public void displayFragment(Fragment fragment);
    public void startIntent(Intent intent);
}