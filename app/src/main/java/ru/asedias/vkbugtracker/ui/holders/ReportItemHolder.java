package ru.asedias.vkbugtracker.ui.holders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.FlowLayout;
import ru.asedias.vkbugtracker.ui.Fonts;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportItemHolder extends  BindableHolder<ReportList.ReportItem> {

    public TextView mTitle;
    public TextView mTime;
    public TextView mState;
    public ImageView mAuthor;
    public ImageView mProduct;
    public FlowLayout mTagsLayout;
    private LayoutInflater mInflater;
    private FlowLayout.LayoutParams mTagLP = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public ReportItemHolder(View itemView, LayoutInflater inflater) {
        super(itemView);
        this.mInflater = inflater;
        this.mTitle = itemView.findViewById(R.id.title);
        this.mTime = itemView.findViewById(R.id.time);
        this.mState = itemView.findViewById(R.id.state);
        this.mTagsLayout = itemView.findViewById(R.id.tags_layout);
        this.mAuthor = itemView.findViewById(R.id.author);
        this.mProduct = itemView.findViewById(R.id.product);
        mTagLP.horizontal_spacing = BugTrackerApp.dp(6);
        mTagLP.vertical_spacing = BugTrackerApp.dp(6);
    }

    @Override
    public void bind(ReportList.ReportItem report) {
        this.mTitle.setText(report.title);
        this.mTime.setText(report.details);
        this.mState.setText(report.status);
        Picasso.with(BugTrackerApp.context)
                .load(report.user.getPhoto200())
                .placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user))
                .transform(new CropCircleTransformation())
                .into(this.mAuthor);
        Picasso.with(BugTrackerApp.context)
                .load(report.product.photo)
                .placeholder(BugTrackerApp.Drawable(R.drawable.ic_doc_text))
                .transform(new CropCircleTransformation())
                .into(this.mProduct);
        this.mTagsLayout.removeAllViews();
        float textPadding = BugTrackerApp.dp(24);
        float fullwidth = 0;
        float width = BugTrackerApp.mMetrics.widthPixels - BugTrackerApp.dp(85);
        for(int i = 1; i < report.tags.size(); i++) {
            final TextView temp = (TextView) mInflater.inflate(R.layout.tag_item, null);
            fullwidth += temp.getPaint().measureText(report.tags.get(i).label) + textPadding;
            if(fullwidth > width) break;
            temp.setText(report.tags.get(i).label);
            temp.setTypeface(Fonts.Regular);
            this.mTagsLayout.addView(temp, mTagLP);
        }
    }
}
