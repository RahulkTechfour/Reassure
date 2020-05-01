package com.luminous.pdi.calendar.adapter.CalenderGrid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.luminous.pdi.R;
import com.luminous.pdi.calendar.Singleton;
import com.luminous.pdi.calendar.adapter.CalendarGridviewAdapter;
import com.luminous.pdi.calendar.dao.CalendarDecoratorDao;
import com.luminous.pdi.calendar.fragment.CalendarFragment;
import com.luminous.pdi.calendar.responce.PendingVisitDatum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalenderGrid extends BaseAdapter {
    private static final String TAG = CalendarGridviewAdapter.class.getSimpleName();
    private Context mContext;
    public static int firstDay;
    private ArrayList<CalendarDecoratorDao> mEventList;
    private View mPreviousView;
    private final Calendar calendar = Calendar.getInstance();
    Drawable highlightDrawable;
    private static final int color = Color.parseColor("#228BC34A");
    private Calendar mCalendar;
    private GregorianCalendar month;
    ArrayList<PendingVisitDatum> pendiingDataNum;
    public CalenderGrid(Context c, ArrayList<CalendarDecoratorDao> items, ArrayList<PendingVisitDatum> pendiingDataNum, GregorianCalendar month) {
        this.mEventList = items;
        mContext = c;
        this.pendiingDataNum=pendiingDataNum;
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
    }



    public int getCount() {
        return mEventList.size();
    }

    public CalendarDecoratorDao getItem(int position) {
        return mEventList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CalenderGrid.CalendarGridViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.calendar_item, null);
            holder = new CalenderGrid.CalendarGridViewHolder(convertView);
            int dimen = mContext.getResources().getDimensionPixelSize(R.dimen.common_40_dp);
            GridView.LayoutParams pParams = new GridView.LayoutParams(dimen, dimen);
            convertView.setLayoutParams(pParams);
            convertView.setTag(holder);



        } else {
            holder = (CalenderGrid.CalendarGridViewHolder) convertView.getTag();
        }

        // comment here
        CalendarDecoratorDao content = getItem(position);
        content.setPosition(position);
        holder.setDay(convertView, content);
        holder.setSelectedView(convertView, content);
        holder.bindDate(content);

        return convertView;
    }

    /**
     * @param view
     * @param selectedGridDate
     * @return
     */
    public View setSelected(View view, String selectedGridDate) {
        if (mPreviousView != null) {
            mPreviousView.findViewById(R.id.llCalendarItem);
            mPreviousView.setBackgroundResource(R.drawable.list_item_background);

            TextView txt = (TextView) mPreviousView.findViewById(R.id.date);
            txt.setTextColor(Color.BLACK);


            ImageView img1 = (ImageView) mPreviousView.findViewById(R.id.date_icon);
            img1.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));
            ImageView img2 = (ImageView) mPreviousView.findViewById(R.id.date_icon2);
            img2.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));
            ImageView img3 = (ImageView) mPreviousView.findViewById(R.id.date_icon3);
            img3.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));

            ImageView img4 = (ImageView) mPreviousView.findViewById(R.id.date_icon4);
            img4.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));
        }

        mPreviousView = view;
        view.setBackgroundResource(R.drawable.calender_oval_view);

        TextView txt = (TextView) view.findViewById(R.id.date);
        txt.setTextColor(Color.BLACK);

        ImageView img1 = (ImageView) view.findViewById(R.id.date_icon);
        img1.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));
        ImageView img2 = (ImageView) view.findViewById(R.id.date_icon2);
        img2.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));
        ImageView img3 = (ImageView) view.findViewById(R.id.date_icon3);
        img3.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));

        ImageView img4 = (ImageView) view.findViewById(R.id.date_icon4);
        img4.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));


        Singleton.getInstance().setCurrentDate(selectedGridDate);
        CalendarFragment.currentDateSelected = selectedGridDate;
        return view;
    }

    /**
     *
     */

    class CalendarGridViewHolder {
        TextView dayView;
        ImageView imgDecorator1;
        ImageView imgDecorator2;
        ImageView imgDecorator3;
        ImageView imgDecorator4;



        /**
         * @param v
         */
        public CalendarGridViewHolder(View v) {
            setLayoutParam(v);
            dayView = (TextView) v.findViewById(R.id.date);
            imgDecorator1 = (ImageView) v.findViewById(R.id.date_icon);
            imgDecorator2 = (ImageView) v.findViewById(R.id.date_icon2);
            imgDecorator3 = (ImageView) v.findViewById(R.id.date_icon3);
            imgDecorator4 = (ImageView) v.findViewById(R.id.date_icon4);

        }

        /**
         * @param view
         */
        private void setLayoutParam(View view) {
            int dimen = mContext.getResources().getDimensionPixelSize(R.dimen.common_40_dp);
            GridView.LayoutParams pParams = new GridView.LayoutParams(dimen, dimen);
            view.setLayoutParams(pParams);
        }

        /**
         * @param view
         * @param content
         */
        private void setDay(View view, CalendarDecoratorDao content) {
            String day = content.getDay();
            view.setVisibility(View.VISIBLE);

            if ((Integer.parseInt(day) > 1) && (content.getPosition() < firstDay)) {
                view.setVisibility(View.INVISIBLE);
            } else if ((Integer.parseInt(day) < 7) && (content.getPosition() > 28)) {
                view.setVisibility(View.INVISIBLE);
            } else {
                dayView.setTextColor(Color.BLACK);




            }
        }

        /**
         * @param v
         * @param content
         */
        public void setSelectedView(View v, CalendarDecoratorDao content) {
            String day = content.getDay();


            if (content.getDate().equals(Singleton.getInstance().getCurrentDate())) {
                setSelected(v, Singleton.getInstance().getCurrentDate());
                mPreviousView = v;
            } else {
                v.setBackgroundResource(R.drawable.list_item_background);
                if (content.getDate().equals(Singleton.getInstance().getTodayDate())) {
                    TextView txtTodayDate = (TextView) v.findViewById(R.id.date);
                    txtTodayDate.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                }
            }
            dayView.setText(day);



            month = Singleton.getInstance().getMonth();

            mCalendar = month;
            mCalendar.set(Calendar.DAY_OF_MONTH, 1);
            mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
            mCalendar.setFirstDayOfWeek(Calendar.SATURDAY);

            highlightDrawable = new ColorDrawable(color);
            int weekDay = mCalendar.get(Calendar.DAY_OF_WEEK);
            if (weekDay==Calendar.SUNDAY || weekDay==Calendar.SUNDAY){
                //  dayView.setTextColor(Color.RED);
                //  System.out.println(mCalendar.get(Calendar.DAY_OF_MONTH));
                //  dayView.getText();
                //  System.out.println(dayView.getText());

            }
        }

        /**
         * @param content
         */
        public void bindDate(CalendarDecoratorDao content) {
            String date = content.getDate();
            if (date.length() == 1) {
                date = "0" + date;
            }

            setDecoratorVisibility(date, content);
        }

        /**
         * @param date
         * @param content
         */
        private void setDecoratorVisibility(String date, CalendarDecoratorDao content) {
            if (date.length() > 0 && content.getCount() > 0) {
                switch (content.getCount()) {
                    case 1:
                        imgDecorator1.setVisibility(View.VISIBLE);
                        imgDecorator2.setVisibility(View.GONE);
                        imgDecorator3.setVisibility(View.GONE);
                        imgDecorator4.setVisibility(View.GONE);

                        break;
                    case 2:
                        imgDecorator1.setVisibility(View.VISIBLE);
                        imgDecorator2.setVisibility(View.VISIBLE);
                        imgDecorator3.setVisibility(View.GONE);
                        imgDecorator4.setVisibility(View.GONE);

                        break;
                    case 3:
                        imgDecorator1.setVisibility(View.VISIBLE);
                        imgDecorator2.setVisibility(View.VISIBLE);
                        imgDecorator3.setVisibility(View.VISIBLE);
                        imgDecorator4.setVisibility(View.VISIBLE);

                        break;

                    default:
                        imgDecorator1.setVisibility(View.VISIBLE);
                        imgDecorator2.setVisibility(View.VISIBLE);
                        imgDecorator3.setVisibility(View.VISIBLE);
                        imgDecorator4.setVisibility(View.VISIBLE);

                        break;
                }

            } else {
                imgDecorator1.setVisibility(View.INVISIBLE);
                imgDecorator2.setVisibility(View.INVISIBLE);
                imgDecorator3.setVisibility(View.INVISIBLE);
                imgDecorator4.setVisibility(View.INVISIBLE);

            }
        }
    }


}
