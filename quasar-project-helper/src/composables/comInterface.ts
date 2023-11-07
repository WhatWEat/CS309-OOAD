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
  projectId: number,
  projectName: string,
  projectDescription: string,
  teacherId: number,
  teacherName: string,
}
