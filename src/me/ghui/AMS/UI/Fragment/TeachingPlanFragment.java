package me.ghui.AMS.UI.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Activity.CourseActivity;
import me.ghui.AMS.UI.Activity.TeachingPlanActivity;
import org.jsoup.select.Elements;

/**
 * Created by ghui on 4/13/14.
 */
public class TeachingPlanFragment extends Fragment {
    public static String POS = "pos";
    private int pos;
    private TextView[] tvs;
    private View root;
    private int ids[] = {
            R.id.line4,
            R.id.line5,
            R.id.line6,
            R.id.line7,
            R.id.line8,
            R.id.line9,
            R.id.line10,
            R.id.line11,
            R.id.line12,
            R.id.line1,
            R.id.line2,
            R.id.line3
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt(POS);
        Log.e("ghui", "Fragment onCreate pos: " + pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.teaching_plan_fragment, container, false);
        findView();
        Elements es = ((TeachingPlanActivity) getActivity()).es;
        fillData(es);
        return root;
    }

    private void findView() {
        tvs = new TextView[12];
        for (int i = 0; i < 12; i++) {
            tvs[i] = (TextView) root.findViewById(ids[i]);
        }
    }

    private void fillData(Elements es) {
        if (es == null) {
            return;
        }
        Elements elements = es.get(pos).children();
        String text = "";
        for (int i = 2; i < elements.size(); i++) {
            text = elements.get(i).text();
            tvs[i - 2].setText(text);
        }
    }
}