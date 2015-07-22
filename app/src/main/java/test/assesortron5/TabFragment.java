package test.assesortron5;

import android.support.v4.app.Fragment;

/**
 * Created by willpassidomo on 2/3/15.
 */
public interface TabFragment {

        public Fragment getFragment();

        public String getTabName();

        public void setFields(Object... object);

        public void getValues();

//        public Fragment newInstance();
}
