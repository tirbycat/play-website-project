package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.Md5Hash;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class AdminUser extends Model {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
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
}