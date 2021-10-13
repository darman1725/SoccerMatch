package org.aplas.soccermatch;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowToast;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import android.app.AlertDialog;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestB3MultiActivities061 extends ViewTest {
    private MainActivity activity;

    @Before
    public void initTest() {
        /****** initiation of Test ******/
        //Robolectric Pack
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void check_01_ListView_Layout() {
        String firstItem = "add 11 players";

        ListView lv1 = (ListView) getViewFromActivity("homePlayer",activity);
        Assert.assertEquals("ListView 'homePlayerList' must has 1 item first ('"+firstItem+"')",1,lv1.getAdapter().getCount());
        Assert.assertEquals("ListView 'homePlayerList' must has 1 item first ('"+firstItem+"')",
                firstItem,lv1.getAdapter().getItem(0).toString());

        ListView lv2 = (ListView) getViewFromActivity("awayPlayer",activity);
        Assert.assertEquals("ListView 'awayPlayerList' must has 1 item first ('"+firstItem+"')",1,lv2.getAdapter().getCount());
        Assert.assertEquals("ListView 'awayPlayerList' must has 1 item first ('"+firstItem+"')",
                firstItem,lv2.getAdapter().getItem(0).toString());
    }

    @Test
    public void check_02_AddHomePlayer_ShowDialog() {
        ImageButton btn = (ImageButton) getViewFromActivity("addHomePlayer",activity);
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertNotNull("An AlertDialog must be shown when 'addHomePlayer' clicked", dialog);
        Assert.assertTrue("An AlertDialog must be shown when 'addHomePlayer' clicked",dialog.isShowing());
        //testField(activity,"homeDialog", Modifier.PRIVATE, AlertDialog.class,false);
        //AlertDialog dialog = (AlertDialog) getFieldValue(activity,"homeDialog");
        //Assert.assertTrue("'homeDialog' must be shown when 'addHomePlayer' clicked",dialog.isShowing());
        View msg = dialog.findViewById(android.R.id.message);
        testViewContent(msg,"AppCompatTextView","Input Player Name:");
        View v = getViewFromDialog("playerName",dialog);
        testViewContent(v,"AppCompatEditText","");
    }

    @Test
    public void check_03_AddHomePlayer_CancelButton() {
        testField(activity,"homePlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> homeList = (ArrayList<String>) getFieldValue(activity,"homePlayer");
        int lastArr1 = homeList.size();
        testField(activity,"awayPlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> awayList = (ArrayList<String>) getFieldValue(activity,"awayPlayer");
        int lastArr2 = awayList.size();

        ListView lv1 = (ListView) getViewFromActivity("homePlayer",activity);
        ListView lv2 = (ListView) getViewFromActivity("awayPlayer",activity);
        int lastList1 = lv1.getAdapter().getCount();
        int lastList2 = lv2.getAdapter().getCount();

        ImageButton btn = (ImageButton) getViewFromActivity("addHomePlayer",activity);
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();

        int nowArr1 = homeList.size();
        int nowArr2 = awayList.size();
        int nowList1 = lv1.getAdapter().getCount();
        int nowList2 = lv2.getAdapter().getCount();

        Assert.assertEquals("Cancel button must has no effect to ArrayList 'homePlayer'",lastArr1,nowArr1);
        Assert.assertEquals("Cancel button must has no effect to ArrayList 'awayPlayer'",lastArr2,nowArr2);
        Assert.assertEquals("Cancel button must has no effect to ListView 'homePlayerList'",lastList1,nowList1);
        Assert.assertEquals("Cancel button must has no effect to ListView 'awayPlayerList'",lastList2,nowList2);
    }

    @Test
    public void check_04_AddHomePlayer_OkButton() {
        testField(activity,"homePlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> homeList = (ArrayList<String>) getFieldValue(activity,"homePlayer");
        int lastArr1 = homeList.size();
        testField(activity,"awayPlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> awayList = (ArrayList<String>) getFieldValue(activity,"awayPlayer");
        int lastArr2 = awayList.size();

        ListView lv1 = (ListView) getViewFromActivity("homePlayer",activity);
        ListView lv2 = (ListView) getViewFromActivity("awayPlayer",activity);
        int lastList1 = lv1.getAdapter().getCount();
        int lastList2 = lv2.getAdapter().getCount();

        ImageButton btn = (ImageButton) getViewFromActivity("addHomePlayer",activity);
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        EditText player = (EditText)getViewFromDialog("playerName",dialog);
        String name = getRandomString(10);
        player.setText(name);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

        int nowArr1 = homeList.size();
        int nowArr2 = awayList.size();
        int nowList1 = lv1.getAdapter().getCount();
        int nowList2 = lv2.getAdapter().getCount();

        String header = "(Add Player no.1) ";
        String homeArrMsg = "OK button in Home Player AlertDialog must has effect to add 1 player to ArrayList 'homePlayer' from EditText 'playerName'";
        Assert.assertEquals(header+homeArrMsg,lastArr1+1,nowArr1);
        Assert.assertEquals(header+homeArrMsg,name,homeList.get(0));
        String homeListMsg = "OK button in Home Player AlertDialog must has effect to add 1 player in ListView 'homePlayerList' from EditText 'playerName'";
        Assert.assertEquals(header+homeListMsg,lastList1,nowList1);
        Assert.assertEquals(header+homeListMsg,name,lv1.getAdapter().getItem(0).toString());
        Assert.assertEquals("OK button in Home Player AlertDialog must has no effect to ArrayList 'awayPlayer'",lastArr2,nowArr2);
        Assert.assertEquals("OK button in Home Player AlertDialog must has no effect to ListView 'awayPlayerList'",lastList2,nowList2);

        for (int i=2; i<12; i++) {
            lastArr1 = homeList.size();
            //lastArr2 = awayList.size();
            lastList1 = lv1.getAdapter().getCount();
            //lastList2 = lv2.getAdapter().getCount();

            //btn.performClick();
            //dialog = ShadowAlertDialog.getLatestAlertDialog();
            //player = (EditText)getViewFromDialog("playerName",dialog);

            name = getRandomString(10);
            player.setText(name);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

            nowArr1 = homeList.size();
            //nowArr2 = awayList.size();
            nowList1 = lv1.getAdapter().getCount();
            //nowList2 = lv2.getAdapter().getCount();

            header = "(Add Player no."+i+") ";
            Assert.assertEquals(header+homeArrMsg,lastArr1+1,nowArr1);
            Assert.assertEquals(header+homeArrMsg,name,homeList.get(nowArr1-1));
            Assert.assertEquals(header+homeListMsg,lastList1+1,nowList1);
            Assert.assertEquals(header+homeListMsg,name,lv1.getAdapter().getItem(nowList1-1).toString());
            //Assert.assertEquals("OK button in Home Player AlertDialog must has no effect to ArrayList 'awayPlayer'",lastArr2,nowArr2);
            //Assert.assertEquals("OK button in Home Player AlertDialog must has no effect to ListView 'awayPlayerList'",lastList2,nowList2);
        }
    }

    @Test
    public void check_05_AddAwayPlayer_ShowDialog() {
        ImageButton btn = (ImageButton) getViewFromActivity("addAwayPlayer",activity);
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertNotNull("An AlertDialog must be shown when 'addAwayPlayer' clicked", dialog);
        Assert.assertTrue("An AlertDialog must be shown when 'addAwayPlayer' clicked",dialog.isShowing());
        View msg = dialog.findViewById(android.R.id.message);
        testViewContent(msg,"AppCompatTextView","Input Player Name:");
        View v = getViewFromDialog("playerName",dialog);
        testViewContent(v,"AppCompatEditText","");
    }

    @Test
    public void check_06_AddAwayPlayer_CancelButton() {
        testField(activity,"homePlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> homeList = (ArrayList<String>) getFieldValue(activity,"homePlayer");
        int lastArr1 = homeList.size();
        testField(activity,"awayPlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> awayList = (ArrayList<String>) getFieldValue(activity,"awayPlayer");
        int lastArr2 = awayList.size();

        ListView lv1 = (ListView) getViewFromActivity("homePlayer",activity);
        ListView lv2 = (ListView) getViewFromActivity("awayPlayer",activity);
        int lastList1 = lv1.getAdapter().getCount();
        int lastList2 = lv2.getAdapter().getCount();

        ImageButton btn = (ImageButton) getViewFromActivity("addAwayPlayer",activity);
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();

        int nowArr1 = homeList.size();
        int nowArr2 = awayList.size();
        int nowList1 = lv1.getAdapter().getCount();
        int nowList2 = lv2.getAdapter().getCount();

        Assert.assertEquals("Cancel button must has no effect to ArrayList 'homePlayer'",lastArr1,nowArr1);
        Assert.assertEquals("Cancel button must has no effect to ArrayList 'awayPlayer'",lastArr1,nowArr2);
        Assert.assertEquals("Cancel button must has no effect to ListView 'homePlayerList'",lastList1,nowList1);
        Assert.assertEquals("Cancel button must has no effect to ListView 'awayPlayerList'",lastList2,nowList2);
    }

    @Test
    public void check_07_AddAwayPlayer_OkButton() {
        testField(activity,"homePlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> homeList = (ArrayList<String>) getFieldValue(activity,"homePlayer");
        int lastArr1 = homeList.size();
        testField(activity,"awayPlayer", Modifier.PRIVATE, ArrayList.class,false);
        ArrayList<String> awayList = (ArrayList<String>) getFieldValue(activity,"awayPlayer");
        int lastArr2 = awayList.size();

        ListView lv1 = (ListView) getViewFromActivity("homePlayer",activity);
        ListView lv2 = (ListView) getViewFromActivity("awayPlayer",activity);
        int lastList1 = lv1.getAdapter().getCount();
        int lastList2 = lv2.getAdapter().getCount();

        ImageButton btn = (ImageButton) getViewFromActivity("addAwayPlayer",activity);
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        EditText player = (EditText)getViewFromDialog("playerName",dialog);
        String name = getRandomString(10);
        player.setText(name);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

        int nowArr1 = homeList.size();
        int nowArr2 = awayList.size();
        int nowList1 = lv1.getAdapter().getCount();
        int nowList2 = lv2.getAdapter().getCount();

        String header = "(Add Player no.1) ";
        String awayArrMsg = "OK button in Away Player AlertDialog must has effect to add 1 player to ArrayList 'awayPlayer' from EditText 'playerName'";
        Assert.assertEquals(header+awayArrMsg,lastArr2+1,nowArr2);
        Assert.assertEquals(header+awayArrMsg,name,awayList.get(0));
        String awayListMsg = "OK button in Away Player AlertDialog must has effect to add 1 player in ListView 'awayPlayerList' from EditText 'playerName'";
        Assert.assertEquals(header+awayListMsg,lastList2,nowList2);
        Assert.assertEquals(header+awayListMsg,name,lv2.getAdapter().getItem(0).toString());
        Assert.assertEquals("OK button in Away Player AlertDialog must has no effect to ArrayList 'homePlayer'",lastArr1,nowArr1);
        Assert.assertEquals("OK button in Away Player AlertDialog must has no effect to ListView 'homePlayerList'",lastList1,nowList1);

        for (int i=2; i<12; i++) {
            lastArr2 = awayList.size();
            lastList2 = lv2.getAdapter().getCount();

            name = getRandomString(10);
            player.setText(name);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

            nowArr2 = awayList.size();
            nowList2 = lv2.getAdapter().getCount();

            header = "(Add Player no."+i+") ";
            Assert.assertEquals(header+awayArrMsg,lastArr2+1,nowArr2);
            Assert.assertEquals(header+awayArrMsg,name,awayList.get(nowArr2-1));
            Assert.assertEquals(header+awayListMsg,lastList2+1,nowList2);
            Assert.assertEquals(header+awayListMsg,name,lv2.getAdapter().getItem(nowList2-1).toString());
        }
    }

    @Test
    public void check_08_Home_ImageButton_Clicked() {
        testField(activity,"homeImgPath", Modifier.PRIVATE, String.class,false);
        ImageButton btn = (ImageButton) getViewFromActivity("homeImage",activity);
        imgButtonClick(btn,activity);

        Assert.assertEquals("The String 'homeImgPath' should be updated from picked Image path",
                getImageUri(activity).toString(),(String)getFieldValue(activity,"homeImgPath"));
        Assert.assertEquals("The image in ImageButton 'homeImage' should be changed after picking an image",-1,getImageResId(btn));
    }

    @Test
    public void check_09_Away_ImageButton_Clicked() {
        testField(activity,"awayImgPath", Modifier.PRIVATE, String.class,false);

        ImageButton btn = (ImageButton) getViewFromActivity("awayImage",activity);
        imgButtonClick(btn,activity);

        Assert.assertEquals("The String 'awayImgPath' should be updated from picked Image path",
                getImageUri(activity).toString(),(String)getFieldValue(activity,"awayImgPath"));
        Assert.assertEquals("The image in ImageButton 'awayImage' should be changed after picking an image",-1,getImageResId(btn));
    }

    @Test
    public void check_10_StartButton_Clicked_when_HomeTeamName_Empty() {
        Button btn = (Button) getViewFromActivity("startBtn",activity);

        btn.performClick();
        Assert.assertNotNull("A Toast should be appear if Home Team Name is still empty",ShadowToast.getLatestToast());
        String msgTxt = "Home team name is still empty";
        Assert.assertEquals("If Home Team Name is still empty, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void check_11_StartButton_Clicked_when_AwayTeamName_Empty() {
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        ((EditText) getViewFromActivity("homeTeam",activity)).setText("aplas home");
        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Away Team Name is still empty",ShadowToast.getLatestToast());
        String msgTxt = "Away team name is still empty";
        Assert.assertEquals("If Away Team Name is still empty, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void check_12_StartButton_Clicked_when_HomeTeamLogo_Empty() {
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        ((EditText) getViewFromActivity("homeTeam",activity)).setText("aplas home");
        ((EditText) getViewFromActivity("awayTeam",activity)).setText("aplas away");
        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Home Team Logo is still empty",ShadowToast.getLatestToast());
        String msgTxt = "Home team logo is still empty";
        Assert.assertEquals("If Home Team Logo is still empty, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void check_13_StartButton_Clicked_when_AwayTeamLogo_Empty() {
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        ((EditText) getViewFromActivity("homeTeam",activity)).setText("aplas home");
        ((EditText) getViewFromActivity("awayTeam",activity)).setText("aplas away");

        imgButtonClick ((ImageButton) getViewFromActivity("homeImage",activity),activity);

        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Away Team Logo is still empty",ShadowToast.getLatestToast());
        String msgTxt = "Away team logo is still empty";
        Assert.assertEquals("If Away Team Logo is still empty, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void check_14_StartButton_Clicked_when_HomePlayer_NotComplete() {
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        ((EditText) getViewFromActivity("homeTeam",activity)).setText("aplas home");
        ((EditText) getViewFromActivity("awayTeam",activity)).setText("aplas away");

        imgButtonClick ((ImageButton) getViewFromActivity("homeImage",activity),activity);
        imgButtonClick ((ImageButton) getViewFromActivity("awayImage",activity),activity);

        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Home Player List is not 11 players yet",ShadowToast.getLatestToast());
        String msgTxt = "Home player list is not complete";
        Assert.assertEquals("Home Player List is not 11 players yet, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());

        for (int i=0; i<10; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addHomePlayer",activity));
        }
        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Home Player List is not 11 players yet",ShadowToast.getLatestToast());
        Assert.assertEquals("Home Player List is not 11 players yet, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void check_15_StartButton_Clicked_when_AwayPlayer_NotComplete() {
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        ((EditText) getViewFromActivity("homeTeam",activity)).setText("aplas home");
        ((EditText) getViewFromActivity("awayTeam",activity)).setText("aplas away");

        imgButtonClick ((ImageButton) getViewFromActivity("homeImage",activity),activity);
        imgButtonClick ((ImageButton) getViewFromActivity("awayImage",activity),activity);

        for (int i=0; i<11; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addHomePlayer",activity));
        }
        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Away Player List is not 11 players yet",ShadowToast.getLatestToast());
        String msgTxt = "Away player list is not complete";
        Assert.assertEquals("Away Player List is not 11 players yet, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());

        for (int i=0; i<10; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addAwayPlayer",activity));
        }
        btn.performClick();

        Assert.assertNotNull("A Toast should be appear if Away Player List is not 11 players yet",ShadowToast.getLatestToast());
        Assert.assertEquals("Away Player List is not 11 players yet, the Toast text should be '"+msgTxt+"'",
                msgTxt,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void check_16_StartButton_Clicked_when_All_Complete() {
        completingMainActivity(activity);
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        btn.performClick();
        Assert.assertNull("A Toast should not be appear if all COMPLETE",ShadowToast.getLatestToast());
    }

    @Test
    public void check_17_StartButton_Clicked_Open_PlayActivity() {
        completingMainActivity(activity);
        Button btn = (Button) getViewFromActivity("startBtn",activity);
        btn.performClick();
        Intent actual = Shadows.shadowOf(activity).getNextStartedActivity();

        Assert.assertEquals("Intent 'PlayActivity' should be activated",".PlayActivity",actual.getComponent().getShortClassName());
        String extra = "HOME_TEAM_NAME";
        Assert.assertEquals("The 'PlayActivity' Intent should has Extra '"+extra+"' with value from EditText 'homeTeam'",
                ((EditText)getViewFromActivity("homeTeam",activity)).getText().toString(),actual.getStringExtra(extra));
        extra = "AWAY_TEAM_NAME";
        Assert.assertEquals("The 'PlayActivity' Intent should has Extra '"+extra+"' with value from EditText 'awayTeam'",
                ((EditText)getViewFromActivity("awayTeam",activity)).getText().toString(),actual.getStringExtra(extra));
        extra = "HOME_IMG_URI";
        Assert.assertEquals("The 'PlayActivity' Intent should has Extra '"+extra+"' with value from String 'homeImgPath'",
                (String)getFieldValue(activity,"homeImgPath"),actual.getStringExtra(extra));
        extra = "AWAY_IMG_URI";
        Assert.assertEquals("The 'PlayActivity' Intent should has Extra '"+extra+"' with value from String 'awayImgPath'",
                (String)getFieldValue(activity,"awayImgPath"),actual.getStringExtra(extra));
        ArrayList<String> homePlayer = (ArrayList<String>) getFieldValue(activity,"homePlayer");
        extra = "HOME_TEAM_PLAYER";
        Assert.assertEquals("The 'PlayActivity' Intent should has Extra '"+extra+"' with value from ArrayList 'homePlayer'",
                homePlayer.toString(),actual.getStringArrayListExtra(extra).toString());
        ArrayList<String> awayPlayer = (ArrayList<String>) getFieldValue(activity,"awayPlayer");
        extra = "AWAY_TEAM_PLAYER";
        Assert.assertEquals("The 'PlayActivity' Intent should has Extra '"+extra+"' with value from ArrayList 'awayPlayer'",
                awayPlayer.toString(),actual.getStringArrayListExtra(extra).toString());
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

/*
    private void completingAll() {
        ((EditText) getViewFromActivity("homeTeam",activity)).setText("aplas home");
        ((EditText) getViewFromActivity("awayTeam",activity)).setText("aplas away");

        imgButtonClick ((ImageButton) getViewFromActivity("homeImage",activity));
        imgButtonClick ((ImageButton) getViewFromActivity("awayImage",activity));

        for (int i=0; i<11; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addHomePlayer",activity));
        }

        for (int i=0; i<11; i++) {
            addPlayerAction((ImageButton) getViewFromActivity("addAwayPlayer",activity));
        }
    }

    private void addPlayerAction(ImageButton btn) {
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        EditText player = (EditText)getViewFromDialog("playerName",dialog);
        String name = getRandomString(10);
        player.setText(name);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
    }

    private Uri getImageUri() {
        return getUriFromResource(R.drawable.icon_goal);
    }

    private void imgButtonClick(ImageButton btn) {
        btn.performClick();
        Intent actual = Shadows.shadowOf(activity).getNextStartedActivity();
        Assert.assertEquals("Intent action should be 'Intent.ACTION_PICK'","android.intent.action.PICK",actual.getAction());
        Assert.assertEquals("Intent type should be 'image/*'","image/*",actual.getType());

        Uri uri = getImageUri();
        Shadows.shadowOf(activity).receiveResult(actual, activity.RESULT_OK, new Intent().setData(uri));
    }

    private Uri getUriFromResource(int id) {
        Resources res = activity.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(id)
                + '/' + res.getResourceTypeName(id) + '/' + res.getResourceEntryName(id) );
    }

 */
}
