<template>
  <el-container style="width: 100%">
    <el-header height="20%">
      <el-row style="height: 200px; width: 1524px; display: flex">
        <el-col style="width: 100px; margin-right: 10px">
          <div>
            <a href="/"><img src="~@/assets/1057inRes.png" alt="" /></a>
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
              <template v-slot:suffix>
                <el-upload
                  :limit="1"
                  action="http://localhost:9090/imageUpload"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                  style="float: left; padding-top: 5px; max-width: 37px"
                >
                  <i class="el-input__icon el-icon-camera"></i>
                </el-upload>
                <el-button
                  style="margin-right: 10px; margin-top: 5px"
                  slot="suffix"
                  type="text"
                  @click="searcher"
                  >搜索</el-button
                >
              </template>
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
              <div v-if="check">
                <favorites :username="user.username" :addToFavorite="0" ref="favorites"></favorites>
              </div>
              <div v-if="!check">
                <a style="color: #55ab41; margin-right: 148px; text-decoration: none;"href="/login">对不起,请前往登录</a>
              </div>

            </el-popover>
            <span>&nbsp;&nbsp;</span>
            <el-popover
              placement="bottom"
              title="个人信息"
              width="300"
              trigger="click"
              content="将来放关于用户的信息。"
            >
              <el-button slot="reference" icon="el-icon-user" circle>
              </el-button>
              <!--用户信息 -->
              <div v-if="check">
                欢迎回来！<span style="color: #55ab41">{{
                  user.username
                }}</span>
                <span id="logout" @click="logout">注销</span>
              </div>
              <div v-if="!check">
                <a
                  style="
                    color: #55ab41;
                    margin-right: 148px;
                    text-decoration: none;
                  "
                  href="/login"
                  >对不起,请前往登录</a
                >
                <a
                  style="color: #55ab41; text-decoration: none"
                  href="/register"
                  >注册</a
                >
              </div>
            </el-popover>
          </el-col>
        </div>
      </el-row>
    </el-header>

    <el-main>
      <el-col
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgb(255 255 255)"
      >
        <div style="display: flex">
          <el-col :span="2" id="gen">
            <span>&nbsp;</span>
          </el-col>
          <div
            style="margin-right: 15px"
            id="text"
            :class="{ selectedOne: picture_text === 1 }"
            @click="tranfer1('text')"
          >
            文本
          </div>
          <div
            id="picture"
            :class="{ selectedOne: picture_text === 2 }"
            @click="tranfer1('picture')"
          >
            图片
          </div>
          <div
            id="picture"
            :class="{
              selectedOne: picture_text === 3,
              picToPic: picTopic1 === false,
            }"
            @click="tranfer1('picToPic')"
          >
            搜图
          </div>
        </div>
        <el-row>
          <el-col :span="2" :class="{picToPic : picture_text == 3}">
            <span>&nbsp;</span>
          </el-col>
          <!-- 搜索结果 -->

          <el-col :span="11" v-if="picture_text == 1">
            <dl>
              <div v-if="recordsNum != 0">
                <div
                  v-for="item in imgAndCaption"
                  align="left"
                  style="display: flex; margin-bottom: 15px"
                >
                  <el-button @click="addToFavorite(item)">收藏</el-button>
                  <a :href="item.url" target="_blank" style="white-space:nowrap;word-break:break-all;">
                    <h3 v-html="lightFn(item.caption, search_word1)"></h3>
                  </a>
                </div>
              </div>
              <div v-if="recordsNum == 0">
                <div style="display: flex; margin-bottom: 15px">
                  <div>
                    <h1>
                      抱歉没有找到与<span style="color: #55ab41">{{
                        search_word
                      }}</span
                      >相关的文本。
                    </h1>
                  </div>
                </div>
              </div>
            </dl>
          </el-col>
          <el-col :span="11"> </el-col>

          <el-col style="max-width: 1200px" v-if="picture_text == 2">
            <dl>
              <div
                v-if="recordsNum != 0"
                style="
                  display: flex;
                  flex-wrap: wrap;
                  justify-content: flex-start;
                "
              >
                <div v-for="item in imgAndCaption" align="left" class="P_item">
                  <div>
                    <img
                      style="height: 200px; border-radius: 10%"
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
                      :style="'width:' + item.width"
                      v-html="lightFn(item.caption, search_word1)"
                    ></p>
                  </div>
                </div>
              </div>
              <div v-if="recordsNum == 0">
                <div style="display: flex; margin-bottom: 15px">
                  <div>
                    <h1>
                      抱歉没有找到与<span style="color: #55ab41">{{
                        search_word
                      }}</span
                      >相关的图片。
                    </h1>
                  </div>
                </div>
              </div>
            </dl>
          </el-col>

          <el-col :span="11" v-if="picture_text == 3">
            <dl>
              <div style="
                  display: flex;
                  flex-wrap: wrap;
                  justify-content: flex-start;
                  min-width:850px
                ">
                <div
                  v-for="item in imgAndCaption1"
                  align="left"
                >
                  <img style="height: 200px;margin-right:15px; border-radius: 10%" :src="item.url" />
                </div>
              </div>
            </dl>
          </el-col>
        </el-row>
        <el-row>
          <!-- 相关搜索 -->
          <h3 style="width: 100px; margin-left: 100px; color: #248e24">
            相关搜索
          </h3>
          <div id="allrelated">
            <div
              class="related"
              v-for="rw in relatedWord"
              @click="searchRelated({ rw })"
            >
              {{ rw }}
            </div>
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
      </el-col>

    </el-main>

    <el-dialog
        title="请选择添加位置"
        :visible.sync="addToFavoritedialogVisible"
        width="20%">
      <favorites :username="user.username" :addToFavorite="1" ref="favorites" @notShowDialog="change"></favorites>
      <span>
        <br>
      </span>
<!--      <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="addToFavoritedialogVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="addToFavoritedialogVisible = false">确 定</el-button>-->
<!--      </span>-->
    </el-dialog>

  </el-container>
</template>

<script>
import axios from "axios";

import favorites from "./Favorites"
// import Favorites from "@/components/Favorites";
// import AddFavorites from "@/components/AddFavorites";

export default {
  components: {favorites},
  // comments: {
  //   favorites,
  //   addFavorites
  // },
  data() {
    const item = {
      message: "123",
    };
    return {
      upImage: [],
      loading: false,
      user: "",
      check: false,
      search_word: "",
      search_word1: "",
      info: "",
      imgAndCaption: [],
      imgAndCaption1: [],
      relatedWord: [],
      pageNum: 1,
      recordsNum: 0,
      picture_text: 1,
      picTopic1: false,
      addToFavoritedialogVisible: false,
    };
  },
  created() {
    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
    }
  },
  mounted() {
    // 不知道为什么只有这样才能获取第一次的值
    this.addToFavoritedialogVisible = true
    setTimeout(() => {
      this.addToFavoritedialogVisible = false
    }, 1)
    this.getFirstPage();
    this.getRelatedWord();
    this.checkToken();
    this.checkLoading();
    setInterval(() => {
      this.checkToken();
    }, 1000 * 60 * 10); //每十分钟检查token
  },
  methods: {
    // =========收藏夹========
    addToFavorite(item) {
      var url = item.url
      var caption = item.caption
      this.$refs.favorites.addFavorite(url, caption)
      this.addToFavoritedialogVisible = true
    },
    change() {
      this.addToFavoritedialogVisible = false
    },
    // =======收藏夹结束======
    checkLoading() {
      const _this = this;
      setInterval(() => {
        if (
          this.search_word != null &&
          this.imgAndCaption != null &&
          this.relatedWord != null
        ) {
          _this.load = true;
          clearInterval();
        }
      }, 2000);
    },
    async checkToken() {
      var jwt = JSON.parse(window.localStorage.getItem("access"));
      if (jwt != null) {
        await axios
          .get(
            "http://localhost:9090/survival?token=" +
              jwt.token +
              "&username=" +
              jwt.username
          )
          .then((response) => {
            if (response.data.message != "success") {
              this.$message.error("错了哦，这是一条错误消息");
            }
          });
      }
    },
    async logout() {
      var _this = this;
      var jwt = JSON.parse(window.localStorage.getItem("access"));
      if (jwt != null) {
        await axios
          .get(
            "http://localhost:9090/user/logout?username=" +
              jwt.username +
              "&token=" +
              jwt.token
          )
          .then(function (response) {
            if (response.data.message == "success") {
              _this.$message({
                message: "退出成功",
                type: "success",
              });
              window.localStorage.removeItem("access");
              setTimeout(() => {
                location.reload();
              }, 3000);
            }
          });
      }
    },
    lightFn(originStr, target) {
      return originStr.replace(
        target,
        '<span style="color:red;">' + target + "</span>"
      );
    },
    //以图搜图的展示
    //上传图片后的回调函数
    handleAvatarSuccess(response) {
      if (response[0] == "图片上传失败!") {
        this.$message.error("图片上传失败!");
      } else {
        this.picTopic1 = true;
        this.imgAndCaption1 = []; //<========================赋值到此
        for (let i = 0; i < response.length; i++) {
          this.imgAndCaption1.push({
            url: response[i],
          });
        }
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    //文本栏，图片栏,搜图栏切换
    tranfer1(val) {
      if (val == "text") {
        this.picture_text = 1;
      }
      if (val == "picture") {
        this.picture_text = 2;
      }
      if (val == "picToPic") {
        this.picture_text = 3;
      }
    },
    searcher() {
      this.$router.push({
        path: "/search",
        query: {
          word: this.search_word,
        },
      });
      location.reload();
    },
    async search() {
      this.pageNum = 1;
      let outer = this;
      this.search_word1 = this.search_word;
      await axios
        .get(
          "http://localhost:9090/search_use_split?word=" +
            outer.search_word +
            "&pageNum=" +
            outer.pageNum
        )
        .then((response) => (outer.info = response.data));
      if (this.info != "") {
        this.imgAndCaption = [];
        this.recordsNum = this.info.recordsNum;
        for (let i = 0; i < this.info.records.length; i++) {
          this.imgAndCaption.push({
            url: this.info.records[i].url,
            caption: this.info.records[i].caption,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
      this.getRelatedWord();
    },
    async searchRelated(word) {
      this.$router.push({
        path: "/search",
        query: {
          word: word.rw,
        },
      });
      location.reload();
    },
    async getFirstPage() {
      this.recordsNum = this.$route.query.recordsNum;
      this.search_word = this.$route.query.word;
      this.search_word1 = this.$route.query.word;
      let outer = this;
      await axios
        .get(
          "http://localhost:9090/search_use_split?word=" +
            this.search_word +
            "&pageNum=1"
        )
        .then((response) => (outer.info = response.data));
      if (this.info != "") {
        this.recordsNum = this.info.recordsNum;
        for (let i = 0; i < this.info.records.length; i++) {
          let img = new Image();
          img.src = this.info.records[i].url;
          this.imgAndCaption.push({
            url: this.info.records[i].url,
            caption: this.info.records[i].caption,
            width: (img.width * 200) / img.height + "px",
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
    },
    async handleCurrentChange(val) {
      // console.log(`当前页: ${val}`);
      let outer = this;
      await axios
        .get(
          "http://localhost:9090/search_use_split?word=" +
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
a {
  text-decoration: none;
  color: #55ab41;
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
  min-width: 840px;
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
  cursor: pointer;
  border: 1px solid #1176db8f;
  padding: 10px;
  margin: 5px;
}
#logout {
  cursor: pointer;
  float: right;
  text-decoration: none;
}
.selectedOne {
  border-bottom: 1px solid #55ab41;
}
.picToPic {
  display: none;
}
#text {
  width: 50px;
  height: 30px;
  cursor: pointer;
}
#picture {
  width: 50px;
  height: 30px;
  cursor: pointer;
}
.P_item {
  margin-right: 15px;
}
@media (max-width: 1372px) {
  #gen {
    display: none;
  }
}
</style>
