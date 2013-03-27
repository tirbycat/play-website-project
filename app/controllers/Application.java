package controllers;

import models.SiteUser;
import java.util.List;
import play.*;
import play.data.Form;
import play.mvc.*;

import utils.Md5Hash;
import views.html.*;

import static play.data.Form.form;

public class Application extends Controller {
    public static class Login {
        public String login;
        public String password;

        public String validate() {
            if (SiteUser.authenticate(login, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static class Registration {
        public String login;
        public String email;
        public String password;

        public String validate() {
            if(login.isEmpty() || SiteUser.exists(login)){
                return "Invalid user login or exists";
            }
            return null;
        }
    }

    public static Result index() {
        SiteUser user = null;
        if(session("userId") != null){
            SiteUser.find.byId(Integer.parseInt(session("userId")));
        }
        return ok(index.render(user, "", form(Login.class)));
    }

    public static Result login() {
        return ok(login.render(form(Login.class)));
    }

    public static Result registration() {
        return ok(registration.render(form(Registration.class)));
    }

    public static Result register() {
        Form<Registration> regForm = form(Registration.class).bindFromRequest();
        if (regForm.hasErrors()) {
            return badRequest(registration.render(regForm));
        } else {
            session().clear();
            SiteUser user = SiteUser.create(regForm.get().login, regForm.get().email, regForm.get().password);
            session("userId",  user.id.toString());
            session("decodeStr", Md5Hash.md5(regForm.get().login + regForm.get().password));
            return ok(index.render(user, "ok", null));
        }
    }

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            SiteUser user = SiteUser.authenticate(loginForm.get().login, loginForm.get().password);
            session("userId",  user.id.toString());
            session("decodeStr", Md5Hash.md5(loginForm.get().login + loginForm.get().password));
            return ok(index.render(user, "ok", null));
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.index()
        );
    }

    @Security.Authenticated(Secured.class)
    public static Result secretData(){
        return ok();
    }
}
