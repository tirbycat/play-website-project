package controllers;

import com.avaje.ebean.Page;
import forms.ChangePasswordForm;
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
import views.html.adminpages.usersScreen;
import views.html.adminpages.adminusersScreen;
import views.html.adminpages.rolesScreen;
import views.html.adminpages.stringsScreen;
import views.html.adminpages.variablesScreen;
import views.html.adminpages.newsScreen;
import views.html.adminpages.mainScreen;
import views.html.adminpages.accountScreen;
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
            AdminUser user = AdminUser.authenticate(loginForm.get().login, loginForm.get().password);
            session("adminUserId",  user.id.toString());
            return redirect(routes.AdminApplication.index());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.AdminApplication.login());
    }

    private static List<MenuItem> makeMainMenu(AdminUser user){
        List<MenuItem> menu = new ArrayList<MenuItem>();
        menu.add(new MenuItem("Users", "#/users"));
        MenuItem sub = new MenuItem("Manage", null);
        sub.addSubMenuItem(new MenuItem("Roles", "#/roles"));
        sub.addSubMenuItem(new MenuItem("Administrators", "#/administrators"));
        menu.add(sub);

        MenuItem sub3 = new MenuItem("News", "#/news");
        menu.add(sub3);

        MenuItem sub1 = new MenuItem("Variables", "#/variables");
        menu.add(sub1);

        MenuItem sub2 = new MenuItem("Strings", "#/strings");
        menu.add(sub2);

        MenuItem userSubMenu = new MenuItem(user.login, null);
        userSubMenu.addSubMenuItem(new MenuItem("Change account", "#/account"));
        userSubMenu.addSubMenuItem(new MenuItem("divider", null));
        userSubMenu.addSubMenuItem(new MenuItem("Logout", routes.AdminApplication.logout().url()));
        menu.add(userSubMenu);

        return menu;
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result index() {
        AdminUser user = AdminUser.find.byId(Integer.parseInt(session("adminUserId")));
        if(user == null){
            return redirect(routes.AdminApplication.logout());
        }
        MenuItem title = new MenuItem("Administration panel", "#/main");
        return ok(admin.render(title, "Company Name", makeMainMenu(user)));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result getScreen(String screen) {

        String table = "";
        List<DataField> fields = new ArrayList<DataField>();
        Page<SiteUser> page = null;
        if(screen.equals("users")){
            table = "SiteUser";
            fields.add( new DataField("id", "ID"));
            fields.add( new DataField("login", "Login"));
            fields.add( new DataField("email", "Email"));

            return ok(usersScreen.render(fields, routes.AjaxController.getTableData(table, 0, "id", "asc", "", "").url()));
        }else if(screen.equals("roles")){
            table = "AdminRole";
            fields.add( new DataField("id", "ID"));
            fields.add( new DataField("roleName", "Name"));
            fields.add( new DataField("userRights", "Rights"));

            return ok(rolesScreen.render(fields, routes.AjaxController.getTableData(table, 0, "id", "asc", "", "").url()));
        }else if(screen.equals("administrators")){
            table = "AdminUser";
            fields.add( new DataField("id", "ID"));
            fields.add( new DataField("login", "Login"));
            fields.add( new DataField("email", "Email"));
            fields.add( new DataField("role.roleName", "Role"));

            return ok(adminusersScreen.render(fields, routes.AjaxController.getTableData(table, 0, "id", "asc", "", "").url()));
        }if(screen.equals("news")){
            table = "News";
            fields.add( new DataField("id", "ID"));
            fields.add( new DataField("date", "Date"));
            fields.add( new DataField("title", "Title"));
            fields.add( new DataField("shortText", "Text"));

            return ok(newsScreen.render(fields, routes.AjaxController.getTableData(table, 0, "id", "asc", "", "").url()));
        }if(screen.equals("variables")){
            table = "Variable";
            fields.add( new DataField("name", "Name"));
            fields.add( new DataField("val", "Value"));

            return ok(variablesScreen.render(fields, routes.AjaxController.getTableData(table, 0, "id", "asc", "", "").url()));
        }else if(screen.equals("strings")){
            table = "Strings";
            fields.add( new DataField("id", "Name"));
            fields.add( new DataField("val", "Value"));

            return ok(stringsScreen.render(fields, routes.AjaxController.getTableData(table, 0, "id", "asc", "", "").url()));
        }else if(screen.equals("account")){
            return ok(accountScreen.render());
        }else{
            return ok(mainScreen.render());
        }
    }
}
