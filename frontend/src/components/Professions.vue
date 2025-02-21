<template>
  <v-container>
    <v-card>
      <v-card-title>Профессии</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="saveProfession">
          <v-text-field v-model="form.name" label="Наименование" required></v-text-field>
          <v-text-field v-model="form.note" label="Примечание"></v-text-field>
          <v-btn type="submit" color="primary">
            {{ editingId ? 'Обновить' : 'Создать' }}
          </v-btn>
          <v-btn v-if="editingId" color="secondary" @click="cancelEdit"> Отмена </v-btn>
        </v-form>
        <v-data-table :headers="headers" :items="professions" class="mt-4">
          <template #item.actions="{ item }">
            <v-btn icon @click="editProfession(item)">
              <v-icon>mdi-pencil</v-icon>
            </v-btn>
            <v-btn icon @click="deleteProfession(item.id)">
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
import ProfessionService, { type Profession } from '@/services/ProfessionService'

const professions = ref<Profession[]>([])
const form = ref({ name: '', note: '' })
const editingId = ref<string | null>(null)

const headers = [
  { text: 'ID', value: 'id' },
  { text: 'Наименование', value: 'name' },
  { text: 'Примечание', value: 'note' },
  { text: 'Действия', value: 'actions', sortable: false },
]

const fetchProfessions = async () => {
  const response = await ProfessionService.getAll(0, 100)
  professions.value = response.content
}

const saveProfession = async () => {
  if (editingId.value) {
    const updated = await ProfessionService.update(editingId.value, form.value)
    const index = professions.value.findIndex((p) => p.id === editingId.value)
    if (index !== -1) professions.value[index] = updated
    editingId.value = null
  } else {
    const created = await ProfessionService.create(form.value)
    professions.value.push(created)
  }
  form.value = { name: '', note: '' }
}

const editProfession = (profession: Profession) => {
  form.value = { name: profession.name, note: profession.note }
  editingId.value = profession.id
}

const cancelEdit = () => {
  editingId.value = null
  form.value = { name: '', note: '' }
}

const deleteProfession = async (id: string) => {
  await ProfessionService.delete(id)
  professions.value = professions.value.filter((p) => p.id !== id)
}

onMounted(fetchProfessions)
</script>
