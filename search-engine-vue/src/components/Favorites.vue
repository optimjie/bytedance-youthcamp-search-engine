<template>
  <div>
    <div v-if="addToFavorite == 0" style="text-align: right;">
      <a style="cursor: pointer" @click="addNode">添加文件夹</a>
    </div>
    <vue-tree-list
      @click="onClick"
      @change-name="onChangeName"
      @delete-node="onDel"
      @add-node="onAddNode"
      :model="data"
      default-tree-node-name="new node"
      default-leaf-node-name="new leaf"
      v-bind:default-expanded="false"
    >
      <template v-slot:leafNameDisplay="slotProps">
        <span>
          {{ slotProps.model.name }}
        </span>
      </template>
      <span class="icon" slot="addTreeNodeIcon"
        ><img
          width="20"
          height="20"
          src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABmJLR0QA/wD/AP+gvaeTAAACuElEQVRoge2ZPUxTURTHf6e0FcqnIEKAQaImbg4dxMAmERIHcMDECVlkNySoi2UyitFZGcCEOMAAGAfR6gJqYtLBzQ9iMCBEtH60QehHeh2wifQWbUt5l8T32+6595z3//fe09e+BzY2NjY2/zOSLqhGaSDOLaANKM2okmJAevDlUVtGaAZ+i38FVGZdzYAJhxbZ+OSzFw8gXFHDpg1sHJvcsdiEfoRGUDidUFsDJcXgSOfRQlRCsba+zOr6OTm18jh1WlfndMLBRigrNS8eQByCx1NHVfm0erDPmzqtK6ytgYICS7RlhaNAKPFMaGFtYUmxJXpyorCwITWkG9gNx2YrHA6tZ3ex2sywDZjGNmAa24BpbAOmsQ2YxmlaAEBcCbPhIp6HPLyLuPkc25BV7YpzeE+U5rI1mkt/4hSl5Ro3MBPycGdlL8tRXcpCxMVCxMXTUDF17hjn93/X1uj/yF4e0W3uAAmEoU8VjAfLssoT5EbLfFO/z+dLgMEeyEU8gEL1zRx4cTU5NrIDMyEPA4vVaef83Q83jVvvtqcvotTpJz2PJi3fgbgShlYqtl9I5GbXWJfbcgOz4SKWoq58lGoMroY7LDfwLOzJWy0ROtL1wA8g++7KkO65ej7+8ZWZeub/xaaeEN6k2wF/jtoy4ms8j49sFPW6AcUlIJi/q+woCc2AHHv9lnjiKMgYEMr3Fauc8bzVUrCU9v3ATtI60nZPwdmt5jO+DwCgRk3ciSfzVkmYstzAt8iXCYG57VeS95VF5fctNxDoDcRQ0r/dOiLqwviZ8ajlPZCkdaR9UKH6cskVxTV/z/RFMPhrtGW+qR9RgzmkXm/5cPxycmBsB5KcGD7ZKSKDCg79bZ3AHEKfv3t6KiVuHu9tr6vcXd0pQodCeQUaABQsChJAEpPf14NTgd5ALDX3Fy7fxxELxJxNAAAAAElFTkSuQmCC"
      /></span>
      <span class="icon" slot="addLeafNodeIcon"
        ><img
          width="18"
          height="18"
          src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABmJLR0QA/wD/AP+gvaeTAAACWUlEQVRoge2ZPUxTURiGnw/7YyGCaDQaHSBhcq2amqCLxrgVB1ixkysxKMSpm6DGsDpRJxObSOtETZ1MqD901C6t0QRpIWnENhVbmh4nDRRCf+7lnprcZzs357z3PPnuuff0FGxsDCGNOsx9KKsDuvPsxAX3tNGYLjPm0irdLgHF1NzH8ozRLC0CVwYO0WOShBYBj1O4bJKEFgEwT0KbAJgjoVUAjEtoFwBjEh0hAO1LdIwAtCfRUQKwW6JRf4cVk6pn4dOWaVkdV4FWsQV0YwvoxhbQjS2gG1tAN7aAbvQdbG1DqSrZYoJc4T0/yxk2t/IAeJzHKZW/Pwci/d29kfBYuFI/VrtAtrDE5/UQvyq5Rl0zouRePLD4cvtFbQKKGqm1EJl8pKVxgjwe/uqbCgaDNdC4BtqZPIBCTb4dSDz429ZSgWxhieWVvX/uxscXd7SvPbuxd4hSN98EXkcsr4BSVVLrIeNBIk9GX4y6LBfIFhOUGi/YZhjMl4p+ywVyxXemZYngt/xUYmMzs6Nd/8zXs++aEHXe8gqUqz/MC1Oc+d+3EjXLBdyOftOyFKxavgaOHh6iVFn9165/zzf9HQAEtWx5BU71+swLE6LWCxzx0eM6bUKSfDnm6XtluUCXODh38pbhHBF1JzwWrmjbjabW5knnF9oaK4rZeCA2DU0cr09cdDeUbIdgMNiVHlQzKLnb4tCHw98u3Y8TA5rYjR40V+evj4jIIwVD+/UTSCNMxsdj0brr+vE+9Tr7XCdGRPArlFfgLICCFUGSSC2y8TsfTd5O7vpn5A9ZreYN22YeogAAAABJRU5ErkJggg=="
      /></span>
      <span class="icon" slot="editNodeIcon"
        ><img
          width="20"
          height="20"
          src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAABmJLR0QA/wD/AP+gvaeTAAAEzElEQVR4nO3db0zUdRzA8ff3DlI6mAsoCsGmq7ksWCgW8QBQR7W1ntV6am0sLbcc4lpq8zYX1XZHTyhpNWyrZ9VobT0oZwYrYA1zRmpr0y1wIISldqQi3LcHcvzTO37HXd339/193o9uP36/7/c7Xvt9fzw47kCSJEmSJEmSJEmSJHNSmV6A2zvX2Pisgg0avi5paTma6ngCssR0MOgbuny5DaUaYoc07C4Jh8OpjOtLw9o8lwY1HIm0zsEAUApCQ01Nr6Uytj/FtXmuYFD77t/4Txtab4tzypbG6upIS09Pz1LGlzskibTWqqKmu7W/pr5cKzUZ7zwFoaFdu/YsZQ55hjhMa62+PNr9robtAMvGx3vLOr+pVFpnxbtGwd7icLg5mXkExEELMWI5QUHr3StbWkJO5xKQRYqHEcsBio4qtbk0FPrOyXzyDEmQDuI79fn2A/EwAK4FAlW/1Nb/mOCZonxaP+F0TgGJk9YoimhdN9a2d/XFjs5E514N5Fb31z7eFw9FwzGn8zrest7pG79HR7PynZ7v5vxqQr1wYu2+3Mmh52LHfira0ze44qnKRNctGx/vLes6XKmi0bnb14crw+GGuBctKP7DaEEq6t+n4CWn57s3Td3vTeRODs07un6kuTJyWzF/5VTEvfJaIFDVX1PfW9Z1eIOKRrOB9uK8vBeTmV22rHlpNg28QvnY+7f8ac3gDu64cjzhCNMoxyazsj4ozstrUMFgNJkVCMhMiTFiOUG5GgicOpTz1rZkMUBApnOGESsRiob2E13VDcGgShoDBIRkMWLVDO6g9NJXfYCeHSk1DPA8yNIwYq0faa5cfbGjixsoh1LFgCT+yrItpaNsHniZhy60pzRO+WioNktHmj/tP/h6qhjg2TtEUze4M2UMABTt6/rb0oIBngRJbZuaWyS7qINhGlSQtGCA50DSh3GycCsfPXB6fzoxwFPPkPRiHCk9yJTypRUDPHOHpB9Dq//mV+cBEPdggPUg7sIAq0HchwHWgrgTA6wEcS8GWAei2TS407UYYBXINMYfbSmPlCkMsAbEDgywAsQeDHA9iF0Y4GoQ+zDAtSB2YoArQezFANeB2I0BrgKxHwNcA+INDHAFiHcwwHgQb2GA0SDewwBjQbyJAUaCeBcDjAPxNgYYBSIYYBDIoxfeTA9GwfOuxQBjQDQPBj5hpOiRlEY5WbiVI6vecy0GGAJSoH8lT5+jMP/0klHcvE3NzYjVl0x9D4CfiSWh2IIBpoBEu2ZeJ4tiEwYYAaIpnvph3hGnKLZhgAEggUgf2VN/3nR8MRQbMcAAkNGfv+DVzyq4PnXzUuKh2IoBBoAMnz1D95k7HaPYjAGZBtFT/DYwAbAoyu0FY/Teu99qDMj0v7SdP8qlK7NLiKG8/cxxJrPyOa8qGfI/xoC/jlH1MCy3//PWMgoyfvbbmdcrcq6zdpWfu9Zs5GPeILK8Cp35HfV/L7N3iPLz9JP3kbtmC9xdB+rGp9b+ndFFZbaMggSqD2RyeiPz3p5geAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWAJiWM6/gwrVqWe/KkMClO8W74GVJEmSJEmSJEmSJMmi/gXAM2cGp0XwwAAAAABJRU5ErkJggg=="
      /></span>
      <span class="icon" slot="delNodeIcon"
        ><img
          width="18"
          height="18"
          src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAABmJLR0QA/wD/AP+gvaeTAAAFFUlEQVR4nO3dT2wUVRzA8e+bbpdiS0uagHgjphXhAg3VhGgTE4OJIeGgJpqomKjogRi9eRJjTEhp9SIaA/EENaYQe0AKWIytYDFqiQdMrH+CJ6No0EDkT7vT9zyUbaTt0pnZ99rflN/31nb7m7f5dGd3Zre7oGmapmmapmmapmmanMxCLyBJ7n62EXFfVUMsw+ZL9ntaUrDEg7gOtgN7qX6tDnjZnGJP9asKl2gQjxhTIxGOIhYkAMbUaASjBAFxOx68i5qJTWCWZBpw4Y8Orlx8knB/MI76xl6a7xjM+OtjYE+bd07+7HdZnq+we2FjLXXL3gee9T1bYA5nPmDs0g6z70zJ19DI1yAAli7rAp5j8WMAGIzbTl1Dp9+hnnKvPLAc686DKfqamY/cONdqV5p9n130Mc3jLcStvfUwAEyRJfFaX9M8gmS8A18M1VDna5Tf+xCt6hREWAVvkxwx8M+0787+oMHZItbe5m3bIYqiK5hovMJP3bSvYl+bDfbw1HWwAvgz1PwFbqU5xV8hBusuS1gKIiwFEZaCCEtBhKUgwlIQYSmIsBREWAoiLAURloIIS0GEpSDCUhBhKYiw5IPUeXxi0eesQIkGce2bKR0Yxd19T/WzWtsoHRjFbtriYWXhEgvi2jcTv/kxNK8i7jpaFYprbSPuOgbNq5h4vVc0ikiQKYzi9Zc71TdlRpnCaGye/EZtUTSKOJAZGOUyoMzAKCcYRRRIRYxyKVAqYpQTiiIGxLWsvzlGufom4t39uDUbK89a00781qeVMcrVFpnY+RGuZX2GFYdJDIg5d5Zo6FCyCzcsJ+4+PustxbW2EXcegYbliUZFw4cx575Ps9SgiQHBWmq6txMNHEh2+Vl2X3PupqYVDR6kZtczYCeyrDhIckCgKpTFgAE+X9vrq+soAPahp+e+fH3T5P2FtVDfmGgTUjFAIgikR1nakHi0ZAyQtsv6f2l3XwmSjgGSQcArSh4wQDoIeEHJCwbkAQSqQskTBuQFBMBaor73oDSW/HfiElHfu7nBgByBuJYNxF1HoTbFP/sWaok7P8GtvTfcwjyXCxDXsoG4+3jig74bKp/7ygmKeJCqMMrlCEU0iBeMcjlBEQviFaNcDlBEgqTFiIYOEQ30JBsuHEXcuazUGOXjDBzgkp+Q3N1P4dUtmB++qWq9vhN1C8mMYSeynboXeEsRA+JWryN+eyA5xkAPNbu23XjQN4WSYvfVeQS3el2GFYdJDIj57RfM2eFEl40GD06enrd25g+tpab7+cS3FDP6Leb3X9MsNWhiQCiNU3jjccxX/Te9WKJzUwl3X2bkBIWdj8HY1SwrDpIcEJgTJdWJwjlQJGKANBCoiJLprG0FFDNygsJrj4rDAIkgMAOlqlPo01CmMMav+Vyxt2S/X1axDrv1RaK+PbPfgacpirCPvER0eK8PjGDvlyUbRG76Bma3SgoiLAURloIIS0GEpSDCUhBhKYiwwoGMk+IVbTkr4HULBmK+5hJwOdT8Bezf69ctSGF3WY7TQecvTMmeRctYWJCI3qDzFyLHwZDjw4JcZj/wU9BtzG8/Ugj78a1BQcwZShieYHHcl1zF8pQZ8vdZIbMV/GGvOcl3GLYCf4feVsAuELHFDDMSekPzchxiTvI5jjbgQyA//6wxudYeDG3mCzJ+Kmi65v0DIF0HK3A8TMSdOG7HCDs4dVgM57Gcw3As1BNRmqZpmqZpmqZpmqblof8AhnsIKx8IIFIAAAAASUVORK5CYII="
      /></span>
      <span class="icon" slot="leafNodeIcon">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          x="0px"
          y="0px"
          width="48"
          height="48"
          viewBox="0 0 48 48"
          style="fill: #000000"
        >
          <path fill="#90CAF9" d="M40 45L8 45 8 3 30 3 40 13z"></path>
          <path fill="#E1F5FE" d="M38.5 14L29 14 29 4.5z"></path>
        </svg>
      </span>
      <span class="icon" slot="treeNodeIcon">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          x="0px"
          y="0px"
          width="48"
          height="48"
          viewBox="0 0 48 48"
          style="fill: #000000"
        >
          <path
            fill="#FFA000"
            d="M40,12H22l-4-4H8c-2.2,0-4,1.8-4,4v8h40v-4C44,13.8,42.2,12,40,12z"
          ></path>
          <path
            fill="#FFCA28"
            d="M40,12H8c-2.2,0-4,1.8-4,4v20c0,2.2,1.8,4,4,4h32c2.2,0,4-1.8,4-4V16C44,13.8,42.2,12,40,12z"
          ></path>
        </svg>
      </span>
    </vue-tree-list>
  </div>
</template>

<script>
import { VueTreeList, Tree, TreeNode } from "vue-tree-list";
import axios from "axios";
export default {
  env: {
    browser: true,
    node: true,
  },
  name: "Favorites",
  props: ["username", "addToFavorite"],

  data() {
    return {
      usernameInFavorite: this.username,
      newTree: {},
      data: {}, // tree list绑定的数据
      treeNodeData: [], // 返回的数据
      editName: 0, // 用于判断何时修改名称
      url: "",
      caption: "",
      idToUrl: {},
      token:true
    };
  },
  created() {
    this.getFavorites();
    this.idToUrl = new Map();
  },
  methods: {
    reload() {

      this.$forceUpdate()
    },
    Token() {
      var jwt = JSON.parse(window.localStorage.getItem("access"));
      if (jwt != null) {
        axios
          .get(
            "http://localhost:9090/survival?token=" +
              jwt.token +
              "&username=" +
              jwt.username
          )
          .then((response) => {
            if (response.data.message != "success") {
              this.$message.error("登录已失效，请重新登录");
              this.token = false;
            } else {
              this.token = true;
            }
          });
      } else {
        this.token = false;
      }
    },
    async getFavorites() {
      console.log("获取收藏夹");
      this.treeNodeData = [];
      let outer = this;
      this.data = new Tree([]);
      await axios
        .get(
          "http://localhost:9090/getFavorites?username=" +
            this.usernameInFavorite
        )
        .then((response) => (outer.treeNodeData = response.data));
      let nodes = [];
      let idToIdx = new Map();
      let set = new Set();
      for (let i = 0; i < this.treeNodeData.length; i++) {
        idToIdx.set(this.treeNodeData[i].id, i);
        let id = this.treeNodeData[i].id;
        let name = this.treeNodeData[i].name;
        let leaf = this.treeNodeData[i].leaf;
        let url = this.treeNodeData[i].url;
        if (this.addToFavorite == 1) {
          nodes.push(
            new TreeNode({
              id: id,
              name: name,
              isLeaf: leaf,
              dragDisabled: true,
              addTreeNodeDisabled: true,
              addLeafNodeDisabled: true,
              editNodeDisabled: true,
              delNodeDisabled: true,
            })
          );
          if (leaf) {
            set.add(i);
            continue;
          }
        } else {
          if (leaf) this.idToUrl.set(id, url);
          nodes.push(
            new TreeNode({
              id: id,
              name: name,
              isLeaf: leaf,
              dragDisabled: true,
            })
          );
        }
      }
      for (let i = 0; i < this.treeNodeData.length; i++) {
        if (this.treeNodeData[i].pid === "0") continue;
        let id = this.treeNodeData[i].id;
        let pid = this.treeNodeData[i].pid;
        if (set.has(i)) continue;
        nodes[idToIdx.get(pid)].addChildren(nodes[idToIdx.get(id)]);
      }
      for (let i = 0; i < this.treeNodeData.length; i++) {
        if (this.treeNodeData[i].pid == 0) {
          this.data.addChildren(nodes[i]);
        }
      }
    },
    async onDel(node) {
      this.Token();
      if (this.token) {
        await axios
          .post("http://localhost:9090/deleteTreeNode", {
            id: node.id,
          })
          .then((response) => console.log(response.data))
          .catch((error) => console.log(error));
        node.remove();
        // console.log(node)
        // alert(node.id)
      } else {
        this.$message.error("登录已失效，请重新登录");
      }
      setTimeout(() => {
        location.reload();
      }, 2000);
    },

    // 有个问题是修改名称时，该方法会执行两次，所以得特判一下，用一个变量即可
    // 搞不懂这样为啥这么写
    async onChangeName(params) {
      this.Token();
      if (this.token) {
        this.editName++;
        if (this.editName == 1) {
          await axios
            .post("http://localhost:9090/updateTreeNodeName", {
              newName: params.newName,
              id: params.id,
            })
            .then((response) => console.log(response.data))
            .catch((error) => console.log(error));
        }
        if (this.editName == 2) {
          this.editName = 0;
        }
        console.log(params);
      } else {
        this.$message.error("登录已失效，请重新登录");
        setTimeout(() => {
          location.reload();
        }, 2000);
      }
    },

    async onAddNode(params) {
      this.Token();
      if (this.token) {
        // console.log(params.isLeaf)
        let outer = this;
        await axios
          .post("http://localhost:9090/addTreeNode", {
            username: outer.usernameInFavorite,
            id: params.id,
            pid: params.pid,
            name: params.name,
            isLeaf: params.isLeaf ? 1 : 0,
            url: params.url,
          })
          .then((response) => console.log(response.data))
          .catch((error) => console.log(error));
        // console.log(params)
      } else {
        this.$message.error("登录已失效，请重新登录");
        setTimeout(() => {
          location.reload();
        }, 2000);
      }
    },

    onClick(params) {
      this.Token();
      if (this.token) {
        if (this.addToFavorite == 1) {
          this.$confirm("收藏到【" + params.name + "】", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
          })
            .then(() => {
              let id = new Date().valueOf();
              let pid = params.id;
              // console.log(this.caption + " " + this.url)
              this.onAddNode({
                username: this.usernameInFavorite,
                id: id,
                pid: pid,
                name: this.caption,
                url: this.url,
                isLeaf: true,
              });
              this.$message({
                type: "success",
                message: "添加成功!",
              });
              this.reload()
              this.$emit("notShowDialog");
              // location.reload();
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消添加",
              });
              this.getFavorites();
            });
        }
        if (params.isLeaf) {
          let url = this.idToUrl.get(params.id);
          window.open(url);
        }
        // console.log(params)
      } else {
        this.$message.error("登录已失效，请重新登录");
        setTimeout(() => {
          location.reload();
        }, 2000);
      }
    },

    // 这个添加是指pid为0的节点，即新建文件夹
    addNode() {
      this.Token();
      if (this.token) {
        let name = "新建文件夹";
        let id = new Date().valueOf();
        var node = new TreeNode({ name: name, id: id });
        this.data.addChildren(node);
        this.onAddNode({
          username: this.usernameInFavorite,
          id: id,
          pid: "0",
          name: name,
        });
        // location.reload();
      } else {
        this.$message.error("登录已失效，请重新登录");
        setTimeout(() => {
          location.reload();
        }, 2000);
      }
    },

    addFavorite(url, caption) {
      // console.log("here?")
      this.url = url;
      this.caption = caption;
      // console.log(this.url + " " + this.caption)
    },
  },
};
</script>

<style scoped></style>
<style>
.vtl-node-content {
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
