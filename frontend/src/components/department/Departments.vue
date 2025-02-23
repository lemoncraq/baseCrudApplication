<template>
  <div class="pa-4" style="height: calc(100vh - 64px)">
    <div class="d-flex justify-space-between align-center mb-2">
      <h2>Отделы</h2>
      <v-btn color="primary" @click="openAddModal">Добавить</v-btn>
    </div>
    <v-data-table
      :headers="headers"
      :items="groupedDepartments"
      :items-per-page="-1"
      :hide-default-footer="true"
      class="elevation-1"
      @click:row="(_, { item }) => openEditModal(item)"
    >
      <template #item.name="{ item }">
        <span :style="item.isParent ? 'font-weight: bold' : 'margin-left: 20px'">
          {{ item.name }}
        </span>
      </template>
      <template #item.parentDepartmentName="{ item }">
        <span v-if="!item.isParent">{{ item.parentDepartmentName }}</span>
      </template>
    </v-data-table>
  </div>
  <DepartmentModal
    v-model="modalOpen"
    :initialData="selectedDepartment"
    :departments="departmentsOptions"
    @save="onSave"
    @delete="onDelete"
  />

  <v-snackbar v-model="snackbar.show" :timeout="3000" :color="snackbar.color">
    {{ snackbar.message }}
  </v-snackbar>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import DepartmentService, { type Department } from '@/services/DepartmentService'
import DepartmentModal from './components/DepartmentModal.vue'

const departments = ref<Department[]>([])
const modalOpen = ref(false)
const selectedDepartment = ref<Department | null>(null)

const snackbar = ref({ show: false, message: '', color: 'success' })

const showSnackbar = (message: string, color: string = 'success') => {
  snackbar.value = { show: true, message, color }
}

const headers = [
  { title: 'Наименование', key: 'name' },
  { title: 'Примечание', key: 'note' },
]

const fetchData = async () => {
  try {
    const response = await DepartmentService.getAll()
    departments.value = response
  } catch (error) {
    showSnackbar('Ошибка загрузки данных', 'error')
  }
}

onMounted(fetchData)

const groupedDepartments = computed(() => {
  const result: any[] = []
  const parents = departments.value.filter((dep) => !dep.parentDepartmentId)
  parents.forEach((parent) => {
    result.push({ ...parent, isParent: true, parentDepartmentName: '' })
    const children = departments.value.filter((dep) => dep.parentDepartmentId === parent.id)
    children.forEach((child) => {
      result.push({
        ...child,
        isParent: false,
        parentDepartmentName: parent.name,
      })
    })
  })
  return result
})

const departmentsOptions = computed(() =>
  departments.value.map((dep) => ({
    id: dep.id,
    name: dep.name,
    parentDepartmentId: dep.parentDepartmentId,
  })),
)

const openAddModal = () => {
  selectedDepartment.value = null
  modalOpen.value = true
}

const openEditModal = (item: Department) => {
  selectedDepartment.value = { ...item }
  modalOpen.value = true
}

const onSave = async (data: any) => {
  try {
    if (selectedDepartment.value) {
      await DepartmentService.update(selectedDepartment.value.id, data)
      showSnackbar('Отдел успешно обновлен')
    } else {
      await DepartmentService.create(data)
      showSnackbar('Отдел успешно создан')
    }
    modalOpen.value = false
    fetchData()
  } catch (error) {
    showSnackbar('Ошибка при сохранении', 'error')
  }
}

const onDelete = async () => {
  try {
    if (selectedDepartment.value) {
      await DepartmentService.delete(selectedDepartment.value.id)
      showSnackbar('Отдел удален', 'error')
      modalOpen.value = false
      fetchData()
    }
  } catch (error: any) {
    showSnackbar('Ошибка при удалении', 'error')
  }
}
</script>
