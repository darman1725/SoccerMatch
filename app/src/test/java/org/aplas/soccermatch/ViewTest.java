package org.aplas.soccermatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.junit.Assert;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowPopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ViewTest {
    final String UNDECLARED_CODE = "APLAS-UNDECLARED";

    protected View getViewFromActivity(String name, Activity activity) {
        int id = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
        return (id > 0)?(View) activity.findViewById(id):null;
    }

    protected View getViewFromFragment(String name, Fragment fragment) {
        int id = fragment.getResources().getIdentifier(name, "id", fragment.getActivity().getPackageName());
        return (id > 0)?(View) fragment.getView().findViewById(id):null;
    }

    protected  String getViewName(View view) {
        return (view.getId()<0)?"":view.getResources().getResourceEntryName(view.getId());
    }

    protected int getRscId(String name, String type, Activity activity) {
        return activity.getResources().getIdentifier(name, type, activity.getPackageName());
    }

    private View currView;
    private boolean found;

    protected void findViewFromLayout(String name, View view) {
        String viewName = getViewName(view);
        if (viewName.equals(name)) {
            currView=view;
            found=true;
        } else {
            if (view instanceof ViewGroup) {
                int i=0;
                int childCount = ((ViewGroup) view).getChildCount();
                while (i<childCount && !found) {
                    findViewFromLayout(name,((ViewGroup) view).getChildAt(i));
                    i++;
                }
            }
        }
    }

    protected View getViLayout(String name, View view) {
        currView=null;
        found=false;
        //findViewFromLayout(name,view);
        return currView;
    }

    protected View getLayoutFromResource(String rscName, Activity activity, String layoutType) {
        int rscId = getRscId(rscName,"layout",activity);
        Assert.assertNotEquals(rscName+".xml is not exist!!",0,rscId);
        LayoutInflater li = LayoutInflater.from(activity.getApplicationContext());
        View view = li.inflate(rscId, null);
        Assert.assertEquals(rscName+".xml should be a "+layoutType,view.getClass().getSimpleName(),layoutType);
        return view;
    }

    protected View getViewFromLayout(String name, View layout) {
        //layout.getContext()
        int id = layout.getContext().getResources().getIdentifier(name, "id", layout.getContext().getPackageName());
        return (id > 0)?(View) layout.findViewById(id):null;
    }

    protected View getViewFromDialog(String name, Dialog layout) {
        //layout.getContext()
        int id = layout.getContext().getResources().getIdentifier(name, "id", layout.getContext().getPackageName());
        return (id > 0)?(View) layout.findViewById(id):null;
    }

    protected void testClassExist(String cname) {
        try {
            Class.forName(cname);
        } catch (Exception e) {
            fail("MainActivity is not exist");
        }
    }

    protected String testViewContent(View view, String expClass, String expText) {
        String viewName = testViewExist(view,expClass);
        assertEquals("Text of "+expClass+" '"+viewName+"' is wrong",expText,((TextView)view).getText().toString());
        return viewName;
    }

    protected void testViewContent(View view, String expClass, String expText, String expHint) {
        String viewName = testViewContent(view,expClass,expText);
        assertEquals("Hint of "+viewName+" is wrong",expHint,((TextView)view).getHint().toString());
    }

    protected void testViewContent(View view, String expClass, String expText, boolean expEnabled) {
        String viewName = testViewContent(view,expClass,expText);
        assertEquals("Enabled status "+viewName+" is wrong",expEnabled,view.isEnabled());
    }

    protected void testViewEnabled(View view, boolean expEnabled) {
        String viewName = getViewName(view);
        assertEquals("Enabled status "+viewName+" is wrong",expEnabled,view.isEnabled());
    }

    protected String testViewExist(View view, String expClass) {
        assertNotNull(expClass+" is not found",view);
        String viewName = getViewName(view);
        assertEquals("View with id="+viewName+" should be a "+expClass, expClass, view.getClass().getSimpleName());
        return viewName;
    }

    protected Object testFieldExist(String name, String expClass, Activity activity) {
        Object field = getFieldValue(activity,name);
        if (field.getClass().getSimpleName().equals("String")) {
            if (((String) field).equals(UNDECLARED_CODE)) {
                assertNotNull("Field " + name + " is not yet declared!!", field);
            }
        }
        assertEquals("Field '"+name+"' should be a "+expClass, expClass, field.getClass().getSimpleName());
        return field;
    }

    protected View testFieldViewExist(String name, String expClass, Activity activity) {
        Object field = getFieldValue(activity,name);
        if (field.getClass().getSimpleName().equals("String")) {
            if (((String) field).equals(UNDECLARED_CODE)) {
                assertNotNull("Field " + name + " is not yet declared!!", field);
            }
        }
        assertNotNull(expClass+" '"+name+"' should be initialized with 'findViewById'",field);
        assertEquals("Field '"+name+"' should be a "+expClass, expClass, field.getClass().getSimpleName());
        //assertNotEquals(expClass+" '"+name+"' should be initialized with 'findViewById'",0,((View)field).getId());
        return (View)field;
    }

    protected String testImageContent(View view, String expClass, ImageView.ScaleType scale, boolean bound) {
        String viewName = testViewExist(view,expClass);
        assertEquals("The scaleType in "+expClass+" with id="+viewName+" is should be "+scale.toString(),scale,((ImageView)view).getScaleType());
        assertEquals("The adjustViewBounds in "+expClass+" with id="+viewName+" is should be "+bound,bound,((ImageView)view).getAdjustViewBounds());
        return viewName;
    }

    protected int getImageResId(ImageView view) {
        return Shadows.shadowOf(view.getDrawable()).getCreatedFromResId();
    }
    protected String testImageContent(View view, String expClass, String src) {
        String viewName = testViewExist(view,expClass);
        Context context = view.getContext();
        int srcId = context.getResources().getIdentifier(src, "drawable",context.getPackageName());
        int actualId = getImageResId((ImageView)view);
        assertEquals("The image source in "+expClass+" with id="+viewName+" is not '"+src+"'",srcId,actualId);
        return viewName;
    }

    protected boolean checkImageContent(View view, String src) {
        Context context = view.getContext();
        int srcId = context.getResources().getIdentifier(src, "drawable",context.getPackageName());
        int actualId = getImageResId((ImageView)view);
        return srcId==actualId;
    }

    protected String testImageContent(View view, String expClass, String src, ImageView.ScaleType scale, boolean bound) {
        String viewName = testImageContent(view,expClass,src);
        assertEquals("The scaleType in "+expClass+" with id="+viewName+" is should be "+scale.toString(),scale,((ImageView)view).getScaleType());
        assertEquals("The adjustViewBounds in "+expClass+" with id="+viewName+" is should be "+bound,bound,((ImageView)view).getAdjustViewBounds());
        return viewName;
    }

    protected String getValueOf(ArrayList<String> list) {
        String str = "";
        for (int i=0; i<list.size(); i++) {
            str += list.get(i)+"-";
        }
        return str.substring(0,str.length()-1);
    }

    protected void performPopupClick(String team, String type, Activity activity) {
        ((ImageButton)getViewFromActivity("add"+team+type,activity)).performClick();
        PopupMenu popup = ShadowPopupMenu.getLatestPopupMenu();
        Menu menu = popup.getMenu();
        int idx = getRandomInteger(0,menu.size()-1);
        MenuItem item = menu.getItem(idx);
        Shadows.shadowOf(popup).getOnMenuItemClickListener().onMenuItemClick(item);
        //return item.getTitle().toString();
    }

    protected void addPlayerAction(ImageButton btn) {
        btn.performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        EditText player = (EditText)getViewFromDialog("playerName",dialog);
        String name = getRandomString(10);
        player.setText(name);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
    }

    protected Uri getImageUri(Activity main) {
        return getUriFromResource(R.drawable.icon_goal,main);
    }

    protected void imgButtonClick(ImageButton btn, Activity main) {
        btn.performClick();
        Intent actual = Shadows.shadowOf(main).getNextStartedActivity();
        Assert.assertEquals("Intent action should be 'Intent.ACTION_PICK'","android.intent.action.PICK",actual.getAction());
        Assert.assertEquals("Intent type should be 'image/*'","image/*",actual.getType());

        Uri uri = getImageUri(main);
        Shadows.shadowOf(main).receiveResult(actual, main.RESULT_OK, new Intent().setData(uri));
    }

    protected Uri getUriFromResource(int id, Activity main) {
        Resources res = main.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(id)
                + '/' + res.getResourceTypeName(id) + '/' + res.getResourceEntryName(id) );
    }

    public void testItem(Object expectVal, Object actualVal, String msg, int type) {
        switch (type) {
            case 1:  assertEquals(msg,expectVal,actualVal);
                break;
            case 2:  assertNotEquals(msg,expectVal,actualVal);
                break;
            case 3:  assertTrue(msg,(boolean)actualVal);
                break;
            case 4:  assertFalse(msg,(boolean)actualVal);
                break;
            case 5:  assertNull(msg,actualVal);
                break;
            case 6:  assertNotNull(msg,actualVal);
                break;
        }
    }

    public List<String> getMethodsName(Method[] methods) {
        List<String> list = new ArrayList<>();
        for (int i=0; i<methods.length; i++) {
            list.add(methods[i].getName());
        }
        return list;
    }

    public double getRandomDouble(int min, int max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public int getRandomInteger(int min, int max) {
        Random r = new Random();
        return min + r.nextInt(max - min + 1);
    }

    /*
    public Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException,IllegalAccessException {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(obj);
    }
    */

    public String getAccessName(int access) {
        if (access== Modifier.PUBLIC) {
            return "Public";
        } else if (access== Modifier.PRIVATE) {
            return "Private";
        } else if (access== Modifier.PROTECTED)  {
            return "Protected";
        } else if (access==17)  {
            return "Public Final";
        } else if (access==18)  {
            return "Private Final";
        } else {
            return "No Information";
        }
    }

    public void testField(Object obj, String fieldName, int access, Class<?> type, boolean isNull) {
        Class<?> activityClass = obj.getClass();
        Field f;
        try {
            f = activityClass.getDeclaredField(fieldName);
            if (access>=0) assertEquals("Access to field \'"+fieldName+"\' must be "+getAccessName(access),access,f.getModifiers());
            assertEquals("Type of field \'"+fieldName+"\' must be "+type.getSimpleName(), type, f.getType());
            f.setAccessible(true);
            if (isNull) {
                assertNull("Field \'"+fieldName+"\' must be Null", f.get(obj));
            } else  {
                assertNotNull("Field \'"+fieldName+"\' must be Constructed", f.get(obj));
            }
        } catch(Exception e) {
            assertTrue("Field "+fieldName+" is not declared!!",false);
        }
    }


    public Object getFieldValue(Object obj, String fieldName) {
        Class<?> activityClass = obj.getClass();
        Field f;
        try {
            f = activityClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch(Exception e) {
            String msg = UNDECLARED_CODE;
            return msg;
        }
    }

    public void testFieldValue(Activity activity, String fieldName, String className, Object value) {
        Object field = getFieldValue(activity,fieldName);
        if (field.getClass().getSimpleName().equals("String")) {
            if (((String)field).equals(UNDECLARED_CODE)) {
                fail("Field "+fieldName+" is not declared!!");
            } else {
                assertEquals("Field '"+fieldName+"' should be a "+className, className, field.getClass().getSimpleName());
                assertEquals("Value of field '"+fieldName+"' must be "+value.toString(), field, value);
            }
        } else {
            assertEquals("Field '"+fieldName+"' should be a "+className, className, field.getClass().getSimpleName());
            assertEquals("Value of field '"+fieldName+"' must be "+value.toString(), field, value);
        }
    }

    public void testMethod(Object obj, String methodName, int access, Class[] params, Class<?> returnType) {
        Class<?> activityClass = obj.getClass();
        Method m;
        try {
            m = activityClass.getDeclaredMethod(methodName,params);
            if (access>=0) assertEquals("Access to field \'"+methodName+"\' must be "+getAccessName(access),m.getModifiers(),access);
            assertEquals("Type of return field \'"+methodName+"\' must be "+returnType.getSimpleName(), m.getReturnType(), returnType);
        } catch(Exception e) {
            assertTrue("Field "+methodName+" is not declared or parameters is wrong!!",false);
        }
    }

    public String listToString(List x) {
        String res = "";
        for (int i=0; i<x.size(); i++){
            res += x.get(i)+";";
        }
        return res;
    }

    public String arrayToString(Object[] x) {
        String res = "";
        for (int i=0; i<x.length; i++){
            res += x[i]+";";
        }
        return res;
    }

    protected String getRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
