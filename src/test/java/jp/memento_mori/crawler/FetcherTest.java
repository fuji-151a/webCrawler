package jp.memento_mori.crawler;

import static org.junit.Assert.*;
import jp.memento_mori.util.TestEnv;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * FetcherのTest.
 * @author yuya
 *
 */
public class FetcherTest {

    /** WebServerのポート番号. */
    private static int port;
    /** WebServer. */
    private static HttpServer server;
    
    private static String hostname;

    /** test resources path. */
    private static final String RES_PATH 
        = System.getProperty("test.resource.dir");
    
    /**
     * 空いてるPortを探してWebServerを起動.
     * @throws Exception Port探索失敗時
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        port = TestEnv.findFreePort();
        StaticHttpHandler handler = new StaticHttpHandler(RES_PATH);
        server = TestEnv.createWebServer(port, handler);
        server.start();
    }
    
    @Before
    public void setUp() throws Exception {
        hostname = "http://localhost:" + port;
    }

    @Test
    public void test() throws Exception {
        String crawlStorageFolder = "target/output";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed(hostname + "/crawl_data/1441786.html");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(Fetcher.class, numberOfCrawlers); 
    }

    /**
     * WebServer を停止する.
     * @throws Exception WebServerの停止に失敗した時に発生.
     */
    public static void shutdownServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
    
}
