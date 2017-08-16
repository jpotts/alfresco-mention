package com.metaversant;

import com.metaversant.alfresco.mentions.service.MentionScanner;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * Created by jpotts, Metaversant on 7/31/17.
 */
public class TextParsingTests extends TestCase {


    @Test
    public void testEasyMatch() {
        //test1
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test1.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));
    }

    @Test
    public void testNoMatch() {
        //test1
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test10.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(0, userList.size());
    }

    @Test
    public void testStartsWith() {
        // test2
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test2.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));
    }

    @Test
    public void testEndsWith() {
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test3.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));
    }

    @Test
    public void testMidString() {
        // test4
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test4.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));
    }

    @Test
    public void  testNonSpaceTrailing() {
        // test5
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test5.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(3, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));
        Assert.assertEquals("tuser2", userList.get(1));
        Assert.assertEquals("tuser3", userList.get(2));
    }

    @Test
    public void testMulti() {
        // test6
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test6.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(3, userList.size());
        Assert.assertEquals("tuser_1", userList.get(0));
        Assert.assertEquals("tuser.2", userList.get(1));
        Assert.assertEquals("tuser-3", userList.get(2));
    }

    @Test
    public void testEnd1k() {
        // test7
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test7.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));

    }

    @Test
    public void testMid2k() {
        // test8
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test8.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));

    }

    @Test
    public void testMulti2k() {
        // test9
        List<String> userList = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/test-files/test9.txt");
            userList = MentionScanner.getUsers(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {}
            }
        }
        Assert.assertNotNull(userList);
        Assert.assertEquals(3, userList.size());
        Assert.assertEquals("tuser1", userList.get(0));
        Assert.assertEquals("tuser2", userList.get(1));
        Assert.assertEquals("tuser3", userList.get(2));
    }
}
