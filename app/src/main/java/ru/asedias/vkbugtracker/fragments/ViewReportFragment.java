package ru.asedias.vkbugtracker.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.GetReportInfo;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.ViewReportAdapter;

/**
 * Created by rorom on 10.11.2018.
 */

public class ViewReportFragment extends RecyclerFragment<ViewReportAdapter> {

    private int rid;

    public ViewReportFragment() {
        this.mAdapter = new ViewReportAdapter();
        this.title = BugTrackerApp.String(R.string.report);
    }

    public static ViewReportFragment newInstance(int rid) {
        ViewReportFragment fr = new ViewReportFragment();
        Bundle args = new Bundle();
        args.putInt("rid", rid);
        fr.setArguments(args);
        return fr;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208)).setProvider(getAdapter());
        this.mList.addItemDecoration(decoration);
        return root;
    }

    @Override
    public WebRequest getRequest() {
        if(getArguments() != null) {
            this.rid = getArguments().getInt("rid", 52963);
        }
        this.getAdapter().setRid(this.rid);
        return new GetReportInfo(this, this.rid, report -> {
            report.photos = processPhotos(report.photos);
            for(int i = 0; i < report.comments.size(); i++) {
                report.comments.get(i).photos = processPhotos(report.comments.get(i).photos);
            }
            getAdapter().setData(report);
            return report;
        });
    }

    private List<Report.Photo> processPhotos(List<Report.Photo> photos) {
        for (int i = 0; i < photos.size(); i++) {
            Report.Photo photo = photos.get(i);
            try {
                JSONObject obj = new JSONObject(photo.json).getJSONObject("temp");
                String base = obj.getString("base");
                if(obj.has("x_")) {
                    JSONArray x = obj.getJSONArray("x_");
                    photo.url_x = (x.getString(0).contains("http") ? "" : base) + x.getString(0) + ".jpg";
                    photo.width = x.getInt(1);
                    photo.height = x.getInt(2);
                }
                if(obj.has("y_")) {
                    JSONArray y = obj.getJSONArray("y_");
                    photo.url_y = (y.getString(0).contains("http") ? "" : base) + y.getString(0) + ".jpg";
                    photo.width = y.getInt(1);
                    photo.height = y.getInt(2);
                }
                if(obj.has("z_")) {
                    JSONArray z = obj.getJSONArray("z_");
                    photo.url_z = (z.getString(0).contains("http") ? "" : base) + z.getString(0) + ".jpg";
                }
                if(obj.has("w_")) {
                    JSONArray w = obj.getJSONArray("w_");
                    photo.url_w = (w.getString(0).contains("http") ? "" : base) + w.getString(0) + ".jpg";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return photos;
    }
}
