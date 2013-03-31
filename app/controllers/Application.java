package controllers;

import forms.LoginForm;
import forms.RegistrationForm;
import models.MenuItem;
import models.SiteUser;
import play.data.Form;
import play.mvc.*;

import secure.Secured;
import utils.Md5Hash;
import views.html.sitepages.index;
import views.html.sitepages.login;
import views.html.sitepages.registration;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller{

    public static Result login() {
        return ok(login.render(form(LoginForm.class), false, routes.Application.authenticate()));
    }

    public static Result registration() {
        return ok(registration.render(form(RegistrationForm.class)));
    }

    public static Result register() {
        Form<RegistrationForm> regForm = form(RegistrationForm.class).bindFromRequest();
        if (regForm.hasErrors()) {
            return badRequest(registration.render(regForm));
        } else {
            session().clear();
            SiteUser user = SiteUser.create(regForm.get().login, regForm.get().email, regForm.get().password);
            session("userId",  user.id.toString());
            session("decodeStr", Md5Hash.md5(regForm.get().login + regForm.get().password));
            return redirect(routes.Application.index());
        }
    }

    public static Result authenticate() {
        Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm, false, routes.Application.authenticate()));
        } else {
            SiteUser user = SiteUser.authenticate(loginForm.get().login, loginForm.get().password);
            session("userId",  user.id.toString());
            session("decodeStr", Md5Hash.md5(loginForm.get().login + loginForm.get().password));
            return redirect(routes.Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.index());
    }

    public static Result index() {
        SiteUser user = null;
        if(session("userId") != null){
            user = SiteUser.find.byId(Integer.parseInt(session("userId")));
        }

        MenuItem title = new MenuItem("Memo tools", routes.Application.index().url());

        List<MenuItem> menu = new ArrayList<MenuItem>();

        if(user != null){
            menu.add(new MenuItem("Bookmarks", routes.Application.index().url()));
            MenuItem sub = new MenuItem("item2", null);
            sub.addSubMenuItem(new MenuItem("item21", routes.Application.index().url()));
            sub.addSubMenuItem(new MenuItem("item22", routes.Application.index().url()));
            menu.add(sub);

            MenuItem userSubMenu = new MenuItem(user.login, null);
            userSubMenu.addSubMenuItem(new MenuItem("Account", routes.Application.index().url()));
            userSubMenu.addSubMenuItem(new MenuItem("divider", null));
            userSubMenu.addSubMenuItem(new MenuItem("Logout", routes.Application.logout().url()));
            menu.add(userSubMenu);
        }else{
            menu.add(new MenuItem("Registration", routes.Application.registration().url()));
        }
        return ok(index.render(title, menu, user == null ? form(LoginForm.class) : null));
    }

    @Security.Authenticated(Secured.class)
    public static Result secretData(){
        return ok();
    }
}
