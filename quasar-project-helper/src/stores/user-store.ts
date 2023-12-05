import {defineStore} from 'pinia';
import {api} from 'boot/axios';
import {useRouter} from "vue-router";

export const useUser = defineStore('user', {
  state: () => ({
    username: null,
    userid: -1,
    identity: -1,
    identity_char: '',
    jwt_token: null,
  }),
  actions: {
    async fetchUser() {
      if (this.username != null) return;
      try {
        const response = await api.get('/get_personal_info');
        if (response.data.statusCode !== 200) {
          console.log('pinia', response.data.msg);
          throw new Error('error in fetchUser');
        } else {
          this.userid = response.data.body.userId;
          this.identity = response.data.body.identity
          this.username = response.data.body.name;
          this.jwt_token = response.data.jwt_token;
          switch (this.identity) {
            case 3:
              this.identity_char = 'stu';
              break;
            case 2:
              this.identity_char = 'ta';
              break;
            case 1:
              this.identity_char = 'tea';
              break;
            case 0:
              this.identity_char = 'adm'
            default:
              this.identity_char = 'stu';
          }
        }
      } catch (error) {
        this.reset();
        const router = useRouter();
        await router.push('/login');
        console.log('该部分在pinaia的user-store.ts中');
        console.log(error);
      }
      return this.userid;
    },
    reset(){
      this.userid = -1;
      this.username = null;
      this.identity = -1;
      this.jwt_token = null;
      localStorage.setItem('Token', '');
    }
  },
});
export const useCurrentPageUser = defineStore('currentPageUser', {
  state: () => ({
    person_id: -1
  }),
})
