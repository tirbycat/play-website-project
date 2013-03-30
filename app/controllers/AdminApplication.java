package controllers;

import com.avaje.ebean.Page;
import forms.LoginForm;
import models.AdminUser;
import models.DataField;
import models.MenuItem;
import models.SiteUser;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import secure.AdminSecured;
import views.html.adminpages.admin;
import views.html.sitepages.login;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class AdminApplication  extends Controller {
    public static Result login() {
        return ok(login.render(form(LoginForm.class), true, routes.AdminApplication.authenticate()));
    }

    public static Result authenticate() {
        Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm, true, routes.AdminApplication.authenticate()));
        } else {
            SiteUser user = SiteUser.authenticate(loginForm.get().login, loginForm.get().password);
            session("adminUserId",  user.id.toString());
            return redirect(routes.AdminApplication.index("main"));
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.AdminApplication.login());
    }

    private static List<MenuItem> makeMainMenu(AdminUser user){
        List<MenuItem> menu = new ArrayList<MenuItem>();
        menu.add(new MenuItem("Users", routes.AdminApplication.index("users")));
        MenuItem sub = new MenuItem("Manage", null);
        sub.addSubMenuItem(new MenuItem("Roles", routes.AdminApplication.index("roles")));
        sub.addSubMenuItem(new MenuItem("Administrators", routes.AdminApplication.index("administrators")));
        menu.add(sub);

        MenuItem userSubMenu = new MenuItem(user.login, null);
        userSubMenu.addSubMenuItem(new MenuItem("Change account", routes.AdminApplication.index("account")));
        userSubMenu.addSubMenuItem(new MenuItem("divider", null));
        userSubMenu.addSubMenuItem(new MenuItem("Logout", routes.AdminApplication.logout()));
        menu.add(userSubMenu);

        return menu;
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result index(String screen) {
        AdminUser user = null;
        if(session("adminUserId") != null && !session("adminUserId").isEmpty()){
            user = AdminUser.find.byId(Integer.parseInt(session("adminUserId")));
        }
        MenuItem title = new MenuItem("Administration panel", routes.AdminApplication.index("main"));

        String table = "";
        List<DataField> fields = new ArrayList<DataField>();
        Page<SiteUser> page = null;
        if(screen.equals("users")){
            table = "users";
            fields.add( new DataField("id", "ID"));
            fields.add( new DataField("login", "Login"));
            fields.add( new DataField("email", "Email"));

            //page = SiteUser.page(pagenum, 10, sortBy, order, filter);
            //page.getList().get(0).getClass()
        }

        return ok(admin.render(title, makeMainMenu(user), fields, routes.AjaxController.getTableData(table, 0, 10, "id", "asc", "").url()));
    }
}
