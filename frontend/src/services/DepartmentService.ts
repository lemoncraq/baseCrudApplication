import type { PaginatedResponse } from '@/types/PaginatedResponse'
import api from './api'

export interface Department {
  id: string
  name: string
  note: string
  parentDepartmentId?: string | null
}

export default class DepartmentService {
  static async getAll(): Promise<Department[]> {
    const response = await api.get(`/departments`)
    return response.data
  }

  static async getById(id: string): Promise<Department> {
    const response = await api.get(`/departments/${id}`)
    return response.data
  }

  static async create(department: Omit<Department, 'id'>): Promise<Department> {
    const response = await api.post('/departments', department)
    return response.data
  }

  static async update(id: string, department: Omit<Department, 'id'>): Promise<Department> {
    const response = await api.put(`/departments/${id}`, department)
    return response.data
  }

  static async delete(id: string): Promise<void> {
    await api.delete(`/departments/${id}`)
  }
}
