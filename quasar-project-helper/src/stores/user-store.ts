import { defineStore } from 'pinia';
import {api} from 'boot/axios';
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
          this.userid = response.data.body.userId;
          this.identity = response.data.body.identity
          this.username = response.data.body.name;
          // console.log(response.data)
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
