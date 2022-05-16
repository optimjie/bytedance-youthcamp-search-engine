<template>
  <el-container style="width: 100%">
    <el-header height="20%">
      <el-row style="height: 200px; width: 1524px; display: flex">
        <el-col style="width: 100px; margin-right: 10px">
          <div>
            <img src="~@/assets/1057inRes.png" alt="" />
          </div>
        </el-col>
        <el-col>
          <div class="input" style="text-align: left">
            <el-input
              style="width: 560px"
              v-model="search_word"
              placeholder="请输入搜索内容"
              prefix-icon="el-icon-search"
            >
              <el-button
                style="margin-right: 10px; margin-top: 5px"
                slot="suffix"
                type="text"
                @click="search"
                >搜索</el-button
              >
            </el-input>
          </div>
        </el-col>
        <div>
          <el-col style="display: flex; float: right; margin-top: 10px">
            <el-popover
              placement="bottom"
              title="收藏夹"
              width="300"
              trigger="click"
              content="将来放收藏夹相关内容，实现效果可以参考edge的收藏夹。"
            >
              <el-button
                slot="reference"
                icon="el-icon-folder"
                circle
              ></el-button>
            </el-popover>
            <span>&nbsp;&nbsp;</span>
            <el-popover
              placement="bottom"
              title="用户"
              width="300"
              trigger="click"
              content="将来放关于用户的信息。"
            >
              <el-button
                slot="reference"
                icon="el-icon-user"
                circle
              ></el-button>
            </el-popover>
          </el-col>
        </div>
      </el-row>
    </el-header>
    <el-main>
      <el-row>
        <!-- 搜索结果 -->
        <!-- <el-col :span="2">
          <span>&nbsp;</span>
        </el-col>
        <el-col :span="11">
          <dl>
            <div v-for="item in imgAndCaption" align="left">
              <h3>{{ item.caption }}</h3>
              <img style="width:100px;" :src="item.url">
            </div>
          </dl>
        </el-col>
        <el-col :span="11">

        </el-col> -->

        <el-col :span="2">
          <span>&nbsp;</span>
        </el-col>
        <el-col style="max-width: 1200px">
          <dl
            style="
              display: flex;
              flex-wrap: wrap;
              justify-content: space-between;
            "
          >
            <div v-for="item in imgAndCaption" align="left">
              <div> 
                <!-- :style="'width:'+(item.width)" -->
                <img
                  style="height:200px; border-radius: 10%"
                  :src="item.url"
                  :alt="item.caption"
                />
                <p
                  style="
                    font-size: 10px;
                    overflow: hidden;
                    word-break: keep-all;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                  "
                >
                  {{ item.caption }}
                </p>
              </div>
            </div>
          </dl>
        </el-col>
        <el-col> </el-col>
      </el-row>
      <el-row>
        <!-- 相关搜索 -->
        <h3 style="width: 100px; margin-left: 100px; color: #248e24">
          相关搜索
        </h3>
        <div id="allrelated">
          <div class="related" v-for="rw in relatedWord">{{ rw }}</div>
        </div>
      </el-row>
      <el-row>
        <el-col :span="2">
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
              :total="recordsNum"
            >
            </el-pagination>
          </div>
        </el-col>
        <el-col :span="12"> </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    const item = {
      message: "123",
    };
    return {
      search_word: "",
      search_result: [],
      info: "",
      imgAndCaption: [],
      relatedWord: [],
      pageNum: 1,
      recordsNum: 0,
    };
  },
  mounted() {
    // this.getRecordsNum();
    this.getFirstPage();
    this.getRelatedWord();
  },
  methods: {
    async search() {
      this.pageNum = 1;
      let outer = this;
      await axios
        .get(
          "http://localhost:9090/search_test?word=" +
            outer.search_word +
            "&pageNum=" +
            outer.pageNum
        )
        .then((response) => (outer.info = response.data));
      this.imgAndCaption = [];
      this.recordsNum = this.info.recordsNum;
      for (let i = 0; i < this.info.records.length; i++) {
        this.imgAndCaption.push({
          url: this.info.records[i].url,
          caption: this.info.records[i].caption,
        });
      }
    },
    async getFirstPage() {
      this.recordsNum = this.$route.query.recordsNum;
      this.search_word = this.$route.query.word;
      let outer = this;
      await axios
        .get(
          "http://localhost:9090/search_test?word=" +
            this.search_word +
            "&pageNum=1"
        )
        .then((response) => (outer.info = response.data));
      this.recordsNum = this.info.recordsNum;
      for (let i = 0; i < this.info.records.length; i++) {
        let img = new Image();
        img.src = this.info.records[i].url;
        img.height = 200
        this.imgAndCaption.push({
          url: this.info.records[i].url,
          caption: this.info.records[i].caption,
          width: (img.width / 2)+'px',
        });
      }
    },
    async handleCurrentChange(val) {
      // console.log(`当前页: ${val}`);
      let outer = this;
      await axios
        .get(
          "http://localhost:9090/search_test?word=" +
            this.search_word +
            "&pageNum=" +
            val
        )
        .then((response) => (outer.info = response.data));
      this.imgAndCaption = [];
      this.recordsNum = this.info.recordsNum;
      for (let i = 0; i < this.info.records.length; i++) {
        this.imgAndCaption.push({
          url: this.info.records[i].url,
          caption: this.info.records[i].caption,
        });
      }
    },
    async getRelatedWord() {
      let outer = this;
      await axios
        .get("http://localhost:9090/related_word?word=" + outer.search_word)
        .then((response) => (outer.info = response.data));
      this.relatedWord = this.info;
    },
  },
};
</script>

<style>
div {
  white-space: nowrap;
}
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
  /* overflow-y: scroll; */
}
#allrelated {
  margin-left: 100px;
  margin-bottom: 20px;
  width: 700px;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.related {
  width: 258px;
  text-align: left;
  margin-right: 10px;

  border: 1px solid #1176db8f;
  padding: 10px;
  margin: 5px;
}
</style>
