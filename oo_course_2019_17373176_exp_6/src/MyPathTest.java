import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MyPathTest {
    private MyPath path1, path2, path3;

    @Before
    public void before() {
        path1 = new MyPath(1, 2, 3, 4);
        path2 = new MyPath(1, 2, 3, 4);
        path3 = new MyPath(1, 2, 3, 4, 5);
    }

    @After
    public void after() {
        // do nothing
    }

    @Test
    public void testEquals() throws Exception {
        // this can be better actually:
        // Assert.assertEquals(path1, path2);
        // Assert.assertNotEquals(path1, path3);
        Assert.assertTrue(path1.equals(path1));
        Assert.assertTrue(path1.equals(path2));
        Assert.assertFalse(path1.equals(path3));
        Assert.assertTrue(path2.equals(path1));
        Assert.assertTrue(path2.equals(path2));
        Assert.assertFalse(path2.equals(path3));
        Assert.assertFalse(path3.equals(path1));
        Assert.assertFalse(path3.equals(path2));
        Assert.assertTrue(path3.equals(path3));
    }
}
