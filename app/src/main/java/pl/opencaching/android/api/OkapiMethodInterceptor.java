package pl.opencaching.android.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class OkapiMethodInterceptor implements Interceptor {

    @Inject
    OkapiMethodInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        if (original.body() != null) {

            Request.Builder requestBuilder = original.newBuilder()
                    .url(original.url() + "?" + bodyToString(original.body()));
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }


        return chain.proceed(original);

    }

    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
