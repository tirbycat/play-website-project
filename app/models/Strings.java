package models;

import com.avaje.ebean.Page;
import org.codehaus.jackson.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tirbycat
 * Date: 31.03.13
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="strings")
public class Strings extends Model {
    @Id
    @Constraints.Required
    @Column(name="str_enum")
    public String id;

    @Constraints.Required
    @Column(name="str")
    public String val;

    public Strings(String id, String val) {
        this.id = id;
        this.val = val;
    }

    public static Strings create(String id, String val){
        Strings var = new Strings(id, val);
        var.save();
        return var;
    }

    public static Finder<String, Strings> find = new Finder<String, Strings>(
            String.class, Strings.class
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
        Page<Strings> p = find.where()
                            .ilike("str_enum", "%" + filter + "%")
                            .orderBy(sortBy + " " + order)
                            .findPagingList(10)
                            .getPage(page);

        List<Strings> list = p.getList();
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
        Strings p = find.byId(id);

        result.put("data", Json.toJson(p));
        return result;
    }

    public static ObjectNode editRecord(Strings object){
        ObjectNode result = Json.newObject();

        object.update();
        return result;
    }

    public static ObjectNode deleteRecord(String id){
        ObjectNode result = Json.newObject();

        Strings.find.ref(id).delete();

        return result;
    }
}