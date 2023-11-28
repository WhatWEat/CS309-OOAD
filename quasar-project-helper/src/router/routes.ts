import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{path: '', component: () => import('pages/IndexPages/IndexPage.vue')},
      {path: '/test', component: () => import('pages/TestPage.vue'),
        meta: {
          freeLogin: true
        }
      },
      {path: '/announcements', component: () => import('pages/CheckAnnouncement.vue')},],
  },
  {
    path: '/projects/:projectID',
    component: () => import('layouts/ProjectLayout.vue'),
    children:
      [{path: '', component: () => import('pages/IndexPages/IndexProjectPage.vue')},
        {path: 'group-list', component: () => import('pages/GroupPage/GroupList.vue')},
        {path: 'group-list/:groupId', component: () => import('pages/GroupPage/GroupInfo.vue')},
        {
          path: 'assignment-list', component: () => import('pages/AssignmentPage/Assignment.vue'),
          children: [
            {
              path: 'personal', component: () => import('pages/AssignmentPage/AssignmentStudent_Personal.vue'),
              children: [
                {path: 'assignmentDetail', component: () => import('pages/AssignmentPage/AssignmentDetail.vue')}
              ]
            },
            {
              path: 'group', component: () => import('pages/AssignmentPage/AssignmentsStudent_Group.vue'),
              children: [
                {path: 'assignmentDetail', component: () => import('pages/AssignmentPage/AssignmentDetail.vue')}
              ]
            }
          ]
        },
        {path: 'announcements', component: () => import('pages/CheckAnnouncement.vue')},
        {path: 'assignment-list/:assignmentId', component: () => import('pages/AssignmentPage/AssignmentDetail.vue')},
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
    meta: {
      freeLogin: true
    },
    component: () => import('pages/LoginPages/UserLogin.vue')
  },
  {
    path: '/logout/:userid',
    meta: {
      freeLogin: true
    },
    component: () => import('pages/LoginPages/UserLogout.vue')
  },
  {
    path: '/register',
    meta: {
      freeLogin: true
    },
    component: () => import('pages/LoginPages/UserRegister.vue')
  },
  {
    path: '/forgotpassword',
    component: () => import('pages/ForgotPassword.vue')
  },


  {
    path: '/admin',
    component: () => import('pages/AdminUsers.vue')
  },

  {
    path: '/student/Assignment',
    component: () => import('pages/AssignmentPage/Assignment.vue'),
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
        path: '/group', component: () => import('pages/AssignmentPage/AssignmentsStudent_Group.vue'),
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
