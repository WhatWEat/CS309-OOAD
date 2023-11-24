import {withDefaults} from 'vue';
import {defineProps} from 'vue-demi';

export interface noticeProps {
  noticeId: number;
  projectId: number;
  title: string;
  content: string;
  creatorId: number;
  createTime?: string;
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
  createTime: "2021-06-01 00:00:00",
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
  name: string,
  projectDescription: string,
  teacherId: number,
  teacherName: string,
  avatar?: File,
}
export const defaultProject: projectProps = {
  projectId: 0,
  name: 'project1',
  projectDescription: 'asdfasf',
  teacherId: 0,
  teacherName: 'tsert1',
}
export interface GroupMember{
  // 用于记录Group中Member的各类属性
  name: string,
  id: number,
  skills?: string[],
}
export interface GroupProps{
  groupId: number,
  groupName: string,
  instructorId: number,
  instructorName: string,
  leaderName: string,
  leaderId: number,
  maxsize: number,
  reportTime: string,
  teamTime: string,
  description?: string|null,
  members: string[],
}
export const defaultGroup: GroupProps = {
  groupId: 0,
  groupName: 'gropup1',
  instructorId: 0,
  instructorName: 'tsert1',
  leaderName: '123',
  leaderId: 0,
  maxsize: 10,
  reportTime: '',
  teamTime: '',
  description: null,
  members: ['123','111','2323'],
}
export interface personProps {
  // 用于记录Person的各类属性
  userid: number,
  identity: number,
  password: string | null,
  phone: string,
  email: string,
  name: string,
  gender: string,
  birthday: string,
  avatar: File,
  programmingSkills: string[]
}
export const defaultPerson: personProps = {
  userid: 0,
  identity: 3,
  password: null,
  phone: '',
  email: '',
  name: '',
  gender: '',
  birthday: '',
  avatar: new File([], ''),
  programmingSkills: ['']
}

export interface assProps {
  assignmentId: number,
  projectId: number,
  title: string,
  fullMark: number,
  description: string,
  type: string,
  creatorId: number,
  deadline: string,
  releaseTime?: string,
  filePaths?: string[],
  requireExtension?: string,
  projectName?: string,
  creatorName?: string,
  files?: File[] | null,
}
