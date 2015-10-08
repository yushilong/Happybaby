package happybaby.pics.common.share;

import android.app.Activity;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by yushilong on 2015/5/19.
 */
public class ShareUtil {
    public static final String WX_APP_ID = "wxbbd9c606803ef151";
    public static final String WX_APP_SECRET = "05db2ef0748893333c41ba4a34d9163e";
    public static final String QQ_APP_ID = "1104815433";
    public static final String QQ_APP_SECRET = "IAjxRPnrVPUgrWz3";
    private Activity activity;
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
    //    private UMWXHandler umwxHandler;
//    private UMWXHandler circleUmwxHandler;
    private QZoneSsoHandler qZoneSsoHandler;
    //    private SinaSsoHandler sinaSsoHandler;
    private UMQQSsoHandler umqqSsoHandler;

    public static ShareUtil newInstance(Activity activity) {
        return new ShareUtil(activity);
    }

    public ShareUtil(Activity activity) {
        this.activity = activity;
//        mController.getConfig().closeToast();
        mController.getConfig().setSinaCallbackUrl("http://sns.whalecloud.com/sina2/callback");
//        umwxHandler = new UMWXHandler(activity, WX_APP_ID, WX_APP_SECRET);
//        umwxHandler.addToSocialSDK();
//        circleUmwxHandler = new UMWXHandler(activity, WX_APP_ID, WX_APP_SECRET);
//        circleUmwxHandler.setToCircle(true);
//        circleUmwxHandler.addToSocialSDK();
        qZoneSsoHandler = new QZoneSsoHandler(activity, QQ_APP_ID, QQ_APP_SECRET);
        qZoneSsoHandler.addToSocialSDK();
//        sinaSsoHandler = new SinaSsoHandler(activity);
//        mController.getConfig().setSsoHandler(sinaSsoHandler);
//        sinaSsoHandler.addToSocialSDK();
        umqqSsoHandler = new UMQQSsoHandler(activity, QQ_APP_ID, QQ_APP_SECRET);
        umqqSsoHandler.addToSocialSDK();
    }

    public void setShareContent(String title, String content, String targetUrl, String imgUrl) {
//        setWXContent(title, content, targetUrl, imgUrl);
//        setWXCircleContent(title, content, targetUrl, imgUrl);
        setQZoneContent(title, content, targetUrl, imgUrl);
//        setSinaContent(title, imgUrl, targetUrl);
        setQQContent(title, imgUrl, targetUrl, imgUrl);
    }

    /**
     * 设置微信分享内容
     *
     * @param title
     * @param content
     * @param targetUrl
     * @param imgUrl
     */
    public void setWXContent(String title, String content, String targetUrl, String imgUrl) {
        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
        weiXinShareContent.setTitle(title);
        weiXinShareContent.setShareContent(content);
        weiXinShareContent.setTargetUrl(targetUrl);
        weiXinShareContent.setShareImage(new UMImage(activity, imgUrl));
        mController.setShareMedia(weiXinShareContent);
    }

    /**
     * 设置微信朋友圈分享内容
     *
     * @param title
     * @param content
     * @param targetUrl
     * @param imgUrl
     */
    public void setWXCircleContent(String title, String content, String targetUrl, String imgUrl) {
        CircleShareContent circleShareContent = new CircleShareContent();
        circleShareContent.setTitle(title);
        circleShareContent.setShareContent(content);
        circleShareContent.setTargetUrl(targetUrl);
        circleShareContent.setShareMedia(new UMImage(activity, imgUrl));
        mController.setShareMedia(circleShareContent);
    }

    /**
     * 设置QQ好友分享内容
     *
     * @param title
     * @param content
     * @param targetUrl
     * @param imgUrl
     */
    public void setQQContent(String title, String content, String targetUrl, String imgUrl) {
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setTitle(title);
        qqShareContent.setShareContent(content);
        qqShareContent.setTargetUrl(targetUrl);
        qqShareContent.setShareMedia(new UMImage(activity, imgUrl));
        mController.setShareMedia(qqShareContent);
    }

    /**
     * 设置QQ空间分享内容
     *
     * @param title
     * @param content
     * @param targetUrl
     * @param imgUrl
     */
    public void setQZoneContent(String title, String content, String targetUrl, String imgUrl) {
        QZoneShareContent qZoneShareContent = new QZoneShareContent();
        qZoneShareContent.setTitle(title);
        qZoneShareContent.setShareContent(content);
        qZoneShareContent.setTargetUrl(targetUrl);
        qZoneShareContent.setShareMedia(new UMImage(activity, imgUrl));
        mController.setShareMedia(qZoneShareContent);
    }

    /**
     * 设置新浪微博分享内容
     *
     * @param title
     * @param imgUrl
     * @param link
     */
    public void setSinaContent(String title, String imgUrl, String link) {
        String content = String.format("#识货推荐#分享一件好货：%s%s[@识货 不只是消费,更有态度]", title, link);
        SinaShareContent sinaShareContent = new SinaShareContent();
        sinaShareContent.setShareMedia(new UMImage(activity, imgUrl));
        sinaShareContent.setShareContent(content);
        mController.setShareMedia(sinaShareContent);
    }

    public void share(SHARE_MEDIA platform) {
        // 参数1为Context类型对象， 参数2为要分享到的目标平台， 参数3为分享操作的回调接口
        mController.postShare(activity, platform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
//                AppUtil.toast(activity, "开始分享");
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String message;
                int statusCode;
                statusCode = (eCode == StatusCode.ST_CODE_SUCCESSED ? 0 : (eCode == StatusCode.ST_CODE_ERROR_CANCEL ? 1 : 2));
                message = (eCode == StatusCode.ST_CODE_SUCCESSED ? "分享成功" : (eCode == StatusCode.ST_CODE_ERROR_CANCEL ? "分享取消" : "分享失败"));
//                AppUtil.toast(activity, message);
            }
        });
    }
}
