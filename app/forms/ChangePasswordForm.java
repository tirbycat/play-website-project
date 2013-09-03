package forms;

/**
 * Created with IntelliJ IDEA.
 * User: tirbycat
 * Date: 03.09.13
 * Time: 23:04
 * To change this template use File | Settings | File Templates.
 */
public class ChangePasswordForm {
    public String password1;
    public String password2;


    public String validate() {
        if (!password1.equals(password2)) {
            return "Invalid user or password";
        }
        return null;
    }
}
