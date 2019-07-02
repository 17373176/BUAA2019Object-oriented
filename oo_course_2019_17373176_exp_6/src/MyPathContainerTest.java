import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MyPathContainerTest {
    private final PathContainer pathContainer = new MyPathContainer();
    private Path path1, path2, path3;

    @Before
    public void before() {
        path1 = new MyPath(1, 2, 3, 4);
        path2 = new MyPath(1, 2, 3, 4);
        path3 = new MyPath(1, 2, 3, 4, 5);
    }

    @After
    public void after() {
        // do something here
    }

    @Test
    public void testAddPath() throws Exception {
        Assert.assertEquals(1, pathContainer.addPath(path1), 1);
       Assert.assertTrue(pathContainer.containsPathId(1));
        Assert.assertEquals(path1, pathContainer.getPathById(1));
        Assert.assertEquals(1, pathContainer.size());

        Assert.assertEquals(1, pathContainer.addPath(path2));
        Assert.assertTrue(pathContainer.containsPathId(1));
        Assert.assertEquals(path2, pathContainer.getPathById(1));
        Assert.assertEquals(1, pathContainer.size());

        Assert.assertEquals(2, pathContainer.addPath(path3));
        Assert.assertTrue(pathContainer.containsPathId(2));
        Assert.assertEquals(path3, pathContainer.getPathById(2));
        Assert.assertEquals(2, pathContainer.size());
    }
}
