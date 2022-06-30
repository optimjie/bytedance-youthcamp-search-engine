- [项目简介](#项目简介)
- [项目运行](#项目运行)
- [技术栈](#技术栈)
  - [前端](#前端)
  - [后端](#后端)
- [功能说明](#功能说明)
- [技术说明](#技术说明)
- [演示图](#演示图)

### 项目简介

1057 Search Engine是**大厂候选人队**参加2022年字节跳动青训营实现的课程大项目。

### 项目运行

1. 修改application.properties中的MySQL的用户名和密码。新建一个数据库，运行search-engine-sql下面的四个文件，创建四个表。
2. 将[悟空数据集]((https://wukong-dataset.github.io/wukong-dataset/download.html))的数据导入到data表中。
3. 运行search-engine-spring-boot\src\test\java\com\searchengine\springboot\segmentation\addAllSeg.java中的addSegs方法，用于添加分词，再运行addAllSegUseSplit方法，用于创建关系表。（注意修改loop，data表中有多少数据就添加多少）

- **前端启动**（保证你本地安装了node.js环境）
  1. git clone 项目 下来之后，cd search-engine-vue 
  2. 执行npm install    && npm run serve
  3. 启动成功访问：http://localhost:8080/即可
- **后端启动**
  1. IDEA打开search-engine-spring-boot，刷新maven
  2. 修改application.yml的数据库与密码 （mysql和redis）
  3. 启动redis

### 技术栈

#### 前端

Vue，Element UI

#### 后端

Spring Boot，MySQL，MyBatis，Spring Security，Redis， PyTorch

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

1. 分词采用开源的[jieba](https://github.com/huaban/jieba-analysis)分词库。首先将[悟空数据集](https://wukong-dataset.github.io/wukong-dataset/download.html)所有的文本数据进行分词创建倒排索引，并建立分词-文本关系表。
2. 关联度算法最先采用在数据库里查询分词（关联词）的方式，现在优化成采用[tfidf算法](https://zh.m.wikipedia.org/zh-cn/Tf-idf)。
3. 关键词过滤类似谷歌搜索"-过滤词"的方式，使用正则匹配校验是否为过滤词，支持多个过滤词。
4. 为了加快搜索速度，使用布隆过滤、B+树索引以及分表来进行优化。
5. 搜索时搜索下拉框联想词推荐使用[Trie树](https://zh.m.wikipedia.org/zh-cn/Trie)（也称前缀树）实现。
6. 以图搜图采用[CLIP](https://github.com/openai/CLIP)预训练模型对图像进行编码提取特征，通过特征之间余弦相似度搜索相似图片。
7. 登录、注销采用Spring Security + JWT + Redis，利用用户的id生成token签名，并存储在Redis中，每一次写操作都要查询token签名是否存在。

### 演示图

| 模块                    |                                                              |                                                              |
| ----------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 首页&下拉框联想词推荐   | <img src="https://s2.loli.net/2022/06/12/wKlDJ7Fg4kaAcZy.png" alt="首页.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/4wqZRM79vPcHOVn.png" alt="首页1.png" style="zoom:50%;" /> |
| 搜索结果：文本&缩略图   | <img src="https://s2.loli.net/2022/06/12/ioUD3QgHMudt47j.png" alt="s1.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/fK5aFR8hVB6IeXA.png" alt="s2.png" style="zoom:50%;" /> |
| 分页展示&相关搜索       | <img src="https://s2.loli.net/2022/06/12/6reylUwm5EOHjnu.png" alt="s3.png" style="zoom:50%;" /> |                                                              |
| 搜索过滤：过滤前&过滤后 | <img src="https://s2.loli.net/2022/06/12/ioUD3QgHMudt47j.png" alt="s1.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/q1WXl8vBtbLzaDk.png" alt="s8.png" style="zoom:50%;" /> |
| 用户注册&用户登录       | <img src="https://s2.loli.net/2022/06/12/49bkQuWnetHFZsM.png" alt="s4.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/j9xfZrQ5Y2wE3aF.png" alt="s5.png" style="zoom:50%;" /> |
| 收藏夹&添加到收藏夹     | <img src="https://s2.loli.net/2022/06/12/K8gi9CpZOcBtsnq.png" alt="s6.png" style="zoom:50%;" /> | <img src="https://s2.loli.net/2022/06/12/ZjLc25grxzDiYX4.png" alt="s7.png" style="zoom:50%;" /> |
| 以图搜图                | [![XgoaEd.png](https://s1.ax1x.com/2022/06/12/XgoaEd.png)](https://imgtu.com/i/XgoaEd) | [![XgonHJ.png](https://s1.ax1x.com/2022/06/12/XgonHJ.png)](https://imgtu.com/i/XgonHJ) |



### Tips

若本地数据库是5.6的话可能会导入错误

1. 需要将`utf8mb4`变为`utf8`
2. 将`utf8mb4_0900_ai_ci`变为`utf8_general_ci