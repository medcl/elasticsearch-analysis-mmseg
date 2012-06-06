package org.elasticsearch.index.analysis;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.File;
import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 12-6-6
 * Time: 下午3:59
 */
public class MMsegTokenizerFactory extends AbstractTokenizerFactory {

    private Settings settings;
    Seg seg_method;
    private String seg_type;

    @Inject
    public MMsegTokenizerFactory(Index index, @IndexSettings Settings indexSettings,Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.settings=settings;

        String path=new File(env.configFile(),"mmseg").getPath();
        logger.info(path);
        Dictionary dic = Dictionary.getInstance(path);
        seg_type = settings.get("seg_type", "max_word");
        if(seg_type.equals("max_word")){
            seg_method = new MaxWordSeg(dic);
        }else if(seg_type.equals("complex")){
            seg_method = new ComplexSeg(dic);
        }else if(seg_type.equals("simple")){
            seg_method =new SimpleSeg(dic);
        }

    }

    @Override
    public Tokenizer create(Reader reader) {
        logger.info(seg_type);
        logger.info(seg_method.toString());
        return  new MMSegTokenizer(seg_method,reader);
    }
}
