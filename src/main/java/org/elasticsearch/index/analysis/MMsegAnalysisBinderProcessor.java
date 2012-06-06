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

/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 8/2/11
 * Time: 5:28 PM
 */
public class MMsegAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor  {

    @Override public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {

    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("mmseg", MMsegTokenizerFactory.class);
        super.processTokenizers(tokenizersBindings);
    }

    @Override public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        analyzersBindings.processAnalyzer("mmseg", MMsegAnalyzerProvider.class);
        super.processAnalyzers(analyzersBindings);
    }

}
