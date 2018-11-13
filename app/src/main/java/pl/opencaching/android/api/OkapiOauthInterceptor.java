package pl.opencaching.android.api;

import android.util.Log;

import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.services.HMACSha1SignatureService;
import com.github.scribejava.core.services.TimestampServiceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.utils.Constants;

/**
 * Created by Wojtek on 14.08.2017.
 */

public class OkapiOauthInterceptor implements Interceptor {

    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_TOKEN = "oauth_token";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_VERSION_VALUE = "1.0";

    private SessionManager sessionManager;

    @Inject
    OkapiOauthInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        Log.d("URL", original.url().toString());
        Log.d("URL", original.url().scheme());
        Log.d("encodedpath", original.url().encodedPath());
        Log.d("query", "" + original.url().query());
        Log.d("path", "" + original.url().host());
        Log.d("encodedQuery", "" + original.url().encodedQuery());
        Log.d("method", "" + original.method());

        ////////////////////////////////////////////////////////////

        final String nonce = new TimestampServiceImpl().getNonce();
        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
        Log.d("nonce", nonce);
        Log.d("time", timestamp);

        String dynamicStructureUrl = original.url().scheme() + "://" + original.url().host() + original.url().encodedPath();

        Log.d("ENCODED PATH", "" + dynamicStructureUrl);
        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
        Log.d("firstBaseString", firstBaseString);
        String generatedBaseString = "";

        if (original.url().encodedQuery() != null) {
            if (sessionManager.getOauthToken().equals("")) {
                generatedBaseString = original.url().encodedQuery() + "&oauth_consumer_key=" + Constants.OPENCACHING_CONSUMER_KEY + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
            } else {
                generatedBaseString = original.url().encodedQuery() + "&oauth_consumer_key=" + Constants.OPENCACHING_CONSUMER_KEY + "&oauth_nonce=" + nonce + "&oauth_token=" + sessionManager.getOauthToken() + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
            }
        } else {
            generatedBaseString = "oauth_consumer_key=" + Constants.OPENCACHING_CONSUMER_KEY + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
        }

        ParameterList result = new ParameterList();
        result.addQuerystring(generatedBaseString);
        generatedBaseString = result.sort().asOauthBaseString();
        Log.d("Sorted", "00--" + result.sort().asOauthBaseString());

        String secoundBaseString = "&" + generatedBaseString;

        if (firstBaseString.contains("%3F")) {
            Log.d("iff", "yess iff");
            secoundBaseString = "%26" + urlEncoded(generatedBaseString);
        }

        String baseString = firstBaseString + secoundBaseString;

        String signature = new HMACSha1SignatureService().getSignature(baseString, Constants.OPENCACHING_CONSUMER_KEY_SECRET, sessionManager.getOauthTokenSecret());
        Log.d("Signature", signature);

        HttpUrl url;

        if (sessionManager.getOauthToken().equals("")) {
            url = originalHttpUrl.newBuilder()
                    .addQueryParameter(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE)
                    .addQueryParameter(OAUTH_CONSUMER_KEY, Constants.OPENCACHING_CONSUMER_KEY)
                    .addQueryParameter(OAUTH_VERSION, OAUTH_VERSION_VALUE)
                    .addQueryParameter(OAUTH_TIMESTAMP, timestamp)
                    .addQueryParameter(OAUTH_NONCE, nonce)
                    .addQueryParameter(OAUTH_SIGNATURE, signature)
                    .build();
        } else {
            url = originalHttpUrl.newBuilder()
                    .addQueryParameter(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE)
                    .addQueryParameter(OAUTH_CONSUMER_KEY, Constants.OPENCACHING_CONSUMER_KEY)
                    .addQueryParameter(OAUTH_VERSION, OAUTH_VERSION_VALUE)
                    .addQueryParameter(OAUTH_TIMESTAMP, timestamp)
                    .addQueryParameter(OAUTH_NONCE, nonce)
                    .addQueryParameter(OAUTH_TOKEN, sessionManager.getOauthToken())
                    .addQueryParameter(OAUTH_SIGNATURE, signature)
                    .build();
        }

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public String urlEncoded(String url) {
        String encodedurl = "";
        try {
            encodedurl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedurl;
    }
}