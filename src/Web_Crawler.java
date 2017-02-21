/**
 * Created by 贾晓磊 on 2016/12/8.
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Web_Crawler {
    private String jinshan = "http://www.iciba.com/";
    private String youdao = "http://dict.youdao.com/w/eng/";
    private String baidu = "http://cn.bing.com/dict/search?q=";

    private Vector<String> tran_jinshan = new Vector<>();
    private Vector<String> tran_youdao = new Vector<>();
    private Vector<String> tran_baidu = new Vector<>();

    private String html_jinshan = "";
    private String html_youdao = "";
    private String html_baidu = "";

    private String USA_Pron = "";
    private String ENG_Pron = "";
    private String word = "";

    public void Set_USA_Pron(String usa){
        this.USA_Pron = usa;
    }

    public void Set_ENG_Pron(String eng){
        this.ENG_Pron = eng;
    }

    public void Set_html_baidu(String h_baidu){
        this.html_baidu = h_baidu;
    }

    public void Set_html_youdao(String h_youdao){
        this.html_youdao = h_youdao;
    }

    public void Set_html_jinshan(String h_jinshan){
        this.html_jinshan = h_jinshan;
    }

    public String Get_html_baidu(){
        return this.html_baidu;
    }

    public String Get_html_jinshan(){
        return this.html_jinshan;
    }

    public String Get_html_youdao(){
        return this.html_youdao;
    }

    public String Get_USA_Pron(){
        return  this.USA_Pron;
    }

    public String Get_ENG_Pron(){
        return this.ENG_Pron;
    }

    public String Get_Pron(){
        String temp = "英： " + this.ENG_Pron + "\t美： " + this.USA_Pron;
        return temp;
    }

    public Vector<String> Get_Tran_jinshan(){
        return this.tran_jinshan;
    }

    public Vector<String> Get_Tran_youdao(){
        return this.tran_youdao;
    }

    public Vector<String> Get_Tran_baidu(){
        return this.tran_baidu;
    }



    private void Crawl_Html() throws Exception {
        URL url_jinshan = new URL(jinshan.trim());
        URL url_youdao = new URL(youdao.trim());
        URL url_baidu = new URL(baidu.trim());

        Scanner input_jinshan = new Scanner(url_jinshan.openStream());
        Scanner input_youdao = new Scanner(url_youdao.openStream());
        Scanner input_baidu = new Scanner(url_baidu.openStream());

        while(input_jinshan.hasNext()){
            html_jinshan += input_jinshan.nextLine() + "\n";
        }
        while(input_baidu.hasNext()){
            html_baidu += input_baidu.nextLine() + "\n";
        }
        while(input_youdao.hasNext()){
            html_youdao += input_youdao.nextLine() + "\n";
        }

        input_jinshan.close();
        input_baidu.close();
        input_youdao.close();

    }

    private void Crawl_Jinshan(){
        Pattern searchMeanPattern = Pattern.compile("(?s)<ul class=\"base-list switch_part\".*?>(.*?)</ul>");
        Matcher m1 = searchMeanPattern.matcher(html_jinshan);
        if(m1.find()){
            String means = m1.group(1);
            Pattern getChinese = Pattern.compile("(?s)<li class=\"clearfix\">(.*?)<p>(.*?)</p>.*?</li>");
            Matcher m2 = getChinese.matcher(means);
            while(m2.find()){
                String nature_code = m2.group(1);
                String translation_code = m2.group(2);
                String nature = "";
                String translation = "";
                Pattern getNature = Pattern.compile("(?m)<span class=\"prop\">(.*?)</span>");
                Matcher m3 = getNature.matcher(nature_code);
                if(m3.find()){
                    nature  += m3.group(1);
                }
                Pattern getMean = Pattern.compile("(?m)<span>(.*?)</span>");
                Matcher m4 = getMean.matcher(translation_code);
                while(m4.find()){
                    translation += m4.group(1);
                }
                String need = nature + "    " + translation;
                tran_jinshan.add(need);
            }
        }
    }

    private void Crawl_Baidu(){
        Pattern pattern = Pattern.compile("(?s)<div class=\"qdef\">.*?<ul>.*?</ul>");
        Matcher matcher = pattern.matcher(html_baidu);
        if (matcher.find()) {
            String means = matcher.group();
            Pattern getChinese = Pattern.compile("(?m)<li>.*?<span class=\"pos\">(.*?)</span>.*?<span class=\"def\">.*?<span>(.*?)</span>.*?</span>.*?</li>");
            Matcher m = getChinese.matcher(means);
            while (m.find()) {
                tran_baidu.add(m.group(1) + " " + m.group(2));
            }
        }
    }

    private void Crawl_Youdao(){
        Pattern searchMeanPattern = Pattern.compile("(?s)<div class=\"trans-container\">.*?<ul>.*?</div>");
        Matcher m1 = searchMeanPattern.matcher(html_youdao);

        if(m1.find()){
            String means = m1.group();
            Pattern getChinese = Pattern.compile("(?m)<li>(.*?)</li>");
            Matcher m2 = getChinese.matcher(means);
            while(m2.find()){
                tran_youdao.add(m2.group(1));
            }
        }
    }

    private void Pronuciation() throws Exception {
        Pattern searchPronPattern = Pattern.compile("(?s)<div class=\"baav\">.*?</h2>");
        Matcher m1 = searchPronPattern.matcher(html_youdao);
        if (m1.find()) {
            String Pron = m1.group();
            Pattern getPron = Pattern.compile("(?m)<span class=\"phonetic\">(.*?)</span>");
            Matcher m2 = getPron.matcher(Pron);
            for (int i = 0; m2.find(); i++) {
                if (i == 0)
                    ENG_Pron = m2.group(1);
                else if (i == 1)
                    USA_Pron = m2.group(1);
                else break;
            }
        }
    }

    Web_Crawler(String Word) throws Exception{
        Word = Word.trim();
        Word.replace(" ", "%20");
        word = Word;
        baidu = baidu + word;
        jinshan = jinshan + word;
        youdao = youdao + word;
        Crawl_Html();
        Crawl_Baidu();;
        Crawl_Jinshan();
        Crawl_Youdao();
        Pronuciation();
    }
}
