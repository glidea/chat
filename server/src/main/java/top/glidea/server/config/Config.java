package top.glidea.server.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Server 全局配置
 */
@Slf4j
public class Config {
    private static Properties CONFIG;

    static {
        CONFIG = new Properties();
        try {
            CONFIG.load(new FileInputStream("server/src/main/resources/application.properties"));
        } catch (IOException e) {
            log.error("配置文件读取错误：", e);
            throw new RuntimeException();
        }
    }

    public static String get(String key) {
        return (String) CONFIG.get(key);
    }
}
