package models;

import com.avaje.ebean.Page;
import org.codehaus.jackson.node.ObjectNode;
import org.joda.time.LocalDate;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

/**
 * Created with IntelliJ IDEA.
 * User: tirbycat
 * Date: 16.09.13
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class News extends Model {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer id;

    @Constraints.Required
    public String title;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Constraints.Required
    public Date date;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public String getStringDate(){
        if(date!=null){
            return simpleDateFormat.format(date);
        }else{
            return simpleDateFormat.format(new Date());
        }
    }

    @Constraints.Required
    public String shortText;

    @Constraints.Required
    public String fullText;

    @Constraints.Required
    public String smallPict;

    @Constraints.Required
    public String bigPict;

    public News() {
    }

    public News(String title, Date date, String shortText, String fullText, String smallPict, String bigPict) {
        this.title = title;
        this.date = date;
        this.shortText = shortText;
        this.fullText = fullText;
        this.smallPict = smallPict;
        this.bigPict = bigPict;
    }

    public static News create(String title, Date date, String shortText, String fullText, String smallPict, String bigPict){
        News var = new News(title, date, shortText, fullText, smallPict, bigPict);
        var.save();
        return var;
    }

    public static Finder<Integer, News> find = new Finder<Integer, News>(
            Integer.class, News.class
    );

    /**
     * Return a page of computer
     *
     * @param page Page to display
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static ObjectNode jsonPage(Integer page, String sortBy, String order, String filter){
        ObjectNode result = Json.newObject();
        Page<News> p = find.where()
                .ilike("title", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(10)
                .getPage(page);

        List<News> list = p.getList();
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
        News p;
        if(id.equals("new")){
            p=new News();
        }else{
            p = find.byId(Integer.parseInt(id));
        }

        result.put("data", Json.toJson(p));
        return result;
    }

    public static ObjectNode editRecord(){
        ObjectNode result = Json.newObject();
        News var =  form(News.class).bindFromRequest().get();
        if(var.id != null){
            var.update();
        }else{
            var.save();
        }
        return result;
    }

    public static ObjectNode deleteRecord(String filter){
        ObjectNode result = Json.newObject();
        News.find.ref(Integer.parseInt(filter)).delete();

        return result;
    }
}