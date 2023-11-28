import {withDefaults} from 'vue';
import {defineProps} from 'vue-demi';

export interface noticeProps {
  noticeId: number;
  projectId: number;
  title: string;
  content: string;
  creatorId: number;
  creatorName?: string;
  createTime?: string;
  stuView: null | string;
  toAll: boolean;
}
withDefaults(defineProps<noticeProps>(), {
  noticeId: 0,
  projectId: 0,
  title: "project1",
  content: "11111111111111111111一共20个1",
  creatorId: 0,
  creatorName: "tsert1",
  stuView: null,
  createTime: "2021-06-01 00:00:00",
  toAll: false,
})

export interface gradeProps {
  homeworkId: number;
  projectId: number;
  name: string;
  grade: number;
  contribution: string;
  comment: string;
  reviewerId: number;
  reviewerName?: string;
  createTime?: string;
}
withDefaults(defineProps<gradeProps>(), {
  homeworkId: 0,
  projectId: 0,
  name: "assignment1",
  grade: 90,
  contribution: "40%",
  comment: "excellent",
  reviewerId: 0,
  reviewerName: "wang",
  createTime: "2021-06-01 00:00:00",
})

export interface tgradeProps {
  homeworkId: number;
  projectId: number;
  name: string;
  studentID: string;
  grade: number;
  comment: string;
  reviewerId: number;
  reviewerName?: string;
  createTime?: string;
}
withDefaults(defineProps<tgradeProps>(), {
  homeworkId: 0,
  projectId: 0,
  name: "assignment1",
  studentID: "12110000",
  grade: 90,
  comment: "excellent",
  reviewerId: 0,
  reviewerName: "wang",
  createTime: "2021-06-01 00:00:00",
})

export interface createProps {
  studentID: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<createProps>(), {
  studentID: "12110001",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})

export interface resetProps {
  studentID: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<resetProps>(), {
  studentID: "12110002",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})

export interface freezeProps {
  studentID: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<freezeProps>(), {
  studentID: "12110003",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})

export interface unfreezeProps {
  studentID: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<unfreezeProps>(), {
  studentID: "12110004",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})
export const defaultNotice: noticeProps = {
  noticeId: 0,
  projectId: 0,
  title: 'project1',
  content: '11111111111111111111一共20个1',
  creatorId: 0,
  creatorName: 'Andy',
  stuView: null,
  toAll: false,
}

export const defaultGrade: gradeProps = {
  homeworkId: 0,
  projectId: 0,
  name: 'assignment1',
  grade: 90,
  contribution: '40%',
  comment: 'excellent',
  reviewerName: 'wang',
  reviewerId: 0,
}

export const defaultTGrade: tgradeProps = {
  homeworkId: 0,
  projectId: 0,
  name: 'assignment1',
  studentID:'12110000',
  grade: 90,
  comment: 'excellent',
  reviewerName: 'wang',
  reviewerId: 0,
}
export const defaultCreate: createProps = {
  studentID: "12110001",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}


export const defaultReset: resetProps = {
  studentID: "12110002",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}


export const defaultFreeze: freezeProps = {
  studentID: "12110003",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}


export const defaultUnfreeze: unfreezeProps = {
  studentID: "12110004",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}
export interface projectProps {
  // 用于记录Project的各类属性
  projectId: number,
  name: string,
  description: string,
  teacherId: number,
  teacherName: string,
  avatar?: File,
}
export const defaultProject: projectProps = {
  projectId: 0,
  name: 'project1',
  description: 'asdfasf',
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
  memberIds?: number[],
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
  memberIds: [1,2,3],
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
