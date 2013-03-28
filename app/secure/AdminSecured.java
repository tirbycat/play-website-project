package secure;

import controllers.routes;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class AdminSecured extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("adminUserId");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return Results.redirect(routes.AdminApplication.login());
    }
}