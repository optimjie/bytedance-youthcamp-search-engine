// import Vue from 'vue'
// import App from './App.vue'
//
// Vue.config.productionTip = false
//
// new Vue({
//   render: h => h(App),
// }).$mount('#app')


import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import router from "./router/index";
import VueTreeList from 'vue-tree-list';

Vue.use(ElementUI);
Vue.use(VueTreeList);

new Vue({
  render: h => h(App),
  router
}).$mount('#app');