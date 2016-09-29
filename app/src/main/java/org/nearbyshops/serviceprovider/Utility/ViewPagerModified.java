package org.nearbyshops.serviceprovider.Utility;

/**
 * Created by sumeet on 29/9/16.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import com.google.common.base.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class ViewPagerModified extends ViewPager {

    @SuppressWarnings("UnusedDeclaration")
    public ViewPagerModified(Context context) {
        super(context);
    }

    @SuppressWarnings("UnusedDeclaration")
    public ViewPagerModified(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Fragment getActiveFragment(FragmentManager fragmentManager, int position) {
        final String name = makeFragmentName(getId(), position);
        final Fragment fragmentByTag = fragmentManager.findFragmentByTag(name);
        if (fragmentByTag == null) {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            fragmentManager.dump("", null, new PrintWriter(outputStream, true), null);
            final String s = new String(outputStream.toByteArray(), Charsets.UTF_8);
            throw new IllegalStateException("Could not find fragment via hacky way.\n" +
                    "We were looking for position: " + position + " name: " + name + "\n" +
                    "Fragment at this position does not exists, or hack stopped working.\n" +
                    "Current fragment manager dump is: " + s);
        }
        return fragmentByTag;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
