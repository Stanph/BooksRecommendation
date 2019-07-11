import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      meta:{
       needLogin:false,
       login:false
      },
      component: () => import('./views/Index.vue')
    }, 
    {
      path:'/register',
      name:'register',
      meta:{
        needLogin:false,
        login:false
      },
      component:() => import('./views/Register.vue')
    },
    {
      path:'/login',
      name:'login',
      meta:{
        needLogin:false,
        login:true
      },
      component:() => import('./views/Login.vue')
    },
    {
      path: '/search/:keyword',
      name: 'search',
      meta:{
        needLogin:false,
        login:false
      },
      component: () => import('./views/Search.vue')
    },
    {
      path: '/books/:id',
      name: 'book',
      meta:{
        needLogin:false,
        login:false
      },
      component: () => import('./views/BookDetail.vue')
    },
    {
      path:'/changePwd',
      name:'changePwd',
      meta:{
        needLogin:true,
        login:false
      },
      component: ()=> import('./views/changePwd.vue')
    },
    {
      path:'/star',
      name:'star',
      meta:{
        needLogin:true,
        login:false
      },
      component:() => import('./views/Star.vue')
    },
    {
      path: '*',
      name:'404',
      meta:{
        needLogin:false,
        login:false
      },
      component:()=>import('./views/404.vue')
    }
  ]
})
