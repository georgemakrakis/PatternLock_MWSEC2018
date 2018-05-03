package com.example.georgemakrakis.patternlock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity
{

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView longRuns = (TextView) findViewById(R.id.longRuns);
        TextView closedCurves = (TextView) findViewById(R.id.closedCurves);
        TextView longCurves = (TextView) findViewById(R.id.longCurves);
        TextView longEdges = (TextView) findViewById(R.id.longEdges);
        TextView shortEdges = (TextView) findViewById(R.id.shortEdges);
        TextView longOrthEdges = (TextView) findViewById(R.id.longOrthEdges);
        TextView shortOrthEdges = (TextView) findViewById(R.id.shortOrthEdges);
        ListView NumFreqListView = (ListView) findViewById(R.id.NumFreqListView);

        List<String> patternsList1 = getIntent().getStringArrayListExtra("patternsList1");
        List<String> patternsList2 = getIntent().getStringArrayListExtra("patternsList2");

        //Log.d("Debug",getLongRuns(patternsList1, patternsList2)+"");


        //Our probable values of every desired pattern "schema" we want
        String[] allLongRuns = {
                "036", "630", "147", "741", "258", "852",
                "012", "210", "345", "543", "678", "876",
                "048", "840", "246", "642"
        };
        String[] allClosedCurves = {
                "0341", "1452", "3674", "4785", "1034", "4367", "2145", "5478", "3014", "4125",
                "0347", "7458", "3410", "4521", "6743", "7854", "4587", "3476", "1254", "0143",
                "8547", "7430", "5214", "4103", "8745", "5412", "7634", "4301", "5874", "4763",
                "2541", "1430",
        };

        String[] allLongCurves = {
                "0341", "1452", "3674", "4785", "1034", "4367", "2145", "5478", "3014", "4125",
                "0347", "7458", "3410", "4521", "6743", "7854", "4587", "3476", "1254", "0143",
                "8547", "7430", "5214", "4103", "8745", "5412", "7634", "4301", "5874", "4763",
                "2541", "1430",
        };

        String[] allLongEdges = {
                "63048", "64258", "64210", "04876", "67840", "01246", "85246", "84036",
        };

        String[] allShortEdges = {
                "314", "425", "647", "758", "403", "514", "847", "736", "043", "154", "376", "487",
                "310", "421", "643", "754", "457", "346", "124", "013", "784", "673", "451", "340",
                "637", "748", "415", "304", "857", "746", "524", "413",
        };

        String[] allLongOrthEdges = {
                "03678", "01258", "25878", "21036", "63012", "87852", "85210", "87630",
        };

        String[] allShortOrthEdges = {
                "034", "145", "367", "478", "143", "254", "476", "587", "301", "412", "634", "754",
                "410", "521", "743", "854", "458", "347", "125", "014", "457", "436", "214", "103",
                "785", "674", "452", "341", "874", "763", "541", "430",
        };


        longRuns.setText("Long runs in your patterns: " +
                getStatData(patternsList1, patternsList2, allLongRuns));
        closedCurves.setText("Closed curves in your patterns: " +
                getStatData(patternsList1, patternsList2, allClosedCurves));
        longCurves.setText("Long curves in your patterns: " +
                getStatData(patternsList1, patternsList2, allLongCurves));
        longEdges.setText("Longs edges in your patterns: " +
                getStatData(patternsList1, patternsList2, allLongEdges));
        shortEdges.setText("Short edges in your patterns: " +
                getStatData(patternsList1, patternsList2, allShortEdges));
        longOrthEdges.setText("Long orthogonal edges in your patterns: " +
                getStatData(patternsList1, patternsList2, allLongOrthEdges));
        shortOrthEdges.setText("Short orthogonal edges in your patterns: " +
                getStatData(patternsList1, patternsList2, allShortOrthEdges));


        int[] numberFreq = getNumberFreq(patternsList1,patternsList2);
        List<String> freqList = new ArrayList<>();
        for (int i = 0; i < numberFreq.length-1; i++)
        {
            freqList.add(i+" --> "+Integer.toString(numberFreq[i])+" times");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,  R.layout.spinner_item, freqList);
        NumFreqListView.setAdapter(adapter);

    }

    public int getStatData(List<String> patternsList1, List<String> patternsList2, String[] data)
    {
        int counter = 0;

        for (String pattern : patternsList1)
        {
            for (String longRun : data)
            {
                if (pattern.contains(longRun))
                {
                    counter++;
                }
            }
        }

        for (String pattern : patternsList2)
        {
            for (String longRun : data)
            {
                if (pattern.contains(longRun))
                {
                    counter++;
                }
            }
        }

        return counter;

    }


    public int[] getNumberFreq(List<String> patternsList1, List<String> patternsList2)
    {
        int[] numberFreq = new int[10];
        for (int i = 0; i < numberFreq.length; i++)
        {
            numberFreq[i] = 0;
        }

        for (String pattern : patternsList1)
        {
            for (int i = 0; i < pattern.length(); i++)
            {
                numberFreq[Character.getNumericValue(pattern.charAt(i))]++;
            }
        }

        for (String pattern : patternsList2)
        {
            for (int i = 0; i < pattern.length(); i++)
            {
                numberFreq[Character.getNumericValue(pattern.charAt(i))]++;
            }
        }

        return numberFreq;
    }
}
