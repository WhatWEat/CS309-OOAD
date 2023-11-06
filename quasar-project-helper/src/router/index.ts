import { route } from 'quasar/wrappers';
import {
  createRouter,
  createWebHistory,
} from 'vue-router';
import {useUserStore} from 'src/composables/useUserStore';
import routes from './routes';

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default route(function (/* { store, ssrContext } */) {
  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    history: createWebHistory(),
    routes

    // Leave this as is and make changes in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    // history: createHistory(process.env.VUE_ROUTER_BASE),
    // history: createHistory('history'),
  });
  Router.beforeEach((to, from, next) => {
    if (to.meta.freeLogin) {
      next();
    } else {
      const {userid} = useUserStore();
      if (userid.value == -1) {
        next('/login');
      }
      next();
    }
  })
  return Router;
});
