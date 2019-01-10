package org.elasticsearch.index.analysis;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 12-6-6
 * Time: 下午3:59
 */
public class MMsegTokenizerFactory extends AbstractTokenizerFactory {

    private Seg seg;

    public MMsegTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings,Seg seg) {
        super(indexSettings, name, settings);
        this.seg=seg;
    }

    @Override
    public Tokenizer create() {
        return  new MMSegTokenizer(seg);
    }

    private static String getAppId(IndexSettings indexSettings) {
        Settings customSettings = indexSettings.getIndexMetaData().getSettings().getAsSettings("index.analysis.analyzer.app_custom");
        return customSettings.get("app_id");
    };

    public static TokenizerFactory getMaxWord(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        String appId = getAppId(indexSettings);
        Dictionary dict = DictionaryProvider.getInstance().getDictionary(appId);
        return new MMsegTokenizerFactory(indexSettings,environment,name,settings,new MaxWordSeg(dict));
    }

    public static TokenizerFactory getComplex(IndexSettings indexSettings, Environment environment, String s, Settings settings) {
        String appId = getAppId(indexSettings);
        Dictionary dict = DictionaryProvider.getInstance().getDictionary(appId);
        return new MMsegTokenizerFactory(indexSettings,environment,s,settings,new ComplexSeg(dict));
    }

    public static TokenizerFactory getSimple(IndexSettings indexSettings, Environment environment, String s, Settings settings) {
        String appId = getAppId(indexSettings);
        Dictionary dict = DictionaryProvider.getInstance().getDictionary(appId);
        return new MMsegTokenizerFactory(indexSettings,environment,s,settings,new SimpleSeg(dict));
    }
}
