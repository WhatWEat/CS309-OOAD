import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/IndexPage.vue') }],
  },
  {
    path: '/projects/:projectID',
    component: () => import('layouts/ProjectLayout.vue'),
    children: [{ path: '', component: () => import('pages/testPage.vue') }],
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },

  {
    path: '/Login-1',
    component: () => import('pages/Login-1.vue')
  },
  {
    path: '/Register',
    component: () => import('pages/Register.vue')
  },
  {
    path: '/PhoneLogin',
    component: () => import('pages/PhoneLogin.vue')
  },
  {
    path: '/EmailLogin',
    component: () => import('pages/EmailLogin.vue')
  },
];

export default routes;
