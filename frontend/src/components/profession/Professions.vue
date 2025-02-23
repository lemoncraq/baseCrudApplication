<template>
  <div class="pa-4" style="height: calc(100vh - 64px)">
    <div class="d-flex justify-space-between align-center mb-2">
      <h2>Профессии</h2>
      <v-btn color="primary" @click="openAddModal">Добавить</v-btn>
    </div>
    <v-data-table
      :headers="headers"
      :items="professions"
      :items-per-page="-1"
      :hide-default-footer="true"
      @click:row="(_, { item }) => openEditModal(item)"
      class="elevation-1"
    ></v-data-table>
  </div>
  <ProfessionModal
    v-model="modalOpen"
    :initialData="selectedProfession"
    @save="onSave"
    @delete="onDelete"
  />

  <v-snackbar v-model="snackbar.show" :timeout="3000" :color="snackbar.color">
    {{ snackbar.message }}
  </v-snackbar>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import ProfessionService, { type Profession } from '@/services/ProfessionService'
import ProfessionModal from './components/ProfessionModal.vue'

const professions = ref<Profession[]>([])
const modalOpen = ref(false)
const selectedProfession = ref<Profession | null>(null)

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
    const response = await ProfessionService.getAll()
    professions.value = response
  } catch (error) {
    showSnackbar('Ошибка загрузки данных', 'error')
  }
}

onMounted(fetchData)

const openAddModal = () => {
  selectedProfession.value = null
  modalOpen.value = true
}

const openEditModal = (item: Profession) => {
  selectedProfession.value = { ...item }
  modalOpen.value = true
}

const onSave = async (data: any) => {
  try {
    if (selectedProfession.value) {
      await ProfessionService.update(selectedProfession.value.id, data)
      showSnackbar('Профессия успешно обновлена')
    } else {
      await ProfessionService.create(data)
      showSnackbar('Профессия успешно создана')
    }
    modalOpen.value = false
    fetchData()
  } catch (error) {
    showSnackbar('Ошибка при сохранении', 'error')
  }
}

const onDelete = async () => {
  try {
    if (selectedProfession.value) {
      await ProfessionService.delete(selectedProfession.value.id)
      showSnackbar('Профессия удалена', 'error')
      modalOpen.value = false
      fetchData()
    }
  } catch (error: any) {
    showSnackbar('Ошибка при удалении', 'error')
  }
}
</script>
