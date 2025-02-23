<template>
  <div class="pa-4" style="height: calc(100vh - 64px)">
    <div class="d-flex justify-space-between align-center mb-2">
      <h2>Сотрудники</h2>
      <v-btn color="primary" @click="openAddModal">Добавить</v-btn>
    </div>
    <v-data-table
      :headers="headers"
      :items="employeesWithNames"
      :items-per-page="-1"
      :hide-default-footer="true"
      class="elevation-1"
      @click:row="(_, { item }) => openEditModal(item)"
    ></v-data-table>
  </div>
  <EmployeeModal
    v-model="modalOpen"
    :initialData="selectedEmployee"
    :professions="professionsOptions"
    :departments="departmentsOptions"
    @save="onSave"
    @delete="onDelete"
  />

  <v-snackbar v-model="snackbar.show" :timeout="3000" :color="snackbar.color">
    {{ snackbar.message }}
  </v-snackbar>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import EmployeeService, { type Employee } from '@/services/EmployeeService'
import ProfessionService from '@/services/ProfessionService'
import DepartmentService from '@/services/DepartmentService'
import EmployeeModal from './components/EmployeeModal.vue'

const employees = ref<Employee[]>([])
const professions = ref<{ id: string; name: string }[]>([])
const departments = ref<{ id: string; name: string }[]>([])
const modalOpen = ref(false)
const selectedEmployee = ref<Employee | null>(null)

const snackbar = ref({ show: false, message: '', color: 'success' })

const showSnackbar = (message: string, color: string = 'success') => {
  snackbar.value = { show: true, message, color }
}

const headers = [
  { title: 'ФИО', key: 'fullName' },
  { title: 'Профессия', key: 'professionName' },
  { title: 'Отдел', key: 'departmentName' },
  { title: 'Примечание', key: 'note' },
]

const fetchEmployees = async () => {
  try {
    const response = await EmployeeService.getAll()
    employees.value = response
  } catch (error) {
    showSnackbar('Ошибка загрузки данных', 'error')
  }
}

onMounted(fetchEmployees)

const openAddModal = () => {
  selectedEmployee.value = null
  modalOpen.value = true
}

const openEditModal = (item: Employee) => {
  selectedEmployee.value = { ...item }
  modalOpen.value = true
}

const onSave = async (data: any) => {
  try {
    if (selectedEmployee.value) {
      await EmployeeService.update(selectedEmployee.value.id, data)
      showSnackbar('Исполнитель успешно обновлен')
    } else {
      await EmployeeService.create(data)
      showSnackbar('Исполнитель успешно создан')
    }
    modalOpen.value = false
    fetchEmployees()
  } catch (error) {
    showSnackbar('Ошибка при сохранении', 'error')
  }
}

const onDelete = async () => {
  try {
    if (selectedEmployee.value) {
      await EmployeeService.delete(selectedEmployee.value.id)
      showSnackbar('Исполнитель удален', 'error')
      modalOpen.value = false
      fetchEmployees()
    }
  } catch (error: any) {
    showSnackbar('Ошибка при удалении', 'error')
  }
}

const employeesWithNames = computed(() => {
  return employees.value.map((emp) => ({
    ...emp,
    professionName: professions.value.find((p) => p.id === emp.professionId)?.name || 'Неизвестно',
    departmentName: departments.value.find((d) => d.id === emp.departmentId)?.name || 'Неизвестно',
  }))
})

const professionsOptions = computed(() => professions.value)
const departmentsOptions = computed(() => departments.value)

const fetchProfessions = async () => {
  const res = await ProfessionService.getAll()
  professions.value = res.map((p) => ({ id: p.id, name: p.name }))
}

const fetchDepartments = async () => {
  const res = await DepartmentService.getAll()
  departments.value = res.map((d) => ({ id: d.id, name: d.name }))
}

onMounted(async () => {
  await fetchProfessions()
  await fetchDepartments()
})
</script>
