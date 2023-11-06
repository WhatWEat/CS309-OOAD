import {defineStore} from 'pinia';
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
        return await api.get('/get_personal_info').then((response) => {
          // console.log(response.data)
          if (response.data.statusCode !== 200) {
            console.log('pinia', response.data.msg);
            localStorage.clear();
            this.userid = -1;
            this.username = null;
            this.identity = -1;
          } else {
            this.userid = response.data.body.userId;
            this.identity = response.data.body.identity
            this.username = response.data.body.name;
          }

          return this.userid;
        }).catch((error) => {
          localStorage.clear();
          this.userid = -1;
          this.username = null;
          this.identity = -1;
          console.log(error);
          return this.userid;
        });
      } catch (error) {
        console.log('该部分在pinaia的user-store.ts中');
        console.log(error);
        return this.userid;
      }
    }
  },
});
export const useCurrentPageUser = defineStore('currentPageUser',{
  state: () => ({
    person_id: -1
  }),
})
