package locklockwords.mistapostle.appspot.com.locklockworks.html;


import android.webkit.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import locklockwords.mistapostle.appspot.com.locklockworks.db.LockLockWorksContract;


/**
 * Created by mistapostle on 17/6/20.
 */

public class YoudaoHelper {

    private static YoudaoHelper instance = new YoudaoHelper();

    public static YoudaoHelper getInstance() {
        return instance;
    }

    public LockLockWorksContract.Word findWord(String word) throws IOException {
        URL u = new URL("http://www.youdao.com/w/" + word);
        Document doc = Jsoup.parse(u, 5000);
//        Elements es = doc.select("#phrsListTab .pronounce > .phonetic");
//        for(int i = 0; i < es.size(); i++){
//            System.out.println("found pronounce : " + es.get(i).text());
//        }


        Elements es = doc.select("#phrsListTab .pronounce  ");

        String pronounce = es.text();
        System.out.println("found com pronounce : " + pronounce);
        es = doc.select("#phrsListTab .trans-container");

        String desc = es.text();
        System.out.println("found trans : " + es.text());
        return new LockLockWorksContract.Word(word, pronounce, desc);

    }
}
