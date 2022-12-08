/*
package stukk;

import stukk.entity.AddressBook;
import stukk.entity.Blog;
import stukk.entity.Cat;
import stukk.entity.Dog;
import stukk.mapper.CatMapper;
import stukk.mapper.DogMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import stukk.service.BlogService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@SpringBootTest(classes = {AnimalApplication.class})
@RunWith(SpringRunner.class)
public class UploadFileTest {
    @Test
    public void test1() {
        */
/*String fileName = "abcd.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);*//*


        List<AddressBook> list = new ArrayList<>();
        list.add(new AddressBook());
        list.add(new AddressBook());
        System.out.println(list.toString());
    }



    @Autowired
    DogMapper dogMapper;
    @org.junit.Test
    public void addDog() throws IOException, InterruptedException {
        String qianzhui = "https://www.bagong.cn";
        String utrl = "https://www.bagong.cn/dog/";
        Document html = Jsoup.parse(new URL(utrl), 5000);
//        System.out.println(html);
        List<Dog> list = new LinkedList<>();
        int pi = 1;
        for(char k = 'A'; k <= 'Z' ;k++){ //先爬A的试一试速度
            System.out.println(k + " 开头的是:");
            String search = ""+k;
            Element ell = html.getElementById(search);
            if(ell == null){
                continue;
            }
            Elements elements = ell.getElementsByTag("li");
            System.out.println("总共爬取"+elements.size()+"数据");
            List<Dog> dogs = new LinkedList<>();
            for(Element el : elements){
                Dog dog = new Dog();
                String src = el.getElementsByTag("img").get(0).attr("src");
                String name = el.getElementsByTag("h4").get(0).text();
                String href = el.getElementsByTag("a").get(0).attr("href");
                dog.setUrl(src);
                dog.setName(name);
                //开始爬具体属性页

                Document JvTiYe = Jsoup.parse(new URL(qianzhui+href) ,5000);
                Element element = JvTiYe.getElementsByClass("list-group").get(0);
                Elements li = element.getElementsByTag("li");
                for(Element e : li){
                    String text = e.text();
                    String as[] = text.split("：");
                    if(as[0].equals("体重")){
                        dog.setWeight(as[1]);
                    }
                    else if(as[0].equals("全名")){
                        dog.setAllName(as[1]);
                    }
                    else if(as[0].equals("功能")){
                        dog.setAbility(as[1]);
                    }
                    else if(as[0].equals("原产地")){
                        dog.setPosition(as[1]);
                    }
                    else if(as[0].equals("寿命")){
                        dog.setLife(as[1]);
                    }
                    else if(as[0].equals("毛长")){
                        dog.setWool(as[1]);
                    }
                    else if(as[0].equals("类型")){
                        dog.setType(as[1]);
                    }
                    else if(as[0].equals("英文名")){
                        dog.setEnglishName(as[1]);
                    }
                    else if(as[0].equals("身高")){
                        dog.setHeight(as[1]);
                    }
                }
                Elements es = JvTiYe.getElementsByClass("bs-callout bs-callout-danger");
                String text = es.get(0).text();
                dog.setAbs(text.substring(3,text.length()));

                Element image = es.get(1);
                Elements imgs = image.getElementsByTag("img");
                String ans = "";
                for(Element i: imgs){
                    String src1 = i.attr("src");
                    ans += src1;
                    ans += ",";
                }
                dog.setImage(ans);
                //开始爬具体属性页
                dogs.add(dog);
            }
            for(Dog dog: dogs){
                boolean b = dogMapper.insert(dog) == 1;
                if(!b){
                    System.out.println("出错了");
                }
            }
            System.out.println("阶段爬完成");
            Thread.sleep(2000);
        }
        System.out.println("爬虫完成");


    }


    @Autowired
    CatMapper catMapper;

    @org.junit.Test
    public void addCat() throws IOException, InterruptedException {
        String qianzhui = "https://www.bagong.cn";
        String utrl = "https://www.bagong.cn/cat/";
        Document html = Jsoup.parse(new URL(utrl), 5000);
//        System.out.println(html);
        List<Cat> list = new LinkedList<>();
        int pi = 1;
        for(char k = 'A'; k <= 'Z' ;k++){ //先爬A的试一试速度
            System.out.println(k + " 开头的是:");
            String search = ""+k;
            Element ell = html.getElementById(search);
            if(ell == null){
                continue;
            }
            Elements elements = ell.getElementsByTag("li");
            System.out.println("总共爬取"+elements.size()+"数据");
            List<Cat> cats = new LinkedList<>();
            for(Element el : elements){
                Cat cat = new Cat();
                String src = el.getElementsByTag("img").get(0).attr("src");
                String name = el.getElementsByTag("h4").get(0).text();
                String href = el.getElementsByTag("a").get(0).attr("href");
                cat.setUrl(src);
                cat.setName(name);
                //开始爬具体属性页

                Document JvTiYe = Jsoup.parse(new URL(qianzhui+href) ,5000);
                Element element = JvTiYe.getElementsByClass("list-group").get(0);
                Elements li = element.getElementsByTag("li");
                for(Element e : li){
                    String text = e.text();
                    String as[] = text.split("：");
                    if(as[0].equals("体重")){
                        cat.setWeight(as[1]);
                    }
                    else if(as[0].equals("全名")){
                        cat.setAllName(as[1]);
                    }
                    else if(as[0].equals("功能")){
                        cat.setAbility(as[1]);
                    }
                    else if(as[0].equals("原产地")){
                        cat.setPosition(as[1]);
                    }
                    else if(as[0].equals("寿命")){
                        cat.setLife(as[1]);
                    }
                    else if(as[0].equals("毛长")){
                        cat.setWool(as[1]);
                    }
                    else if(as[0].equals("类型")){
                        cat.setType(as[1]);
                    }
                    else if(as[0].equals("英文名")){
                        cat.setEnglishName(as[1]);
                    }
                    else if(as[0].equals("身高")){
                        cat.setHeight(as[1]);
                    }
                }
                Elements es = JvTiYe.getElementsByClass("bs-callout bs-callout-danger");
                String text = es.get(0).text();
                cat.setAbs(text.substring(3,text.length()));

                Element image = es.get(1);
                Elements imgs = image.getElementsByTag("img");
                String ans = "";
                for(Element i: imgs){
                    String src1 = i.attr("src");
                    ans += src1;
                    ans += ",";
                }
                cat.setImage(ans);
                //开始爬具体属性页
                cats.add(cat);
            }

            for(Cat cat:cats){
                boolean b = catMapper.insert(cat) == 1;
                if(!b){
                    System.out.println("出现错误，暂停爬数据");
                }
            }
            Thread.sleep(1000);
        }
        System.out.println("爬虫完成");

    }


    @Autowired
    BlogService blogService;

    @org.junit.Test
    public void 爬文章() throws Exception{
        String url = "https://www.chongshe.cn/new/cwxw/2";
        String qianzhui = "https://www.chongshe.cn/";
        Document page = Jsoup.parse(new URL(url), 5000);
        Element list = page.getElementsByClass("list").get(0);
        Elements li = list.getElementsByTag("li");
        int pi = 1;
        for(Element el : li){
            Blog blog = new Blog();
            String image = el.getElementsByTag("img").get(0).attr("src");
            String name = el.getElementsByClass("bt").get(0).text();
            String des = el.getElementsByClass("des").get(0).text();
            blog.setName(name);
            blog.setDes(des);
            blog.setImage(image);

            String href = el.getElementsByTag("a").get(0).attr("href");
            Document newPage = Jsoup.parse(new URL(qianzhui + href), 5000);
            Element element = newPage.getElementsByClass("text").get(0);
            blog.setContent(element.toString());
            blog.setView(0);
            blogService.save(blog);
        }
    }

}
*/
