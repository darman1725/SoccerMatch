package org.aplas.soccermatch;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities081 extends ViewTest {
    private ActivityScenario<LogActivity> logScenario;
    private Intent logIntent;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        //Robolectric Pack
        MainActivity main = Robolectric.buildActivity(MainActivity.class).create().get();
        completingMainActivity(main);
        ((Button)getViewFromActivity("startBtn",main)).performClick();
        Intent playIntent = Shadows.shadowOf(main).getNextStartedActivity();
        PlayActivity play = Robolectric.buildActivity(PlayActivity.class,playIntent).create().get();
        completingPlayActivity(play);
        ((Button)getViewFromActivity("matchFinishBtn",play)).performClick();

        logIntent = Shadows.shadowOf(main).getNextStartedActivity();
        logScenario = ActivityScenario.launch(logIntent);
    }

    @Test
    public void check_01_Contents_onCreate() {
        logScenario.moveToState(Lifecycle.State.CREATED);
        logScenario.onActivity(activity -> {
            testFieldViewExist("logView","RecyclerView",activity);
            testFieldViewExist("restartBtn","AppCompatButton",activity);
        });
    }

    @Test
    public void check_02_Header_Result() {
        logScenario.moveToState(Lifecycle.State.STARTED);
        logScenario.onActivity(activity -> {
            String str = "Result: "+activity.getIntent().getStringExtra("MATCH_RESULT");
            assertEquals("TextView 'matchResultTxt' must show the result of match",
                    str, ((TextView)getViewFromActivity("matchResultTxt",activity)).getText().toString());

            str = "Score: "+activity.getIntent().getStringExtra("MATCH_SCORE");
            assertEquals("TextView 'matchScoreTxt' must show the score of match",
                    str, ((TextView)getViewFromActivity("matchScoreTxt",activity)).getText().toString());
        });
    }

    @Test
    public void check_03_LogView_RecyclerView() {
        logScenario.moveToState(Lifecycle.State.STARTED);
        logScenario.onActivity(activity -> {
            RecyclerView logView = (RecyclerView) getViewFromActivity("logRcView", activity);
            ArrayList<String> eventList = activity.getIntent().getStringArrayListExtra("MATCH_EVENT");

            assertEquals("RecyclerView 'logRcView' Layout Manager should be LinearLayoutManager",
                    "LinearLayoutManager", logView.getLayoutManager().getClass().getSimpleName());
            assertEquals("RecyclerView 'logRcView' Item Animator should be DefaultItemAnimator",
                    "DefaultItemAnimator", logView.getItemAnimator().getClass().getSimpleName());

            assertEquals("RecyclerView 'logRcView' adapter should be in LogAdapter type",
                    "LogAdapter", logView.getAdapter().getClass().getSimpleName());

            LogAdapter logAdapter = (LogAdapter) logView.getAdapter();
            assertTrue("RecyclerView 'logRcView' item count should be same with extra 'MATCH_EVENT'",
                    eventList.size() == logAdapter.getItemCount());

            for (int i = 0; i < logView.getChildCount(); i++) {
                //System.out.println(logAdapter.getItemCount()+"/ Loop ke-"+i);
                View view = logView.findViewHolderForAdapterPosition(i).itemView;
                String[] data = eventList.get(i).split("@");
                TextView txt = (TextView) getViewFromLayout("txtName", view);
                assertTrue("TextView 'txtName' in RecycleView must show event name",
                        data[0].equals(txt.getText().toString()));

                String icon = "";
                switch (data[0]) {
                    case "Goal":
                        icon = "icon_goal";
                        break;
                    case "Yellow Card":
                        icon = "icon_yellow_card";
                        break;
                    case "Red Card":
                        icon = "icon_red_card";
                        break;
                }
                assertTrue("ImageView 'eventIcon' in RecycleView must show related icon to event",
                        checkImageContent(getViewFromLayout("eventIcon", view), icon));

                txt = (TextView) getViewFromLayout("txtTime", view);
                assertTrue("TextView 'txtTime' in RecycleView must show event name",
                        data[3].equals(txt.getText().toString()));
                txt = (TextView) getViewFromLayout("txtPlayer", view);
                assertTrue("TextView 'txtPlayer' in RecycleView must show event name",
                        (data[1] + " (" + data[2] + ")").equals(txt.getText().toString()));
            }

        });
    }

    @Test
    public void check_04_RestartBtn_Click () {
        logScenario.moveToState(Lifecycle.State.STARTED);
        logScenario.onActivity(activity -> {
            ((Button)getViewFromActivity("restartBtn",activity)).performClick();
            Intent actual = Shadows.shadowOf(activity).getNextStartedActivity();
            Assert.assertNotNull("Intent 'MainActivity' should be activated",actual);
            Assert.assertEquals("Intent 'MainActivity' should be activated",".MainActivity",actual.getComponent().getShortClassName());
        });
    }

    protected void completingMainActivity(MainActivity main) {
        ((EditText) getViewFromActivity("homeTeam",main)).setText("aplas home");
        ((EditText) getViewFromActivity("awayTeam",main)).setText("aplas away");

        imgButtonClick ((ImageButton) getViewFromActivity("homeImage",main),main);
        imgButtonClick ((ImageButton) getViewFromActivity("awayImage",main),main);

        for (int i=0; i<11; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addHomePlayer",main));
        }

        for (int i=0; i<11; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addAwayPlayer",main));
        }
    }

    protected void completingPlayActivity(PlayActivity activity) {
        Button btn = (Button)getViewFromActivity("matchStartBtn",activity);
        ArrayList<String> team = new ArrayList<>(Arrays.asList("Home", "Away"));
        ArrayList<String> type = new ArrayList<>(Arrays.asList("Score","Yellow","Red"));
        int maxLoop;
        ShadowLooper shadowLooper = startGame(activity);
        shadowLooper.pause();
        maxLoop = getRandomInteger(4,8);
        for (int i=0; i<maxLoop; i++) {
            performPopupClick(team.get(getRandomInteger(0,1)),type.get(getRandomInteger(0,2)),activity);
        }
        btn.performClick();

        btn.performClick();
        shadowLooper.runOneTask();
        shadowLooper.pause();
        maxLoop = getRandomInteger(4,8);
        for (int i=0; i<maxLoop; i++) {
            performPopupClick(team.get(getRandomInteger(0,1)),type.get(getRandomInteger(0,2)),activity);
        }
        btn.performClick();

        btn.performClick();
        shadowLooper.runOneTask();
        shadowLooper.pause();
        maxLoop = getRandomInteger(4,8);
        for (int i=0; i<maxLoop; i++) {
            performPopupClick(team.get(getRandomInteger(0,1)),type.get(getRandomInteger(0,2)),activity);
        }
        btn.performClick();

    }

    protected ShadowLooper startGame(PlayActivity activity) {
        activity.onStart();
        Button btn = (Button)getViewFromActivity("matchStartBtn",activity);

        btn.performClick();
        Handler handler = (Handler)getFieldValue(activity,"handler");
        ShadowLooper shadowLooper = Shadows.shadowOf(handler.getLooper());
        shadowLooper.runOneTask();
        assertTrue("The runnable should be activated with 'postDelayed'",shadowLooper.getScheduler().areAnyRunnable());

        return shadowLooper;
    }

}
