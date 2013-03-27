package models;

import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

/**
 * Created with IntelliJ IDEA.
 * SiteUser: tirbycat
 * Date: 16.03.13
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }

    @Test
    public void createAndRetrieveUser() {
        new SiteUser("bob@gmail.com", "Bob", "secret").save();
        SiteUser bob = SiteUser.find.where().eq("email", "bob@gmail.com").findUnique();
        assertNotNull(bob);
        assertEquals("Bob", bob.login);
    }

    @Test
    public void tryAuthenticateUser() {
        new SiteUser("bob@gmail.com", "Bob", "secret").save();

        assertNotNull(SiteUser.authenticate("bob@gmail.com", "secret"));
        assertNull(SiteUser.authenticate("bob@gmail.com", "badpassword"));
        assertNull(SiteUser.authenticate("tom@gmail.com", "secret"));
    }
}