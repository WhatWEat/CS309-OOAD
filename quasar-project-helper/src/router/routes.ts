import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{path: '', component: () => import('pages/IndexPage.vue')},
              {path: '/test', component: () => import('pages/TestPage.vue')}],
  },
  {
    path: '/projects/:projectID',
    component: () => import('layouts/ProjectLayout.vue'),
    children: [{path: '', component: () => import('pages/TestPage.vue')}],
  },
  {
    path: '/login',
    component: () => import('pages/LoginPage/UserLogin.vue')
  },
  {
    path: '/register',
    component: () => import('pages/LoginPage/UserRegister.vue')
  },
  {
    path: '/forgotpassword',
    component: () => import('pages/ForgotPassword.vue')
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
  //Group——Routers
  {
    path: '/teacher/:teacherId/BatchImport',
    component: () => import('pages/GroupPage/BatchImport.vue'),
  },
  {
    path: '/teacher/:teacherId/GroupInfo',
    component: () => import('pages/GroupPage/GroupTeacherPage.vue'),
  },
  {
    path: '/groupInfo/:groupId',
    component: () => import('pages/GroupPage/GroupInfo.vue'),
  }
];

export default routes;
