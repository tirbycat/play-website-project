package models;

import com.avaje.ebean.Page;
import org.codehaus.jackson.JsonNode.*;
import org.codehaus.jackson.node.ObjectNode;
import play.db.ebean.Model;
import play.libs.Json;
import scala.util.parsing.json.JSONArray;
import utils.Md5Hash;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Email;
import static play.data.Form.form;
/**
 * Created with IntelliJ IDEA.
 * SiteUser: tirbycat
 * Date: 16.03.13
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class SiteUser extends Model {
    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE)
    public Integer id;

    @Required
    @MaxLength(40)
    @Column(length = 40, columnDefinition = "VARCHAR(40)")
    public String login;

    @Required
    @MaxLength(255)
    @Email
    public String email;

    @Required
    @MaxLength(40)
    @Column(length = 40, columnDefinition = "VARCHAR(40)")
    public String password;

    @OneToMany(targetEntity=Account.class,cascade=CascadeType.ALL)
    @JoinColumn(name="user_id",referencedColumnName="id")
    public List<Account> accounts;

    public SiteUser(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = Md5Hash.md5(password);
    }

    public static SiteUser create(String login, String email, String password){
        SiteUser user = new SiteUser(login, email, password);
        user.save();
        return user;
    }

    public static SiteUser authenticate(String login, String password) {
        return find.where().eq("login", login)
                .eq("password", Md5Hash.md5(password)).findUnique();
    }

    public static Boolean exists(String login){
        return find.where().eq("login", login).findUnique() != null;
    }

    public static Finder<Integer, SiteUser> find = new Finder<Integer, SiteUser>(
            Integer.class, SiteUser.class
    );

    /**
     * Return a page of computer
     *
     * @param page Page to display
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<SiteUser> page(Integer page, String sortBy, String order, String filter) {
        return
                find.where()
                        .ilike("login", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        //.fetch("accounts")
                        .findPagingList(10)
                        .getPage(page);
    }

    public static ObjectNode jsonPage(Integer page, String sortBy, String order, String filter){
        ObjectNode result = Json.newObject();
        Page<SiteUser> p = page(page, sortBy, order, filter);

        List<SiteUser> list = p.getList();
        result.put("data", Json.toJson(list));
        result.put("sortBy", sortBy);
        result.put("order", order);
        result.put("pageIndex", p.getPageIndex());
        result.put("pageCount", p.getTotalPageCount());
        result.put("hasPrev", p.hasPrev());
        result.put("hasNext", p.hasNext());
        result.put("getDisplayNum", p.getDisplayXtoYofZ(" to "," of "));

        return result;
    }

    public static ObjectNode editRecord(String filter){
        ObjectNode result = Json.newObject();
        return result;
    }

    public static ObjectNode deleteRecord(String filter){
        ObjectNode result = Json.newObject();
        return result;
    }
}