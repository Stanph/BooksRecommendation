<template>
  <fragment>
    <v-container v-for="type in types" :key="type" fluid grid-list-md grey lighten-4>
      <v-subheader>{{ type }}</v-subheader>
      <div v-if="type==='图书详情'">
        <v-layout row wrap>
          <v-flex xs12 sm6 md4>
            <v-hover>
              <v-card slot-scope="{ hover }" class="mx-auto" color="grey lighten-4" max-width="600">
                <v-img :aspect-ratio="9/12" v-bind:src="bookDetail[0].l">
                  <v-expand-transition>
                    <div
                      v-if="hover"
                      class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
                      style="height: 100%; font-size = small"
                    >
                      {{bookDetail[0].publisher}}
                      <br />
                      {{bookDetail[0].year}}
                      <br />
                      {{bookDetail[0].isbn}}
                    </div>
                  </v-expand-transition>
                </v-img>
                <v-card-text class="pt-4" style="position: relative;">
                  <h3 class="display-1 font-weight-light orange--text mb-2">{{bookDetail[0].title}}</h3>
                  <div class="font-weight-light title mb-2">Author: {{bookDetail[0].author}}</div>
                </v-card-text>
                <v-divider dark></v-divider>
                <v-card-actions
                  class="pa-3"
                  @click="insertRating(bookDetail[0].bookid, bookDetail[0].score*2)"
                >
                  Rate this book
                  <v-spacer></v-spacer>
                  <span class="black--text text--lighten-2 caption mr-2">({{bookDetail[0].score }})</span>
                  <v-rating
                    v-model="bookDetail[0].score"
                    background-color="grey"
                    color="yellow accent-4"
                    dense
                    half-increments
                    hover
                    size="18"
                  ></v-rating>
                </v-card-actions>
              </v-card>
            </v-hover>
          </v-flex>
          <v-snackbar v-model="snackbar" :timeout="2000" top>
            {{ text }}
            <v-btn color="pink" flat @click="snackbar = false">Close</v-btn>
          </v-snackbar>
        </v-layout>
      </div>
      <div v-else>
        <v-layout row wrap>
          <v-spacer></v-spacer>
          <v-flex v-for="(item,index) in brBooks" :key="index+1" xs12 sm6 md4>
            <v-hover>
              <v-card
                slot-scope="{ hover }"
                class="mx-auto"
                color="grey lighten-4"
                max-width="600"
              >
                <v-img :aspect-ratio="9/12" v-bind:src="item.l" @click="BooksGoTo(item.bookid)">
                  <v-expand-transition>
                    <div
                      v-if="hover"
                      class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
                      style="height: 100%; font-size = small"
                    >
                      {{item.publisher}}
                      <br />
                      {{item.year}}
                      <br />
                      {{item.isbn}}
                    </div>
                  </v-expand-transition>
                </v-img>
                <v-card-text class="pt-4" style="position: relative;">
                  <h3 class="display-1 font-weight-light orange--text mb-2">{{item.title}}</h3>
                  <div class="font-weight-light title mb-2">Author: {{item.author}}</div>
                </v-card-text>
                <v-divider dark></v-divider>
                <v-card-actions class="pa-3" @click="insertRating(item.bookid, item.score*2)">
                  Rate this book
                  <v-spacer></v-spacer>
                  <span class="black--text text--lighten-2 caption mr-2">({{ item.score }})</span>
                  <v-rating
                    v-model="item.score"
                    background-color="grey"
                    color="yellow accent-4"
                    dense
                    half-increments
                    hover
                    size="18"
                  ></v-rating>
                </v-card-actions>
              </v-card>
            </v-hover>
          </v-flex>
          <v-snackbar v-model="snackbar" :timeout="2000" top>
            {{ text }}
            <v-btn color="pink" flat @click="snackbar = false">Close</v-btn>
          </v-snackbar>
        </v-layout>
      </div>
    </v-container>
  </fragment>
</template>

<script>
export default {
  data: () => ({
    snackbar: false,
    text: "",
    bookDetail: [],
    brBooks: [],
    types: ["图书详情", "相关图书推荐"]
  }),
  created() {
    this.initData();
    /** 初始化其他 */
    let pId = this.$route.params.pId;
    if (pId) {
      this.initOther(pId);
    }
  },
  watch: {
    //监听相同路由下参数变化的时候，从而实现异步刷新
    $route(to, from) {
      //做一些路由变化的响应
      //打开加载动画
      this.loading = true;
      //重新获取数据
      this.initData();
      /** 初始化其他数据 */
      let pid = this.$route.params.pid;
      if (pid) {
        this.initOther(pid);
      }
    }
  },
  methods: {
    getBRBooks() {
      this.$axios
        .post("books/brByItem", {
          bookid: parseInt(this.$route.params.id)
        })
        .then(
          function(response) {
            response.data.books.forEach(q => {
              const que = {};
              que.bookid = q.bookid;
              que.year = q.yearofpublication;
              que.isbn = q.isbn;
              que.title = q.booktitle;
              que.author = q.bookauthor;
              que.publisher = q.publisher;
              que.s = q.imageurls;
              que.m = q.imageurlm;
              que.l = q.imageurll;
              que.score = (q.score / 2).toFixed(1);
              this.brBooks.push(que);
            });
          }.bind(this)
        );
    },
    getBookDetail() {
      this.$axios
        .post("books/id", {
          bookid: parseInt(this.$route.params.id)
        })
        .then(
          function(response) {
            const que = {};
            que.bookid = response.data.books.bookid;
            que.year = response.data.books.yearofpublication;
            que.isbn = response.data.books.isbn;
            que.title = response.data.books.booktitle;
            que.author = response.data.books.bookauthor;
            que.publisher = response.data.books.publisher;
            que.s = response.data.books.imageurls;
            que.m = response.data.books.imageurlm;
            que.l = response.data.books.imageurll;
            que.score = (response.data.books.score / 2).toFixed(1);
            this.bookDetail.push(que);
          }.bind(this)
        );
    },
    initData() {
      this.bookDetail = [];
      this.brBooks = [];
      this.getBookDetail();
      this.getBRBooks();
    }
  }
};
</script>
<style>
.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.5;
  position: absolute;
  width: 100%;
}
</style>
