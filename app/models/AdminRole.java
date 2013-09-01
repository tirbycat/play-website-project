package models;

import com.avaje.ebean.Page;
import org.codehaus.jackson.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

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

    public static Finder<Integer, AdminRole> find = new Finder<Integer, AdminRole>(
            Integer.class, AdminRole.class
    );

    /**
     * Return a page of computer
     *
     * @param page Page to display
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static ObjectNode jsonPage(int page, String sortBy, String order, String filter){
        ObjectNode result = Json.newObject();
        Page<AdminRole> p = find.where()
                .ilike("roleName", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(10)
                .getPage(page);

        List<AdminRole> list = p.getList();
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
}
