<template>
  <v-card flat tile>
    <v-container fluid grid-list-md grey lighten-4>
      <div>
        <v-layout row wrap>
          <v-flex v-for="(item,index) in brBooks" :key="index+1" xs12 sm6 md4>
            <v-hover>
              <v-card slot-scope="{ hover }" class="mx-auto" color="grey lighten-4" max-width="600" >
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
  </v-card>
</template>

<script>
export default {
  data: () => ({
    snackbar: false,
    text: "",
    brBooks: []
  }),
  created() {
    this.getBRBooks();
  },
  methods: {
    getBRBooks() {
      this.$axios
        .post("books/rating", {
          token: this.$cookie.get("token")
        })
        .then(
          function(response) {
            response.data.books.forEach(q => {
              const que = {};
              que.bookid=q.bookid;
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
    initData() {
      this.brBooks = [];
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
