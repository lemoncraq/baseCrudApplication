<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>
        <span class="text-h5">{{
          isEdit ? 'Редактировать сотрудника' : 'Добавить сотрудника'
        }}</span>
      </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="localData.fullName"
          label="ФИО *"
          :rules="[(v) => !!v.trim() || 'ФИО обязательно']"
          required
        ></v-text-field>

        <v-text-field v-model="localData.note" label="Примечание"></v-text-field>
        <v-select
          v-model="localData.professionId"
          :items="professions"
          item-title="name"
          item-value="id"
          label="Профессия *"
          :rules="[(v) => !!v || 'Выберите профессию']"
          required
        ></v-select>

        <v-select
          v-model="localData.departmentId"
          :items="departments"
          item-title="name"
          item-value="id"
          label="Отдел *"
          :rules="[(v) => !!v || 'Выберите отдел']"
          required
        ></v-select>
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
import type { Employee } from '@/services/EmployeeService'
import ConfirmDeletionModal from '@/components/deleteModal/ConfirmDeletionModal.vue'

const props = defineProps<{
  modelValue: boolean
  initialData?: Employee | null
  professions: { id: string; name: string }[]
  departments: { id: string; name: string }[]
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'save', data: Omit<Employee, 'id'> | Employee): void
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

const localData = ref({ fullName: '', note: '', professionId: '', departmentId: '' })
const isEdit = ref(false)

const isValidForm = computed(
  () =>
    !!localData.value.fullName.trim() &&
    !!localData.value.professionId &&
    !!localData.value.departmentId,
)

watch(
  () => props.initialData,
  (newVal) => {
    if (newVal) {
      localData.value = {
        fullName: newVal.fullName,
        note: newVal.note,
        professionId: newVal.professionId,
        departmentId: newVal.departmentId,
      }
      isEdit.value = true
    } else {
      localData.value = { fullName: '', note: '', professionId: '', departmentId: '' }
      isEdit.value = false
    }
  },
  { immediate: true },
)

const onSave = () => {
  if (isEdit.value && props.initialData) {
    emits('save', { id: props.initialData.id, ...localData.value })
  } else {
    emits('save', localData.value)
  }
  localData.value = { fullName: '', note: '', professionId: '', departmentId: '' }
  dialog.value = false
}

const onCancel = () => {
  localData.value = { fullName: '', note: '', professionId: '', departmentId: '' }
  dialog.value = false
}

const deleteDialog = ref(false)
const openDeleteConfirm = () => {
  deleteDialog.value = true
}

const onDelete = () => {
  emits('delete')
  localData.value = { fullName: '', note: '', professionId: '', departmentId: '' }
  dialog.value = false
}
</script>
