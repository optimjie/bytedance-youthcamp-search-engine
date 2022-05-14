<template>
  <div>
    <el-row type="flex" justify="end">  <!-- 将来放用户登录以及收藏夹的东西 -->
      <el-popover
          placement="bottom"
          title="收藏夹"
          width="300"
          trigger="click"
          content="将来放收藏夹相关内容，实现效果可以参考edge的收藏夹。">
        <el-button slot="reference" icon="el-icon-folder" circle></el-button>
      </el-popover>
      <span>&nbsp;&nbsp;</span>
      <el-popover 
          placement="bottom"
          title="用户"
          width="300"
          trigger="click"
          content="将来放关于用户的信息。">
        <el-button slot="reference" icon="el-icon-user" circle></el-button>
      </el-popover>
    </el-row>
    <el-row id="input">
      <img src="~@/assets/1057.png" alt="">
        <el-input v-model="search_word" placeholder="请输入搜索内容" prefix-icon="el-icon-search">
          <el-button style="margin-right:10px" slot="suffix" type="text" @click="search">搜索</el-button>
        </el-input>
    </el-row>
  </div>
</template>

<script>

import axios from "axios";

export default {
  data() {
    return {
      search_word: '',
    }
  },
  methods: {
    search() {
      // 需要请求数据来显示
      this.$router.push({
        path: '/search',
        query: {
          word : this.search_word,
          recordsNum: this.recordsNum
        }
      });
    }
  }
};
</script>

<style scoped>
  #input {
    text-align: center; /*让div内部文字居中*/
    border-radius: 40px;
    width: 600px;
    height: 500px;
    margin: auto;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
</style>
