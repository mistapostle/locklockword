package locklockwords.mistapostle.appspot.com.locklockworks;

import org.junit.Test;

import locklockwords.mistapostle.appspot.com.locklockworks.html.YoudaoHelper;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class YoudaoHelperTest {
    @Test
    public void findBtWord() throws Exception {
        YoudaoHelper h = new YoudaoHelper();
        h.findWord("little by little");

    }
}