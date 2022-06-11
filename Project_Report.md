### 项目地址

[项目地址](https://github.com/optimjie/bytedance-youthcamp-search-engine)

### 技术栈

#### 前端

Vue，Element UI

#### 后端

Spring Boot，MySQL，MyBatis，Spring Security，Redis

### 功能说明

1. 支持搜索存文本信息。
2. 支持用户自定义关键字过滤。
3. 支持搜索结果按条目分页展示。
4. 实现了关联度算法，把关联度高的信息优先展示。
5. 支持“相关搜索”功能。
6. 支持搜索时搜索下拉框联想词推荐。
7. 支持用户注册、登录、注销。
8. 支持用户收藏夹功能。
   - 用户可以新增、删除、重命名个人收藏夹。
   - 用户可以收藏搜索结果里的一条或多条结果，放入其中一个收藏夹。
   - 用户可以删除收藏夹里的内容。
9. 支持搜索图片
   - 用户输入纯文本，根据纯文本搜索出关联的图片数据。
   - 图片搜索结果以缩略图展示，支持点击打开原图。
10. 支持以图搜图
   - 用户上传一张图片，可以搜索出关联的图片。
### 技术说明

1. 分词采用开源的[jieba分词库]([huaban/jieba-analysis: 结巴分词(java版) (github.com)](https://github.com/huaban/jieba-analysis))。首先将所有的文本数据进行分词，建立分词-文本关系表。
2. 关联度算法采用[tfidf算法]([tf-idf - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.m.wikipedia.org/zh-cn/Tf-idf))。
3. 为了加快搜索速度，使用加B+树索引以及分表来进行优化。
4. 搜索时搜索下拉框联想词推荐使用[Trie树]([Trie - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.m.wikipedia.org/zh-cn/Trie))（也称前缀树）实现。

### 演示图

| 模块                    |                                                              |                                                              |
| ----------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 首页&下拉框联想词推荐   | <img src="https://s2.loli.net/2022/06/12/wKlDJ7Fg4kaAcZy.png" alt="首页.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/4wqZRM79vPcHOVn.png" alt="首页1.png" style="zoom:50%;" /> |
| 搜索结果：文本&缩略图   | <img src="https://s2.loli.net/2022/06/12/ioUD3QgHMudt47j.png" alt="s1.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/fK5aFR8hVB6IeXA.png" alt="s2.png" style="zoom:50%;" /> |
| 分页展示&相关搜索       | <img src="https://s2.loli.net/2022/06/12/6reylUwm5EOHjnu.png" alt="s3.png" style="zoom:50%;" /> |                                                              |
| 搜索过滤：过滤前&过滤后 | <img src="https://s2.loli.net/2022/06/12/ioUD3QgHMudt47j.png" alt="s1.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/q1WXl8vBtbLzaDk.png" alt="s8.png" style="zoom:50%;" /> |
| 用户注册&用户登录       | <img src="https://s2.loli.net/2022/06/12/49bkQuWnetHFZsM.png" alt="s4.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/j9xfZrQ5Y2wE3aF.png" alt="s5.png" style="zoom:50%;" /> |
| 收藏夹&添加到收藏夹     | <img src="https://s2.loli.net/2022/06/12/K8gi9CpZOcBtsnq.png" alt="s6.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/ZjLc25grxzDiYX4.png" alt="s7.png" style="zoom:50%;" /> |
| 以图搜图                |                                                              |                                                              |



​	