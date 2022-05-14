<template>
  <el-container style="width: 100%">
    <el-header height="20%">
      <el-row style="height:200px;width:100%">
        <el-col style="width: 7%">
          <div>
            <img src="~@/assets/1057inRes.png" alt="">
          </div>
        </el-col>
        <el-col style="width: 15%">
          <div class="input">
            <el-input v-model="search_word" placeholder="请输入搜索内容" prefix-icon="el-icon-search">
              <el-button style="margin-right:10px; margin-top:5px" slot="suffix" type="text" @click="search">搜索</el-button>
            </el-input>
          </div>
        </el-col>
        <el-col style="width: 73%">
          <span>&nbsp;&nbsp;</span>
        </el-col>
        <el-col style="width: 5%">
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
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-row>  <!-- 搜索结果 -->
        <el-col :span="2">
          <span>&nbsp;</span>
        </el-col>
        <el-col :span="11">
          <dl>
            <div v-for="item in imgAndCaption" align="left">
              <h1>{{ item.caption }}</h1>
              <img :src="item.url">
            </div>
          </dl>
        </el-col>
        <el-col :span="11">

        </el-col>
      </el-row>
      <el-row>  <!-- 相关搜索 -->

      </el-row>
      <el-row>
        <el-col :span="1">
          <span>&nbsp;</span>
        </el-col>
        <el-col :span="11">
          <div align="left">
            <el-pagination
                background
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-size="2"
                layout="prev, pager, next, jumper"
                :total="recordsNum">
            </el-pagination>
          </div>
        </el-col>
        <el-col :span="12">

        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>


<script>


import axios from "axios";

export default {
  data() {
    const item = {
      message: '123'
    };
    return {
      search_word: '',
      search_result: [],
      info: '',
      imgAndCaption: [],
      pageNum: 1,
      recordsNum: 0
    }
  },
  mounted() {
    // this.getRecordsNum();
    this.getFirstPage();
  },
  methods: {
    async search() {
      this.pageNum = 1
      let outer = this;
      await axios
          .get('http://localhost:9090/search_test?word=' + outer.search_word + '&pageNum=' + outer.pageNum)
          .then(response => (
              outer.info = response.data
          ))
      this.imgAndCaption = []
      this.recordsNum = this.info.recordsNum
      for (let i = 0; i < this.info.records.length; i++) {
        this.imgAndCaption.push({
          url: this.info.records[i].url,
          caption: this.info.records[i].caption
        })
      }
    },
    async getFirstPage() {
      this.recordsNum = this.$route.query.recordsNum
      this.search_word = this.$route.query.word
      let outer = this;
      await axios
          .get('http://localhost:9090/search_test?word=' + this.search_word + '&pageNum=1')
          .then(response => (
              outer.info = response.data
          ))
      this.recordsNum = this.info.recordsNum
      for (let i = 0; i < this.info.records.length; i++) {
        this.imgAndCaption.push({
          url: this.info.records[i].url,
          caption: this.info.records[i].caption
        })
      }
    },
    async handleCurrentChange(val) {
      // console.log(`当前页: ${val}`);
      let outer = this;
      await axios
          .get('http://localhost:9090/search_test?word=' + this.search_word + '&pageNum=' + val)
          .then(response => (
              outer.info = response.data
          ))
      this.imgAndCaption = []
      this.recordsNum = this.info.recordsNum
      for (let i = 0; i < this.info.records.length; i++) {
        this.imgAndCaption.push({
          url: this.info.records[i].url,
          caption: this.info.records[i].caption
        })
      }
    }
  },
};
</script>

<style>
.input .el-input__inner {
  height: 50px;
}
.el-header {
  /*background-color: #B3C0D1;*/
  display: flex;
  position: relative;
  width: 100%;
  height: 5px;
}
.el-main {
  /*background-color: #E9EEF3;*/
  margin-top: 0;
  position: absolute;
  left: 0;
  right: 0;
  top: 60px;
  bottom: 0;
  overflow-y: scroll;
}
</style>
