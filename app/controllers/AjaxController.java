package controllers;

import forms.AdminUserForm;
import forms.ChangePasswordForm;
import models.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import secure.AdminSecured;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        Class classDefinition = null;
        try {
            java.lang.reflect.Method method;
            classDefinition = Class.forName("models." + table);
            switch(mode){
                case "tabledata":
                    method = classDefinition.getMethod("jsonPage", Integer.class, String.class, String.class, String.class);
                    return ok((ObjectNode)method.invoke(null, page, sortBy, order, filter));
                case "windata":
                    method = classDefinition.getMethod("jsonValue", String.class);
                    return ok((ObjectNode)method.invoke(null, filter));
                case "delete":
                    method = classDefinition.getMethod("deleteRecord", String.class);
                    return ok((ObjectNode)method.invoke(null, filter));
                case "save":
                    method = classDefinition.getMethod("editRecord");
                    return ok((ObjectNode)method.invoke(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return badRequest();
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result getListBoxData(String list, String filter){
        java.lang.reflect.Method method;

        Class classDefinition = null;
        try {
            classDefinition = Class.forName("models." + list);
            method = classDefinition.getMethod("toList", String.class);
            return ok((ObjectNode)method.invoke(null, filter));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return badRequest();
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result changePassword() {
        Form<ChangePasswordForm> passForm = form(ChangePasswordForm.class).bindFromRequest();
        if (passForm.hasErrors()) {
            return ok(passForm.errorsAsJson());
        } else {
            AdminUser.changePassword(Integer.parseInt(session("adminUserId")), passForm.get().password1);
        }
        return ok("ok");
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result loadFile() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("file");
        if (picture != null) {
            String fileName = RandomStringUtils.randomAlphanumeric(12);
            String ext = picture.getFilename().substring(picture.getFilename().length()-3, picture.getFilename().length());

            String contentType = picture.getContentType();

            File saveTo = new File(Variable.getStringValue("CONTENT_PATH_FS"), fileName + "." + ext);

            File target = picture.getFile();

            boolean result = target.renameTo(saveTo);
            if(!result){
                try {
                    Files.copy(target.toPath(), saveTo.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                    target.delete();
                } catch (IOException e) {
                    return internalServerError(e.getMessage());
                }
            }
            return ok(Variable.getStringValue("CONTENT_PATH_HTTP") + saveTo.getName());
        } else {
            return badRequest("Missing file");
        }
    }
}
