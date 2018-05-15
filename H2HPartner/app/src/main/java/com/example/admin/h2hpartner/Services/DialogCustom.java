package com.example.admin.h2hpartner.Services;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.admin.h2hpartner.Services.CustomServices;
import com.example.admin.h2hpartner.R;


public class DialogCustom extends PopupWindow implements View.OnClickListener {
    Activity activity;
    Context context;
    PopupWindow popW;
    View popUp;
    private DialogButtonClick mClickListener;
    TextView tvTitle;
    String lsTitle = "";
    Boolean lbCancelVisible;
    ImageView ivClose;

    ImageView imageDetail;
    TextView textDetail;

    public static final String TAG = "Login";


    public DialogCustom(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void showDialog() {


      /*  popUp =  LayoutInflater.from(context).inflate(R.layout.dialog_custom,null);



        tvTitle   = (TextView)popUp.findViewById(R.id.tvTitle);
        ivClose = (ImageView)popUp.findViewById(R.id.ivClose);

        tvTitle.setText(lsTitle);
       // tvMessage.setText(lsMessage);
       // cbOK.setOnClickListener(this);
       // cbCancel.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        popW = new PopupWindow(popUp,0,0,true);
        Bundle b = CustomServices.getCoordinates(context, .80, .40);
        popW.setWidth(b.getInt("WIDTH"));
        popW.setHeight(b.getInt("HEIGHT"));
        popW.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popW.setOutsideTouchable(false);
       // getData();




        popW.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

      *//*  popUp.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_down));*//*
*/
    }


/*
    private void getData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LISTPRINTERADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:"+response);
                if (response.contains("name"));
                JSONObject jobject = null;

                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i =0 ; i<jsonArray.length() ; i++){

                        ProductGetterSetter productGetterSetter = new ProductGetterSetter();
                        jobject = jsonArray.getJSONObject(i);
                        productGetterSetter.setId(Integer.parseInt(jobject.getString("pro_id")));
                        productGetterSetter.setName(jobject.getString("pro_name"));
                        productGetterSetter.setImage( NFServices.getImageURL() + "/"+jobject.getString("pro_img"));

                        textDetail.setText(productGetterSetter.getName().trim());
                        //imageDetail.setImageResource(PicassoClient.downloadImage(context,productGetterSetter.getImage());

                        Picasso
                                .with(context)
                                .load(productGetterSetter.getImage())
                                .into(imageDetail);


                        // PGS.add(productGetterSetter);




                    }//Log.d(TAG,"array"+PGS);
                    //  adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Check your network connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(id));



                return params;
            }



        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
*/


    public void setDialogButtonClickListener(DialogButtonClick listener) {
        mClickListener = listener;


    }

    public interface DialogButtonClick {

        void DialogButtonClicked(View view);
    }

    @Override
    public void onClick(View v) {
        mClickListener.DialogButtonClicked(v);
        if (popW != null) {
            popW.dismiss();

        } else {

            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(popUp);
            popUp = null;
        }
    }


    public void setTitle(String lsTitle) {

        this.lsTitle = lsTitle;
    }

    //  public void setMessage(String lsMessage)
    //  {
    //     this.lsMessage = lsMessage;
    // }

    public void setCancel(boolean lbEnableCancel) {
        lbCancelVisible = lbEnableCancel;
    }

}