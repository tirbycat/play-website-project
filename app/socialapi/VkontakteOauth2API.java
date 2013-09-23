package socialapi;


import controllers.*;
import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.libs.F;
import play.libs.OAuth;
import play.mvc.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class VkontakteOauth2API extends Controller{
    private OAuth oAuth;
    private static String app_id;
    private static String secret;
    private static String redirectUri;
    private static String accessToken;
    private static String vkApiUrl = "api.vk.com";
    private static String perms = "notify,friends,photos,wall,groups";


    public static Result getSocialToken() {
        String param = Http.Context.current().request().getQueryString("code");


        return Results.async(
            play.libs.WS.url(getAccessTokenUrl(param)).get().map(
                new F.Function<play.libs.WS.Response, Result>() {
                    public Result apply(play.libs.WS.Response response) {
                        return Results.ok(response.getBody());
                    }
                }
            )
        );
    }

    public VkontakteOauth2API(String appId, String secret, String perms)
    {
        this.app_id = appId;
        this.secret = secret;
        try {
            this.redirectUri = URLEncoder.encode("localhost:9000" + routes.VkontakteOauth2API.getSocialToken().url(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.perms = perms;
    }

    protected static String getLoginRedirectUrl()
    {
        return "http://" + vkApiUrl + "/oauth/authorize?client_id=" + app_id +
                "&display=page&scope=" + perms + "&redirect_uri="+ redirectUri;
    }

    protected static String getAccessTokenUrl(String code)
    {
        String url = "https://" + vkApiUrl + "/oauth/access_token?client_id=" + app_id + "&client_secret=" + secret +
                "&code=" + code+ "&redirect_uri=" + redirectUri;
        return url;
    }

    public static boolean isCodeResponse() {
        String param = Http.Context.current().request().getQueryString("code");

        return param != null;
    }

    public Result retrieveVerificationCode() {
        return Results.redirect(getLoginRedirectUrl());
    }

}

/*
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VkontakteOauth2API
{
    private static final Logger log = LoggerFactory.getLogger(VkontakteOauth2API.class);
    
    private String secret;
    private String appId;
    private String redirectUri;
    private String accessToken;
    private String vkApiUrl = "api.vk.com";
    private String perms = "notify,friends,photos,wall,groups";

    public VkontakteOauth2API(String secret, String appId, String redirectUri)
    {
        this.secret = secret;
        this.appId = appId;
        this.redirectUri = redirectUri;
    }
    
    public VkontakteOauth2API(String secret, String appId, String redirectUri, String perms)
    {
        this.secret = secret;
        this.appId = appId;
        this.redirectUri = redirectUri;
        this.perms = perms;
    }

    public VkontakteOauth2API(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getLoginRedirectURL()
    {
        return "http://" + vkApiUrl + "/oauth/authorize?client_id=" + appId + "&redirect_uri=" + redirectUri + 
                "&display=page&scope=" + perms;
    }

    public JSONObject getAccessToken(String code) throws VKException
    {
        JSONObject answerJson = null;
        try
        {
            String url = "https://" + vkApiUrl + "/oauth/access_token?client_id=" + appId + "&client_secret=" + secret + 
                    "&code="+code+"&redirect_uri=" + redirectUri;
            
            Client c = Client.create();
            WebResource r = c.resource(url);
            String answer = r.get(String.class);
            answerJson = (JSONObject) (new JSONParser()).parse(answer);            
            accessToken = (String)answerJson.get("access_token");
            if(accessToken == null || accessToken.equals(""))
            {
                throw new VKException("error: " + answerJson.get("error") + 
                        "; error_description: " + answerJson.get("error_description"));
            }
        }
        catch (ParseException ex)
        {
            log.error("", ex);
        }
        
        return answerJson;
    }
    
    public JSONArray getFriends(String userId) throws VKException
    {
        JSONArray answer = null;
        
        String timestamp = ((Long)(System.currentTimeMillis()/1000)).toString();
        String random = ((Double)Math.random()).toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", userId);
        params.put("access_token", accessToken);
        params.put("timestamp", timestamp);
        params.put("random", random);

        answer = (JSONArray) makeAPICall("friends.get", params).get("response");
            
        return answer;
    }
    
    public JSONArray getProfiles(String userId, String fields) throws VKException
    {
        JSONArray answer = null;
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", userId);
        params.put("fields", fields);
        params.put("access_token", accessToken);

        answer = (JSONArray) makeAPICall("getProfiles", params).get("response");
            
        return answer;
    }
    
    public static void loadUserPhoto(String url, File outFile)
    {    
        try{
            URL myURL = new URL(url);
            URLConnection myUC = myURL.openConnection();

            DataInputStream in = new DataInputStream(myUC.getInputStream());
            DataOutputStream os = new DataOutputStream(new FileOutputStream(outFile));

            byte pictbuf[] = new byte[myUC.getContentLength()];
            in.readFully(pictbuf);

            os.write(pictbuf);
            os.close();
        }catch( Exception ex ){
            log.info("", ex);
        }
    }
    
    public JSONObject makeAPICall(String method, Map<String, String> params) throws VKException
    {
        JSONObject answerJson = null;
        try
        {
            String url = "https://" +vkApiUrl + "/method/" + method + "?access_token=" + accessToken;
            
            if(params != null && !params.isEmpty())
                for(String key : params.keySet())
                    url += "&" + key + "=" + params.get(key);
            
            Client c = Client.create();
            WebResource r = c.resource(url);
            String answer = r.get(String.class);
            answerJson = (JSONObject) (new JSONParser()).parse(answer);
            if(answerJson.get("response") == null)
                throw new VKException("error while call " + method + "; url: " + url + "; answer: " + answer);
        }
        catch (ParseException ex)
        {
            log.error("", ex);
        }

        return answerJson;
    }

    public String getVkApiUrl()
    {
        return vkApiUrl;
    }

    public void setVkApiUrl(String vkApiUrl)
    {
        this.vkApiUrl = vkApiUrl;
    }
}
*/