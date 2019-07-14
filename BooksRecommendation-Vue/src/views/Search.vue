<template>
  <v-card flat tile>
    <v-container fluid grid-list-md grey lighten-4>
      <v-layout row wrap>
        <v-flex v-for="(item,index) in data" :key="index+1" xs12 sm6 md4>
          <v-card dense>
            <v-card class="mx-auto elevation-20" color="blue" dark style="max-width: 400px;"  >
              <v-layout justify-space-between>
                <v-flex xs8>
                  <v-card-title primary-title>
                    <div class="headline">{{item.title}}</div>
                    <div>Author: {{item.author}}</div>
                    <div>Publisher: {{item.publisher}}</div>
                    <div>Publication Year: {{item.year}}</div>
                    <div>ISBN: {{item.isbn}}</div>
                  </v-card-title>
                </v-flex>
                <v-img
                  class="shrink ma-2"
                  contain
                  height="125px"
                  v-bind:src="item.url"
                  style="flex-basis: 125px"
                  @click="BooksGoTo(item.bookid)"
                ></v-img>
              </v-layout>
              <v-divider dark></v-divider>
              <v-card-actions class="pa-3" @click="insertRating(item.bookid, item.score*2)">
                Rate this book
                <v-spacer></v-spacer>
                <span class="gery--text text--lighten-2 caption mr-2">({{item.score}})</span>
                <v-rating
                  v-model="item.score"
                  background-color="white"
                  color="yellow accent-4"
                  dense
                  half-increments
                  hover
                  size="18"
                ></v-rating>
              </v-card-actions>
            </v-card>
          </v-card>
        </v-flex>
        <v-snackbar v-model="snackbar" :timeout="2000" top>
          {{ text }}
          <v-btn color="pink" flat @click="snackbar = false">Close</v-btn>
        </v-snackbar>
      </v-layout>
    </v-container>
  </v-card>
</template>
<script>
export default {
  data() {
    return {
      snackbar: false,
      data: [],
      text: "",
      lock: false,
      books: []
    };
  },
  computed: {
    keyword() {
      return this.$route.params.keyword;
    }
  },
  created() {
    this.search();
  },
  watch: {
    $route(to, from) {
      if (to.params.keyword !== from.params.keyword) {
        this.data = [];
        this.books = [];
        this.lock = false;
        this.search();
      }
    },
    type: "initData"
  },
  methods: {
    search() {
      if (!this.lock) {
        this.lock = true;
        this.$axios
          .post("http://127.0.0.1:2333/search", {
            keyword: this.keyword,
          })
          .then(
            function(response) {
              response.data.forEach(q => {
                const que = {};
                que.bookid = q._source.BookID;
                que.year = q._source.YearOfPublication;
                que.isbn = q._source.ISBN;
                if (typeof q.highlight != "undefined") {
                  que.title = q.highlight.BookTitle[0];
                } else {
                  que.title = q._source.BookTitle;
                }
                que.author = q._source.BookAuthor;
                que.publisher = q._source.Publisher;
                que.url = q._source.ImageURLL;
                que.score = (q._source.score / 2).toFixed(1);
                this.books.push(que);
              });
              this.initData();
            }.bind(this)
          );
      }
    },
    initData() {
      this.data = [];
      this.data = this.books;
    }
  }
};
</script>
