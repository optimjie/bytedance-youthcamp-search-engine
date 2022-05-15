package com.searchengine.controller;

import com.searchengine.utils.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clip")
@Slf4j
public class CLIPController {

    SocketUtil socketUtil = new SocketUtil();

    /**
     * 文本转图片
     * @param searchInfo
     * @return
     * @throws Exception
     */
    @GetMapping()
    public List<String> searchImgBySentence(@RequestParam("sentence") String searchInfo) throws Exception{
        return socketUtil.sentence2Img(searchInfo);
    }

    /**
     * 以图搜图
     * @return
     */
    @GetMapping("/img")
    public List<String> searchImgByImg(){
        return null;
    }
}
