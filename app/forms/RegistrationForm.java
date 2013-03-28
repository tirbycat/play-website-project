package forms;

import models.SiteUser;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationForm  {
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