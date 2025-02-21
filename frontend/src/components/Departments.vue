<template>
  <v-container>
    <v-card>
      <v-card-title>Отделы</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="saveDepartment">
          <v-text-field v-model="form.name" label="Наименование" required></v-text-field>
          <v-text-field v-model="form.note" label="Примечание"></v-text-field>
          <v-select
            v-model="form.parentDepartmentId"
            :items="departments"
            item-text="name"
            item-value="id"
            label="Родительский отдел"
            clearable
          ></v-select>
          <v-btn type="submit" color="primary">
            {{ editingId ? 'Обновить' : 'Создать' }}
          </v-btn>
          <v-btn v-if="editingId" color="secondary" @click="cancelEdit"> Отмена </v-btn>
        </v-form>
        <v-data-table :headers="headers" :items="departments" class="mt-4">
          <template #item.actions="{ item }">
            <v-btn icon @click="editDepartment(item)">
              <v-icon>mdi-pencil</v-icon>
            </v-btn>
            <v-btn icon @click="deleteDepartment(item.id)">
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
import DepartmentService, { type Department } from '@/services/DepartmentService'

const departments = ref<Department[]>([])
const form = ref({ name: '', note: '', parentDepartmentId: null as string | null })
const editingId = ref<string | null>(null)

const headers = [
  { text: 'ID', value: 'id' },
  { text: 'Наименование', value: 'name' },
  { text: 'Примечание', value: 'note' },
  { text: 'Родительский отдел', value: 'parentDepartmentId' },
  { text: 'Действия', value: 'actions', sortable: false },
]

const fetchDepartments = async () => {
  const response = await DepartmentService.getAll(0, 100)
  departments.value = response.content
}

const saveDepartment = async () => {
  if (editingId.value) {
    const updated = await DepartmentService.update(editingId.value, form.value)
    const index = departments.value.findIndex((d) => d.id === editingId.value)
    if (index !== -1) departments.value[index] = updated
    editingId.value = null
  } else {
    const created = await DepartmentService.create(form.value)
    departments.value.push(created)
  }
  form.value = { name: '', note: '', parentDepartmentId: null }
}

const editDepartment = (dept: Department) => {
  form.value = {
    name: dept.name,
    note: dept.note,
    parentDepartmentId: dept.parentDepartmentId || null,
  }
  editingId.value = dept.id
}

const cancelEdit = () => {
  editingId.value = null
  form.value = { name: '', note: '', parentDepartmentId: null }
}

const deleteDepartment = async (id: string) => {
  await DepartmentService.delete(id)
  departments.value = departments.value.filter((d) => d.id !== id)
}

onMounted(fetchDepartments)
</script>
