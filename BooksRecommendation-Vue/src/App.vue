<template>
  <v-app>
    <vue-snotify></vue-snotify>
    <v-navigation-drawer clipped fixed v-model="drawer" app>
      <div style="text-align:center">
        <img style="width:80%" src="./assets/logo.png" />
      </div>
      <v-divider />
      <v-list dense>
        <v-list-tile v-for="item in menu" :key="item.name" :to="item.path">
          <v-list-tile-action>
            <v-icon>{{item.icon}}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{item.text}}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar color="blue" dark fixed app clipped-left>
      <v-toolbar-side-icon @click.stop="drawer = !drawer" class="hidden-lg-and-up"></v-toolbar-side-icon>
      <v-toolbar-title class="hidden-md-and-down">图书推荐系统</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-text-field
        flat
        solo-inverted
        hide-details
        prepend-inner-icon="search"
        label="搜索一下~"
        v-on:keyup.enter="doSearch()"
        v-model="keyword"
        style="margin-right:20px;max-width:500px"
      ></v-text-field>
      <div v-if="isLogin">
        <v-menu offset-y bottom>
          <template v-slot:activator="{on}">
            <v-btn icon large v-on="on">
              <v-avatar size="32px" tile>{{username}}</v-avatar>
            </v-btn>
          </template>
          <v-list>
            <v-list-tile @click="goTo('changePwd')">
              <v-list-tile-title>修改密码</v-list-tile-title>
            </v-list-tile>
            <v-list-tile @click="logout()">
              <v-list-tile-title>退出登录</v-list-tile-title>
            </v-list-tile>
          </v-list>
        </v-menu>
      </div>
      <v-btn @click="goTo('login')" v-if="!isLogin" icon large>
        <v-avatar size="32px" tile>{{username}}</v-avatar>
      </v-btn>
      <v-btn @click="goTo('register')" icon large>
        <v-avatar size="32px" tile>注册</v-avatar>
      </v-btn>
    </v-toolbar>
    <v-content>
      <router-view />

      <v-snackbar v-model="snackbar" :timeout="2000" top>
        {{ text }}
        <v-btn color="pink" flat @click="snackbar = false">Close</v-btn>
      </v-snackbar>
    </v-content>
    <v-footer color="blue lighten-2" app>
      <a>
        <span class="white--text">&nbsp;&copy; 2019</span>
      </a>
    </v-footer>
  </v-app>
</template>
<script>
export default {
  data() {
    return {
      drawer: null,
      username: "登录",
      snackbar: false,
      text: "",
      isLogin: false,
      keyword: "",
      menu: [
        { icon: "home", path: { path: "/" }, text: "热门图书及推荐" },
        { icon: "star", path: { path: "/star" }, text: "已评价图书" }
      ]
    };
  },
  created() {
    this.checkCookie();
  },
  watch: {
    $route: "checkCookie"
  },
  methods: {
    goTo(itemname) {
      this.$router.push({ name: itemname });
    },
    checkCookie() {
      if (this.$cookie.get("token") !== null) {
        this.username = this.$cookie.get("name");
        this.isLogin = true;
        this.$socket.removeAllListeners();
        this.$socket.emit("login", String(this.$cookie.get("token")));
      } else {
        this.username = "登录";
        this.isLogin = false;
      }
    },
    logout() {
      this.$cookie.delete("token");
      this.$cookie.delete("name");
      this.username = "登录";
      this.isLogin = false;
      this.$router.push("/");
    },
    doSearch() {
      if (this.keyword !== "") {
        this.$router.push("/search/" + this.keyword);
        this.keyword = "";
      }
    }
  },
  computed: {
    toolbars() {
      return this.$store.state.toolbars;
    }
  }
};
</script>
