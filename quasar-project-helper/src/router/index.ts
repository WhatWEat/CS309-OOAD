import { route } from 'quasar/wrappers';
import {
  createRouter,
  createWebHistory,
} from 'vue-router';
import routes from './routes';
import {useUser} from 'stores/user-store';

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
  Router.beforeEach(async (to, from, next) => {
    if (to.meta.freeLogin) {
      next();
    } else {
      const userStore = useUser();
      let flag = true
      if (userStore.userid == -1) {
        const try_user = await userStore.fetchUser();
        if (try_user === -1) {
          flag = false
        }
      }
      if (!flag) {
        console.log('not login in router')
        next('/login');
      }
      next();
    }
  })
  return Router;
});
