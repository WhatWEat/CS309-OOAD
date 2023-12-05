import {api} from "boot/axios";
import {useRoute} from 'vue-router';
import {isArray} from "util";
import Vue, { ComponentOptions } from 'vue'
import * as echarts from "echarts";

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

export function formatDateStringPro(isoString: string) {
  const date = new Date(isoString);

  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();

  //把秒数也要算进去啊
  return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`;
}

export function useProjectId() {
  const route = useRoute();

  const projectId = route.params.projectID as string;
  return Number(projectId);
}

export function usePersonId() {
  const route = useRoute();

  const personId = route.params.personId as string;
  return Number(personId);
}

export async function getAvatarUrl() {
  try {
    if (localStorage.getItem(`avatar`) && localStorage.getItem(`avatar_time`)) {
      const now = new Date();
      const last = new Date(localStorage.getItem(`avatar_time`) as string);
      if (now.getTime() - last.getTime() < 1000 * 60) {
        // console.log("avatar from cache")
        return localStorage.getItem(`avatar`);
      }
    }
    const res = (await api.get(`/get_personal_info`)).data.body.avatarPath;
    localStorage.setItem(`avatar`, res);
    localStorage.setItem(`avatar_time`, new Date().toISOString());
    return localStorage.getItem('avatar');
  } catch (error) {

    console.error("Failed to get avatar URL", error);
    return "https://cdn.quasar.dev/img/boy-avatar.png";
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
    const res = (await api.get(`/get_personal_info/${id}`)).data.body.avatarPath;
    localStorage.setItem(`avatar_${id}`, res);
    localStorage.setItem(`avatar_${id}_time`, new Date().toISOString());
    return localStorage.getItem(`avatar_${id}`);
  } catch (error) {
    console.error("Failed to get avatar URL", error);
    return "https://cdn.quasar.dev/img/boy-avatar.png";
  }
}
export function getDownloadBlob(blobFile: Blob, fileName: string) {
  const blobUrl = URL.createObjectURL(blobFile);

  const link = document.createElement('a');
  link.href = blobUrl;
  link.download = fileName;
  document.body.appendChild(link);
  link.click(); // 模拟点击

  document.body.removeChild(link);
  URL.revokeObjectURL(blobUrl);
}
// download chart
export function downloadChart(myChart: echarts.ECharts | null) {
  if (!myChart) return;

  const url = myChart.getDataURL({
    type: 'png',
    pixelRatio: 2,
    backgroundColor: '#fff'
  });

  const link = document.createElement('a');
  link.href = url;
  link.download = 'chart.png';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
//******************Li weihao******************//
export function merger(key: object, value: []): object;
export function merger(key: string, value: string): object;
export function merger(key: any, value: any): object {
  const obj: Record<string, any> = {};

  if (Array.isArray(key)) {
    // 处理 key 为数组的情况
    for (let i = 0; i < key.length; i++) {
      obj[key[i]] = value[i];
    }
  } else if (typeof key === 'string') {
    // 处理 key 为字符串的情况
    obj[key] = value;
  } else {
    return {};
    // throw new Error("Invalid argument types");
  }

  return obj;
}

export async function getUserData() {
  const userDate = {
    username: null,
    userid: -1,
    identity: -1,
    jwt_token: null,
  }
  try {
    const response = await api.get('/get_personal_info');
    if (response.data.statusCode !== 200) {
      throw new Error('error in fetchUser');
    } else {
      userDate.userid = response.data.body.userId;
      userDate.identity = response.data.body.identity
      userDate.username = response.data.body.name;
      userDate.jwt_token = response.data.jwt_token;
    }
    return userDate;
  } catch (error) {
    userDate.userid = -1;
    userDate.username = null;
    userDate.identity = -1;
    userDate.jwt_token = null;
    return userDate;
  }
}

// interface RoundedComponent {
//   width: number;
//   height: number;
// }

// export const roundedMixin: ComponentOptions<RoundedComponent> = {
//   computed: {
//     roundedStyle() {
//       const radius = Math.min(this.width, this.height) / 10;
//       return {
//         borderRadius: `${radius}px`,
//       };
//     },
//   },
// };



