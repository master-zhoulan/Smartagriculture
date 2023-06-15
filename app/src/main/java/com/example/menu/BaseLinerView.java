package com.example.menu;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseLinerView {
    public View mView;

    // 最多显示30 条X轴数据 超过重新开始
    public static final int MAX_VALUES = 100;

    private LineChart line_chart;

    private LineDataSet dataSet;

    private List<Entry> entries;

    //线条颜色
    public String lineColor;

    //填充颜色
    public String lineColor2;

    //零线
    public int zeroLine = 30;

    //折线图最大值
    public int maxValue = 45;

    //折线图最小值
    public int minValue = 30;

    //折线图步进
    public float step = 0.5f;



    public boolean init = false;

    private int i = 2;

    private LineData lineData;


    public void initView() {
        line_chart = mView.findViewById(R.id.liner);
        initData();
        setAxis();
        setLegend();
        //  设置图表控件是否可以进行触控操作
        line_chart.setEnabled(false);
        //  调用图表控件「描述方法」并直接禁用
        line_chart.getDescription().setEnabled(false);
    }

    private void setLegend() {
        //  获得图例的实例
        Legend legend = line_chart.getLegend();
        //  设置图例是否在图表控件内部显示
        legend.setDrawInside(true);
        //  设置图例的排列方向
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //  设置图例在图表控件中的水平对齐方向
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //  设置图例在图表控件中的垂直对齐方向
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //  设置图例的色块大小
        legend.setFormSize(15);
        //  设置图例的色块形状
        legend.setForm(Legend.LegendForm.CIRCLE);
        //  设置图例的文字大小
        legend.setTextSize(15);
        //  设置图例的文字颜色
        legend.setTextColor(Color.BLACK);
    }


    abstract public String getLineName();


    private void setAxis() {
        //  获得X轴实例
        XAxis xl = line_chart.getXAxis();

        //  设置X轴显示位置
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        //  设置X轴步长
        xl.setGranularity(1);
        //  设置是否显示X轴的延伸线
        xl.setDrawGridLines(false);

        //  获得左边Y轴实例
        YAxis yl = line_chart.getAxisLeft();
        // 不画网格
        yl.setDrawGridLines(false);
        //  设置左边Y轴最小单位为0
        yl.setAxisMinimum(minValue);
        // 设置Y轴步进
        yl.setGranularity(step);


        yl.setAxisMaximum(maxValue); // 设置Y轴最大值为100
        yl.setLabelCount(10, false); // 强制设置标签个数

        //  获得右边Y轴的实例
        YAxis yl_right = line_chart.getAxisRight();

        //  设置右边Y轴是否可用
        yl_right.setEnabled(false);
    }

    private void initData() {
        entries = new ArrayList<>();
        for(int i = 0;i < MAX_VALUES;i++){
            entries.add(new Entry(i,zeroLine));
        }



        setDataStyle();
    }

    /**
     * 设置折线的样式
     *
     */
    private void setDataStyle() {
        //        List<ILineDataSet> dataSets = new ArrayList<>();

        dataSet = new LineDataSet(entries, getLineName());

        //  折线的颜色
        dataSet.setFillColor(Color.parseColor(lineColor));
        //  填充色的颜色

        dataSet.setDrawFilled(lineColor2 != null);
        if(lineColor2 !=null) {
            dataSet.setColor(Color.parseColor(lineColor2));
        }
        //  折线的宽度
        dataSet.setLineWidth(2);
        //  是否设置圆点空洞
        dataSet.setDrawCircleHole(false);
        //  设置圆点的半径
        dataSet.setCircleRadius(1);
        //  设置圆的背景颜色
        dataSet.setCircleColor(Color.parseColor("#78C256"));
        //  设置数值字体大小
        dataSet.setValueTextSize(13);
        //  设置数值字体颜色
        dataSet.setValueTextColor(Color.parseColor("#78C256"));
        //  设置折线的显示模式
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        lineData = new LineData(dataSet);
        line_chart.setData(lineData);
        init = true;
    }

    /**
     * 实时更新折线图中坐标点数据
     */
    public void onUdata(double v) {
        if(!init){
            return;
        }
        if(i > MAX_VALUES - 1){
            i = 0;
        }
        entries.set(i,new Entry(i,(int)v));
        line_chart.setData(lineData);
        line_chart.invalidate();
        i++;

    }
}