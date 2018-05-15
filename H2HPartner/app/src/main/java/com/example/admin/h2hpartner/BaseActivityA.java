package com.example.admin.h2hpartner;/*
package com.example.admin.h2hpartner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.h2hpartner.Services.StaticRefs;
import com.example.admin.h2hpartner.UI.Login;

public class BaseActivityA extends Activity implements View.OnClickListener {

    Context context;
    Activity activity;
    public LinearLayout baseLayout,llfooter,llheading;
    TextView tvTitle;
    ImageView ivSearch ,ivActivityTitle, ivClose,ivback;
    DrawerLayout mDrawer;
    LinearLayout leftMenu, llDrawerLogout, llDrawerMyReport, llDrawerMyBookings,llDrawerPrescribtion;
    ImageView icon;
    Intent intent;
    EditText etSearch;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    static String TAG = "BaseActivityA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.base_layout);

        context = this;
        activity = this;

        baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
        llfooter = (LinearLayout) findViewById(R.id.llfooter);
        llheading = (LinearLayout) findViewById(R.id.app_heading);



        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        icon = (ImageView) findViewById(R.id.ivMenu);
        leftMenu = (LinearLayout) findViewById(R.id.leftMenu);
        ivActivityTitle = (ImageView) findViewById(R.id.ivActivityTitle);
        llDrawerLogout = (LinearLayout) findViewById(R.id.llDrawerLogout);

        ivback = (ImageView) findViewById(R.id.ivback);
        tvTitle=(TextView)findViewById(R.id.tvActivityTitle);
        tvTitle.setVisibility(View.GONE);
        ivback.setVisibility(View.GONE);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);


        llDrawerLogout.setOnClickListener(this);
        icon.setOnClickListener(this);
        ivback.setOnClickListener(this);
       */
/* llDrawerAboutUs.setOnClickListener(this);
        llDrawerMyBookings.setOnClickListener(this);
        llDrawerPrescribtion.setOnClickListener(this);*//*

        //  ivSearch.setOnClickListener(this);


       */
/* sp = getSharedPreferences(App.PTData, context.MODE_PRIVATE);

        String lsMobile = sp.getString(Constants.BM_MOBILE, "");
*//*

      */
/*  if (lsMobile == null || lsMobile == "") {
            ivOrder.setVisibility(View.GONE);
        } else {
            ivOrder.setVisibility(View.VISIBLE);
        }

        ivOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderHistory.class);
                startActivity(intent);

            }
        });*//*


    }

    public void closeDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawers();
        }
    }


    @Override
    public void setContentView(int id) {

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(id, baseLayout);

    }


    @Override
    public void setTitle(CharSequence ls_title) {
        icon.setVisibility(View.GONE);
        ivActivityTitle.setVisibility(View.GONE);
        ivback.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(ls_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivMenu:
                if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
                    closeDrawer();
                } else {
                    mDrawer.openDrawer(leftMenu);
                    //setupMenu();
                }
                break;

            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.llDrawerLogout:

                sp = context.getSharedPreferences("prefs_h2h", MODE_PRIVATE);
                editor = sp.edit();
                editor.remove(StaticRefs.TOKEN).commit();
                editor.commit();
                Intent intent = new Intent(activity, Login.class);
                startActivity(intent);
                finish();
                break;

        }

    }
    public void hideFooter(Boolean value){

        if (value ==true) {

         llfooter.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {

     */
/*   if(mDrawer.isDrawerOpen(Gravity.LEFT))
        {
            closeDrawer();
        }
        else if(etSearch.getVisibility() == View.VISIBLE)
        {
            etSearch.setVisibility(View.GONE);
            ivClose.setVisibility(View.GONE);
            ivSearch.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            CustomServices.hideSoftKeyboard(getParent());
        }
        else
        {
            super.onBackPressed();
        }*//*


     super.onBackPressed();
    }

}


*/
