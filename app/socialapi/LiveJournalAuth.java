package socialapi;
/*
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class LiveJournalAuth {
    private static final Logger log = LoggerFactory.getLogger(LiveJournalAuth.class);    
    protected XmlRpcClient xmlRpcClient;
    private String login, password;
    private static final String baseUrl="http://www.livejournal.com";

    public LiveJournalAuth(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public void LJPost(String title, String body){
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL("http://www.livejournal.com/interface/xmlrpc"));
            config.setBasicEncoding("UTF-8");
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{};
            Object result = client.execute("LJ.XMLRPC.getchallenge", params);
            if(result instanceof HashMap){
                HashMap res = (HashMap) result;
                if(res.containsKey("challenge")){
                    String challange = (String) res.get("challenge");
                    
                    HashMap paramObj = new HashMap();
                    paramObj.put("username", login);
                    paramObj.put("auth_method", "challenge");
                    paramObj.put("auth_challenge", challange);
                    paramObj.put("auth_response", DigestUtils.md5Hex(challange.concat(DigestUtils.md5Hex(password))));
                    paramObj.put("ver", "1");
                    paramObj.put("lineendings", "\n");
                    paramObj.put("subject", title);
                    paramObj.put("event", body);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(new Date());
                    paramObj.put("day", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                    paramObj.put("mon", String.valueOf(cal.get(Calendar.MONTH)));
                    paramObj.put("year", String.valueOf(cal.get(Calendar.YEAR)));
                    paramObj.put("hour", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
                    paramObj.put("min", String.valueOf(cal.get(Calendar.MINUTE)));
                    paramObj.put("security", "public");
                    
                    HashMap propsObj = new HashMap();
                    propsObj.put("opt_preformatted", true);
                    propsObj.put("opt_backdated", true);
                    propsObj.put("taglist", "Взлёты и падения");
                    
                    paramObj.put("props", propsObj);
                    
                    Object[] postParams = new Object[]{paramObj};
                    Object postResult = client.execute("LJ.XMLRPC.postevent", postParams);
                }
            }
            
        } catch (Exception ex) {
            log.error("lj error", ex);
        }
    }
    
    public HashMap<String, Object> login(){
        HashMap<String, Object> map=new HashMap<String, Object>();
        try{
            String xml="<?xml version=\"1.0\"?><methodCall><methodName>LJ.XMLRPC.login</methodName><params><param>"
                    + "<value><struct><member><name>username</name><value><string>"+login+"</string></value></member>"
                    + "<member><name>password</name><value><string>"+password+"</string></value></member>"
                    + "<member><name>getpickwurls</name><value><int>1</int></value></member>"
                    + "<member><name>ver</name><value><int>1</int></value></member></struct></value></param></params></methodCall>";
            URL url = new URL(baseUrl+"/interface/xmlrpc"); 
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false); 
            connection.setRequestMethod("POST"); 
            connection.setRequestProperty("Content-Type", "text/xml"); 
            connection.setRequestProperty("User-Agent", "XMLRPC Client 1.0");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(xml.getBytes().length));
            connection.setUseCaches (false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(xml);
            wr.flush();            
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); 
            DocumentBuilder builder = domFactory.newDocumentBuilder();            
            Document doc = builder.parse(connection.getInputStream());
            XPath xpath = XPathFactory.newInstance().newXPath();            
            XPathExpression expr = xpath.compile("//member/name[text()='userid']/following-sibling::value/int/text()");
            Integer id=((Double)expr.evaluate(doc, XPathConstants.NUMBER)).intValue();
            map.put("id", id);
            expr = xpath.compile("//member/name[text()='fullname']/following-sibling::value/string/text()");            
            Object obj = expr.evaluate(doc, XPathConstants.STRING);
            if(obj!=null){
                String fullname=(String)obj;
                if(fullname.contains(" ")){
                    String names[]=fullname.split(" ");                    
                    map.put("lname", names[0]);
                    map.put("fname", names[1]);
                }else{
                    map.put("fname", fullname);
                    map.put("lname", fullname);
                }
            }else{
                expr = xpath.compile("//member/name[text()='username']/following-sibling::value/string/text()");            
                obj = expr.evaluate(doc, XPathConstants.STRING);
                if(obj!=null){
                    String username=(String)obj;
                    map.put("fname", username);
                    map.put("lname", username);
                }
            }
            log.info(doc.getTextContent());
            expr = xpath.compile("//member/name[text()='defaultpicurl']/following-sibling::value/string/text()");
            obj=expr.evaluate(doc, XPathConstants.STRING);
            if(obj!=null)
                map.put("photo", (String)obj);
            else map.put("photo", "http://l-stat.livejournal.com/img/userinfo.gif");
            connection.disconnect();            
        }catch(Exception ex){
            log.error("lj error", ex);
            return null;
        }
        return map;
    }
}
*/