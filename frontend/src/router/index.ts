import { createRouter, createWebHistory } from 'vue-router'
import Professions from '@/components/Professions.vue'
import Departments from '@/components/Departments.vue'
import Employees from '@/components/Employees.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    redirect: '/professions',
  },
  {
    path: '/professions',
    name: 'Professions',
    component: Professions,
  },
  {
    path: '/departments',
    name: 'Departments',
    component: Departments,
  },
  {
    path: '/employees',
    name: 'Employees',
    component: Employees,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
