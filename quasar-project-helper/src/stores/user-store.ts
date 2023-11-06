import { defineStore } from 'pinia';
import {api} from 'boot/axios';
import {Notify} from 'quasar';
export const useUser = defineStore('user', {
  state: () => ({
    username: null,
    userid: -1,
    identity: -1,
  }),
  actions: {
    async fetchUser() {
      if(this.username != null) return;
      try{
        await api.get('/get_personal_info').then((response) => {
          // console.log(response.data)
          if (response.data.statusCode !== 200) {
            console.log(response.data.msg);
            Notify.create({
              message: 'Login expired, please log in again.',
              position: 'top',
            })
            this.router.push('/login');
            return;
          } else {
            this.userid = response.data.body.userId;
            this.identity = response.data.body.identity
            this.username = response.data.body.name;
          }
        }).catch((error) => {
          console.log(error);
        });
      } catch (error) {
        console.log('该部分在pinaia的user-store.ts中');
        console.log(error);
      }
    }
  },
});
export const useCurrentPageUser = defineStore('currentPageUser',{
  state: () => ({
    person_id: -1
  }),
})
