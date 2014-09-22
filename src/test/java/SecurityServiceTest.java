import com.nowopen.encrypt.SecurityAESService;
import com.nowopen.encrypt.SecurityDESedeService;
import com.nowopen.encrypt.SecurityRSAService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by admin@OYJ on 2014/9/19.
 */
public class SecurityServiceTest      extends TestCase
{
    public SecurityServiceTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( SecurityServiceTest.class );
    }

    public void testRSA()
    {
        assertTrue( true );
        String s = "雘keshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.com";
        String e = SecurityRSAService.getInstance().encrypt(s);
        String ss = SecurityRSAService.getInstance().decrypt(e);

        System.out.println(s);
        System.out.println(e);
        System.out.println(ss);

        assertEquals(s, ss);
    }

    public void testDESede()
    {
        assertTrue( true );
        String s = "雘keshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.com";
        String e = SecurityDESedeService.getInstance().encrypt(s);
        String ss = SecurityDESedeService.getInstance().decrypt(e);

        System.out.println(s);
        System.out.println(e);
        System.out.println(ss);

        assertEquals(s, ss);
    }

    public void testAES()
    {
        assertTrue( true );
        String s = "雘keshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.comkeshfi^&***@gadsfl.com";
        String e = SecurityAESService.getInstance().encrypt(s);
        String ss = SecurityAESService.getInstance().decrypt(e);

        System.out.println(s);
        System.out.println(e);
        System.out.println(ss);

        assertEquals(s, ss);
    }
}