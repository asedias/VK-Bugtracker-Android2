package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.UpdateList;
import ru.asedias.vkbugtracker.ui.holders.CommentHolder;

/**
 * Created by rorom on 18.11.2018.
 */

public class UpdatesAdapter extends RecyclerView.Adapter<CommentHolder> {

    private UpdateList data = new UpdateList();

    public void setData(UpdateList data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(((Activity) parent.getContext()).getLayoutInflater());
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        UpdateList.Update update = data.updates.get(position);
        holder.bind(update);
        holder.date.setText("");
        for(int i = 0; i < update.info.size(); i++) {
            if(i < update.info.size() - 1 || update.info.size() == 1) {
                holder.date.append(update.info.get(i));
                holder.date.append(", ");
            } else {
                SpannableStringBuilder sb = new SpannableStringBuilder(update.info.get(i));
                sb.setSpan(new TextAppearanceSpan(BugTrackerApp.context, R.style.TextAppearance_AppCompat_Body2), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new ForegroundColorSpan(BugTrackerApp.Color(R.color.darker_gray)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.date.append(sb);
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.updates.size();
    }
}
