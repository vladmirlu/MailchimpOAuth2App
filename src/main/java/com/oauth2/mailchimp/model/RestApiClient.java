package com.oauth2.mailchimp.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestApiClient {
    @Value("${client_id}") private String client_id;
    @Value("${client_secret}") private String client_secret;
    @Value("${authorize_uri}") private String authorize_uri;
    @Value("${redirect_uri}") private String redirect_uri;
    @Value("${access_token_uri}") private String access_token_uri;
    @Value("${auth_code}") private String auth_code;

    public String getClient_id() {
        return client_id;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public String getAccess_token_uri() {
        return access_token_uri;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getAuthorize_uri() {
        return authorize_uri;
    }
}
