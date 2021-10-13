package org.aplas.soccermatch;

import android.view.View;
import android.view.ViewGroup;
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

public class TestB3MultiActivities051 extends ViewTest {
    private MainActivity activity;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        //Robolectric Pack
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void check_01_Layout_List() {
        View layout = getLayoutFromResource("layout_list",activity,"LinearLayout");
        View x = getViewFromLayout("listItem",(ViewGroup)layout);
        testViewContent(x,"TextView","");
    }

    @Test
    public void check_02_Layout_Dialog() {
        View layout = getLayoutFromResource("layout_dialog",activity,"LinearLayout");
        View x = getViewFromLayout("playerName",(ViewGroup)layout);
        //testViewExist(x,"EditText");
        testViewContent(x,"EditText","");
    }

    @Test
    public void check_03_Layout_Log() {
        View layout = getLayoutFromResource("layout_log",activity,"CardView");
        testViewExist(getViewFromLayout("eventIcon",(ViewGroup)layout),"ImageView");
        testViewExist(getViewFromLayout("txtName",(ViewGroup)layout),"TextView");
        testViewExist(getViewFromLayout("txtTime",(ViewGroup)layout),"TextView");
        testViewExist(getViewFromLayout("txtPlayer",(ViewGroup)layout),"TextView");
    }

    @Test
    public void check_03_Fragment_Footer() {
        View layout = getLayoutFromResource("fragment_footer",activity,"LinearLayout");
        testViewExist(getViewFromLayout("playCard",(ViewGroup)layout),"CardView");
        testImageContent(getViewFromLayout("playImg",(ViewGroup)layout),"ImageView","socceranim");
        testViewContent(getViewFromLayout("footerTxt",(ViewGroup)layout),"TextView","Soccer Last News:");
        testViewExist(getViewFromLayout("newsTxt",(ViewGroup)layout),"TextView");
    }

}
