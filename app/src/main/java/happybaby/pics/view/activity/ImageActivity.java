package happybaby.pics.view.activity;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import happybaby.pics.R;
import happybaby.pics.base.BaseActivity;
import happybaby.pics.common.AppUtil;
import happybaby.pics.common.ImageUtil;
import happybaby.pics.common.StringUtil;
import happybaby.pics.common.share.ShareUtil;
import happybaby.pics.models.PicsModel;

/**
 * Created by yushilong on 2015/9/29.
 */
public class ImageActivity extends BaseActivity {
    ShareUtil shareUtil;
    PicsModel model;
    DialogPlus dialogPlus;

    @Override
    public int IGetContentViewResId() {
        return R.layout.activity_image;
    }

    @Override
    public void IFindViews() {
        getToolbar().setBackgroundResource(android.R.color.black);
        getToolbar().setNavigationIcon(R.mipmap.ic_action_remove);
        getToolbarTitle().setTextColor(Color.WHITE);
    }

    @Override
    public void IInitData() {
        SimpleDraweeView iv_photo = (SimpleDraweeView) findViewById(R.id.iv_photo);
        model = (PicsModel) getIntent().getSerializableExtra("model");
        if (model != null) {
            if (!StringUtil.isEmpty(model.image_url)) {
                iv_photo.setImageURI(ImageUtil.getUri(model.image_url));
            }
            if (!StringUtil.isEmpty(model.abs))
                getToolbarTitle().setText(model.abs);
        }
    }

    @Override
    public int getMenuResId() {
        return R.menu.menu_image;
    }

    @Override
    public Toolbar.OnMenuItemClickListener getOnMenuItemClickListener() {
        Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share:
                        share();
                        break;
                    case R.id.save:
                        save();
                        break;
                }
                return true;
            }
        };
        return onMenuItemClickListener;
    }

    private void save() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(model.image_url).openConnection();
                    connection.setConnectTimeout(5 * 1000);
                    if (connection.getResponseCode() != 200)
                        AppUtil.toast(IGetContext(), "文件保存失败，请重试！");
                    else {
                        InputStream stream = connection.getInputStream();
                        String savePath = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/happybaby/";
                        File savedir = new File(savePath);
                        if (!savedir.exists()) {
                            savedir.mkdirs();
                        }
                        //
                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String fileName = "happybaby_" + timeStamp + ".jpg";// 照片命名
                        File pictureFile = new File(savePath, fileName);
                        FileOutputStream outStream = new FileOutputStream(pictureFile);
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = stream.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                        }
                        outStream.close();
                        stream.close();
                        AppUtil.toast(IGetContext(), "文件保存成功！\n" + pictureFile.getPath());
                    }
                } catch (IOException e) {
                    AppUtil.toast(IGetContext(), "文件保存失败，请重试！");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void share() {
        if (shareUtil == null) {
            shareUtil = ShareUtil.newInstance(IGetActivity());
            shareUtil.setShareContent("好东西要和好伙伴一起分享哦~!", model.abs, model.image_url, model.image_url);
        }
        if (dialogPlus == null) {
            dialogPlus = DialogPlus.newDialog(IGetContext()).setContentHolder(new ViewHolder(R.layout.share)).setGravity(Gravity.BOTTOM).setCancelable(true).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(DialogPlus dialogPlus, View view) {
                    dialogPlus.dismiss();
                    switch (view.getId()) {
                        case R.id.wx:
                            shareUtil.share(SHARE_MEDIA.WEIXIN);
                            break;
                        case R.id.wxq:
                            shareUtil.share(SHARE_MEDIA.WEIXIN_CIRCLE);
                            break;
                        case R.id.qq:
                            shareUtil.share(SHARE_MEDIA.QQ);
                            break;
                        case R.id.qqzone:
                            shareUtil.share(SHARE_MEDIA.QZONE);
                            break;
                        case R.id.sina:
                            shareUtil.share(SHARE_MEDIA.SINA);
                            break;
                    }
                }
            }).create();
        }
        dialogPlus.show();
    }
}
