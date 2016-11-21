package org.elasticsearch.index.analysis;

import com.chenlb.mmseg4j.analysis.CutLetterDigitFilter;
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;


public class CutLetterDigitTokenFilter extends AbstractTokenFilterFactory {

    public CutLetterDigitTokenFilter(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override public TokenStream create(TokenStream tokenStream) {
        return new CutLetterDigitFilter(tokenStream);

    }
}
