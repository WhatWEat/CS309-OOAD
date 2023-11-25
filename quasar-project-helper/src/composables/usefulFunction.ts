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

export function formatDateString(isoString: string) {
  const date = new Date(isoString);

  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();

  return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
}

export function useProjectId() {
  const route = useRoute();
  console.log("route",route)

  const projectId = route.params.projectID as string;
  return Number(projectId);
}

export function usePersonId() {
  const route = useRoute();
  const personId = route.params.personID as string;
  return Number(personId);
}

export async function getAvatarUrl() {
  try {
    const res = await api.get(`/get_avatar`, {responseType: 'arraybuffer'});
    const blob = new Blob([res.data], {type: 'image/jpeg'});
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = function () {
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
export async function getAvatarUrlById(id: number) {
  try {
    if (localStorage.getItem(`avatar_${id}`) && localStorage.getItem(`avatar_${id}_time`)) {
      const now = new Date();
      const last = new Date(localStorage.getItem(`avatar_${id}_time`) as string);
      if (now.getTime() - last.getTime() < 1000 * 60 * 2) {
        return localStorage.getItem(`avatar_${id}`);
      }
    }
    const res = await api.get(`/get_avatar/${id}`, {responseType: 'arraybuffer'});
    const blob = new Blob([res.data], {type: 'image/jpeg'});
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = function () {
      const base64data = reader.result;
      if (typeof base64data === "string") {
        localStorage.setItem(`avatar_${id}`, base64data);
        localStorage.setItem(`avatar_${id}_time`, new Date().toISOString());
      }
    }
    return localStorage.getItem(`avatar_${id}`);
  } catch (error) {
    console.error("Failed to get avatar URL", error);
    return null;
  }
}

