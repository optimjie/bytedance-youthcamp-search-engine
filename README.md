# 目前功能

1.基于mysql建立分词库实现倒排索引查询

分词库sql文件在/static-file/search_engine.sql



# TODO

1.mysql查询效率问题，目前sql文件只导入了7w左右个分词（50k的小数据包分词量应该在50w左右），在test下面的/segmentation/addAllSeg，需要优化or换成别的数据库（RDB？）；

2.分词数据清洗（"的" "了" 空格以及符号等无意义数据）；
