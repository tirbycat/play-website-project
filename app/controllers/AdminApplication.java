package controllers;

import forms.LoginForm;
import models.AdminUser;
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
            session().clear();
            SiteUser user = SiteUser.authenticate(loginForm.get().login, loginForm.get().password);
            session("adminUserId",  user.id.toString());
            return redirect(routes.AdminApplication.index());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.AdminApplication.index());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result index() {
        AdminUser user = null;
        if(session("adminUserId") != null && !session("adminUserId").isEmpty()){
            user = AdminUser.find.byId(Integer.parseInt(session("adminUserId")));
        }
        MenuItem title = new MenuItem("Administration panel", routes.AdminApplication.index());

        List<MenuItem> menu = new ArrayList<MenuItem>();
        menu.add(new MenuItem("item1", routes.AdminApplication.index()));
        MenuItem sub = new MenuItem("item2", null);
        sub.addSubMenuItem(new MenuItem("item21", routes.AdminApplication.index()));
        sub.addSubMenuItem(new MenuItem("item22", routes.AdminApplication.index()));
        menu.add(sub);

        MenuItem userSubMenu = new MenuItem(user.login, null);
        userSubMenu.addSubMenuItem(new MenuItem("item21", routes.AdminApplication.index()));
        userSubMenu.addSubMenuItem(new MenuItem("divider", null));
        userSubMenu.addSubMenuItem(new MenuItem("Logout", routes.AdminApplication.logout()));
        menu.add(userSubMenu);

        return ok(admin.render(title, menu));
    }
}
