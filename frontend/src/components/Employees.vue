<template>
  <v-container>
    <v-card>
      <v-card-title>Сотрудники</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="saveEmployee">
          <v-text-field v-model="form.fullName" label="ФИО" required></v-text-field>
          <v-text-field v-model="form.note" label="Примечание"></v-text-field>
          <v-select
            v-model="form.professionId"
            :items="professions"
            item-text="name"
            item-value="id"
            label="Профессия"
            required
          ></v-select>
          <v-select
            v-model="form.departmentId"
            :items="departments"
            item-text="name"
            item-value="id"
            label="Отдел"
            required
          ></v-select>
          <v-btn type="submit" color="primary">
            {{ editingId ? 'Обновить' : 'Создать' }}
          </v-btn>
          <v-btn v-if="editingId" color="secondary" @click="cancelEdit"> Отмена </v-btn>
        </v-form>
        <v-data-table :headers="headers" :items="employees" class="mt-4">
          <template #item.actions="{ item }">
            <v-btn icon @click="editEmployee(item)">
              <v-icon>mdi-pencil</v-icon>
            </v-btn>
            <v-btn icon @click="deleteEmployee(item.id)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import EmployeeService, { type Employee } from '@/services/EmployeeService'
import ProfessionService from '@/services/ProfessionService'
import DepartmentService from '@/services/DepartmentService'

const employees = ref<Employee[]>([])
const professions = ref<{ id: string; name: string }[]>([])
const departments = ref<{ id: string; name: string }[]>([])
const form = ref({ fullName: '', note: '', professionId: '', departmentId: '' })
const editingId = ref<string | null>(null)

const headers = [
  { text: 'ID', value: 'id' },
  { text: 'ФИО', value: 'fullName' },
  { text: 'Профессия', value: 'professionName' },
  { text: 'Отдел', value: 'departmentName' },
  { text: 'Примечание', value: 'note' },
  { text: 'Действия', value: 'actions', sortable: false },
]

const fetchEmployees = async () => {
  const response = await EmployeeService.getAll(0, 100)
  employees.value = response.content
}

const fetchProfessions = async () => {
  const response = await ProfessionService.getAll(0, 100)
  professions.value = response.content.map((p: any) => ({ id: p.id, name: p.name }))
}

const fetchDepartments = async () => {
  const response = await DepartmentService.getAll(0, 100)
  departments.value = response.content.map((d: any) => ({ id: d.id, name: d.name }))
}

const saveEmployee = async () => {
  if (editingId.value) {
    const updated = await EmployeeService.update(editingId.value, form.value)
    const index = employees.value.findIndex((e) => e.id === editingId.value)
    if (index !== -1) employees.value[index] = updated
    editingId.value = null
  } else {
    const created = await EmployeeService.create(form.value)
    employees.value.push(created)
  }
  form.value = { fullName: '', note: '', professionId: '', departmentId: '' }
}

const editEmployee = (employee: Employee) => {
  form.value = {
    fullName: employee.fullName,
    note: employee.note,
    professionId: employee.professionId,
    departmentId: employee.departmentId,
  }
  editingId.value = employee.id
}

const cancelEdit = () => {
  editingId.value = null
  form.value = { fullName: '', note: '', professionId: '', departmentId: '' }
}

const deleteEmployee = async (id: string) => {
  await EmployeeService.delete(id)
  employees.value = employees.value.filter((e) => e.id !== id)
}

onMounted(async () => {
  await fetchProfessions()
  await fetchDepartments()
  await fetchEmployees()
})
</script>
