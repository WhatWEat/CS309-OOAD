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
  stuView: null | number[];
  toAll: boolean;
  type?: number;
  status?: number;
  projectName?: string;
  groupId?: number;
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
  title?: string;
  projectId: number;
  assignmentId: number;
  submitterId: string;
  grade: number;
  comment: string;
  review: string;
  submittedTime?: string;
  submitterName?: string;
}
withDefaults(defineProps<gradeProps>(), {
  homeworkId: 0,
  projectId: 0,
  assignmentId: 1,
  submitterId:'12110000',
  grade: 90,
  comment: "excellent",
  review: 'wang',
  submittedTime: "2021-06-01 00:00:00",
})

export interface tgradeProps {
  homeworkId: number;
  projectId: number;
  assignmentId: number;
  submitterId: string;
  grade: number;
  comment: string;
  review: string;
  submittedTime?: string;
}
withDefaults(defineProps<tgradeProps>(), {
  homeworkId: 0,
  projectId: 0,
  assignmentId:1,
  submitterId: "12110000",
  grade: 90,
  comment: "excellent",
  review: 'wang',
  submittedTime: "2021-06-01 00:00:00",
})

export interface createProps {
  userId: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<createProps>(), {
  userId: "12110001",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})

export interface resetProps {
  userId: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<resetProps>(), {
  userId: "12110002",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})

export interface freezeProps {
  userId: string;
  password?: string;
  email?: string;
  phone?: string;
}
withDefaults(defineProps<freezeProps>(), {
  userId: "12110003",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})

export interface unfreezeProps {
  userId: string;
  password: string;
  email: string;
  phone: string;
}
withDefaults(defineProps<unfreezeProps>(), {
  userId: "12110004",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
})
export const defaultNotice: noticeProps = {
  noticeId: 0,
  projectId: 0,
  title: '',
  content: '',
  creatorId: 0,
  creatorName: '',
  stuView: null,
  toAll: false,
}

export const defaultGrade: gradeProps = {
  homeworkId: 0,
  projectId: 0,
  assignmentId: 1,
  submitterId:'12110000',
  grade: 90,
  comment: 'excellent',
  review: 'wang',
}

export const defaultTGrade: tgradeProps = {
  homeworkId: 0,
  projectId: 0,
  assignmentId: 1,
  submitterId:'12110000',
  grade: 90,
  comment: 'excellent',
  review: 'wang',
}
export const defaultCreate: createProps = {
  userId: "12110001",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}


export const defaultReset: resetProps = {
  userId: "12110002",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}


export const defaultFreeze: freezeProps = {
  userId: "12110003",
  password: "Aa@123456",
  email: "123456@qq.com",
  phone: "12345678910",
}


export const defaultUnfreeze: unfreezeProps = {
  userId: "12110004",
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
  technicalStack?: string[],
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
  userId: number,
  identity: number,
  password: string | null,
  phone: string,
  email: string,
  name: string,
  gender: string,
  birthday: string,
  avatar: File,
  avatarPath?: string,
  programmingSkills: string[]
}
export const defaultPerson: personProps = {
  userId: 0,
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
