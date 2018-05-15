package com.example.admin.h2hpartner;

import android.os.Build;
import android.widget.TextView;

import com.example.admin.h2hpartner.UI.Pricing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

/**
 * Created by apple on 26/03/18.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)

public class HomeActivityTest {
    private Pricing activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(Pricing.class);
    }

    @Test
    public void validateTextViewContent() {
        TextView appNameTextView = (TextView) activity.findViewById(R.id.tvPricingName1);
        assertTrue("tvPricingName1".equals(appNameTextView.getText().toString()));
    }
}
