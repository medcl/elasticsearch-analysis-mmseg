package org.elasticsearch.index.analysis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ConcurrentHashMap;
import com.chenlb.mmseg4j.Dictionary;

public class DictionaryProvider {

    private static final Logger logger = LogManager.getLogger();
    private static final DictionaryProvider SINGLETON = new DictionaryProvider();

    private ConcurrentHashMap<String, Dictionary> dictionaries;

    private DictionaryProvider() {
        dictionaries = new ConcurrentHashMap<>();
    };

    public static DictionaryProvider getInstance() {
        return SINGLETON;
    };

    public synchronized Dictionary getDictionary(String appId) {
        if (appId == null) {
            return dictionaries.computeIfAbsent("default", (key) -> Dictionary.getInstance());
        }
        return dictionaries.computeIfAbsent(appId, (key) -> {
            logger.info("Load customized dictionary for app {}", key);
            final Dictionary dict = Dictionary.getInstance(key);
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted())
                    try {
                        Thread.sleep(180 * 1000);
                        dict.reload();
                    }
                    catch (InterruptedException e) {
                        break;
                    }
            }).start();
            return dict;
        });
    }

}
