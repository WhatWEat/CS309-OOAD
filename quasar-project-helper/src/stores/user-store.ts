import { defineStore } from 'pinia';
export const useUser = defineStore('user', {
  state: () => ({
    username: null,
    userid: -1,
  }),
  actions: {
    async fetchUser() {
      if(this.username != null) return;
      try{
        const response = await fetch('https://jsonplaceholder.typicode.com/users/1');
        const data = await response.json();
        this.username = data.name;
        this.userid = data.id;
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
