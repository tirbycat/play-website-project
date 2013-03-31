package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pzigel
 * Date: 28.03.13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class MenuItem {
    public String title;
    public String url;
    public List<MenuItem> subMenu;

    public MenuItem(String title, String url) {
        this.title = title;
        this.url = url;
        subMenu = new ArrayList<MenuItem>();
    }

    public void addSubMenuItem(MenuItem item){
        subMenu.add(item);
    }
}
