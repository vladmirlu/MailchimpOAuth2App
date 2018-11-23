package com.oauth2.mailchimp.service;

import com.oauth2.mailchimp.model.RestApiClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectService {
    private static final String CODE_REGEX = ("^[a-z0-9]+$");

    @Autowired
    RestApiClient restClient;

    public void process() throws IOException {
        CloseableHttpClient client = buildHttpClient();
        try {
            printAuthUrl(client);
            String authCode = restClient.getAuth_code();
            if (authCode.matches(CODE_REGEX)) {
                System.out.println("auth code = " + authCode);
                HttpResponse response = client.execute(buildAccessTokenPost(authCode));
                String entity = EntityUtils.toString(response.getEntity());
                if (!entity.equals("{\"error\":\"expired_token\"}")) {
                    System.out.println("Access_token = " + entity.substring(17, 49));
                } else {
                    System.out.println("Could not obtain Access_token. Response entity:" + entity);
                }
                if (response.getEntity() != null)
                    EntityUtils.consume(response.getEntity());
            }
        } finally {
            client.close();
        }
    }

    private void printAuthUrl(CloseableHttpClient client) throws IOException {
        try {
            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = buildAuthorizeGet();
            CloseableHttpResponse response = client.execute(httpGet, context);
            System.out.println(response.getStatusLine());
            if (response.getEntity() != null) {
                EntityUtils.consume(response.getEntity());
            }
            response.close();
            URI location = URIUtils.resolve(httpGet.getURI(), context.getTargetHost(), context.getRedirectLocations());
            System.out.println("Authorize_URL " + location);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private CloseableHttpClient buildHttpClient() {
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }

    private HttpGet buildAuthorizeGet() {
        String url = restClient.getAuthorize_uri() + restClient.getClient_id() + "&response_type=code&redirect_uri=" + restClient.getRedirect_uri();
        return new HttpGet(url);
    }

    private HttpPost buildAccessTokenPost(String code) throws IOException {
        HttpPost post = new HttpPost(restClient.getAccess_token_uri());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
        nameValuePairs.add(new BasicNameValuePair("client_id", restClient.getClient_id()));
        nameValuePairs.add(new BasicNameValuePair("client_secret", restClient.getClient_secret()));
        nameValuePairs.add(new BasicNameValuePair("redirect_uri", restClient.getRedirect_uri()));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        return post;
    }
}
