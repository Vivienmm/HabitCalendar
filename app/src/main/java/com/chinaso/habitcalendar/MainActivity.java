package com.chinaso.habitcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.habitcalendar.data.ACache;
import com.chinaso.habitcalendar.data.MarkEntity;
import com.chinaso.habitcalendar.utils.DateUtil;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.ib_calendar)
    ImageView ibCalendar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.fl_current)
    FrameLayout flCurrent;
    @BindView(R.id.rl_tool)
    RelativeLayout mRelativeTool;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    Map<String, Calendar> map = new HashMap<>();
    //int colorBlue,colorRed,colorGreen;
    ArrayList<MarkEntity> marksList = new ArrayList<>();
    private int mYear;
    private MarkEntity futureMarkEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));

            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        map.put(getSchemeCalendar(mYear, mCalendarView.getCurMonth(), mCalendarView.getCurDay(), 0xffFFD700, "今").toString(),
                getSchemeCalendar(mYear, mCalendarView.getCurMonth(), mCalendarView.getCurDay(), 0xffFFD700, "今"));
    }

    private void initData() {
        ACache aCache = ACache.get(MainActivity.this);
        //使用getAsObject()，直接进行强转
        marksList = (ArrayList<MarkEntity>) aCache.getAsObject("days");
        if (null == marksList) {
            marksList = new ArrayList<>();
        } else {
                int mSize = marksList.size();
                for (int i = 0; i < mSize ; i++) {
                    int year = marksList.get(i).getYear();
                    int month = marksList.get(i).getMonth();
                    int day = marksList.get(i).getDay();
                    Log.i("date--", "for" + year + month + day);
//                    colorBlue = MainActivity.this.getResources().getColor(R.color.blue);
//                    colorRed = MainActivity.this.getResources().getColor(R.color.red);
//                    colorGreen = MainActivity.this.getResources().getColor(R.color.green);
                    map.put(getSchemeCalendar(year, month, day, 	0xFFADD8E6, "记").toString(),
                            getSchemeCalendar(year, month, day, 0xFFADD8E6, "记"));

                }

            //此方法在巨大的数据量上不影响遍历性能，推荐使用
            mCalendarView.setSchemeDate(map);
        }


    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        if(isClick){
            int clickMonth = calendar.getMonth();
            int clickDay = calendar.getDay();
            int clickYear = calendar.getYear();

            mTextLunar.setVisibility(View.VISIBLE);
            mTextYear.setVisibility(View.VISIBLE);
            mTextMonthDay.setText(clickMonth + "月" + clickDay + "日");
            mTextYear.setText(String.valueOf(clickYear));
            mTextLunar.setText(calendar.getLunar());

            if(futureMarkEntity!=null){
                map.remove(getSchemeCalendar(futureMarkEntity.getYear(), futureMarkEntity.getMonth(), futureMarkEntity.getDay(), 0xFF40db25, "未").toString());
            }
            if (map.containsKey(getSchemeCalendar(clickYear, clickMonth, clickDay, 0xFFADD8E6, "记").toString())) {
                map.remove(getSchemeCalendar(clickYear, clickMonth, clickDay, 0xFFADD8E6, "记").toString());
                marksList.remove(new MarkEntity(clickYear, clickMonth, clickDay));

            } else {
                MarkEntity newEntity = new MarkEntity(clickYear, clickMonth, clickDay);
                marksList.add(newEntity);

                if (null != futureMarkEntity && DateUtil.getTimeDistance(DateUtil.entittyToDate(newEntity), DateUtil.entittyToDate(futureMarkEntity)) > 0) {
                    map.put(getSchemeCalendar(clickYear, clickMonth, clickDay, 0xFFdf1356, "记").toString(),
                            getSchemeCalendar(clickYear, clickMonth, clickDay, 0xFFdf1356, "记"));
                } else {
                    map.put(getSchemeCalendar(clickYear, clickMonth, clickDay, 0xFFADD8E6, "记").toString(),
                            getSchemeCalendar(clickYear, clickMonth, clickDay, 0xFFADD8E6, "记"));
                }

                MarkEntity lastEntity=null;
                int currentSize=marksList.size();
                if(currentSize>1){
                    lastEntity=marksList.get(currentSize-2);
                    int gap=DateUtil.getTimeDistance(DateUtil.entittyToDate(lastEntity), DateUtil.entittyToDate(newEntity));
                    futureMarkEntity = DateUtil.getSomeDay(DateUtil.entittyToDate(newEntity), gap);
                    map.put(getSchemeCalendar(futureMarkEntity.getYear(), futureMarkEntity.getMonth(), futureMarkEntity.getDay(), 0xFF40db25, "未").toString(),
                            getSchemeCalendar(futureMarkEntity.getYear(), futureMarkEntity.getMonth(),  futureMarkEntity.getDay(), 0xFF40db25, "未"));
                }

            }

            mCalendarView.setSchemeDate(map);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        ACache aCache = ACache.get(MainActivity.this);
        if (marksList.size() > 1) {
            Collections.sort(marksList);
            int size = marksList.size();
            MarkEntity lastMarkEntity = marksList.get(size - 1);
            MarkEntity beforeMarkEntity = marksList.get(size - 2);


            Date dateLast = DateUtil.entittyToDate(lastMarkEntity);
            Date dateBefore = DateUtil.entittyToDate(beforeMarkEntity);

            int gap = DateUtil.getTimeDistance(dateBefore, dateLast);

            MarkEntity newDate = DateUtil.getSomeDay(dateLast, gap);
            marksList.add(newDate);


        }
        aCache.clear();
        Log.i("date--", "onPause-days" + marksList.size());
        aCache.put("days", marksList);
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onClick(View v) {

    }


}
