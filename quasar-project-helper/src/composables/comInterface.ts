import {withDefaults} from 'vue';
import {defineProps} from 'vue-demi';

export interface noticeProps {
  noticeId: number;
  projectId: number;
  title: string;
  content: string;
  creatorId: number;
  stuView: null | string;
  toAll: boolean;
}
withDefaults(defineProps<noticeProps>(), {
  noticeId: 0,
  projectId: 0,
  title: "project1",
  content: "asdfasf",
  creatorId: 0,
  stuView: null,
  toAll: false,
})
export const defaultNotice: noticeProps = {
  noticeId: 0,
  projectId: 0,
  title: 'project1',
  content: 'asdfasf',
  creatorId: 0,
  stuView: null,
  toAll: false,
}

export interface projectProps {
  // 用于记录Project的各类属性
  projectId: number,
  projectName: string,
  projectDescription: string,
  teacherId: number,
  teacherName: string,
}

export interface GroupMember{
  // 用于记录Group中Member的各类属性
  name: string,
  id: number,
  skills: string[],
}
