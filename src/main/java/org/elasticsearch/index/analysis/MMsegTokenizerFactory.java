package org.elasticsearch.index.analysis;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

import java.io.File;
import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 12-6-6
 * Time: 下午3:59
 */
@Deprecated
public class MMsegTokenizerFactory extends AbstractTokenizerFactory {

    Dictionary dic;
    private String segType;

    @Inject
    public MMsegTokenizerFactory(Index index, IndexSettingsService indexSettings,Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings.getSettings(), name, settings);
        dic = Dictionary.getInstance();
        segType = settings.get("seg_type", "max_word");
    }

    @Override
    public Tokenizer create() {
        Seg segMethod=null;
        if(segType.equals("max_word")){
            segMethod = new MaxWordSeg(dic);
        }else if(segType.equals("complex")){
            segMethod = new ComplexSeg(dic);
        }else if(segType.equals("simple")){
            segMethod =new SimpleSeg(dic);
        }
        return  new MMSegTokenizer(segMethod);
    }
}
