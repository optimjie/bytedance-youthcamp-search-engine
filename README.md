# 目前功能

1.基于mysql建立分词库实现倒排索引查询




# TODO

- [ ] mysql查询效率问题，目前sql文件已导入50k的全部分词（共11w左右）和关系表（63w左右），分词库sql文件在/static-files/search_engine.sql；

- [ ] 分词数据清洗（"的" "了" 空格以及符号等无意义数据）；

- [x] 根据tidif值排序查询（tidif值大小根据每个文本的前5个关联性最强的分词从高到低排序）



- [ ] 
  以图搜图功能 可以采用CNN+Milvus


  参考

  https://milvus.io/docs/v2.0.x/image_similarity_search.md

  https://blog.csdn.net/qq_29220369/article/details/120523530 

  https://www.csdn.net/tags/MtTaEg4sOTEzMjUyLWJsb2cO0O0O.html
