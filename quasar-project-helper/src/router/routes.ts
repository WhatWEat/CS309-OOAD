import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{path: '', component: () => import('pages/IndexPages/IndexPage.vue')},
      {path: '/test', component: () => import('pages/TestPage.vue')}],
  },
  {
    path: '/projects/:projectID',
    component: () => import('layouts/ProjectLayout.vue'),
    children:
      [{path: '', component: () => import('pages/IndexProjectPages.vue')},
        {path: '/group-list', component: () => import('pages/GroupPage/GroupInfo.vue')},
        {path: '/group-list/:groupId', component: () => import('pages/GroupPage/GroupInfo.vue')},
      ],
  },
  {
    path: '/person/:personId',
    component: () => import('pages/PersonPages/PersonInfo.vue'),
  },
  {
    path: '/login',
    component: () => import('pages/LoginPages/UserLogin.vue')
  },
  {
    path: '/register',
    component: () => import('pages/LoginPages/UserRegister.vue')
  },
  {
    path: '/forgotpassword',
    component: () => import('pages/ForgotPassword.vue')
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
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
