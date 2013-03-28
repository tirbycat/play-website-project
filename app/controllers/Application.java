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
            session().clear();
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

        MenuItem title = new MenuItem("Administration panel", routes.Application.index());

        List<MenuItem> menu = new ArrayList<MenuItem>();
        menu.add(new MenuItem("item1", routes.Application.index()));
        MenuItem sub = new MenuItem("item2", null);
        sub.addSubMenuItem(new MenuItem("item21", routes.Application.index()));
        sub.addSubMenuItem(new MenuItem("item22", routes.Application.index()));
        menu.add(sub);
        if(user != null){
            MenuItem userSubMenu = new MenuItem(user.login, null);
            userSubMenu.addSubMenuItem(new MenuItem("item21", routes.AdminApplication.index()));
            userSubMenu.addSubMenuItem(new MenuItem("divider", null));
            userSubMenu.addSubMenuItem(new MenuItem("Logout", routes.AdminApplication.logout()));
            menu.add(userSubMenu);
        }else{

        }
        return ok(index.render(title, menu, form(LoginForm.class)));
    }

    @Security.Authenticated(Secured.class)
    public static Result secretData(){
        return ok();
    }
}
