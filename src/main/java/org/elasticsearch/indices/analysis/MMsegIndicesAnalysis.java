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

    @Inject
    public MMsegIndicesAnalysis(final Settings settings,
                                IndicesAnalysisService indicesAnalysisService,Environment env) {
        super(settings);
        String path=new File(env.configFile().toFile(),"mmseg").getPath();
        final Dictionary dic = Dictionary.getInstance(path);

        indicesAnalysisService.analyzerProviderFactories().put("mmseg",
                new PreBuiltAnalyzerProviderFactory("mmseg", AnalyzerScope.GLOBAL,
                        new MMSegAnalyzer(dic)));

        indicesAnalysisService.analyzerProviderFactories().put("mmseg_maxword",
                new PreBuiltAnalyzerProviderFactory("mmseg_maxword", AnalyzerScope.GLOBAL,
                        new MaxWordAnalyzer(dic)));

        indicesAnalysisService.analyzerProviderFactories().put("mmseg_complex",
                new PreBuiltAnalyzerProviderFactory("mmseg_complex", AnalyzerScope.GLOBAL,
                        new ComplexAnalyzer(dic)));

        indicesAnalysisService.analyzerProviderFactories().put("mmseg_simple",
                new PreBuiltAnalyzerProviderFactory("mmseg_simple", AnalyzerScope.GLOBAL,
                        new SimpleAnalyzer(dic)));

        indicesAnalysisService.tokenizerFactories().put("mmseg",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "mmseg";
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new MaxWordSeg(dic));
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("mmseg_maxword",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "mmseg_maxword";
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new MaxWordSeg(dic));
                    }
                }));
        indicesAnalysisService.tokenizerFactories().put("mmseg_complex",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "mmseg_complex";
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new ComplexSeg(dic));
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("mmseg_simple",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "mmseg_simple";
                    }

                    @Override
                    public Tokenizer create() {
                        return new MMSegTokenizer(new SimpleSeg(dic));
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("cut_letter_digit",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "cut_letter_digit";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new CutLetterDigitFilter(tokenStream);
                    }
                }));

    }
}