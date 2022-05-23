package com.searchengine.springboot.segmentation;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.Segmentation;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;


@SpringBootTest
@Slf4j
public class test {

    @Autowired
    private SegmentationDao segmentationDao;
    private JiebaSegmenter segmenter = new JiebaSegmenter();

    String[] sentences =
            new String[]{
                    "美沃可视数码裂隙灯,检查眼前节健康状况",
                    "欧美夏季ebay连衣裙 气质圆领通勤绑带收腰连衣裙 zc3730"
            };
    @Test
    public void segTest1(){
        for (String sentence : sentences) {
            long start = System.currentTimeMillis();
            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
            TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
            List<Keyword> list=tfidfAnalyzer.analyze(sentence,10);
            long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
            for (SegToken token : tokens) {
                System.out.println(token.word);
            }
        }
    }

    @Test
    public void tfidfTest1(){
        String content="孩子上了幼儿园 安全防拐教育要做好 卧槽这也太牛逼了吧";
        int topN=5;
        TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
        List<Keyword> list=tfidfAnalyzer.analyze(content,10);
        for(Keyword word:list)
            System.out.print(word.getName()+":"+word.getTfidfvalue()+",");
    }
    @Test
    public void testDemo() {
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }

    @Test
    public void testReturn1(){
        int s = segmentationDao.insertSeg("牛逼");
        log.info("id:{}",s);
    }

}
