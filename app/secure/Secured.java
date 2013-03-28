package secure;

import controllers.routes;
import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

/**
 * Created with IntelliJ IDEA.
 * User: tirbycat
 * Date: 16.03.13
 * Time: 23:28
 * To change this template use File | Settings | File Templates.
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("userId");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return Results.redirect(routes.Application.login());
    }
}