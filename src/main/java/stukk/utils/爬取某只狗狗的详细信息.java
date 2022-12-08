/*
package stukk.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class 爬取某只狗狗的详细信息 {


    public static void main(String[] args) throws IOException {
        String url = " https://www.bagong.cn/dog/aierlanlielangquan";
        Document document = Jsoup.parse(new URL(url), 5000);
        Element el = document.getElementsByClass("list-group").get(0);
//        System.out.println(el);
        Elements li = el.getElementsByTag("li");
        for(Element e : li){
            String text = e.text();
            System.out.println(text);
        }

        Elements elements = document.getElementsByClass("bs-callout bs-callout-danger");
        String text = elements.get(0).text();
//        System.out.println(text);
//<div class="bs-callout bs-callout-danger">
//                        <h4>档案</h4>
//                        爱尔兰雪达犬以其独特的光泽的外套，长而流下他们流线型的身体。爱尔兰雪达犬带流苏的尾巴水平。
//                    </div>

        Element image = elements.get(1);
        Elements imgs = image.getElementsByTag("img");
        String ans = "";
        for(Element i: imgs){
            String src = i.attr("src");
            ans += src;
            ans += ",";
        }
        System.out.println(ans);
        String[] split = ans.split(",");
        for(int j = 0;j<split.length;j++)
        System.out.println(split[j]);
    }


}
*/
