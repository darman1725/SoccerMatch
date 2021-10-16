package org.aplas.soccermatch;

import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import org.robolectric.shadows.ShadowPopupMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities071 extends ViewTest {
    private PlayActivity activity;
    private Intent playIntent;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        //Robolectric Pack
        MainActivity main = Robolectric.buildActivity(MainActivity.class).create().get();
        completingMainActivity(main);
        ((Button)getViewFromActivity("startBtn",main)).performClick();
        playIntent = Shadows.shadowOf(main).getNextStartedActivity();
        activity = Robolectric.buildActivity(PlayActivity.class,playIntent).create().get();
    }

    @Test
    public void check_01_Contents_onCreate() {
/*
        String val = "00:00:00";

        assertEquals("TextView 'timeShow' text must start with '"+val+"'",
                val,((TextView)getViewFromActivity("timerShow",activity)).getText().toString());
*/
        ArrayList<String> event = (ArrayList<String>)getFieldValue(activity,"eventList");
        assertNotNull("ArrayList 'eventList' should be costructed in declaration",event);
        assertEquals("ArrayList 'eventList' content should be empty in initiation",0,event.size());

        testFieldValue(activity,"separator","String","@");
        testFieldExist("handler","Handler",activity);
        testFieldViewExist("timerTxt","AppCompatTextView",activity);
        testFieldViewExist("homeTeamTxt","AppCompatTextView",activity);
        testFieldViewExist("awayTeamTxt","AppCompatTextView",activity);
        testFieldViewExist("homeScoreTxt","AppCompatTextView",activity);
        testFieldViewExist("awayScoreTxt","AppCompatTextView",activity);
        testFieldViewExist("homeLogo","AppCompatImageView",activity);
        testFieldViewExist("awayLogo","AppCompatImageView",activity);
        testFieldViewExist("homeScoreBtn","AppCompatImageButton",activity);
        testFieldViewExist("homeYellowBtn","AppCompatImageButton",activity);
        testFieldViewExist("homeRedBtn","AppCompatImageButton",activity);
        testFieldViewExist("awayScoreBtn","AppCompatImageButton",activity);
        testFieldViewExist("awayYellowBtn","AppCompatImageButton",activity);
        testFieldViewExist("awayRedBtn","AppCompatImageButton",activity);
        testFieldViewExist("startMatch","AppCompatButton",activity);
        testFieldViewExist("finishMatch","AppCompatButton",activity);
    }

    @Test
    public void check_02_Contents_ExtraData() {
        int homeImg = getImageResId((ImageView) getViewFromActivity("homeLogo",activity));
        int awayImg = getImageResId((ImageView) getViewFromActivity("awayLogo",activity));
        activity.onStart();

        String extra = "HOME_TEAM_NAME";
        Assert.assertEquals("Home Team name must equal with extra '"+extra+"'",
                activity.getIntent().getStringExtra(extra),
                ((TextView)getViewFromActivity("homeTeam",activity)).getText().toString());

        extra = "AWAY_TEAM_NAME";
        Assert.assertEquals("Away Team name must equal with extra '"+extra+"'",
                activity.getIntent().getStringExtra(extra),
                ((TextView)getViewFromActivity("awayTeam",activity)).getText().toString());

        extra = "HOME_TEAM_PLAYER";
        ArrayList<String> homePlayer = (ArrayList<String>) getFieldValue(activity,"homePlayer");
        Assert.assertEquals("ArrayList 'homePlayer' value must equal with extra '"+extra+"'",
                activity.getIntent().getStringArrayListExtra(extra).toString(), homePlayer.toString());

        extra = "AWAY_TEAM_PLAYER";
        ArrayList<String> awayPlayer = (ArrayList<String>) getFieldValue(activity,"awayPlayer");
        Assert.assertEquals("ArrayList 'awayPlayer' value must equal with extra '"+extra+"'",
                activity.getIntent().getStringArrayListExtra(extra).toString(), awayPlayer.toString());

        Assert.assertNotEquals("Home Team logo should be changed",homeImg,
                getImageResId((ImageView) getViewFromActivity("homeLogo",activity)));
        Assert.assertNotEquals("Away Team logo should be changed",awayImg,
                getImageResId((ImageView) getViewFromActivity("awayLogo",activity)));
    }



    @Test
    public void check_03_Fragment_Activated() {
        activity.onStart();

        String frClass = "FooterFragment";
        activity.getSupportFragmentManager().executePendingTransactions();

        List listFragment = activity.getSupportFragmentManager().getFragments();
        assertTrue("There is no active fragment in PlayActivity",listFragment.size()>0);
        Fragment footer = (Fragment) listFragment.get(0);
        assertEquals("Fragment tag name is wrong","footer",footer.getTag());
        assertEquals("The fragment container class should be "+frClass,activity.getPackageName()+"."+frClass,footer.getClass().getName());
    }

    @Test
    public void check_04_Fragment_Content() {
        activity.onStart();
        //Check Fragment Content
        FragmentScenario<FooterFragment> frScenario = FragmentScenario.launch(FooterFragment.class);
        frScenario.moveToState(Lifecycle.State.CREATED);
        frScenario.onFragment(fragment -> {
            View playCard = getViewFromFragment("playCard",fragment);
            testViewExist(playCard,"CardView");
            View imgPlay = getViewFromLayout("playImg",playCard);
            testViewExist(imgPlay,"ImageView");
            View footerTxt = getViewFromLayout("footerTxt",playCard);
            testViewContent(footerTxt,"TextView","Soccer Last News:");
            View newsTxt = getViewFromLayout("newsTxt",playCard);
            testViewContent(newsTxt,"TextView","");
        });
    }
/*
    @Test
    public void check_XX_StartMatch_Clicked() {
        ActivityScenario<PlayActivity> play = ActivityScenario.launch(playIntent);
        play.moveToState(Lifecycle.State.STARTED);
        play.onActivity(act -> {
            ShadowLooper looper = startGame(act);
            TextView txt = (TextView)getViewFromActivity("timerShow",act);
            System.out.println(txt.getText().toString());
            //looper.runToNextTask();
            //System.out.println(txt.getText().toString());
            looper.idle(2, TimeUnit.SECONDS);
            System.out.println(txt.getText().toString());
        });
    }
*/
    @Test
    public void check_05_StartMatch_Clicked() throws InterruptedException {
        ShadowLooper shadowLooper = startGame(activity);

        assertEquals("TextView 'timerShow' should be started","true",String.valueOf(!shadowLooper.isPaused()));

        testViewContent(getViewFromActivity("matchStartBtn",activity),"AppCompatButton","Stop");
        testViewEnabled(getViewFromActivity("addHomeScore",activity),true);
        testViewEnabled(getViewFromActivity("addHomeYellow",activity),true);
        testViewEnabled(getViewFromActivity("addHomeRed",activity),true);
        testViewEnabled(getViewFromActivity("addAwayScore",activity),true);
        testViewEnabled(getViewFromActivity("addAwayYellow",activity),true);
        testViewEnabled(getViewFromActivity("addAwayRed",activity),true);
        testViewEnabled(getViewFromActivity("matchFinishBtn",activity),false);

        //int timeSec = getRandomInteger(2,7);
        //shadowLooper.getScheduler().advanceBy(2,TimeUnit.SECONDS);
        //shadowLooper.pause();

        TextView timer = (TextView) getViewFromActivity("timerShow",activity);
        assertEquals("TextView 'timerShow' should be in run","00:00:01",timer.getText().toString());
    }

    @Test
    public void check_06_AddHomeGoal() {
        String team = "Home";
        String type = "Score";
        String player = testPopupAfterClick(team,type);

        TextView score = (TextView)getViewFromActivity(team.toLowerCase()+"Score",activity);
        assertEquals("TextView '"+team.toLowerCase()+"Score' should be increased after 1 player selected",String.valueOf(1),score.getText().toString());

        testDataAfterClick(team,type,player);
    }

    @Test
    public void check_07_AddAwayGoal() {
        String team = "Away";
        String type = "Score";
        String player = testPopupAfterClick(team,type);

        TextView score = (TextView)getViewFromActivity(team.toLowerCase()+"Score",activity);
        assertEquals("TextView '"+team.toLowerCase()+"Score' should be increased after 1 player selected",String.valueOf(1),score.getText().toString());

        testDataAfterClick(team,type,player);
    }

    @Test
    public void check_08_AddHomeYellowCard() {
        String team = "Home";
        String type = "Yellow";
        String player = testPopupAfterClick(team,type);

        testDataAfterClick(team,type,player);
    }

    @Test
    public void check_09_AddAwayYellowCard() {
        String team = "Away";
        String type = "Yellow";
        String player = testPopupAfterClick(team,type);

        testDataAfterClick(team,type,player);
    }

    @Test
    public void check_10_AddHomeRedCard() {
        String team = "Home";
        String type = "Red";
        String player = testPopupAfterClick(team,type);

        testDataAfterClick(team,type,player);
    }

    @Test
    public void check_11_AddAwayRedCard() {
        String team = "Away";
        String type = "Red";
        String player = testPopupAfterClick(team,type);

        testDataAfterClick(team,type,player);
    }

    @Test
    public void check_12_StopMatch_Clicked() {
        ShadowLooper shadowLooper = startGame(activity);

        ((Button)getViewFromActivity("matchStartBtn",activity)).performClick();
        assertFalse("The runnable should be stopped with 'removeCallbacks'",shadowLooper.getScheduler().areAnyRunnable());
        testViewContent(getViewFromActivity("matchStartBtn",activity),"AppCompatButton","Start");
        testViewEnabled(getViewFromActivity("addHomeScore",activity),false);
        testViewEnabled(getViewFromActivity("addHomeYellow",activity),false);
        testViewEnabled(getViewFromActivity("addHomeRed",activity),false);
        testViewEnabled(getViewFromActivity("addAwayScore",activity),false);
        testViewEnabled(getViewFromActivity("addAwayYellow",activity),false);
        testViewEnabled(getViewFromActivity("addAwayRed",activity),false);
        testViewEnabled(getViewFromActivity("matchFinishBtn",activity),true);
    }

    @Test
    public void check_13_Simulate_ManyEvents() {
        Button btn = (Button)getViewFromActivity("matchStartBtn",activity);
        ArrayList<String> event = (ArrayList<String>)getFieldValue(activity,"eventList");
        ArrayList<String> team = new ArrayList<>(Arrays.asList("Home", "Away"));
        ArrayList<String> type = new ArrayList<>(Arrays.asList("Score","Yellow","Red"));
        int maxLoop;
        int count=0;
        int homeScore=0;
        int awayScore=0;
        ShadowLooper shadowLooper = startGame(activity);
        shadowLooper.pause();
        maxLoop = getRandomInteger(4,8);
        for (int i=0; i<maxLoop; i++) {
            String str1 = team.get(getRandomInteger(0,1));
            String str2 = type.get(getRandomInteger(0,2));
            performPopupClick(str1,str2,activity);
            if (str1.equals("Home") && str2.equals("Score")) homeScore++;
            if (str1.equals("Away") && str2.equals("Score")) awayScore++;
            count++;
        }
        btn.performClick();

        btn.performClick();
        shadowLooper.runOneTask();
        shadowLooper.pause();
        maxLoop = getRandomInteger(4,8);
        for (int i=0; i<maxLoop; i++) {
            String str1 = team.get(getRandomInteger(0,1));
            String str2 = type.get(getRandomInteger(0,2));
            performPopupClick(str1,str2,activity);
            if (str1.equals("Home") && str2.equals("Score")) homeScore++;
            if (str1.equals("Away") && str2.equals("Score")) awayScore++;
            count++;
        }
        btn.performClick();

        btn.performClick();
        shadowLooper.runOneTask();
        shadowLooper.pause();
        maxLoop = getRandomInteger(4,8);
        for (int i=0; i<maxLoop; i++) {
            String str1 = team.get(getRandomInteger(0,1));
            String str2 = type.get(getRandomInteger(0,2));
            performPopupClick(str1,str2,activity);
            if (str1.equals("Home") && str2.equals("Score")) homeScore++;
            if (str1.equals("Away") && str2.equals("Score")) awayScore++;
            count++;
        }
        btn.performClick();

        assertEquals("All events (Goal,Yellow Card,Red Card) should be stored in 'evenList'",count,event.size());
        String score = ((TextView)getViewFromActivity("homeScore",activity)).getText().toString()+"-"+
                ((TextView)getViewFromActivity("awayScore",activity)).getText().toString();
        assertEquals("Score calculation is not correct for all case",homeScore+"-"+awayScore,score);
    }

    @Test
    public void check_14_FinishBtn_Click() {
        completingPlayActivity(activity);

        ((Button)getViewFromActivity("matchFinishBtn",activity)).performClick();

        Intent actual = Shadows.shadowOf(activity).getNextStartedActivity();
        Assert.assertNotNull("Intent 'LogActivity' should be activated",actual);
        Assert.assertEquals("Intent 'LogActivity' should be activated",".LogActivity",actual.getComponent().getShortClassName());

        int homeScore = Integer.parseInt(((TextView)getViewFromActivity("homeScore",activity)).getText().toString());
        int awayScore = Integer.parseInt(((TextView)getViewFromActivity("awayScore",activity)).getText().toString());
        String matchResult, scoreResult;
        if (homeScore==awayScore) {
            matchResult = "Draw";
            scoreResult = homeScore+"-"+awayScore;
        } else if (homeScore>awayScore) {
            matchResult = ((TextView)getViewFromActivity("homeTeam",activity)).getText().toString()+" Win!!";
            scoreResult = homeScore+"-"+awayScore;
        } else {
            matchResult = ((TextView)getViewFromActivity("awayTeam",activity)).getText().toString()+" Win!!";
            scoreResult = awayScore+"-"+homeScore;
        }

        String extra = "MATCH_RESULT";
        assertTrue("The 'LogActivity' Intent should has Extra '"+extra+"' regarding Match Result",
                matchResult.equals(actual.getStringExtra(extra)));
        extra = "MATCH_SCORE";
        assertTrue("The 'LogActivity' Intent should has Extra '"+extra+"' regarding Match Score",
                scoreResult.equals(actual.getStringExtra(extra)));

        extra = "MATCH_EVENT";
        ArrayList<String> event = (ArrayList<String>)getFieldValue(activity,"eventList");
        assertTrue("The 'LogActivity' Intent should has Extra '"+extra+"' with value from ArrayList 'eventList'",
                event.toString().equals(actual.getStringArrayListExtra(extra).toString()));
    }

    private String testPopupAfterClick(String team, String type) {
        ShadowLooper shadowLooper = startGame(activity);
        shadowLooper.pause();

        ((ImageButton)getViewFromActivity("add"+team+type,activity)).performClick();

        PopupMenu popup = ShadowPopupMenu.getLatestPopupMenu();

        assertNotNull("The "+team.toLowerCase()+" player popup menu should be shown",popup);
        Menu menu = popup.getMenu();
        ArrayList<String> list = (ArrayList<String>) getFieldValue(activity,team.toLowerCase()+"Player");
        assertEquals("The size of shown Popup menu must be same with size of ArrayList extra '"+team.toUpperCase()+"_TEAM_PLAYER'",
                list.size(),menu.size());
        String x ="";

        for (int i=0;i<menu.size();i++)
            assertTrue("The content of player in shown Popup menu must be same with size of ArrayList extra ''"+team.toUpperCase()+"_TEAM_PLAYER'",
                    list.contains(menu.getItem(i).getTitle().toString().trim()));

        int idx = getRandomInteger(0,menu.size()-1);
        MenuItem item = menu.getItem(idx);
        Shadows.shadowOf(popup).getOnMenuItemClickListener().onMenuItemClick(item);
        return item.getTitle().toString();
    }

    private void testDataAfterClick(String team, String type, String playerName) {
        String newsType = "";
        switch (type) {
            case "Score" : newsType="Goal"; break;
            case "Yellow" : newsType="Yellow Card"; break;
            case "Red" : newsType="Red Card"; break;
        }
        String teamName = ((TextView)getViewFromActivity(team.toLowerCase()+"Team",activity)).getText().toString();
        String timeShow = ((TextView)getViewFromActivity("timerShow",activity)).getText().toString();

        ArrayList<String> event = (ArrayList<String>)getFieldValue(activity,"eventList");
        assertEquals("ArrayList 'eventList' content should be added 1 player after clicked",1,event.size());
        assertEquals("The string added to ArrayList 'eventList' is not correct"+event.toString(),
                newsType+"@"+playerName+"@"+teamName+"@"+timeShow,event.get(0));

        //Check Fragment
        activity.getSupportFragmentManager().executePendingTransactions();
        List listFragment = activity.getSupportFragmentManager().getFragments();
        Fragment footer = (Fragment) listFragment.get(0);

        View footerTxt = getViewFromFragment("footerTxt",footer);
        View newsTxt = getViewFromFragment("newsTxt",footer);

        testViewContent(footerTxt,"AppCompatTextView","Soccer Last News:");


        String txt = newsType+" for "+teamName+ " by "+playerName+" at "+timeShow;
        testViewContent(newsTxt,"AppCompatTextView",txt);
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
