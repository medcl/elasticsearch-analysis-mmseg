package org.elasticsearch.indices.analysis;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;


public class LCMissingAnalyzerHandler implements MissingAnalyzerHandler {
    private static final ESLogger log = Loggers.getLogger("mmseg-analyzer");

    private Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>();


    private String getAppId(String name) {
        // name is in the form of "app_{app_id}_{class}"
        String[] tmps = name.split("_");
        if (tmps.length < 3) {
            return null;
        }
        return tmps[1];
    }


    @Override
    public synchronized Analyzer analyzerMissing(String name) {
        String appId = getAppId(name);
        if (appId == null)
            return null;
        if (analyzers.containsKey(appId))
            return analyzers.get(appId);

        log.info("Missing analyzer handling for " + appId + ".");
        final Dictionary dic = Dictionary.getInstance(appId);
        ComplexAnalyzer complexAnalyzer = new ComplexAnalyzer(dic);
        analyzers.put(appId, complexAnalyzer);
        new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted())
                    try {
                        Thread.sleep(180 * 1000);
                        dic.reload();
                    }
                    catch (InterruptedException e) {
                        break;
                    }
            }
        }.start();
        return complexAnalyzer;
    }
}
