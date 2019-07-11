<template>
  <v-layout style="height:100%;" align-center justify-center>
    <v-flex xs12 sm8 md4>
      <v-card class="elevation-12">
        <v-toolbar color="blue" dark>
          <v-toolbar-title>注册账号</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form>
            <v-text-field :conter="30" v-model="account" label="账号"></v-text-field>
            <v-text-field
              :rules="pwdRules"
              :conter="30"
              v-model="newpwd"
              label="密码"
              type="password"
            ></v-text-field>
            <v-text-field
              :rules="pwdRules"
              :conter="30"
              v-model="newpwd2"
              label="密码确认"
              type="password"
            ></v-text-field>
            <v-text-field :conter="30" v-model="city" label="所在城市"></v-text-field>
            <v-text-field :conter="30" v-model="state" label="所在省份"></v-text-field>
            <v-text-field :conter="30" v-model="country" label="所在国家"></v-text-field>
            <v-text-field :conter="30" v-on:keyup.enter="register" v-model="age" label="年龄"></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            :disabled="(account.length<1||newpwd.length<6||newpwd2.length<6||city.length<1||state.length<1||country.length<1||age.length<1)"
            color="primary"
            dark
            @click="rigester()"
          >注册</v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
    <v-snackbar v-model="snackbar" :timeout="2000" top>
      {{ text }}
      <v-btn color="pink" flat @click="snackbar = false">Close</v-btn>
    </v-snackbar>
  </v-layout>
</template>
<script>
export default {
  data: () => ({
    account: "",
    newpwd: "",
    newpwd2: "",
    city: "",
    state: "",
    country: "",
    age: "",
    snackbar: false,
    text: "",
    pwdRules: [
      v => !!v || "密码不能为空！",
      v => (v.length >= 6 && v.length <= 30) || "密码长度为6~30位"
    ]
  }),
  methods: {
    rigester() {
      if (this.newpwd.length >= 6 && this.newpwd2.length >= 6) {
        if (this.newpwd === this.newpwd2) {
          this.$axios
            .post("users/register", {
              account: this.account,
              newPwd: this.newpwd,
              city: this.city,
              state: this.state,
              country: this.country,
              age: parseInt(this.age)
            })
            .then(
              function(response) {
                if (response.data.code === 0) {
                  this.text = "注册成功!";
                  this.snackbar = true;
                  this.$router.push("/");
                } else if (response.data.code === -1) {
                  this.text = "账号已存在";
                  this.snackbar = true;
                } else {
                  this.text = "注册失败!";
                  this.snackbar = true;
                }
              }.bind(this)
            );
        } else {
          this.text = "两次输入的密码不一致！";
          this.snackbar = true;
        }
      } else {
        this.text = "注册失败!";
        this.snackbar = true;
      }
    }
  }
};
</script>
