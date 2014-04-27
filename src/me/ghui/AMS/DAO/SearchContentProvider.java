package me.ghui.AMS.DAO;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import me.ghui.AMS.UI.Activity.ScoreActivity;

import java.util.ArrayList;

/**
 * Created by ghui on 4/27/14.
 */
public class SearchContentProvider extends ContentProvider {
    private MatrixCursor cursor;
    public static ArrayList<String[]> suggests = new ArrayList<String[]>();

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query = uri.getLastPathSegment();
        Log.e("ghui", "query: " + query);
        if (SearchManager.SUGGEST_URI_PATH_QUERY.equals(query)) {
            return null;
        } else {
            //todo
            cursor = new MatrixCursor(new String[]{BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2, SearchManager.SUGGEST_COLUMN_INTENT_DATA});
            suggests.clear();
            for (String[] str : ScoreActivity.suggest) {
                if (str[0].contains(query) || str[1].contains(query)) {
                    suggests.add(str);
                }
            }
            int j = 0;
            for (String[] suggest : suggests) {
                cursor.addRow(new Object[]{j, suggest[1], suggest[0],suggest[0]});
            }
            return cursor;
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
