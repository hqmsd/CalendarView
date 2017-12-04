package com.example.hou.calendarview;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private CalendarView calendarView1;
private TextView textView;
private TextView textView2;
private TextView textView3;
private String ho;
private String data;
private String hou;
private String res;
private String nong;
private String yue;
private String ji;
private String yi;
private int year;
private int week;
private int day;
private int juli;
private Date date;
private int wee;
private String an;
public static final int UPDATE_TEXT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal);
        textView = (TextView)findViewById(R.id.title);
        textView2 = (TextView)findViewById(R.id.nongli);
        textView3 = (TextView)findViewById(R.id.jinji);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"2/han.ttf");
        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        textView3.setTypeface(typeface);
        Calendar calendar = Calendar.getInstance();
        calendarView1 = (CalendarView)findViewById(R.id.calendar1);
        calendarView1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                 data = i+"年"+(i1+1)+"月"+i2+"日";
                 hou = i+"-"+(i1+1)+"-"+i2;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date= simpleDateFormat.parse(hou,new ParsePosition(0));
                day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                week= Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
                year = Calendar.getInstance().get(Calendar.YEAR);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date);
                int weeko = calendar2.get(Calendar.WEEK_OF_YEAR);
                int days  = calendar2.get(Calendar.DAY_OF_WEEK);
                int yea  = calendar2.get(Calendar.YEAR);
                int dayss = calendar2.get(Calendar.DAY_OF_MONTH);
                if (weeko==1&&dayss>=21)
                {wee = weeko+ (52 * (yea - year))+52;}
                else
                {wee = weeko+(52 * (yea - year));}
                juli = Math.abs(7*(wee-week)+days-day);
                Toast.makeText(getApplicationContext(),"距离今天有："+juli,Toast.LENGTH_SHORT).show();
                sendRequst();
            }
        });
    }

    private void sendRequst(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://v.juhe.cn/calendar/day?date="+hou+"&key=d754d8afae8ce63f1396c3ae8924950f")
                            .build();
                    Response response = client.newCall(request).execute();
                    res = response.body().string();
                    parsegoson(res);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ho==null) {
                            textView.setText(nong+yue+"   "+an+"年");
                        }
                        else {
                            textView.setText(nong+ho+"   "+an+"年");
                        }
                        textView2.setText("宜："+ji);
                        textView3.setText("忌："+yi);
                    }
                });
            }
        }).start();}
    private void parsegoson(String res){
        Gson gson = new Gson();
        Ys ys = gson.fromJson(res,Ys.class);
        if (ys.getResult().toString()!="null") {
            ho = ys.getResult().getData().getHoliday();
            nong = ys.getResult().getData().getLunarYear();
            yue = ys.getResult().getData().getLunar();
            ji = ys.getResult().getData().getAvoid();
            yi = ys.getResult().getData().getSuit();
            an = ys.getResult().getData().getAnimalsYear();
        }
    }

}
