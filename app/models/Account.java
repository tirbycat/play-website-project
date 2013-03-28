package models;

import com.avaje.ebean.Page;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * SiteUser: tirbycat
 * Date: 16.03.13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Account extends Model{
    @Id
    public Integer id;

    @Constraints.Required
    @Constraints.MaxLength(40)
    @Column(length = 40, columnDefinition = "VARCHAR(40)")
    public String name;

    @Constraints.Required
    public String url;

    public String password;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static List<Account> findTodoInvolving(Integer userId) {
        return find.where()
                .eq("assigned_to_id", userId)
                .findList();
    }

    public static Account create(String name, String password) {
        Account account = new Account(name, password);
        account.save();
        return account;
    }

    public static Model.Finder<String, Account> find = new Model.Finder(String.class, Account.class);

    /**
     * Return a page of computer
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Account> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
                find.where()
                        .ilike("name", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .fetch("company")
                        .findPagingList(pageSize)
                        .getPage(page);
    }
}
