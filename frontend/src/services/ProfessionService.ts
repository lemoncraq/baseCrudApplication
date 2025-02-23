import type { PaginatedResponse } from '@/types/PaginatedResponse'
import api from './api'

export interface Profession {
  id: string
  name: string
  note: string
}

export default class ProfessionService {
  static async getAll(): Promise<Profession[]> {
    const response = await api.get(`/professions`)
    return response.data
  }

  static async getById(id: string): Promise<Profession> {
    const response = await api.get(`/professions/${id}`)
    return response.data
  }

  static async create(profession: Omit<Profession, 'id'>): Promise<Profession> {
    const response = await api.post('/professions', profession)
    return response.data
  }

  static async update(id: string, profession: Omit<Profession, 'id'>): Promise<Profession> {
    const response = await api.put(`/professions/${id}`, profession)
    return response.data
  }

  static async delete(id: string): Promise<void> {
    await api.delete(`/professions/${id}`)
  }
}
