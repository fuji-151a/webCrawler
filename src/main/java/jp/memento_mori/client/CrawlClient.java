package jp.memento_mori.client;

import jp.memento_mori.crawler.Crawler;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlClient {
    /** crawl Url. */
    private static String crawlUrl;
    /** output Directory. */
    private static String outputDir;
    /** start Id. */
    private static int startId;
    /** end Id. */
    private static int endId;

    /**
     * CrawClient.
     * @param args Conf File
     * @throws Exception 
     */
    public static void main(final String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("引数が3つ入ります");
            System.exit(1);
        }
        String conf = args[0];
        startId = Integer.valueOf(args[1]);
        endId = Integer.valueOf(args[2]);
        PropertiesConfiguration pc = new PropertiesConfiguration(conf);
        crawlUrl = pc.getString("crawlUrl");
        outputDir = pc.getString("outputDir");
        if (startId > endId) {
            System.exit(1);
        }

        int numberOfCrawlers = 1;
        int i;
        Crawler crawl = new Crawler(outputDir);
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(outputDir);
        config.setMaxDepthOfCrawling(0);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer
            = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller
            = new CrawlController(config, pageFetcher, robotstxtServer);

        for (i = startId; i <= endId; i++) {
            controller.addSeed(crawlUrl + i);
        }

        controller.start(Crawler.class, numberOfCrawlers);
    }
}
