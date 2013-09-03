package forms;

import models.AdminUser;
import models.SiteUser;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public class LoginForm {
    public String login;
    public String password;

    public String validate() {
        if (AdminUser.authenticate(login, password) == null) {
            return "Invalid user or password";
        }
        return null;
    }
}