package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class AdminRole extends Model {
    public static final Integer ADD_USER = 0;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    public Integer id;

    @Constraints.Required
    public String roleName;

    @Constraints.Required
    public String userRights;

    public AdminRole(String roleName, String userRights) {
        this.roleName = roleName;
        this.userRights = userRights;
    }

    public static AdminRole create(String name, String permissions){
        AdminRole role = new AdminRole(name, permissions);
        role.save();
        return role;
    }
}
