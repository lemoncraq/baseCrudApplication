<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>
        <span class="text-h5">{{ isEdit ? 'Редактировать профессию' : 'Добавить профессию' }}</span>
      </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="localData.name"
          label="Наименование *"
          :rules="[(v) => !!v.trim() || 'Наименование обязательно']"
          required
        ></v-text-field>
        <v-text-field v-model="localData.note" label="Примечание"></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" :disabled="!isValidForm" @click="onSave">Сохранить</v-btn>
        <v-btn color="secondary" @click="onCancel">Отмена</v-btn>
        <v-btn v-if="isEdit" color="error" @click="openDeleteConfirm">Удалить</v-btn>
      </v-card-actions>
    </v-card>

    <ConfirmDeletionModal v-model="deleteDialog" @confirm="onDelete" />
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, computed } from 'vue'
import type { Profession } from '@/services/ProfessionService'
import ConfirmDeletionModal from '@/components/deleteModal/ConfirmDeletionModal.vue'

const props = defineProps<{
  modelValue: boolean
  initialData?: Profession | null
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'save', data: Omit<Profession, 'id'> | Profession): void
  (e: 'delete'): void
}>()

const dialog = ref(props.modelValue)
watch(
  () => props.modelValue,
  (newVal) => {
    dialog.value = newVal
  },
)
watch(dialog, (newVal) => {
  emits('update:modelValue', newVal)
})

const localData = ref({ name: '', note: '' })
const isEdit = ref(false)

const isValidForm = computed(() => !!localData.value.name.trim())

watch(
  () => props.initialData,
  (newVal) => {
    if (newVal) {
      localData.value = { name: newVal.name, note: newVal.note }
      isEdit.value = true
    } else {
      localData.value = { name: '', note: '' }
      isEdit.value = false
    }
  },
  { immediate: true },
)

const deleteDialog = ref(false)
const openDeleteConfirm = () => {
  deleteDialog.value = true
}

const onSave = () => {
  if (isEdit.value && props.initialData) {
    emits('save', { id: props.initialData.id, ...localData.value })
  } else {
    emits('save', localData.value)
  }
  localData.value = { name: '', note: '' }
  dialog.value = false
}

const onCancel = () => {
  localData.value = { name: '', note: '' }
  dialog.value = false
}

const onDelete = () => {
  emits('delete')
  localData.value = { name: '', note: '' }
  dialog.value = false
}
</script>
