// 在一个文件中，例如 useUserStore.ts
import { useUser } from 'stores/user-store';
import { storeToRefs } from 'pinia';
import { Ref } from 'vue';

export function useUserStore(): { username: Ref<string | null>, userid: Ref<number | null>, identity: Ref<number | null> } {
  const userStore = useUser();
  userStore.fetchUser();
  return storeToRefs(userStore);
}
