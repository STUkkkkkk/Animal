/*
package stukk.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class 爬文章 {

    public static void main(String[] args) throws Exception {
        String url = "https://www.chongshe.cn/new/cwxw/";
        String qianzhui = "https://www.chongshe.cn/";
        Document page = Jsoup.parse(new URL(url), 5000);
        Element list = page.getElementsByClass("list").get(0);
        Elements li = list.getElementsByTag("li");
        int pi = 0;
        for(Element el : li){
            String image = el.getElementsByTag("img").get(0).attr("src");
            String name = el.getElementsByClass("bt").get(0).text();
            String des = el.getElementsByClass("des").get(0).text();

            System.out.println(image + "==> " +name + " === > " + des);

            String href = el.getElementsByTag("a").get(0).attr("href");
            Document newPage = Jsoup.parse(new URL(qianzhui + href), 5000);
            Element element = newPage.getElementsByClass("text").get(0);
            System.out.println(element.toString());
        }
    }

}
*/
