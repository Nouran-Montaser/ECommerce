package com.example.nouran.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.List;

public class ChartViewCommon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view_common);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(ChartViewCommon.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        int countCCat=0,countFCat=0,countECat=0,quantity = 0;
        String name ;
        for (int i=0;i<ProductAdapter.o.size();i++)
        {
            name = ProductAdapter.o.get(i).getName();
            quantity = ProductAdapter.o.get(i).getQuantity();
            if (name.equals("shirt")) {
                countCCat += quantity;
            }else if (name.equals("Laptop")) {
                countECat += quantity;
            }else if (name.equals("chocolate"))
            {
                countFCat +=quantity;
            }
            else if (name.equals("TV"))
                countECat +=quantity;
        }
        Log.i("ttttt",countCCat+" "+countECat+" "+countFCat);
        data.add(new ValueDataEntry("Clothes", countCCat));
        data.add(new ValueDataEntry("Food", countFCat));
        data.add(new ValueDataEntry("Electornics", countECat));

        pie.data(data);

        pie.title("Order History in Last month");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
    }
}