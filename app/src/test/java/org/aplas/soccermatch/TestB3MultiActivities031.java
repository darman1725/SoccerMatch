package org.aplas.soccermatch;


import android.view.View;
import android.view.ViewParent;
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities031 extends ViewTest {
    private PlayActivity activity;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        activity = Robolectric.buildActivity(PlayActivity.class).create().get();
    }

    @Test
    public void check_01_MainTitle() {
        String cardName="playTitleCard";
        String mainTitle="Soccer Match Score";
        View titleCard = getViewFromActivity(cardName,activity);
        testViewExist(titleCard,"CardView");
        if (((CardView) titleCard).getChildCount()==0) {
            fail("CardView with id="+cardName+" is empty. It should contain a TextView");
        } else {
            View titleTxt = getViewFromLayout("playTitleTxt",titleCard);
            testViewContent(titleTxt,"AppCompatTextView",mainTitle);
        }
    }

    @Test
    public void check_02_HomeTeamView() {
        View img = getViewFromActivity("homeLogo",activity);
        testViewExist(img,"AppCompatImageView");
        View team = getViewFromActivity("homeTeam",activity);
        testViewExist(team,"AppCompatTextView");
        View score = getViewFromActivity("homeScore",activity);
        testViewContent(score,"AppCompatTextView","0");
        View btn1 = getViewFromActivity("addHomeScore",activity);
        testImageContent((ImageView) btn1,"AppCompatImageButton","icon_goal", ImageView.ScaleType.FIT_CENTER, true);
        View btn2 = getViewFromActivity("addHomeYellow",activity);
        testImageContent((ImageView) btn2,"AppCompatImageButton","icon_yellow_card", ImageView.ScaleType.FIT_CENTER, true);
        View btn3 = getViewFromActivity("addHomeRed",activity);
        testImageContent((ImageView) btn3,"AppCompatImageButton","icon_red_card", ImageView.ScaleType.FIT_CENTER, true);
    }

    @Test
    public void check_03_AwayTeamView() {
        View img = getViewFromActivity("awayLogo",activity);
        testViewExist(img,"AppCompatImageView");
        View team = getViewFromActivity("awayTeam",activity);
        testViewExist(team,"AppCompatTextView");
        View score = getViewFromActivity("awayScore",activity);
        testViewContent(score,"AppCompatTextView","0");
        View btn1 = getViewFromActivity("addAwayScore",activity);
        testImageContent((ImageView) btn1,"AppCompatImageButton","icon_goal", ImageView.ScaleType.FIT_CENTER, true);
        View btn2 = getViewFromActivity("addAwayYellow",activity);
        testImageContent((ImageView) btn2,"AppCompatImageButton","icon_yellow_card", ImageView.ScaleType.FIT_CENTER, true);
        View btn3 = getViewFromActivity("addAwayRed",activity);
        testImageContent((ImageView) btn3,"AppCompatImageButton","icon_red_card", ImageView.ScaleType.FIT_CENTER, true);
    }

    @Test
    public void check_04_TimerShow() {
        View timer = getViewFromActivity("timerShow",activity);
        testViewContent(timer,"AppCompatTextView","00:00:00",true);
        ViewParent card = timer.getParent();
        assertTrue("TextView 'timerShow' should be in CardView", card instanceof CardView);
    }

    @Test
    public void check_05_PlayButton() {
        View startBtn = getViewFromActivity("matchStartBtn",activity);
        testViewContent(startBtn,"AppCompatButton","Start",true);
        View finishBtn = getViewFromActivity("matchFinishBtn",activity);
        testViewContent(finishBtn,"AppCompatButton","Finish",false);
    }



    @Test
    public void check_06_FragmentContainer() {
        String frClass = "FooterFragment";
        testClassExist(activity.getPackageName()+"."+frClass);

        View frame = getViewFromActivity("footer",activity);
        testViewExist(frame,"FrameLayout");
    }


}
