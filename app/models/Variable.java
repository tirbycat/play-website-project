package models;

import com.avaje.ebean.Page;
import org.codehaus.jackson.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;
import utils.Md5Hash;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tirbycat
 * Date: 31.03.13
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Variable extends Model {
    @Id
    @Constraints.Required
    public String name;

    @Constraints.Required
    public String val;

    public Variable(String name, String val) {
        this.name = name;
        this.val = val;
    }

    public static Variable create(String name, String val){
        Variable var = new Variable(name, val);
        var.save();
        return var;
    }

    public static Finder<String, Variable> find = new Finder<String, Variable>(
            String.class, Variable.class
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
        Page<Variable> p = find.where()
                            .ilike("name", "%" + filter + "%")
                            .orderBy(sortBy + " " + order)
                            .findPagingList(10)
                            .getPage(page);

        List<Variable> list = p.getList();
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
        Variable p = find.byId(id);

        result.put("data", Json.toJson(p));
        return result;
    }

    public static ObjectNode editRecord(Variable object){
        ObjectNode result = Json.newObject();

        object.update();
        return result;
    }

    public static ObjectNode deleteRecord(String filter){
        ObjectNode result = Json.newObject();
        return result;
    }
}