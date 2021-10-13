package org.aplas.soccermatch;


import android.view.View;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities041 extends ViewTest {
    private LogActivity activity;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        //Robolectric Pack
        activity = Robolectric.buildActivity(LogActivity.class).create().get();
    }

    @Test
    public void check_01_Contents() {
        View v1 = getViewFromActivity("matchResultTxt",activity);
        testViewExist(v1,"AppCompatTextView");
        View v2 = getViewFromActivity("matchScoreTxt",activity);
        testViewExist(v2,"AppCompatTextView");
        View v3 = getViewFromActivity("logRcView",activity);
        testViewExist(v3,"RecyclerView");
        View v4 = getViewFromActivity("restartBtn",activity);
        testViewContent(v4,"AppCompatButton", "New Match");
    }
}
