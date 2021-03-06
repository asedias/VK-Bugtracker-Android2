package ru.asedias.vkbugtracker.ui.holders.reportview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.fragments.ProfileFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.ThemeController;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 11.11.2018.
 */

public class AuthorHolder extends BindableHolder<Report.Author> {

    private ImageView photo;
    private TextView name;
    private TextView date;

    public AuthorHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_author, null, false));
        this.photo = itemView.findViewById(R.id.photo);
        this.name = itemView.findViewById(R.id.title);
        this.date = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(Report.Author data) {
        super.bind(data);
        Picasso.with(BTApp.context).load(data.author_photo.contains("http") ? data.author_photo : "https://vk.com"+data.author_photo).transform(new CropCircleTransformation()).into(this.photo);
        this.name.setText(data.author_name);
        this.date.setText(data.date);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        ((FragmentStackActivity)v.getContext()).replaceFragment(ProfileFragment.newInstance(data.uid), 0);
    }
}
