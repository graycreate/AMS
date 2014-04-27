package me.ghui.AMS.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Activity.GradesActivity;
import me.ghui.AMS.UI.Activity.ScoreActivity;
import me.ghui.AMS.utils.StringHelper;
import org.jsoup.select.Elements;

/**
 * Created by ghui on 4/19/14.
 */
public class CourseGradeFragment extends Fragment {
    public static final String POS = "pos";
    private int pos;
    private TextView[] tvs;
    private View root;
    private int ids[] = {
            R.id.line1,
            R.id.line2,
            R.id.line3,
            R.id.line4,
            R.id.line5
    };
    String request_data;
    private GradesActivity parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt(POS);
        parent = (GradesActivity) getActivity();
        Log.e("ghui", "Fragment onCreate pos: " + pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.course_grade_fragment, container, false);
        findView();
        initListener();
        Elements es = ((GradesActivity) getActivity()).es;
        fillData(es);
        return root;
    }

    private void findView() {
        tvs = new TextView[5];
        for (int i = 0; i < 5; i++) {
            tvs[i] = (TextView) root.findViewById(ids[i]);
        }
    }

    private void initListener() {
        tvs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.showToast("查看原始成绩！");
            }
        });

        tvs[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, ScoreActivity.class);
                intent.putExtra("data", request_data);
                startActivity(intent);
            }
        });
    }

    private void fillData(Elements es) {
        if (es == null) {
            return;
        }
        Elements elements = es.get(pos).children();
        String text = "";
        for (int i = 1; i < elements.size(); i++) {
            text = elements.get(i).text();
            tvs[i - 1].setText(text);
            if (i == 4) {
                String str = elements.get(i).select("a").attr("onclick");
                request_data = StringHelper.getUrlData(str);
                Log.e("ghui", "request_data: " + request_data);
            }
        }
    }
}