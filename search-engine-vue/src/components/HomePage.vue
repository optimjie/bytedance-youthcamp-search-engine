<template>
  <div>
    <el-row type="flex" justify="end">
      <!-- 将来放用户登录以及收藏夹的东西 -->
      <el-popover
        placement="bottom"
        title="收藏夹"
        width="300"
        trigger="click"
        content="将来放收藏夹相关内容，实现效果可以参考edge的收藏夹。"
      >
        <el-button slot="reference" icon="el-icon-folder" circle></el-button>
      </el-popover>
      <span>&nbsp;&nbsp;</span>
      <el-popover
        placement="bottom"
        title="个人信息"
        width="300"
        trigger="click"
        content="将来放关于用户的信息。"
      >
        <el-button slot="reference" icon="el-icon-user" circle> </el-button>
        <!--用户信息 -->
        <div v-if="check">
          欢迎回来！<span style="color: #55ab41">{{ user.username }}</span>
          <span id="logout" @click="logout">注销</span>
        </div>
        <div v-if="!check">
          <a
            style="color: #55ab41; margin-right: 148px; text-decoration: none"
            href="/login"
            >对不起,请前往登录</a
          >
          <a style="color: #55ab41; text-decoration: none" href="/register"
            >注册</a
          >
        </div>
      </el-popover>
    </el-row>
    <el-row id="input">
      <div>
        <img src="~@/assets/1057.png" alt="" />
      </div>
      <el-input
        v-model="search_word"
        placeholder="请输入搜索内容"
        prefix-icon="el-icon-search"
      >
        <el-button
          style="margin-right: 10px"
          slot="suffix"
          type="text"
          @click="search"
          >搜索</el-button
        >
      </el-input>
    </el-row>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      search_word: "",
      user: "",
      check: false,
    };
  },
  created() {
    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
    }
  },
  mounted() {
    this.checkToken();
    setInterval(() => {
      this.checkToken();
    }, 1000 * 60 * 10); //每十分钟检查token
  },
  methods: {
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
              this.$message.error("登录时间到达,请重新登录");
              window.localStorage.removeItem("access");
              setTimeout(() => {
                location.reload();
              }, 3000);
            }
          });
      }
    },
    async logout() {
      var _this = this;
      var jwt = JSON.parse(window.localStorage.getItem("access"));
      if (jwt != null) {
        await axios
            .get("http://localhost:9090/user/logout?username="+jwt.username+"&token="+jwt.token)
            .then(function (response) {
                if (response.data.message == "success") {
              _this.$message({
                  message: "退出成功",
                  type: "success",
                })
              window.localStorage.removeItem("access");
              setTimeout(() => {
                location.reload();
              }, 3000);
            }
            })
      }
    },
    search() {
      // 需要请求数据来显示
      this.$router.push({
        path: "/search",
        query: {
          word: this.search_word,
          recordsNum: this.recordsNum,
        },
      });
    },
  },
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
a {
  text-decoration: none;
}
#logout {
  cursor: pointer;
  float: right;
  text-decoration: none;
}
</style>
