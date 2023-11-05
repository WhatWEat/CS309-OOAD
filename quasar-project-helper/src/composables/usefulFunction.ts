export function truncate(str: string, num = 15, endSymbol = '...'): string {
  // 超过多少长度就截断字符串，截断和面加上什么符号
  // 默认截断15个字符，加上...
  if (str.length > num) {
    return str.substring(0, num) + endSymbol;
  }
  return str;
}

import {useRoute} from 'vue-router';

export function useProjectId() {
  const route = useRoute();
  const projectId = route.params.projectID as string;
  return Number(projectId);
}
