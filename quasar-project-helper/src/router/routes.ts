import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{path: '', component: () => import('pages/IndexPages/IndexPage.vue')},
      {path: '/test', component: () => import('pages/TestPage.vue')}],
  },
  {
    path: '/projects/:projectID',
    component: () => import('layouts/ProjectLayout.vue'),
    children:
      [{path: '', component: () => import('pages/IndexProjectPages.vue')},
        {path: 'group-list', component: () => import('pages/GroupPage/GroupInfo.vue')},
        {path: 'group-list/:groupId', component: () => import('pages/GroupPage/GroupInfo.vue')},
      ],
  },
  {
    path: '/person/:personId',
    component: () => import('layouts/PersonLayout.vue'),
    children: [
      {path: '', component: () => import('pages/PersonPages/PersonInfo.vue')},
      {path: 'projects', component: () => import('pages/PersonPages/PersonProjects.vue')},],
  },
  {
    path: '/login',
    component: () => import('pages/LoginPages/UserLogin.vue')
  },
  {
    path: '/register',
    component: () => import('pages/LoginPages/UserRegister.vue')
  },
  {
    path: '/forgotpassword',
    component: () => import('pages/ForgotPassword.vue')
  },

  //Group——Routers
  {
    path: '/teacher/:teacherId/GroupInfo',
    component: () => import('pages/GroupPage/GroupTeacherPage.vue'),
  },
  {
    path: '/groupInfo/:groupId',
    component: () => import('pages/GroupPage/GroupInfo.vue'),
  },

  {
    path: '/student/GroupInfo',
    component: () => import('pages/GroupPage/GroupStudentPage.vue')
  },
  {
    path: '/student/Assignment',
    component: () => import('pages/AssignmentPage/AssignmentStudent.vue'),
    children: [
      {
        path: '/personal', component: () => import('pages/AssignmentPage/AssignmentStudent_Personal.vue'),
        children: [
          {
            path: '/mengbi1/:assignmentId',
            component: () => import('pages/AssignmentPage/AssignmentDetail.vue')
          }
        ]
      },

      {
        path: '/group', component: () => import('pages/AssignmentPage/AssignmentStudent_group.vue'),
        children: [
          {
            path: '/mengbi2/:assignmentId',
            component: () => import('pages/AssignmentPage/AssignmentDetail.vue')
          }
        ]
      },
    ]
  },
  {
    path: '/teacherAssignment',
    component: () => import('pages/AssignmentPage/AssignmentTeacher.vue')
  },



  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
