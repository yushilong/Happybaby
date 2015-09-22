package happybaby.pics.common;

import android.net.Uri;

/**
 * Created by yushilong on 2015/9/22.
 */
public class ImageUtil {
    public static Uri getUri(String uriStr) {
        return Uri.parse(uriStr == null ? "" : uriStr);
    }
}
