package org.aplas.soccermatch;


import android.view.View;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import androidx.cardview.widget.CardView;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities021 extends ViewTest {
    private MainActivity activity;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        //Robolectric Pack
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void check_01_MainTitle() {
        String cardName="mainTitleCard";
        String mainTitle="APLAS SOCCER BOARD";
        View titleCard = getViewFromActivity(cardName,activity);
        testViewExist(titleCard,"CardView");
        if (((CardView) titleCard).getChildCount()==0) {
            fail("CardView with id="+cardName+" is empty. It should contain a TextView");
        } else {
            View titleTxt = getViewFromLayout("mainTitleTxt",titleCard);//((CardView) titleCard).getChildAt(0);
            testViewContent(titleTxt,"AppCompatTextView",mainTitle);
        }
    }

    @Test
    public void check_02_HomeTeamView() {
        View homeLabel = getViewFromActivity("homeLabel",activity);
        testViewContent(homeLabel,"AppCompatTextView","Home Team");
        View homeTeam = getViewFromActivity("homeTeam",activity);
        testViewContent(homeTeam,"AppCompatEditText","","Enter home team name");
        View homeImg = getViewFromActivity("homeImage",activity);
        testImageContent(homeImg,"AppCompatImageButton","teamlogo", ImageView.ScaleType.FIT_CENTER,true);
        View homePlayerNumber = getViewFromActivity("homePlayerNumber",activity);
        testViewContent(homePlayerNumber,"AppCompatTextView","0 player(s)");
        View addHomePlayer = getViewFromActivity("addHomePlayer",activity);
        testImageContent(addHomePlayer,"AppCompatImageButton","icon_add_button", ImageView.ScaleType.FIT_END,false);
        testViewExist(addHomePlayer,"AppCompatImageButton");
        View homePlayer = getViewFromActivity("homePlayer",activity);
        testViewExist(homePlayer,"ListView");
    }

    @Test
    public void check_03_AwayTeamView() {
        View homeLabel = getViewFromActivity("awayLabel",activity);
        testViewContent(homeLabel,"AppCompatTextView","Away Team");
        View homeTeam = getViewFromActivity("awayTeam",activity);
        testViewContent(homeTeam,"AppCompatEditText","","Enter away team name");
        View homeImg = getViewFromActivity("awayImage",activity);
        testViewExist(homeImg,"AppCompatImageButton");
        View homePlayerNumber = getViewFromActivity("awayPlayerNumber",activity);
        testViewContent(homePlayerNumber,"AppCompatTextView","0 player(s)");
        View addHomePlayer = getViewFromActivity("addAwayPlayer",activity);
        testViewExist(addHomePlayer,"AppCompatImageButton");
        View homePlayer = getViewFromActivity("awayPlayer",activity);
        testViewExist(homePlayer,"ListView");
    }

    @Test
    public void check_04_StartButton() {
        View btn = getViewFromActivity("startBtn",activity);
        testViewContent(btn,"AppCompatButton","Next");
    }


}
