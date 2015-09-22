package happybaby.pics.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yushilong on 2015/5/14.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter {
    private ArrayList models;
    public Activity activity;
    private View anchor;
    private RecyclerView recyclerView;

    public BaseAdapter(Activity activity, ArrayList models) {
        this.activity = activity;
        this.models = models;
    }

    public BaseAdapter(Activity activity, ArrayList models, final RecyclerView recyclerView, final View anchor) {
        this.models = models;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.anchor = anchor;
        if (anchor != null)
            anchor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                        anchor.setVisibility(View.GONE);
                    }
                }
            });
    }

    public BaseAdapter() {
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (anchor != null) {
            if (position > getShowAnchorCount() - 1)
                anchor.setVisibility(View.VISIBLE);
            else
                anchor.setVisibility(View.GONE);
        }
    }

    public int getShowAnchorCount() {
        return 10;//default is 10
    }

    public ArrayList getModels() {
        return models;
    }
}
