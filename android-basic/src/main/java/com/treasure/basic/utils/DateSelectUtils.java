package com.treasure.basic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.treasure.basic.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author：果冻
 * Time： 2019/09/29  11:57
 * Description：日期选择工具类
 */
@SuppressLint("SimpleDateFormat")
public class DateSelectUtils {

    public static final String FORMAT1 = "yyyy-MM-dd";
    public static final String FORMAT2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT3 = "yyyy/MM/dd";
    public static final String FORMAT4 = "yyyy/MM/dd HH:mm";
    public static final String FORMAT5 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT6 = "HH:mm";
    public static final String FORMAT7 = "MM/dd HH:mm";
    public static final String FORMAT8 = "MM-dd";
    public static final String FORMAT9 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT10 = "yyyyMMddHHmmss";
    public static final String FORMAT11 = "HH";
    public static final String FORMAT12 = "yyyy/MM";
    public static final String FORMAT13 = "yyyy-MM";
    public static final String FORMAT14 = "yyyy";
    public static final String FORMAT15 = "HH:mm:ss";
    public static final String FORMAT16 = "MM";
    public static final String FORMAT17 = "MM/dd";
    public static final String FORMAT18 = "M";
    public static final String FORMAT19 = "yyyy-MM-dd HH:mm:ss.SSS";


    /****************************时间选择器***************************/
    //年月日选择,输出Date
    public static void selectDate(Context context, OnTimeSelectListener listener) {
        selectDate(context, false, listener);
    }

    public static void selectDate(Context context, String startDateStr, String endDateStr, OnTimeSelectListener listener) {
        if (TextUtils.isEmpty(startDateStr) || startDateStr.length() != 10 || TextUtils.isEmpty(endDateStr) || endDateStr.length() != 10) {
            return;
        }
        if (!startDateStr.contains("-") || !endDateStr.contains("-")) {
            return;
        }
        String[] startSplit = startDateStr.split("-");
        String[] endSplit = endDateStr.split("-");
        // 系统当前时间
        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(Integer.valueOf(startSplit[0]), Integer.valueOf(startSplit[1]) - 1, Integer.valueOf(startSplit[2]));
        endDate.set(Integer.valueOf(endSplit[0]), Integer.valueOf(endSplit[1]) - 1, Integer.valueOf(endSplit[2]));

        TimePickerBuilder builder = new TimePickerBuilder(context, listener);
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("完成")
//                .setContentTextSize(18)
                .setTitleSize(18)
                .setTitleText("")
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setCancelColor(context.getResources().getColor(R.color.color_303133))
                .setDate(selectedDate)
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        builder.setRangDate(startDate, endDate);
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 年月日选择,输出Date
     * isLimit true:时间无限制    false:前后十年
     */
    public static void selectDate(Context context, boolean isLimit, OnTimeSelectListener listener) {

        // 系统当前时间
        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, listener);
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 默认年月日显示,截止时间为当前时间
     */
    public static void selectDtEndCurrent(Context context, OnTimeSelectListener listener) {

        Calendar selectedDate = Calendar.getInstance();         //系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        TimePickerView pvCustomTime = new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, true, false, false, false})      //default is all
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 默认年月日显示,截止时间为当前时间
     */
    public static void selectDtEndCurrent(Context context, int defaultYear, OnTimeSelectListener listener) {

        Calendar selectedDate = Calendar.getInstance();         //系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(defaultYear, 0, 1);
        TimePickerView pvCustomTime = new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, true, false, false, false})      //default is all
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 默认年月日显示,截止时间为当前时间
     */
    public static void selectDtEndCurrent(Context context, String format, OnTimeSelectListener listener) {

        Calendar selectedDate = Calendar.getInstance();         //系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        TimePickerView pvCustomTime;
        TimePickerBuilder builder = new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, true, false, false, false})      //default is all
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY);
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 默认年月日显示,截止时间为当前时间
     */
    public static void selectDtEndCurrent(Context context, String format, OnSelectDateListener listener, OnDismissListener dismissListener) {

        Calendar selectedDate = Calendar.getInstance();         //系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        TimePickerView pvCustomTime;
        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        })
                .setType(new boolean[]{true, true, true, false, false, false})      //default is all
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY);
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        pvCustomTime = builder.build();
        pvCustomTime.setOnDismissListener(dismissListener::onDismiss);
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 默认年月日显示,起始时间为当前时间
     */
    public static void selectDtFromCurrent(Context context, OnTimeSelectListener listener) {
        selectDtFromCurrent(context, FORMAT1, listener);
    }

    /**
     * 起始时间为当前时间
     */
    public static void selectDtFromCurrent(Context context, String format, OnTimeSelectListener listener) {

        Calendar selectedDate = Calendar.getInstance();         //系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH)
                , selectedDate.get(Calendar.DATE), selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 11, 31);
        TimePickerBuilder builder = new TimePickerBuilder(context, listener)
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 0, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY);
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT5:
                case FORMAT9:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT7:
                    type = new boolean[]{false, true, true, true, true, false};
                    break;
                case FORMAT8:
                    type = new boolean[]{false, true, true, false, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                default:
                    break;
            }
        }
        builder.setType(type);
        builder.build().show();
    }

    /**
     * 年月选择,时间为十年前到当前,直接返回字符串
     *
     * @param isLimit 是否限制前后十年
     * @param listener 选择回调
     */
    public static void selectYearMonthPast10Years(Context context, boolean isLimit, OnSelectDateListener listener) {
        // 系统当前时间
        Calendar selectedDate = Calendar.getInstance();
        // 设置十年前作为开始时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);

        // 结束时间为当前时间
        Calendar endDate = Calendar.getInstance();

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime("yyyy.MM", date);
            listener.onSelect(time);
        });
/**
 * .setTitleBgColor(Color.WHITE)
 *                 .setDividerColor(context.getResources().getColor(R.color.colorEEE))
 *                 .setCancelColor(context.getResources().getColor(R.color.color999))
 *                 .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
 *                 .setTextColorCenter(context.getResources().getColor(R.color.color333))
 *                 .setCancelText("取消")
 *                 .setSubmitText("完成")
 *                 .setTitleSize(18)
 *                 .setTitleText("")
 *                 .setContentTextSize(17)
 *                 .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
 *                 .setDate(selectedDate)
 *                 .setLabel("年", "月", "日", "", "", "秒")
 *                 .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
 *                 .setDividerColor(Color.LTGRAY)
 *                 //新UI
 *                 .setCancelText(" ")
 *                 .setSubmitText("确认")
 *                 .setTextColorOut(Color.parseColor("#A2A7BC"))
 *                 .setTextColorCenter(Color.parseColor("#1152FF"))
 *                 .setContentTextSize(14)
 *                 .setDividerType(WheelView.DividerType.FILL)
 *                 .setDividerColor(Color.parseColor("#00000000"))
 *                 .setTitleBgColor(Color.parseColor("#00000000"))
 *                 .setDividerColor(Color.WHITE)
 */
        builder.setType(new boolean[]{true, true, false, false, false, false}) // 只显示年月
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "", "", "", "")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                //新UI
                .setCancelText(" ")
                .setSubmitText("确认")
                .setTextColorOut(Color.parseColor("#A2A7BC"))
                .setTextColorCenter(Color.parseColor("#1152FF"))
                .setContentTextSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .setDividerColor(Color.parseColor("#00000000"))
                .setTitleBgColor(Color.parseColor("#00000000"))
                .setDividerColor(Color.WHITE)
                .build();

        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }

        TimePickerView pvCustomTime = builder.build();
//        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource();
        pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.GONE);
        pvCustomTime.show();
    }

    /**
     * 年月日选择,直接返回字符串
     *
     * @param isLimit 是否限制前后十年
     * @param format  输出时间格式
     */
    public static void selectDate(Context context, boolean isLimit, String format, OnSelectDateListener listener) {

        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "", "", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                //新UI
                .setCancelText(" ")
                .setSubmitText("确认")
                .setTextColorOut(Color.parseColor("#A2A7BC"))
                .setTextColorCenter(Color.parseColor("#1152FF"))
                .setContentTextSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .setDividerColor(Color.parseColor("#00000000"))
                .setTitleBgColor(Color.parseColor("#00000000"))
                .setDividerColor(Color.WHITE)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
//        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        if (!TextUtils.isEmpty(format)) {
            if(format.equals(FORMAT2)){
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.VISIBLE);
            }else {
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.GONE);
            }
        }
        pvCustomTime.show();
    }
    /**
     * 年月日选择,时间为当前后3天,直接返回字符串
     *
     * @param isLimit 是否限制前后十年
     * @param format  输出时间格式
     */
    public static void selectDateSetRangeSetSelectedEarliest(Context context, boolean isLimit, String format,Calendar startDate,Calendar endDate
            , OnSelectDateListener listener
             ) {

        Calendar selectedDate = startDate;
//        // 设置前后十年
//        Calendar startDate = Calendar.getInstance();
//        // 开始时间设置为当前时间之后3天
//        startDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH) + 3);
//        Calendar endDate = Calendar.getInstance();
////        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
//        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "", "", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                //新UI
                .setCancelText(" ")
                .setSubmitText("确认")
                .setTextColorOut(Color.parseColor("#A2A7BC"))
                .setTextColorCenter(Color.parseColor("#1152FF"))
                .setContentTextSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .setDividerColor(Color.parseColor("#00000000"))
                .setTitleBgColor(Color.parseColor("#00000000"))
                .setDividerColor(Color.WHITE)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
//        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        if (!TextUtils.isEmpty(format)) {
            if(format.equals(FORMAT2)){
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.VISIBLE);
            }else {
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.GONE);
            }
        }
        pvCustomTime.show();
    }
    /**
     * 年月日选择,时间为当前后3天,直接返回字符串
     *
     * @param isLimit 是否限制前后十年
     * @param format  输出时间格式
     */
    public static void selectDateSetRange(Context context, boolean isLimit, String format,Calendar startDate,Calendar endDate
            , OnSelectDateListener listener
             ) {

        Calendar selectedDate = Calendar.getInstance();
//        // 设置前后十年
//        Calendar startDate = Calendar.getInstance();
//        // 开始时间设置为当前时间之后3天
//        startDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH) + 3);
//        Calendar endDate = Calendar.getInstance();
////        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
//        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "", "", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                //新UI
                .setCancelText(" ")
                .setSubmitText("确认")
                .setTextColorOut(Color.parseColor("#A2A7BC"))
                .setTextColorCenter(Color.parseColor("#1152FF"))
                .setContentTextSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .setDividerColor(Color.parseColor("#00000000"))
                .setTitleBgColor(Color.parseColor("#00000000"))
                .setDividerColor(Color.WHITE)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
//        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        if (!TextUtils.isEmpty(format)) {
            if(format.equals(FORMAT2)){
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.VISIBLE);
            }else {
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.GONE);
            }
        }
        pvCustomTime.show();
    }
    /**
     * 年月日选择,次日达
     *
     * @param isLimit 是否限制前后十年
     * @param format  输出时间格式
     */
    public static void selectDateNextDay(Context context, boolean isLimit, String format, OnSelectDateListener listener) {

        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        // 开始时间设置为当前时间之后3天
        startDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH) + 1);
//        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "", "", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                //新UI
                .setCancelText(" ")
                .setSubmitText("确认")
                .setTextColorOut(Color.parseColor("#A2A7BC"))
                .setTextColorCenter(Color.parseColor("#1152FF"))
                .setContentTextSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .setDividerColor(Color.parseColor("#00000000"))
                .setTitleBgColor(Color.parseColor("#00000000"))
                .setDividerColor(Color.WHITE)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
//        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        if (!TextUtils.isEmpty(format)) {
            if(format.equals(FORMAT2)){
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.VISIBLE);
            }else {
                pvCustomTime.findViewById(R.id.signPoint).setVisibility(View.GONE);
            }
        }
        pvCustomTime.show();
    }


    /**
     * 年月日选择,直接返回字符串
     *
     * @param isLimit 是否限制前后十年
     * @param format  输出时间格式
     */
    public static void selectDate(Context context, boolean isLimit, String format, OnSelectDateListener listener, OnDismissListener dismissListener) {

        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.setOnDismissListener(dismissListener::onDismiss);
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.bg_order_filter_background);
        pvCustomTime.show();
    }

    /**
     * 年月日选择,直接返回字符串
     *
     * @param startYear 开始年
     * @param format    输出时间格式
     */
    public static void selectDate(Context context, int startYear, String format, OnSelectDateListener listener) {

        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(startYear, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        builder.setRangDate(startDate, endDate);
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 年月日选择,直接返回字符串
     *
     * @param isLimit 是否限制前后十年
     * @param title   标题
     * @param format  输出时间格式
     */
    public static void selectDate(Context context, boolean isLimit, String title, String format, OnSelectDateListener listener) {

        Calendar selectedDate = Calendar.getInstance();
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);

        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText(title)
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                case FORMAT11:
                    type = new boolean[]{false, false, false, true, false, false};
                    break;
                case FORMAT12:
                case FORMAT13:
                    type = new boolean[]{true, true, false, false, false, false};
                    break;
                case FORMAT14:
                    type = new boolean[]{true, false, false, false, false, false};
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 年月日选择,直接返回字符串
     *
     * @param isLimit     是否限制前后yearLimit年
     * @param currentTime
     * @param format      输出时间格式
     */
    public static void selectDateLimitYear(Context context, boolean isLimit, int yearLimit, String currentTime, String format, OnSelectDateListener listener) {

        Calendar selectedDate = Calendar.getInstance();
        // 设置前后yearLimit年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - yearLimit, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + yearLimit, 11, 31);
        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            String time = simpleDateFormat.format(date);
            listener.onSelect(time);
        });

        Calendar currentCalender;
        if (currentTime.equals("请选择")) {
            currentCalender = selectedDate;
        } else {
            currentCalender = Calendar.getInstance();
            currentCalender.setTime(timeToDate(format, currentTime));
        }

        builder.setType(new boolean[]{true, true, true, false, false, false})
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("")
                .setContentTextSize(17)
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setDate(currentCalender)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 年月日+上午/下午
     *
     * @param context
     * @param format   输出时间格式
     * @param listener
     */
    public static void showHalfDayDialog(Context context, String format, OnSelectHalfDayListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 10);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date startDate = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 10);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 30);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date endDate = calendar.getTime();

//        DateTimeWheel2Dialog dialog = new DateTimeWheel2Dialog(context);
//        dialog.show();
//        int config = DateTimeWheel2Dialog.SHOW_YEAR_MONTH_DAY_HOUR;
//        dialog.configShowUI(config);
//        dialog.setCancelButton("取消", null);
//        dialog.setOKButton("完成", (v, selectedDate, amPm) -> {
//            listener.onSelect(dateToTime(format, selectedDate), amPm == 0);
//            return false;
//        });
//        dialog.setDateArea(startDate, endDate, true);
//        dialog.updateSelectedDate(new Date());
    }

    /**
     * 获取当前分钟
     *
     * @return
     */
    public static String getCurrentMinute() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("mm");
        Date date = new Date(currentTime);
        String sim = formatter.format(date);
        return sim;
    }

    /**
     * 获取当前小时
     *
     * @return 当前小时
     */
    public static int getCurrentHour() {
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前年月日
     */
    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT1);
        Date date = new Date(currentTime);
        String sim = formatter.format(date);
        return sim;
    }

    /**
     * 获取系统当前日期
     */
    public static String getCurrDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }


    /**
     * 获取当前时间戳
     *
     * @param format 时间格式
     * @return 当前时间戳
     */
    public static Long getCurrDateTimestamp(String format) {
        return timeToStamp(getCurrDate(format), format);
    }

    /************************日期时间转化***************************/

    public static Date timeToDate(String format, String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(time);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToTime(String format, Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static long timeToStamp(String time, String format) {
        try {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(format);
            Date parse = simpleDateFormat1.parse(time);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String stampToDate(String temp, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = Long.valueOf(temp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 年月日选择,直接返回字符串
     * TimePicker在popupWindow中弹出被覆盖的问题
     *
     * @param isLimit 是否限制前后十年
     * @param format  输出时间格式
     */
    public static void selectDateOnPopupWindow(Context context, boolean isLimit, String format, OnSelectDateListener listener) {

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH), 9, 0);
        // 设置前后十年
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 1);
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 0, 1);
//        TimePickerDialog dialog = new TimePickerDialog.Builder();
        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v) -> {
            String time = dateToTime(format, date);
            listener.onSelect(time);
        });
        builder.setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .setTitleBgColor(Color.WHITE)
                .setDividerColor(context.getResources().getColor(R.color.colorEEE))
                .setCancelColor(context.getResources().getColor(R.color.color999))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.color333))
                .setCancelText("取消")
                .setSubmitText("完成")
                .setTitleSize(18)
                .setTitleText("选择日期")
                //设置两横线之间的间隔倍数
                .setLineSpacingMultiplier(2f)
                .setContentTextSize(17)
                .setDate(selectedDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.LTGRAY)
                .build();
        if (isLimit) {
            builder.setRangDate(startDate, endDate);
        }
        boolean[] type = new boolean[]{};
        if (!TextUtils.isEmpty(format)) {
            switch (format) {
                case FORMAT1:
                case FORMAT3:
                    type = new boolean[]{true, true, true, false, false, false};
                    break;
                case FORMAT2:
                case FORMAT4:
                    type = new boolean[]{true, true, true, true, true, false};
                    break;
                case FORMAT6:
                    type = new boolean[]{false, false, false, true, true, false};
                    break;
                default:
                    break;
            }
        }
        builder.setType(type);
        TimePickerView pvCustomTime = builder.build();
        pvCustomTime.findViewById(com.bigkoo.pickerview.R.id.rv_topbar).setBackgroundResource(R.drawable.shape_bg_picker_title);
        pvCustomTime.show();
    }

    /**
     * 获取时间差
     */
    public static long getDays(String startTime, String endTime, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            long time1 = calendar.getTimeInMillis();
            calendar.setTime(endDate);
            long time2 = calendar.getTimeInMillis();
            long days = (time2 - time1) / (1000 * 3600 * 24);
            return days;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 根据Time获取间隔小时数
     */
    public static double getHours(String beginTime, String endTime) {
        return getHours(beginTime, endTime, FORMAT4);
    }

    /**
     * 根据Time获取间隔小时数
     */
    public static double getHours(String beginTime, String endTime, String format) {

        long beginTemp = timeToStamp(beginTime, format);
        long endTemp = timeToStamp(endTime, format);
        double between_days = ((endTemp - beginTemp) * 1.0) / ((1000 * 60 * 60) * 1.0);
        //构造方法的字符格式这里如果小数不足2位,会以0补足
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(between_days));
    }

    /**
     * 时间格式转换  1--->2
     */
    public static String format2format(String time, String format1, String format2) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(format1);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(format2);
        try {
            Date parse = simpleDateFormat1.parse(time);
            return simpleDateFormat2.format(parse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据两个秒数 获取两个时间差
     */
    public static String getDatePoor(long endDate, long nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的秒时间差异
        //long diff = (endDate * 1000L) - (nowDate * 1000L);
        long diff = endDate - nowDate;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String res = "";
        if (day != 0) {
            res += day + "天";
        }
        if (hour != 0) {
            res += hour + "小时";
        }
        if (min != 0) {
            res += min + "分钟";
        }
        return res;
    }

    /**
     * 计算时分格式的时间大小
     *
     * @param s1 时间1
     * @param s2 时间2
     * @return s2 > s1 : true
     */
    public static boolean compHMTime(String s1, String s2) {
        try {
            if (!s1.contains(":") || !s2.contains(":")) {
                return false;
            } else {
                String[] array1 = s1.split(":");
                int total1 = Integer.valueOf(array1[0]) * 3600 + Integer.valueOf(array1[1]) * 60;
                String[] array2 = s2.split(":");
                int total2 = Integer.valueOf(array2[0]) * 3600 + Integer.valueOf(array2[1]) * 60;
                return total2 - total1 > 0;
            }
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past 未来*天
     * @return 格式yyyy-MM-dd
     */
    public static String getFeatureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past 未来*天
     * @return 格式yyyy-MM-dd
     */
    public static String getFeatureDate(String time, String timeFormat, String resultFormat, int past) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, past);
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 获取指定月份的第一天
     *
     * @param formate1
     * @param formate2
     * @param s
     * @return
     */
    public static String getFirstDayInMonth(String formate1, String formate2, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(formate1);
        String monthEnd = "", monthStart = "";

        try {
            Date date = new SimpleDateFormat(formate2).parse(s);
            Calendar cal = Calendar.getInstance();
            //将传入的时间年月  转给cal对象
            cal.setTime(date);
            //在这个时间基础上设置为这个月的第一天
            cal.add(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            //获取这个格式的第一天
            monthStart = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            //获取这个月的最后一天
            monthEnd = sdf.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthStart;
    }

    /**
     * 获取指定月份的最后一天
     *
     * @param formate1
     * @param formate2
     * @param s
     * @return
     */
    public static String getEndDayInMonth(String formate1, String formate2, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(formate1);
        String monthEnd = "", monthStart = "";

        try {
            Date date = new SimpleDateFormat(formate2).parse(s);
            Calendar cal = Calendar.getInstance();
            //将传入的时间年月  转给cal对象
            cal.setTime(date);
            //在这个时间基础上设置为这个月的第一天
            cal.add(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            //获取这个格式的第一天
            monthStart = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            //获取这个月的最后一天
            monthEnd = sdf.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthEnd;
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param time         指定日期
     * @param timeFormat   指定日期格式
     * @param resultFormat 返回的日期格式
     */
    public static String getFirstDayOfMonth(String time, String timeFormat, String resultFormat) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @param time         指定日期
     * @param timeFormat   指定日期格式
     * @param resultFormat 返回的日期格式
     */
    public static String getLastDayOfMonth(String time, String timeFormat, String resultFormat) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 返回指定日期的周的第一天
     *
     * @param time         指定日期
     * @param timeFormat   指定日期格式
     * @param resultFormat 返回的日期格式
     */
    public static String getFirstDayOfLastWeek(String time, String timeFormat, String resultFormat) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1 * 7);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 返回指定日期的上周的最后一天
     *
     * @param time         指定日期
     * @param timeFormat   指定日期格式
     * @param resultFormat 返回的日期格式
     */
    public static String getLastDayOfLastWeek(String time, String timeFormat, String resultFormat) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1 * 7);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 返回指定日期的周的第一天
     *
     * @param time         指定日期
     * @param timeFormat   指定日期格式
     * @param resultFormat 返回的日期格式
     */
    public static String getFirstDayOfWeek(String time, String timeFormat, String resultFormat) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
//        calendar.add(Calendar.DATE);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 获取昨天
     *
     * @param time         指定日期
     * @param timeFormat   指定日期格式
     * @param resultFormat 返回的日期格式
     */
    public static String getYesterday(String time, String timeFormat, String resultFormat) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);//昨天
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 返回指定日期的上个月的第一天or最后一天
     *
     * @param firstOrLast 1第一天  2最后一天
     */
    public static String getLastDayOfLastMonth(String time, String timeFormat, String resultFormat, int firstOrLast) {
        Date date = timeToDate(timeFormat, time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, 1);
        if (firstOrLast == 2) {
            calendar.roll(Calendar.DATE, -1);
        }
        return dateToTime(resultFormat, calendar.getTime());
    }

    /**
     * 获取近30的开始时间
     *
     * @return
     */
    public static String getPastThirtyDayTime(String timeFormat) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -30);
        String startDate = new SimpleDateFormat(timeFormat).format(now.getTime());
        return startDate;
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthday
     * @param timeFormat
     * @return
     */
    public static int birthdayToAge(String birthday, String timeFormat) {
        int age = 0;
        int year = 0;
        int month = 0;
        int day = 0;
        //String类型转换为date类型
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        Date date = null;
        try {
            date = formatter.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        if (date == null) {
        } else {
            //date类型转成long类型
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去出生年月日
        int yearMinus = yearNow - year;
        int monthMinus = monthNow - month;
        int dayMinus = dayNow - day;
        age = yearMinus;// 先大致赋值
        if (yearMinus <= 0) {
            age = 0;
            return age;
        }
        if (monthMinus < 0) {
            age = age - 1;
        } else if (monthMinus == 0) {
            if (dayMinus < 0) {
                age = age - 1;
            }
        }
        return age;
    }

    public static boolean isAfter(String startDate, String endDate) {
        return timeToDate(FORMAT1, startDate).getTime() > timeToDate(FORMAT1, endDate).getTime();
    }

    public static boolean isBefore(String startDate, String endDate) {
        return timeToDate(FORMAT1, startDate).getTime() < timeToDate(FORMAT1, endDate).getTime();
    }


    public static boolean isAfter(String format,String startDate, String endDate) {
        return timeToDate(format, startDate).getTime() > timeToDate(FORMAT1, endDate).getTime();
    }

    public static boolean isBefore(String format,String startDate, String endDate) {
        return timeToDate(format, startDate).getTime() < timeToDate(FORMAT1, endDate).getTime();
    }

    public static String getBefore(int number) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, number);
        return dateToTime(FORMAT1, instance.getTime());
    }

    public interface OnSelectDateListener {
        void onSelect(String time);
    }

    public interface OnSelectHalfDayListener {
        void onSelect(String time, boolean isAm);
    }

    public interface OnDismissListener {
        void onDismiss(Object o);
    }
}
