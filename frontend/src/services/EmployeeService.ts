import type { PaginatedResponse } from '@/types/PaginatedResponse'
import api from './api'

export interface Employee {
  id: string
  fullName: string
  note: string
  professionId: string
  departmentId: string
  professionName?: string
  departmentName?: string
}

export default class EmployeeService {
  static async getAll(page = 0, size = 10): Promise<PaginatedResponse<Employee>> {
    const response = await api.get(`/employees?page=${page}&size=${size}`)
    return response.data
  }

  static async getById(id: string): Promise<Employee> {
    const response = await api.get(`/employees/${id}`)
    return response.data
  }

  static async create(employee: Omit<Employee, 'id'>): Promise<Employee> {
    const response = await api.post('/employees', employee)
    return response.data
  }

  static async update(id: string, employee: Omit<Employee, 'id'>): Promise<Employee> {
    const response = await api.put(`/employees/${id}`, employee)
    return response.data
  }

  static async delete(id: string): Promise<void> {
    await api.delete(`/employees/${id}`)
  }
}
