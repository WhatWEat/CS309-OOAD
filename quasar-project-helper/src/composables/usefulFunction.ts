import {api} from "boot/axios";
import {useRoute} from 'vue-router';

export function truncate(str: string, num = 15, endSymbol = '...'): string {
  // 超过多少长度就截断字符串，截断和面加上什么符号
  // 默认截断15个字符，加上...
  if (str.length > num) {
    return str.substring(0, num) + endSymbol;
  }
  return str;
}

export function useProjectId() {
  const route = useRoute();
  const projectId = route.params.projectID as string;
  return Number(projectId);
}

export function usePersonId() {
  const route = useRoute();
  const personId = route.params.personID as string;
  return Number(personId);
}

export async function getAvatarUrl(){
  try {
    const res = await api.get(`/get_avatar`, { responseType: 'arraybuffer' });
    const blob = new Blob([res.data], { type: 'image/jpeg' });
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = function() {
      const base64data = reader.result;
      if (typeof base64data === "string") {
        localStorage.setItem('avatar', base64data);
      }
    }
    return localStorage.getItem('avatar');
  } catch (error) {
    console.error("Failed to get avatar URL", error);
    return null;
  }
}

