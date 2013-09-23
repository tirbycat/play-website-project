package socialapi;
/*
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoyMirMailRuOauth2 {
    private static final Logger log = LoggerFactory.getLogger(MoyMirMailRuOauth2.class);
    private String appId, scope, redirectUri, accessToken, appSecret, appKey;
    
    public MoyMirMailRuOauth2(String appId, String appSecret, String appKey, String redirectUri, String scope) {
        this.appId = appId;
        this.scope = scope;
        this.appSecret=appSecret;
        this.appKey=appKey;
        try{
            this.redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        }catch(Exception ex){
            log.error("encode url error", ex);
        }
    }
    public String getLoginRedirectURL(){
        return String.format("https://connect.mail.ru/oauth/authorize?"
                + "client_id=%s&scope=%s&response_type=code&redirect_uri=%s", appId, scope, redirectUri);        
    }
    public String getAccessToken(String code){
        String url=String.format("https://connect.mail.ru/oauth/token?code=%s&redirect_uri=%s&grant_type=authorization_code"+
				"&client_id=%s&client_secret=%s", code, redirectUri, appId, appSecret);        
        JSONObject obj=sendHttpPost(url);
        if(obj!=null){
            if(obj.containsKey("error_code")) 
                return null;
            accessToken=(String)obj.get("access_token");
            return accessToken;
        }
        return null;
    }
    
    public JSONObject makeAPICall(TreeMap<String, Object> params){
        if(accessToken==null)
            return null;        
        if(params==null)
            params=new TreeMap<String, Object>();
        params.put("app_id", appId);
        params.put("session_key", accessToken);
        params.put("secure", "1");
                
        List<String> strings=new ArrayList<String>();
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            strings.add(""+pairs.getKey()+"="+pairs.getValue());            
        }
        params.put("sig", sig(strings));
        String url=String.format("http://www.appsmail.ru/platform/api?%s", createQueryString(params));
        JSONObject obj=sendHttpGet(url);
        return obj;
    }
    
    private String sig(List<String> params){
        String str="";
        for(int i=0;i<params.size();i++)
                str+=params.get(i);
        str+=appSecret;
        return md5(str);
    }
    
    public static String md5(String s) {
        try {          
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();	        
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();	 
        } catch (Exception ex){
            log.error("md5 hash error" ,ex);
        }
        return null;
    }
    private JSONObject sendHttpPost(String URL){
        try {		  
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);	  
            HttpPost httpPostRequest = new HttpPost(URL);
            httpPostRequest.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
            HttpEntity entity = response.getEntity();
            if (entity != null) {			   
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))
                    instream = new GZIPInputStream(instream);                            
                String resultString= convertStreamToString(instream);
                instream.close();
                JSONObject jsonObjRecv = (JSONObject)JSONValue.parse(resultString);
                return jsonObjRecv;
            } 	
        }
        catch (Exception ex){	  
            log.error("error", ex);
        }
        return null;
    }
    private JSONObject sendHttpGet(String URL) {
        JSONObject jsonObjRecv=null;
        String resultString="";
        try {	  
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);	  

            HttpGet httpGetRequest=new HttpGet(URL);
            httpGetRequest.setHeader("Accept", "application/json");

            HttpResponse response = (HttpResponse) httpclient.execute(httpGetRequest);	  	
            HttpEntity entity = response.getEntity();
            if (entity != null){
            InputStream instream = entity.getContent();
            Header contentEncoding = response.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) 
                instream = new GZIPInputStream(instream);
            resultString= convertStreamToString(instream);
            instream.close();	 
            jsonObjRecv = (JSONObject)JSONValue.parse(resultString);
            }	   	   	
        }
        catch (Exception ex)
        {	  
            log.error("error",ex);
            jsonObjRecv=new JSONObject();
            jsonObjRecv.put("response", resultString);
        }
        return jsonObjRecv;
    }
    private String convertStreamToString(InputStream is){	 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();	
            String line = null;        
            while ((line = reader.readLine()) != null)
                sb.append(line);
            is.close();
            return sb.toString();
        } catch (Exception ex){
            log.error("error", ex);
        }         
        return null;
    }
    public static String createQueryString(TreeMap<String, Object> map){
        String result="";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            result+=""+pairs.getKey()+"="+pairs.getValue();
            result+="&";
            it.remove();
        }
        return result.substring(0, result.length()-1);
    }
}
*/