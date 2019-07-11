import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import store from './store'
import Axios from 'axios'
import VueCookie from 'vue-cookie'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import SocketIO from 'socket.io-client'
import vueSnotify from 'vue-snotify'
import "vue-snotify/styles/simple.css";
import MescrollVue from 'mescroll.js/mescroll.vue'
import 'normalize.css'

Vue.config.productionTip = false
Axios.defaults.baseURL = 'BooksRecommendation';
Axios.defaults.headers.post['Content-Type'] = 'application/json';
Vue.prototype.$axios = Axios;
Vue.use(VueCookie);
Vue.use(vueSnotify);
Vue.use(MescrollVue);
var socket=SocketIO('http://127.0.0.1:2333/socket.io');

Vue.prototype.$socket = socket;
Vue.prototype.BooksGoTo = function BooksGoTo(id){
  this.$router.push(`/books/${id}`);
};
Vue.prototype.insertRating = function insertRating(id, score) {
  this.$axios
    .post("ratings/insert", {
      token: this.$cookie.get("token"),
      bookid: id,
      score: score
    })
    .then(
      function(response) {
        if (response.data.code === 0) {
          this.text = "评分成功！";
          this.snackbar = true;
        } else {
          this.text = "评分失败！";
          this.snackbar = true;
        }
      }.bind(this)
    );
};
router.beforeEach((to, from, next) => {
  if(to.meta.needLogin){
    if(Vue.prototype.$cookie.get('token')!==null){
      next();
    }else{
      next('/login');
    }
  }else{
    if(to.meta.login&&Vue.prototype.$cookie.get('token')!==null){
      next('/');
    }else{
      next();
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
