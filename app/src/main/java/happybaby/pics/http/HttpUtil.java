package happybaby.pics.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import happybaby.pics.common.AppUtil;
import happybaby.pics.common.StringUtil;

/**
 * Created by yushilong on 2015/5/4.
 */
public class HttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static String TAG = "HTTP";
    private static String version;
    private static String deviceId;
    private static String channel;//渠道名
    private static final Handler handler;

    static {
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        deviceId = AppUtil.getDeviceId();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * @param requestBody
     * @param tClass
     * @param httpCallback
     */
    public static void async(String url, RequestBody requestBody, final Class<?> tClass, final HttpCallback httpCallback) {
        if (!AppUtil.isNetConnected()) {
            if (httpCallback != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallback.failure(10009, "网络故障，请检查后重试!");
                    }
                });
        }
        Log.i(TAG, "url--->" + url);
        Log.i(TAG, "args--->" + requestBody);
        mOkHttpClient.newCall(getRequest(url, requestBody, tClass == null ? TAG : tClass.getSimpleName())).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                if ((e instanceof SocketTimeoutException) || (e instanceof ConnectTimeoutException))
                    if (httpCallback != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpCallback.failure(10010, "连接超时，请稍后重试！");
                            }
                        });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                parseResponse(response, httpCallback, tClass);
            }
        });
    }

    /**
     * 解析约定
     *
     * @param response
     * @param httpCallback
     * @param tClass
     */
    private static void parseResponse(Response response, final HttpCallback httpCallback, final Class<?> tClass) {
        final Gson mGson = new Gson();
        try {
            final String str = response.body().string();
            Log.i(TAG, "response--->" + str);
            JSONObject result = new JSONObject(str);
            final int status = result.optInt("status");//status：10000成功
            final String msg = result.optString("message");
            if (status == 10000 || status == 0) {
                Object object = new JSONTokener(result.opt("data").toString()).nextValue();
                if (tClass == null) {//当tClass为null时，data部分的数据完全自己在代码中解析了，就不再使用该HTTP工具类帮忙解析了
                    if (httpCallback != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpCallback.success(str);
                            }
                        });
                    return;
                }
                if (object instanceof JSONObject) {
                    final JSONObject jsonObject = (JSONObject) object;
                    if (httpCallback != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpCallback.success(mGson.fromJson(jsonObject.toString(), tClass));
                            }
                        });
                } else if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    final List<Object> list = new ArrayList<>();
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject child = jsonArray.optJSONObject(i);
                        list.add(mGson.fromJson(child.toString(), tClass));
                    }
                    if (httpCallback != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpCallback.success(list);
                            }
                        });
                }
            } else {
                if (httpCallback != null)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallback.failure(status, msg);
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @param requestBody 为null是GET方式，否则为POST方式
     * @return
     */
    private static Request getRequest(String url, RequestBody requestBody, String tag) {
        Request.Builder builder = new Request.Builder();
        builder.tag(tag);
        builder.url(url);
//        if (UserUtil.getCookie() != null) {
//            builder.addHeader("Cookie", UserUtil.getCookie());
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("Android").append(Build.VERSION.RELEASE).append(" ");
//        sb.append(android.os.Build.BRAND).append(" ");
//        sb.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append(" ");
//        sb.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append(" ");
//        sb.append("HARDWARE ").append(android.os.Build.HARDWARE).append(" ");
//        sb.append("MODEL ").append(android.os.Build.MODEL).append(" ");
//        sb.append("shihuo/").append(AppUtil.getVersionName()).append(" ");
//        sb.append("sc(").append(AppUtil.getDeviceId()).append(" ,");
//        sb.append(AppUtil.getChannel()).append(")").append(" ");
//        builder.addHeader("User-Agent", sb.toString());
        if (requestBody != null)
            builder.post(requestBody);
        return builder.build();
    }

    /**
     * @param url    请求的基础地址
     * @param params 形成Token的参数集合
     * @return url+？+固定参数+token参数
     */
    public static String rebuildUrl(String url, SortedMap<String, Object> params) {
        if (params == null)
            params = new TreeMap<>();
        putCustomParams(params);
        params.remove("token");//移除之前map中存在的token,该token不参与排序
        params.put("token", buildToken(params));
        String rebuildUrl = url + "?" + buildParams(params);
        return rebuildUrl;
    }

    public static String rebuildUrl(String url, SortedMap<String, Object> params, SortedMap<String, Object> tokenParams) {
        if (params == null)
            params = new TreeMap<>();
        String timestamp = System.currentTimeMillis() + "";
        putCustomParamsNoTimestamp(params);
        putCustomParamsNoTimestamp(tokenParams);
        params.put("timestamp", timestamp);
        tokenParams.put("timestamp", timestamp);
        params.remove("token");//移除之前map中存在的token,该token不参与排序
        params.put("token", buildToken(tokenParams));
        String rebuildUrl = url + "?" + buildParams(params);
        return rebuildUrl;
    }

    /**
     * 添加常用参数
     *
     * @param params
     */
    private static void putCustomParams(SortedMap<String, Object> params) {
        params.put("v", AppUtil.getVersionName());
        params.put("platform", AppUtil.getPlatform());
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("clientCode", deviceId == null ? AppUtil.getDeviceId() : deviceId);
//        params.put("channel", channel == null ? AppUtil.getChannel() : channel);
    }

    /**
     * 添加常用参数
     *
     * @param params
     */
    private static void putCustomParamsNoTimestamp(SortedMap<String, Object> params) {
        params.put("v", AppUtil.getVersionName());
        params.put("platform", AppUtil.getPlatform());
        params.put("clientCode", deviceId == null ? AppUtil.getDeviceId() : deviceId);
//        params.put("channel", channel == null ? AppUtil.getChannel() : channel);
    }

    /**
     * 自然排序
     */
    public static String buildToken(SortedMap<String, Object> map) {
        int j = 0;
        String[] str = new String[map.size()];
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            str[j] = (String) entry.getKey();
            j++;
        }
        StringBuilder sb = new StringBuilder();
        Arrays.sort(str);
        for (int i = 0; i < str.length; i++) {
            String value = map.get(str[i]).toString();
            sb.append(value);
        }
        return StringUtil.MD5(sb.toString() + "123456").toLowerCase();
    }

    public static String buildParams(SortedMap<String, Object> params) {
        StringBuilder urlParams = new StringBuilder();
        for (String key : params.keySet()) {
            urlParams.append(key).append("=");
            try {
                urlParams.append(URLEncoder.encode(params.get(key).toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
            urlParams.append("&");
        }
        return urlParams.toString().substring(0, urlParams.toString().length() - 1);
    }

    /**
     * 取消标记为tag的请求
     *
     * @param tag
     */
    public static void cancel(String tag) {
        mOkHttpClient.cancel(tag);
    }
}
