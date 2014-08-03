package jp.memento_mori.util;

import java.net.ServerSocket;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;

/**
 * Testが使用する共通の処理.
 * @author yuya
 *
 */
public class TestEnv {

    /**
     * 空のコンストラクタ.
     */
    public TestEnv() {
    }

    /**
     * 空いてるPortを探す.
     * @return ポート番号
     * @throws Exception 探索に失敗した場合に発生
     */
    public static Integer findFreePort() throws Exception {
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }

    /**
     * WebServerの作成.
     * @param port ポート番号
     * @param handler パスの処理
     * @return WebServer
     */
    public static HttpServer createWebServer(final Integer port,
                                             final HttpHandler handler) {
        HttpServer server = new HttpServer();
        server.addListener(new NetworkListener("listen", "localhost", port));
        server.getServerConfiguration().addHttpHandler(handler, "/");
        return server;
    }

}
