import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const Home = () => import('../components/HomePage')
const SearchResult = () => import('../components/SearchResult')

const routes = [
    { path: '/', component: Home },
    { path: '/search', component: SearchResult },
    { path: '/*', redirect: '/' }  // 当匹配不上时重定向的到首页
]

const router = new Router({
    routes,
    mode: "history"
})

export default router