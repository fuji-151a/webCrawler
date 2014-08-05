package jp.memento_mori.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern
            .compile(".*(\\.(css|js|bmp|gif|jpe?g"
                    + "|png|tiff?|mid|mp2|mp3|mp4"
                    + "|wav|avi|mov|mpeg|ram|m4v|pdf"
                    + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    
    /** sleeptime. */
    private static long SLEEP_TIME = 30000;
    
    /** output先を指定. */
    private String outputDir;
    

    /**
     * Default Constracter.
     * @param output 出力先のDirectory
     */
    public Crawler(final String output) {
        this.outputDir = output;
    }

    public Crawler() {
    }
    
    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith("http://www.ics.uci.edu/");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            if (!noMatchFilter(html)) {
                storeCrawlData(html);
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ファイルの保存.
     * @param crawlData crawlしたHTML
     */
    private void storeCrawlData(final String crawlData) {
        try {
            String tmp = UUID.randomUUID().toString() + ".html";
            FileOutputStream fos
                = new FileOutputStream(new File("./output/" + tmp));
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter pw = new PrintWriter(osw);
            pw.print(crawlData);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * レシピがない場合のFilter.
     * @param html crawlしたHTML
     * @return boolean true or false
     */
    private boolean noMatchFilter(final String html) {
        String str = "レシピが見つかりませんでした";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(html);
        return m.find();
    }

}
