package socialapi;
              /*
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookOauth2
{
    private static final Logger log = LoggerFactory.getLogger(FacebookOauth2.class);
    
    private String secret;
    private String appId;
    private String redirectUri;
    private String perms = "publish_stream, email";
    private String accessToken = "";

    public FacebookOauth2(String secret, String appId, String redirectUri)
    {
        this.secret = secret;
        this.appId = appId;
        this.redirectUri = redirectUri;
    }

    public FacebookOauth2(String secret, String appId, String redirectUri, String perms)
    {
        this.secret = secret;
        this.appId = appId;
        this.redirectUri = redirectUri;
        this.perms = perms;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getLoginRedirectURL()
    {
        return "https://graph.facebook.com/oauth/authorize?client_id="
                + appId + "&display=page&redirect_uri=" + redirectUri + "&scope=" + perms;
    }

    private String getAuthURL(String authCode)
    {
        return "https://graph.facebook.com/oauth/access_token?client_id="
                + appId + "&redirect_uri="
                + redirectUri + "&client_secret=" + secret + "&code=" + authCode;
    }
    
    public String getAccessCode(String code)
    {
        try
        {
            String url = getAuthURL(code);
            
            Client c = Client.create();
            WebResource r = c.resource(url);
            String answer = r.get(String.class);
            
            int ind = answer.indexOf("access_token=") + "access_token=".length();
            if(answer.indexOf("&", ind) != -1)
                accessToken = answer.substring(ind, answer.indexOf("&", ind));
            else
                accessToken = answer.substring(ind, answer.length());
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }
        
        return accessToken;
    }
    
    public JSONObject makeAPICall(String method)
    {
        JSONObject answerJson = null;
        try
        {
            String url = "https://graph.facebook.com" + method + "&access_token=" + accessToken;
            Client c = Client.create();
            WebResource r = c.resource(url);
            String answer = r.get(String.class);
            answerJson = (JSONObject) (new JSONParser()).parse(answer);
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }

        return answerJson;
    }
    
    public JSONObject makeAPIPostCall(String method, Form formParams)
    {
        JSONObject answerJson = null;
        formParams.add("access_token", accessToken);
        try
        {
            String url = "https://graph.facebook.com" + method;
            Client c = Client.create();
            WebResource r = c.resource(url);
            String answer = r.post(String.class, formParams);
            answerJson = (JSONObject) (new JSONParser()).parse(answer);
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }

        return answerJson;
    }
        
    public JSONObject makeAPIMultipartCall(String method, FormDataMultiPart formParams)
    {
        JSONObject answerJson = null;
        formParams.field("access_token", accessToken);
        try
        {
            String url = "https://graph.facebook.com" + method;
            Client c = Client.create();
            WebResource r = c.resource(url);
            String answer = r.type(MediaType.MULTIPART_FORM_DATA).post(String.class, formParams);
            answerJson = (JSONObject) (new JSONParser()).parse(answer);
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }

        return answerJson;
    }
        
    public JSONObject parseSignedRequest(String signedRequest)
    {
        JSONObject signedData = null;
        String[] parts = StringUtils.split(signedRequest, ".");
        String encodedSig = parts[0];
        String encodedData = parts[1];
        try
        {
            String jsonDataString = new String(base64Decode(encodedData), Charset.forName("UTF-8"));
            signedData = (JSONObject) (new JSONParser()).parse(jsonDataString);

            String algorithm = (String) signedData.get("algorithm");
            if (!algorithm.equals("AES-256-CBC HMAC-SHA256") && !algorithm.equals("HMAC-SHA256"))
                return null;

            byte[] key = secret.getBytes();
            SecretKey hmacKey = new SecretKeySpec(key, "HMACSHA256");
            Mac mac = Mac.getInstance("HMACSHA256");
            mac.init(hmacKey);
            byte[] digest = mac.doFinal(parts[1].getBytes());

            if (!Arrays.equals(base64Decode(encodedSig), digest))
                return null;

            // for requests that are signed, but not encrypted, we"re done
            if (algorithm.equals("HMAC-SHA256"))
                return signedData;

            // otherwise, decrypt the payload
            byte[] iv = base64Decode((String) signedData.get("iv"));
            IvParameterSpec ips = new IvParameterSpec(iv);
            SecretKey aesKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, ips);
            byte[] raw_ciphertext = base64Decode((String) signedData.get("payload"));
            byte[] plaintext = cipher.doFinal(raw_ciphertext);
            return (JSONObject) (new JSONParser()).parse(new String(plaintext).trim());
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }

        return signedData;
    }

    public static byte[] base64Decode(String input) throws IOException {
        return new Base64(true).decode(input);
    }    
}
*/