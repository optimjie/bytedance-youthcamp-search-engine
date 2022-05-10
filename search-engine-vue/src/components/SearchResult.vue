<template>
  <el-container>
    <el-header height="20%">
      <img src="~@/assets/1057inRes.png" alt="">
      &nbsp;&nbsp;
      <el-input v-model="search_word" placeholder="请输入搜索内容" prefix-icon="el-icon-search">
        <el-button style="margin-right:10px" slot="suffix" type="text" @click="search">搜索</el-button>
      </el-input>
    </el-header>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="date" label="日期" width="140">
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120">
        </el-table-column>
        <el-table-column prop="address" label="地址">
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>


<script>

export default {
  data() {
    const item = {
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
    };
    return {
      search_word: '',
      tableData: Array(20).fill(item)
    }
  },
  created() {
    this.get();
  },
  methods: {
    get() {
      this.search_word = this.$route.query.word;
    }
  },
};
</script>

<style>
.el-header {
  display: flex;
  position: relative;
  width: 100%;
  height: 80px;
}
.el-main {
  margin-top: 20px;
  position: absolute;
  left: 200px;
  right: 0;
  top: 60px;
  bottom: 0;
  overflow-y: scroll;
}
</style>
