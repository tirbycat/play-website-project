package controllers;

import models.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import secure.AdminSecured;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 29.03.13
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public class AjaxController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result getTableData(String table, int page, int pagesize, String sortBy, String order, String filter){
        if(table.equals("users")){
            return ok(SiteUser.jsonPage(page, pagesize, sortBy, order, filter));
        }else if(table.equals("variable")){
            return ok(Variable.jsonPage(page, pagesize, sortBy, order, filter));
        }else if(table.equals("strings")){
            return ok(Strings.jsonPage(page, pagesize, sortBy, order, filter));
        }else if(table.equals("roles")){
            return ok(AdminRole.jsonPage(page, pagesize, sortBy, order, filter));
        }else if(table.equals("administrators")){
            return ok(AdminUser.jsonPage(page, pagesize, sortBy, order, filter));
        }else{
            return badRequest();
        }
    }
}
