package ro.mta.proiect.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ro.mta.proiect.MainActivity;
import ro.mta.proiect.R;
import ro.mta.proiect.tables.UserDetails;

public class AdminGraphFragment extends Fragment {

    public AdminGraphFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AnyChartView anyChartView = getView().findViewById(R.id.fragment_admin_any_chart);
        anyChartView.setProgressBar(getView().findViewById(R.id.fragment_admin_any_chart_progress_bar));

        Cartesian cartesian = AnyChart.column();

        HashMap<String, UserDetails> allUsersDetails =  UserDetails.getAllUsersDetails();
        HashMap<String, Integer> chapterStatistics = new HashMap<>();

        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String, UserDetails> stringUserDetailsEntry : allUsersDetails.entrySet()) {
            Map.Entry pair = (Map.Entry)stringUserDetailsEntry;

            for (Map.Entry<String, Integer> userReadChapters : ((UserDetails)pair.getValue()).getReadChapters().entrySet()) {
                Map.Entry chaptersPair = (Map.Entry)userReadChapters;
                if (chapterStatistics.containsKey(chaptersPair.getKey())) {
                    chapterStatistics.put((String)chaptersPair.getKey(), chapterStatistics.get((String)chaptersPair.getKey())  + (Integer)chaptersPair.getValue());
                } else {
                    chapterStatistics.put((String)chaptersPair.getKey(), (Integer)chaptersPair.getValue());
                }
            }
        }

        for (Map.Entry<String, Integer> chapterStatisticsSet : chapterStatistics.entrySet()) {
            Map.Entry pair = (Map.Entry)chapterStatisticsSet;
            data.add(new ValueDataEntry((String)pair.getKey(), (Integer)pair.getValue()));
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);

        cartesian.title("Chapters read by the users");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Chapters");
        cartesian.yAxis(0).title("Number of times read");

        anyChartView.setChart(cartesian);

    }
}
