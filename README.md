Mmseg Analysis for Elasticsearch
==================================

The Mmseg Analysis plugin integrates Lucene mmseg4j-analyzer:http://code.google.com/p/mmseg4j/ into elasticsearch, support customized dictionary.

The plugin ships with analyzers: `mmseg_maxword`  ,`mmseg_complex` ,`mmseg_simple` and tokenizers: `mmseg_maxword`  ,`mmseg_complex` ,`mmseg_simple`  and token_filter: `cut_letter_digit` .

Versions
--------

Mmseg ver  | ES version
-----------|-----------
master | 2.3.x -> master
1.9.3 | 2.3.3
1.8.1 | 2.2.1
1.7.0 | 2.1.1
1.5.0 | 2.0.0
1.4.0 | 1.7.0
1.3.0 | 1.6.0
1.2.1 | 0.90.2
1.1.2 | 0.20.1


Package
-------------

```
mvn package
```

Install
-------------

Unzip and place into elasticsearch's plugins folder,
you can checkout example from https://github.com/medcl/elasticsearch-rtf

Custom Analysis Configuration Example (elasticsearch.yml)
-------------


```
index:
  analysis:
    analyzer:
      mmseg_maxword:
        type: custom
        filter:
        - lowercase
        tokenizer: mmseg_maxword
      mmseg_maxword_with_cut_letter_digi:
        type: custom
        filter:
        - lowercase
        - cut_letter_digit
        tokenizer: mmseg_maxword    
```

Mapping Configuration
-------------

Here is a quick example:

1.Create a index

```
curl -XPUT http://localhost:9200/index

```

2.Create a mapping

```
curl -XPOST http://localhost:9200/index/fulltext/_mapping -d'
{
    "fulltext": {
             "_all": {
            "analyzer": "mmseg_maxword",
            "search_analyzer": "mmseg_maxword",
            "term_vector": "no",
            "store": "false"
        },
        "properties": {
            "content": {
                "type": "string",
                "store": "no",
                "term_vector": "with_positions_offsets",
                "analyzer": "mmseg_maxword",
                "search_analyzer": "mmseg_maxword",
                "include_in_all": "true",
                "boost": 8
            }
        }
    }
}'
```

3.Indexing some docs

```
curl -XPOST http://localhost:9200/index/fulltext/1 -d'
{content:"美国留给伊拉克的是个烂摊子吗"}
'

curl -XPOST http://localhost:9200/index/fulltext/2 -d'
{content:"公安部：各地校车将享最高路权"}
'

curl -XPOST http://localhost:9200/index/fulltext/3 -d'
{content:"中韩渔警冲突调查：韩警平均每天扣1艘中国渔船"}
'

curl -XPOST http://localhost:9200/index/fulltext/4 -d'
{content:"中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"}
'
```

4.Query with highlighting

```
curl -XPOST http://localhost:9200/index/fulltext/_search  -d'
{
    "query" : { "term" : { "content" : "中国" }},
    "highlight" : {
        "pre_tags" : ["<tag1>", "<tag2>"],
        "post_tags" : ["</tag1>", "</tag2>"],
        "fields" : {
            "content" : {}
        }
    }
}
'
```

Here is the query result

```

{
    "took": 14,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 2,
        "max_score": 2,
        "hits": [
            {
                "_index": "index",
                "_type": "fulltext",
                "_id": "4",
                "_score": 2,
                "_source": {
                    "content": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
                },
                "highlight": {
                    "content": [
                        "<tag1>中国</tag1>驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首 "
                    ]
                }
            },
            {
                "_index": "index",
                "_type": "fulltext",
                "_id": "3",
                "_score": 2,
                "_source": {
                    "content": "中韩渔警冲突调查：韩警平均每天扣1艘中国渔船"
                },
                "highlight": {
                    "content": [
                        "均每天扣1艘<tag1>中国</tag1>渔船 "
                    ]
                }
            }
        ]
    }
}

```


Have fun.
