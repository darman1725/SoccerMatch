package org.aplas.soccermatch;

import android.app.Activity;
import android.os.Build;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities011 extends ViewTest {
    private Activity activity;
    private final String layoutName = "activity_main";
    private final String appName  = "SoccerMatch";
    private final String packageName = "org.aplas.soccermatch";
    private final String targetDevice = "9";
    private final String actName = "MainActivity";
    private final String backwardComp = "androidx.appcompat.app.AppCompatActivity";
    private final int minSDK = 23;

    private ResourceTest rsc;


    @Before
    public void initTest() {
        //Robolectric
        String actClass = "org.aplas.soccermatch.MainActivity";
        testClassExist(actClass);
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void check_01_ProjectConfiguration() { //Check Project Name (Should be Basic101)
        assertEquals("Application Name is Wrong", appName.toLowerCase(), getAppName(activity.getPackageName()));
        String packName = packageName;//+"."+appName.toLowerCase();
        assertEquals("Package Name is Wrong", packName, activity.getPackageName());
        assertEquals("Target Device is Wrong",targetDevice, Build.VERSION.RELEASE);
        assertEquals("Minimum SDK Version is Wrong",minSDK,BuildConfig.MIN_SDK_VERSION);
        assertEquals("Activity Name is Wrong", actName, activity.getClass().getSimpleName());

        int resId = activity.getResources().getIdentifier(layoutName, "layout", activity.getPackageName());
        assertNotEquals("Layout Name is Wrong", 0, resId);

        assertEquals("Activity super class is Wrong", backwardComp, activity.getClass().getSuperclass().getName());
        checkClassImplementation("androidx.recyclerview.widget.RecyclerView");
        checkClassImplementation("androidx.cardview.widget.CardView");
        checkClassImplementation("androidx.appcompat.app.AppCompatActivity");
    }

    public void checkClassImplementation(String className) {
        try {
            Class.forName(className);
        } catch (Exception e) {
            String[] arr = className.split(".");
            fail("Project did not implement "+arr[arr.length-1]+" yet. Check gradle configuration");
        }
    }

    private String getAppName(String packName) {
        String[] list = packName.split("\\.");
        String res = list[list.length-1];
        return res;
    }

    //Resource Test
    @Test
    public void check_02_StringResource() {
        rsc = new ResourceTest(activity.getResources());
        rsc.testStringResource("app_name","Soccer Match");
    }

    @Test
    public void check_03_DrawableResource() {
        rsc = new ResourceTest(activity.getResources());
        rsc.testImgResource("icon_add_button");
        rsc.testImgResource("icon_goal");
        rsc.testImgResource("icon_next");
        rsc.testImgResource("icon_player");
        rsc.testImgResource("icon_red_card");
        rsc.testImgResource("icon_reload");
        rsc.testImgResource("icon_yellow_card");
        rsc.testImgResource("socceranim");
        rsc.testImgResource("teamlogo");
    }
}
