<template>
  <div>
    <el-form ref="loginForm" :model="form" :rules="rules" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item label="账号" prop="username">
        <el-input
          type="text"
          placeholder="请输入用户名"
          v-model="form.username"
        ></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          type="password"
          placeholder="请输入密码"
          v-model="form.password"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <!-- <el-button id="login_button" type="primary" @click="submitForm('form')">登录</el-button> -->
        <el-button id="login_button" type="primary" @click="submitForm()"
          >登录</el-button
        >
        <a href="/register" id="register">立刻注册</a>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "Login",
  data() {
    return {
      form: {
        username: "test",
        password: "qwe123456",
      },
      rules: {
        username: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
          // ,
          // { validator: validPassword, trigger: 'blur' }
        ],
      },
    };
  },
  methods: {
    submitForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          let _this = this;
          axios
            .post("http://localhost:9090/user/login", this.form, {
              headers: { "Content-Type": "application/json" },
            })
            .then(function (ressponse) {
              if (ressponse.data.message == "success") {
                localStorage.setItem("access", JSON.stringify(ressponse.data));
                _this.$message({
                  message: "登录成功",
                  type: "success",
                });
                setTimeout(() => {
                  //设置延迟执行
                  _this.$router.replace({ path: "/" });
                }, 3000);
              } else {
                _this.$message.error({
                  message: "登录失败",
                });
              }
            })
            .catch(function (error) {
              console.log("异常信息:" + error);
            });
        } else {
          this.$message({
            message: "用户名或密码错误",
            type: "warning",
          });
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.login-box {
  width: 350px;
  margin: 120px auto;
  border: 1px solid #dcdfe6;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 30px #dcdfe6;
}

.login-title {
  text-align: center;
}
#login_button {
  width: 350px;
  margin-top: 18px;
}
#register {
  height: 10px;
  float: right;
  text-decoration: none;
  cursor: pointer;
  color: #4e6ef2;
  line-height: 50px;
}
</style>
