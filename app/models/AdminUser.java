package models;

import com.avaje.ebean.Page;
import forms.AdminUserForm;
import org.codehaus.jackson.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;
import utils.Md5Hash;
import com.typesafe.plugin.*;

import javax.persistence.*;
import java.util.List;
import static play.data.Form.form;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class AdminUser extends Model{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer id;

    @Constraints.Required
    @Constraints.MaxLength(40)
    @Column(length = 40, columnDefinition = "VARCHAR(40)")
    public String login;

    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Constraints.MaxLength(40)
    @Column(length = 40, columnDefinition = "VARCHAR(40)")
    public String password;

    @OneToOne(targetEntity=AdminRole.class,cascade=CascadeType.ALL)
    @JoinColumn(name="role_id",referencedColumnName="id")
    public AdminRole role;

    public AdminUser() {
    }

    public AdminUser(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = Md5Hash.md5(password);
    }

    public static AdminUser create(String login, String email, String password){
        AdminUser user = new AdminUser(login, email, password);
        user.save();
        return user;
    }

    public static AdminUser authenticate(String login, String password) {
        return find.where().eq("login", login)
                .eq("password", Md5Hash.md5(password)).findUnique();
    }

    public static Boolean exists(String login){
        return find.where().eq("login", login).findUnique() != null;
    }

    public static Finder<Integer, AdminUser> find = new Finder<Integer, AdminUser>(
            Integer.class, AdminUser.class
    );

    /**
     * Return a page
     *
     * @param page Page to display
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static ObjectNode jsonPage(Integer page, String sortBy, String order, String filter){
        ObjectNode result = Json.newObject();
        Page<AdminUser> p = find.where()
                .ilike("login", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(10)
                .getPage(page);

        List<AdminUser> list = p.getList();
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

    public static ObjectNode jsonValue(String id){
        ObjectNode result = Json.newObject();
        AdminUser p;
        if(id.equals("new")){
            p=new AdminUser();
        }else{
            p = find.byId(Integer.parseInt(id));
        }

        result.put("data", Json.toJson(p));
        return result;
    }

    public static ObjectNode editRecord(){
        ObjectNode result = Json.newObject();
        AdminUserForm object =  form(AdminUserForm.class).bindFromRequest().get();
        if(object.id != null){
            AdminUser au = AdminUser.find.ref(object.id);
            au.login = object.login;
            au.email = object.email;

            if(object.roleId != 0 ){
                au.role = AdminRole.find.byId(object.roleId);
            }
            au.update();
        }else{
            AdminUser au = new AdminUser(object.login, object.email, object.password);
            if(object.roleId != 0 ){
                au.role = AdminRole.find.byId(object.roleId);
            }
            au.save();

            if(!object.email.isEmpty()){
                MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
                mail.setSubject("Administration panel");
                mail.addRecipient(object.login + "<" + object.email + ">");
                mail.addFrom("<noreply@tirbycat.ru>");

                mail.send("Вам предоставлен доступ в систему управления контентом сайта http://tirbycat.ru/admimistration"  + "\nЛогин: " + object.login + "\nПароль: " + object.password);
            }
        }

        return result;
    }

    public static void changePassword(Integer id, String password){
        AdminUser au = AdminUser.find.ref(id);
        au.password = Md5Hash.md5(password);
        au.update();
    }

    public static ObjectNode deleteRecord(String id){
        ObjectNode result = Json.newObject();
        AdminUser.find.ref(Integer.parseInt(id)).delete();

        return result;
    }
}