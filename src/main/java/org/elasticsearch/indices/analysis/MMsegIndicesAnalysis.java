package org.elasticsearch.indices.analysis;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.*;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

import java.io.File;

/**
 * Registers indices level analysis components so, if not explicitly configured,
 * will be shared among all indices.
 */
public class MMsegIndicesAnalysis extends AbstractComponent {

    private static final String MMSEG = "mmseg";
    private static final String MMSEG_MAXWORD = "mmseg_maxword";
    private static final String MMSEG_COMPLEX = "mmseg_complex";
    private static final String MMSEG_SIMPLE = "mmseg_simple";
    private static final String CUT_LETTER_DIGIT= "cut_letter_digit";
    @Inject
    public MMsegIndicesAnalysis(final Settings settings,
                                IndicesAnalysisService indicesAnalysisService,Environment env) {
        super(settings);
        final Dictionary dic = Dictionary.getInstance();

        indicesAnalysisService.analyzerProviderFactories().put(MMSEG,
                new PreBuiltAnalyzerProviderFactory(MMSEG, AnalyzerScope.GLOBAL,
                        new MMSegAnalyzer(dic)));

        indicesAnalysisService.analyzerProviderFactories().put(MMSEG_MAXWORD,
                new PreBuiltAnalyzerProviderFactory(MMSEG_MAXWORD, AnalyzerScope.GLOBAL,
                        new MaxWordAnalyzer(dic)));

        indicesAnalysisService.analyzerProviderFactories().put(MMSEG_COMPLEX,
                new PreBuiltAnalyzerProviderFactory(MMSEG_COMPLEX, AnalyzerScope.GLOBAL,
                        new ComplexAnalyzer(dic)));

        indicesAnalysisService.analyzerProviderFactories().put(MMSEG_SIMPLE,
                new PreBuiltAnalyzerProviderFactory(MMSEG_SIMPLE, AnalyzerScope.GLOBAL,
                        new SimpleAnalyzer(dic)));

        indicesAnalysisService.tokenizerFactories().put(MMSEG,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return MMSEG;
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new MaxWordSeg(dic));
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put(MMSEG_MAXWORD,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return MMSEG_MAXWORD;
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new MaxWordSeg(dic));
                    }
                }));
        indicesAnalysisService.tokenizerFactories().put(MMSEG_COMPLEX,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return MMSEG_COMPLEX;
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new ComplexSeg(dic));
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put(MMSEG_SIMPLE,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return MMSEG_SIMPLE;
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new SimpleSeg(dic));
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put(CUT_LETTER_DIGIT,
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return CUT_LETTER_DIGIT;
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new CutLetterDigitFilter(tokenStream);
                    }
                }));

    }
}