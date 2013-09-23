package socialapi;

import play.libs.F.Function;
import play.libs.OAuth;
import play.libs.WS;
import play.mvc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tirbycat
 * Date: 07.09.13
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class OAuthAuthorizator {
    private static final Logger log = LoggerFactory.getLogger(OAuthAuthorizator.class);

    protected String secret;
    protected String appId;
    protected String redirectUri;
    protected String accessToken;
    protected String socialApiUrl = "api.vk.com";
    protected String perms = "notify,friends,photos,wall,groups";

    public OAuthAuthorizator(String secret, String appId, String redirectUri)
    {
        this.secret = secret;
        this.appId = appId;
        this.redirectUri = redirectUri;


    }

    public OAuthAuthorizator(String secret, String appId, String redirectUri, String perms)
    {
        this.secret = secret;
        this.appId = appId;
        this.redirectUri = redirectUri;
        this.perms = perms;
    }

    public OAuthAuthorizator(String accessToken)
    {
        this.accessToken = accessToken;
    }

    abstract protected String getLoginRedirectURL();
    abstract protected String getAuthURL(String authCode);

    public String getAccessCode(String code)
    {
        try
        {
            String url = getAuthURL(code);

            Result result = Results.async(
                WS.url(url).get().map(
                    new Function<WS.Response, Result>() {
                        public Result apply(WS.Response response) {
                            return Results.ok(response.getBody());
                        }
                    }
                )
            );
//            result.asJson();

//            Client c = Client.create();
//            WebResource r = c.resource(url);
//            String answer = r.get(String.class);
//
//            int ind = answer.indexOf("access_token=") + "access_token=".length();
//            if(answer.indexOf("&", ind) != -1)
//                accessToken = answer.substring(ind, answer.indexOf("&", ind));
//            else
//                accessToken = answer.substring(ind, answer.length());
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }

        return accessToken;
    }
}
