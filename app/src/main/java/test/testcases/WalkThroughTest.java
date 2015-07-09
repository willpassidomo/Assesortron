package test.testcases;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import test.assesortron3.R;
import test.assesortron3.SiteWalkthrough;
import test.objects.WalkThrough;
import test.persistence.Constants;

/**
 * Created by otf on 7/6/15.
 */
public class WalkThroughTest extends ActivityUnitTestCase<SiteWalkthrough> {
    Intent mLaunchIntent;
    Button lanuchNextButton = null;

    public WalkThroughTest(Class<SiteWalkthrough> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(), WalkThrough.class);
        startActivity(mLaunchIntent, null, null);

         lanuchNextButton = (Button) getActivity().findViewById(R.id.walk_through_action_button);
    }

    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {
        startActivity(mLaunchIntent, null, null);
        lanuchNextButton.performClick();
        final Intent launchIntent = getStartedActivityIntent();

        assertNotNull("Intent was null", launchIntent);

        String shouldBeSiteVisitId = "123456789TestSiteVisitID";

        final String payload = launchIntent.getStringExtra(Constants.SITE_VISIT_ID);

        assertEquals("Incorrect SiteVisitID", shouldBeSiteVisitId, payload);

        //TODO
        //repeat for all fields of the Intent
    }



}
