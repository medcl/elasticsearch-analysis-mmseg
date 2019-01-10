/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.Loggers;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.analysis.*;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;


/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 8/2/11
 * Time: 4:44 PM
 */
public class MMsegAnalyzerProvider extends AbstractIndexAnalyzerProvider<MMSegAnalyzer>  {

    private static final Logger logger = Loggers.getLogger(MMsegAnalyzerProvider.class.getName());
    private final MMSegAnalyzer analyzer;

    public MMsegAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        analyzer=new MMSegAnalyzer();
    }

    public MMsegAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings,MMSegAnalyzer analyzer){
        super(indexSettings, name, settings);
        this.analyzer=analyzer;
    }

    @Override public MMSegAnalyzer get() {
        return this.analyzer;
    }

    private static String getAppId(IndexSettings indexSettings) {
        Settings customSettings = indexSettings.getIndexMetaData().getSettings().getAsSettings("index.analysis.analyzer.app_custom");
        return customSettings.get("app_id");
    };

    public static AnalyzerProvider<? extends Analyzer> getMaxWord(IndexSettings indexSettings, Environment environment, String s, Settings settings) {
        String appId = getAppId(indexSettings);
        Dictionary dict = DictionaryProvider.getInstance().getDictionary(appId);
        return new MMsegAnalyzerProvider(indexSettings,environment,s,settings,new MaxWordAnalyzer(dict));
    }

    public static AnalyzerProvider<? extends Analyzer> getComplex(IndexSettings indexSettings, Environment environment, String s, Settings settings) {
        String appId = getAppId(indexSettings);
        Dictionary dict = DictionaryProvider.getInstance().getDictionary(appId);
        return new MMsegAnalyzerProvider(indexSettings,environment,s,settings,new ComplexAnalyzer(dict));
    }

    public static AnalyzerProvider<? extends Analyzer> getSimple(IndexSettings indexSettings, Environment environment, String s, Settings settings) {
        String appId = getAppId(indexSettings);
        Dictionary dict = DictionaryProvider.getInstance().getDictionary(appId);
        return new MMsegAnalyzerProvider(indexSettings,environment,s,settings,new SimpleAnalyzer(dict));
    }
}
