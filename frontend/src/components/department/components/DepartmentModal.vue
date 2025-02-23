<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>
        <span class="text-h5">{{ isEdit ? 'Редактировать отдел' : 'Добавить отдел' }}</span>
      </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="localData.name"
          label="Наименование *"
          :rules="[(v) => !!v.trim() || 'Наименование обязательно']"
          required
        ></v-text-field>

        <v-text-field v-model="localData.note" label="Примечание"></v-text-field>
        <v-select
          v-model="localData.parentDepartmentId"
          :disabled="isEdit"
          :items="parentDepartments"
          item-title="name"
          item-value="id"
          label="Родительский отдел"
          clearable
          hint="Родительский отдел нельзя изменить при редактировании"
          :persistent-hint="isEdit"
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
import { ref, watch, defineProps, defineEmits, computed, onMounted } from 'vue'
import type { Department } from '@/services/DepartmentService'
import ConfirmDeletionModal from '@/components/deleteModal/ConfirmDeletionModal.vue'

const props = defineProps<{
  modelValue: boolean
  initialData?: Department | null
  departments: { id: string; name: string; parentDepartmentId?: string | null }[]
}>()

const parentDepartments = computed(() => props.departments.filter((dep) => !dep.parentDepartmentId))

const emits = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'save', data: Omit<Department, 'id'> | Department): void
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

const localData = ref({ name: '', note: '', parentDepartmentId: null as string | null })
const isEdit = ref(false)

const isValidForm = computed(() => !!localData.value.name.trim())

watch(
  () => props.initialData,
  (newVal) => {
    if (newVal) {
      localData.value = {
        name: newVal.name,
        note: newVal.note,
        parentDepartmentId: newVal.parentDepartmentId || null,
      }
      isEdit.value = true
    } else {
      localData.value = { name: '', note: '', parentDepartmentId: null }
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
  localData.value = { name: '', note: '', parentDepartmentId: null as string | null }
  dialog.value = false
}

const onCancel = () => {
  localData.value = { name: '', note: '', parentDepartmentId: null as string | null }
  dialog.value = false
}

const deleteDialog = ref(false)
const openDeleteConfirm = () => {
  deleteDialog.value = true
}

const onDelete = () => {
  emits('delete')
  localData.value = { name: '', note: '', parentDepartmentId: null as string | null }
  dialog.value = false
}
</script>
