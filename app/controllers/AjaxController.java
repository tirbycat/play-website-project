package controllers;

import models.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.data.Form;
import secure.AdminSecured;

import static play.data.Form.form;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 29.03.13
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public class AjaxController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result getTableData(String table, int page, String sortBy, String order, String filter, String mode){
//        String body = request().body().asFormUrlEncoded();
        switch(table){
        case "users":
            switch(mode){
                case "tabledata":
                    return ok(SiteUser.jsonPage(page, sortBy, order, filter));
                case "windata":
                    return ok(SiteUser.jsonPage(page, sortBy, order, filter));
                case "delete":
                    return ok(SiteUser.deleteRecord(filter));
                case "save":
                    return ok(SiteUser.editRecord(filter));
            }
            break;
        case "variable":
            switch(mode){
                case "tabledata":
                    return ok(Variable.jsonPage(page, sortBy, order, filter));
                case "windata":
                    return ok(Variable.jsonValue(filter));
                case "delete":
                    return ok(Variable.deleteRecord(filter));
                case "save":
                    Variable var =  form(Variable.class).bindFromRequest().get();
                    return ok(Variable.editRecord(var));
            }
            break;
        case "strings":
            switch(mode){
                case "tabledata":
                    return ok(Strings.jsonPage(page, sortBy, order, filter));
                case "windata":
                    return ok(Strings.jsonValue(filter));
                case "delete":
                    return ok(Strings.deleteRecord(filter));
                case "save":
                    Strings var =  form(Strings.class).bindFromRequest().get();
                    return ok(Strings.editRecord(var));
            }
            break;
        case "roles":
            switch(mode){
                case "tabledata":
                    return ok(AdminRole.jsonPage(page, sortBy, order, filter));
                case "windata":
                    return ok(AdminRole.jsonValue(filter));
                case "delete":
                    return ok(AdminRole.deleteRecord(filter));
                case "save":
                    AdminRole var =  form(AdminRole.class).bindFromRequest().get();
                    return ok(AdminRole.editRecord(var));
            }
            break;
        case "administrators":
            return ok(AdminUser.jsonPage(page, sortBy, order, filter));
        default:
            return badRequest();
        }

        return badRequest();
    }
}
