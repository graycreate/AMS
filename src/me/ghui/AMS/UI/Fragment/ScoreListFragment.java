package me.ghui.AMS.UI.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Activity.ScoreActivity;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by ghui on 4/24/14.
 */
public class ScoreListFragment extends Fragment {
    public static final String POS = "pos";
    public int pos;
    ScoreActivity parent;
    private TextView[] tvs;
    private View root;
    private int ids[] = {
            R.id.line1,
            R.id.line2,
            R.id.line3,
            R.id.line4,
            R.id.line5,
            R.id.line6,
            R.id.line7
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt(POS);
        parent = (ScoreActivity) getActivity();
        Log.e("ghui", "Fragment onCreate pos: " + pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.score_list_fragment, container, false);
        findView();
        fillData(parent.es.get(pos).children());
        return root;
    }

    private void findView() {
        tvs = new TextView[7];
        for (int i = 0; i < 7; i++) {
            tvs[i] = (TextView) root.findViewById(ids[i]);
        }
    }

    private void fillData(Elements es) {
        es.remove(1);
        for (int i = 0; i < 7; i++) {
            tvs[i].setText(es.get(i).text());
        }
    }
}