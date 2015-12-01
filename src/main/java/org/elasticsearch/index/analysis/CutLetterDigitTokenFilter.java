package org.elasticsearch.index.analysis;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.CutLetterDigitFilter;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

import java.io.File;
import java.io.Reader;



public class CutLetterDigitTokenFilter extends AbstractTokenFilterFactory {

    @Inject
    public CutLetterDigitTokenFilter(Index index, IndexSettingsService indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings.getSettings(), name, settings);
    }

    @Override public TokenStream create(TokenStream tokenStream) {
        return new CutLetterDigitFilter(tokenStream);

    }
}