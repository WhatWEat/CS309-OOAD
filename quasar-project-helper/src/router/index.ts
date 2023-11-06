import { route } from 'quasar/wrappers';
import {
  createRouter,
  createWebHistory,
} from 'vue-router';
import routes from './routes';
import {useUser} from 'stores/user-store';
import {Notify} from 'quasar';
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
      const saved_jwt_token = localStorage.getItem('Token');
      console.log('jwt_token', saved_jwt_token)
      if (saved_jwt_token == null) {
        const try_user = await userStore.fetchUser();
        console.log('try_user', try_user)
        if (try_user === undefined || try_user === -1) {
          flag = false
        }
      }

      if (!flag) {
        Notify.create({
          message: 'login is expired, please login again',
          position: 'top',
        })
        next('/login');
      }
      next();
    }
  })
  return Router;
});
