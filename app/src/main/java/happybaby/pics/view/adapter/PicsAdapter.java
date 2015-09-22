package happybaby.pics.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import happybaby.pics.R;
import happybaby.pics.base.BaseAdapter;
import happybaby.pics.common.DeviceUtil;
import happybaby.pics.common.ImageUtil;
import happybaby.pics.models.PicsModel;

/**
 * Created by yushilong on 2015/9/22.
 */
public class PicsAdapter extends BaseAdapter {
    public PicsAdapter(Activity activity, ArrayList models) {
        super(activity, models);
    }

    class ViewHoler extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_photo;
        TextView tv_title;

        public ViewHoler(View itemView) {
            super(itemView);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.iv_photo = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.getDefaultDisplay().getWidth() / 2 - 20);
            this.iv_photo.setLayoutParams(layoutParams);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() == RecyclerView.NO_POSITION)
                        return;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pics_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHoler viewHoler = (ViewHoler) holder;
        PicsModel model = (PicsModel) getModels().get(position);
        viewHoler.tv_title.setText(model.desc);
        viewHoler.iv_photo.setImageURI(ImageUtil.getUri(model.thumbnail_url));
    }
}
