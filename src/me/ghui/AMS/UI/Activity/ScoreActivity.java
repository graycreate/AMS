package me.ghui.AMS.UI.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Animation.ZoomOutPageTransformer;
import me.ghui.AMS.UI.Fragment.ScoreListFragment;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghui on 4/24/14.
 */
public class ScoreActivity extends BaseActivity {
    String url;
    private ViewPager mPager;
    public Elements es;
    public static ArrayList<String[]> suggest = new ArrayList<String[]>();
    public Elements es_summary;
    public FragmentStatePagerAdapter pagerAdapter;
    private int count;
    private PagerTitleStrip pagerTitleStrip;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String idOrName = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            idOrName = intent.getStringExtra(SearchManager.QUERY);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            idOrName = intent.getData().getLastPathSegment();
        }
        Log.e("ghui", "idOrName: " + idOrName);
        int id =  doMySearch(idOrName);
        Log.e("ghui", "id::, " + id);
        if (id == -1) {
            showToast("无此人！");
            return;
        }
        mPager.setCurrentItem(id);
    }

    private int doMySearch(String idOrName) {
        for (String[]s:suggest) {
            if (s[0].equals(idOrName) || s[1].equals(idOrName)) {
                return suggest.indexOf(s);
            }
        }
        return -1;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.score_list;
    }

    @Override
    public void init() {
        url = Constants.FINAL_GRADE_INFO_URL + getIntent().getStringExtra("data");
        Log.e("ghui", "url: " + url);
        initFragments();
        getScores();
    }

    private void getScores() {
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = NetUtils.getDataFromServer(ScoreActivity.this, url);
                Elements elements = document.select("table[bordercolorlight=#000000]").select("table[style=border-collapse:collapse]").select("tbody");
                if (elements.size() == 0) {
                    showToast("无数据");
                    dismissProgressBar();
                    return;
                }
                es = elements.select("tr");
                Log.e("ghui", "es size1: " + es.size());
                String text;
                List<Element> list = new ArrayList<Element>();
                for (Element e : es) {
                    text = e.children().get(0).text();
                    if (text.charAt(0) != 'Z' && text.charAt(0) != 'B' && text.charAt(0) != 'D') {
                        list.add(e);
                    }
                }
                es.removeAll(list);
                Log.e("ghui", "es size2: " + es.size());
                suggest.clear();
                for (Element e : es) {
                    suggest.add(new String[]{e.children().get(0).text(), e.children().get(1).text()});
                }
                es_summary = elements.last().select("tr");
                es_summary.remove(0);
                Log.e("ghui", "elements.size: " + elements.size());
                count = es.size();
                mPager.post(new Runnable() {
                    @Override
                    public void run() {
                        pagerTitleStrip.setVisibility(View.VISIBLE);
                        mPager.setAdapter(pagerAdapter);
                        dismissProgressBar();
                    }
                });
            }
        }).start();
    }

    private void initFragments() {
        pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setGravity(Gravity.CENTER);
        mPager = (ViewPager) findViewById(R.id.vPager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mPager.setPageTransformer(true, new DepthPageTransformer());
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            private String getProTitle(int pos) {
                if (es == null) {
                    Log.e("ghui", "es is null");
                    return "";
                }
                String title = es.get(pos).children().get(1).text();
                Log.e("ghui", "title: " + title);
                return title;
            }

            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                ScoreListFragment fragment = new ScoreListFragment();
                Bundle args = new Bundle();
                args.putInt(ScoreListFragment.POS, i);
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getProTitle(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return count;
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stat:
                //todo
                Intent intent = new Intent(this, ScoreStatsDialog.class);
                if (es_summary == null) {
                    showToast("无数据！");
                    return true;
                }
                intent.putExtra("summary", es_summary.text());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score_list_menu, menu);
        // Add SearchWidget.
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //Applies white color on searchview text
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//        EditText editText = (EditText) searchView.findViewById(id);
//        editText.setTextColor(Color.WHITE);
//        editText.setHintTextColor(Color.WHITE);
//        editText.setCursorVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}