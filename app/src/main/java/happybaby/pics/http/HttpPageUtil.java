package happybaby.pics.http;

import com.squareup.okhttp.RequestBody;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by yushilong on 2015/5/14.
 */
public class HttpPageUtil {
    private int page = 1;
    private int pagesize = 30;
    private String url;
    private RequestBody requestBody;
    private Class<?> tClass;
    private HttpCallback httpCallback;
    private boolean isRefreshState;
    private SortedMap<String, Object> sortedMap;
    private boolean isReachTheBottom;
    private String pageKey = "page";//default page
    private String pageSizeKey = "pagesize";//default pagesize

    public HttpPageUtil(String url, SortedMap<String, Object> sortedMap, RequestBody requestBody, Class<?> tClass, HttpCallback httpCallback) {
        this.url = url;
        this.sortedMap = sortedMap;
        this.requestBody = requestBody;
        this.tClass = tClass;
        this.httpCallback = httpCallback;
    }

    public void async() {
        if (isReachTheBottom)
            return;
        if (sortedMap == null)
            sortedMap = new TreeMap<>();
        sortedMap.put(pageKey, page + "");
        sortedMap.put(pageSizeKey, pagesize + "");
        HttpUtil.async(url, requestBody, tClass, httpCallback);
    }

    public void first() {
        page = 1;
        isRefreshState = true;
        isReachTheBottom = false;
    }

    public void next() {
        page = page + 1;
        isRefreshState = false;
    }

    public boolean isRefreshState() {
        return isRefreshState;
    }

    public void setIsReachTheBottom(boolean isReachTheBottom) {
        this.isReachTheBottom = isReachTheBottom;
    }

    public boolean isReachTheBottom() {
        return isReachTheBottom;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    public void setPageSizeKey(String pageSizeKey) {
        this.pageSizeKey = pageSizeKey;
    }
}
