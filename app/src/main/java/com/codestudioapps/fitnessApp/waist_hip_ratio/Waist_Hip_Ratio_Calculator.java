package com.codestudioapps.fitnessApp.waist_hip_ratio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.codestudioapps.fitnessApp.R;
import com.codestudioapps.fitnessApp.general.MyApplication;
import com.codestudioapps.fitnessApp.utils.GlobalFunction;
import com.codestudioapps.fitnessApp.utils.SharedPreferenceManager;
import com.codestudioapps.fitnessApp.utils.TypefaceManager;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.NetworkRequestCheckListener;
import java.io.PrintStream;
import java.util.ArrayList;


public class Waist_Hip_Ratio_Calculator extends Activity {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_wrist;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_wrist = new ArrayList<>();
    float bmi_hip;
    float bmi_waist;
    EditText et_height;
    EditText et_weight;
    String gender = "";
    GlobalFunction globalFunction;
    String height_unit;
    float inserted_hip;
    float inserted_waist;
    ImageView iv_back;
    ListView listViewGender;
    ListView listViewHeight;
    private PopupWindow popupWindowGender;

    public PopupWindow popupWindowHeight;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_heightunit;
    TextView tv_search_whr;
    TextView tv_weightunit;
    TextView tv_whr;
    TypefaceManager typefaceManager;
    String weight_unit;
    double whr;


    public void attachBaseContext(Context context) {
        super.attachBaseContext(uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap(context));
    }


    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.waist_hip_ratio);
        this.globalFunction = new GlobalFunction(this);
        this.sharedPreferenceManager = new SharedPreferenceManager(this);
        this.typefaceManager = new TypefaceManager(getAssets(), this);
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.adView = (AdView) findViewById(R.id.adView);


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        this.et_height = (EditText) findViewById(R.id.et_height);
        this.et_weight = (EditText) findViewById(R.id.et_weight);
        this.tv_heightunit = (TextView) findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) findViewById(R.id.tv_weightunit);
        this.tv_search_whr = (TextView) findViewById(R.id.tv_search_whr);
        this.tv_genderunit = (TextView) findViewById(R.id.tv_genderunit);
        this.tv_gender = (TextView) findViewById(R.id.tv_gender);
        this.tv_whr = (TextView) findViewById(R.id.tv_whr);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.tv_whr.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_whr.setTypeface(this.typefaceManager.getBold());
        this.tv_genderunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(67108864);
        }
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight(true));
        this.tv_weightunit.setOnClickListener(showPopupWindowHeight(true));
        this.tv_gender.setOnClickListener(showPopupWindowGender());
        this.arraylist_wrist.clear();
        this.arraylist_wrist.add(getString(R.string.cm));
        this.arraylist_wrist.add(getString(R.string.inch));
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_wrist = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.text1, this.arraylist_wrist);
        this.tv_search_whr.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (Waist_Hip_Ratio_Calculator.this.et_height.getText().toString().trim().equals("") || Waist_Hip_Ratio_Calculator.this.et_height.getText().toString().trim().equals(".")) {
                    Waist_Hip_Ratio_Calculator.this.et_height.setError(Waist_Hip_Ratio_Calculator.this.getString(R.string.Enter_Height));
                } else if (Waist_Hip_Ratio_Calculator.this.et_weight.getText().toString().trim().equals("") || Waist_Hip_Ratio_Calculator.this.et_weight.getText().toString().trim().equals(".")) {
                    Waist_Hip_Ratio_Calculator.this.et_weight.setError(Waist_Hip_Ratio_Calculator.this.getString(R.string.Enter_Weight));
                } else {
                    Waist_Hip_Ratio_Calculator.this.height_unit = Waist_Hip_Ratio_Calculator.this.tv_heightunit.getText().toString();
                    Waist_Hip_Ratio_Calculator.this.weight_unit = Waist_Hip_Ratio_Calculator.this.tv_weightunit.getText().toString();
                    Waist_Hip_Ratio_Calculator.this.inserted_waist = Float.parseFloat(Waist_Hip_Ratio_Calculator.this.et_height.getText().toString());
                    Waist_Hip_Ratio_Calculator.this.inserted_hip = Float.parseFloat(Waist_Hip_Ratio_Calculator.this.et_weight.getText().toString());
                    StringBuilder sb = new StringBuilder();
                    sb.append("inserted_waist");
                    sb.append(Waist_Hip_Ratio_Calculator.this.inserted_waist);
                    Log.d("inserted_waist", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("inserted_height");
                    sb2.append(Waist_Hip_Ratio_Calculator.this.inserted_hip);
                    Log.d("inserted_height", sb2.toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("random_number==>");
                    sb3.append(random);
                    printStream.println(sb3.toString());
                    if (random == 2) {
                        Waist_Hip_Ratio_Calculator.this.showIntertitial();
                    } else {
                        Waist_Hip_Ratio_Calculator.this.calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Waist_Hip_Ratio_Calculator.this.onBackPressed();
            }
        });
    }

    private OnClickListener showPopupWindowHeight(final boolean z) {
        return new OnClickListener() {
            public void onClick(View view) {
                Waist_Hip_Ratio_Calculator.this.popupWindowHeight(z).showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowHeight(final boolean z) {
        this.popupWindowHeight = new PopupWindow(this);
        this.listViewHeight = new ListView(this);
        this.listViewHeight.setDividerHeight(0);
        if (z) {
            this.listViewHeight.setAdapter(this.adapter_wrist);
        } else {
            this.listViewHeight.setAdapter(this.adapter_wrist);
        }
        this.listViewHeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (z) {
                    Waist_Hip_Ratio_Calculator.this.tv_heightunit.setText((CharSequence) Waist_Hip_Ratio_Calculator.this.arraylist_wrist.get(i));
                } else {
                    Waist_Hip_Ratio_Calculator.this.tv_weightunit.setText((CharSequence) Waist_Hip_Ratio_Calculator.this.arraylist_wrist.get(i));
                }
                Waist_Hip_Ratio_Calculator.this.popupWindowHeight.dismiss();
            }
        });
        this.popupWindowHeight.setFocusable(true);
        this.popupWindowHeight.setWidth(this.tv_heightunit.getMeasuredWidth());
        this.popupWindowHeight.setHeight(-2);
        this.popupWindowHeight.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), 17170443));
        this.popupWindowHeight.setContentView(this.listViewHeight);
        return this.popupWindowHeight;
    }

    private OnClickListener showPopupWindowGender() {
        return new OnClickListener() {
            public void onClick(View view) {
                Waist_Hip_Ratio_Calculator.this.popupWindowGender().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowGender() {
        this.popupWindowGender = new PopupWindow(this);
        this.listViewGender = new ListView(this);
        this.listViewGender.setDividerHeight(0);
        this.listViewGender.setAdapter(this.adapter_wrist);
        this.listViewGender.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Waist_Hip_Ratio_Calculator.this.tv_gender.setText((CharSequence) Waist_Hip_Ratio_Calculator.this.arraylist_gender.get(i));
                Waist_Hip_Ratio_Calculator.this.tv_genderunit.setText((CharSequence) Waist_Hip_Ratio_Calculator.this.arraylist_gender.get(i));
                Waist_Hip_Ratio_Calculator.this.dismissPopupGender();
            }
        });
        this.popupWindowGender.setFocusable(true);
        this.popupWindowGender.setWidth(this.tv_gender.getMeasuredWidth());
        this.popupWindowGender.setHeight(-2);
        this.popupWindowGender.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), 17170443));
        this.popupWindowGender.setContentView(this.listViewGender);
        return this.popupWindowGender;
    }


    public void dismissPopupGender() {
        if (this.popupWindowGender != null) {
            this.popupWindowGender.dismiss();
        }
    }


    public void calculate() {
        StringBuilder sb = new StringBuilder();
        sb.append("inserted_waist");
        sb.append(this.inserted_waist);
        Log.d("inserted_waist", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("inserted_hip");
        sb2.append(this.inserted_hip);
        Log.d("inserted_hip", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("height_unit");
        sb3.append(this.height_unit);
        Log.d("height_unit", sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("weight_unit");
        sb4.append(this.weight_unit);
        Log.d("weight_unit", sb4.toString());
        if (this.height_unit.equalsIgnoreCase(getString(R.string.inch))) {
            this.bmi_waist = this.inserted_waist;
        } else {
            this.bmi_waist = this.inserted_waist / 2.54f;
        }
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.inch))) {
            this.bmi_hip = this.inserted_hip;
        } else {
            this.bmi_hip = this.inserted_hip / 2.54f;
        }
        this.gender = this.tv_gender.getText().toString().trim();
        this.whr = (double) (this.bmi_waist / this.bmi_hip);
        String str = this.gender.equals(getString(R.string.Female)) ? this.whr <= 0.8d ? "Low" : (this.whr <= 0.8d || this.whr >= 0.85d) ? "High" : "Moderate" : this.whr <= 0.95d ? "Low" : (this.whr <= 0.95d || this.whr >= 1.0d) ? "High" : "Moderate";
        StringBuilder sb5 = new StringBuilder();
        sb5.append("whr->");
        sb5.append(String.format("%.2f", new Object[]{Double.valueOf(this.whr)}));
        Log.d("whr->", sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append("gender->");
        sb6.append(this.gender);
        Log.d("gender->", sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append("health_risk->");
        sb7.append(str);
        Log.d("health_risk->", sb7.toString());
        Intent intent = new Intent(this, Waist_Hip_Ratio_Result.class);
        intent.putExtra("whr", String.format("%.2f", new Object[]{Double.valueOf(this.whr)}));
        intent.putExtra("health_risk", str);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        this.adView.setVisibility(8);
        ActivityCompat.finishAfterTransition(this);
    }

    public void showIntertitial() {
        if (this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            calculate();
        } else if (MyApplication.interstitial == null || !MyApplication.interstitial.isLoaded()) {
            if (!MyApplication.interstitial.isLoading()) {
                ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                    public void onNoResponse() {
                    }

                    public void onResponseObtained() {
                        MyApplication.interstitial.loadAd(new Builder().build());
                    }
                });
            }
            calculate();
        } else {
            MyApplication.interstitial.show();
        }
    }


    public void onResume() {
        super.onResume();
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue() && MyApplication.interstitial != null && !MyApplication.interstitial.isLoaded() && !MyApplication.interstitial.isLoading()) {
            ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                public void onNoResponse() {
                }

                public void onResponseObtained() {
                    MyApplication.interstitial.loadAd(new Builder().build());
                }
            });
        }
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            MyApplication.interstitial.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    MyApplication.interstitial.loadAd(new Builder().build());
                    Waist_Hip_Ratio_Calculator.this.calculate();
                }

                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (MyApplication.interstitial != null && !MyApplication.interstitial.isLoading()) {
                        ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                            public void onNoResponse() {
                            }

                            public void onResponseObtained() {
                                MyApplication.interstitial.loadAd(new Builder().build());
                            }
                        });
                    }
                }
            });
        }
    }
}
