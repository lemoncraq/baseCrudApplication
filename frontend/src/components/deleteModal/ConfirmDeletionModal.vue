<template>
  <v-dialog v-model="dialog" max-width="400">
    <v-card>
      <v-card-title class="headline">Подтверждение удаления</v-card-title>
      <v-card-text> Вы уверены, что хотите удалить эту запись? </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="secondary" text @click="cancel">Отмена</v-btn>
        <v-btn color="error" text @click="confirm">Удалить</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue'

const props = defineProps<{ modelValue: boolean }>()
const emits = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'confirm'): void
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

const confirm = () => {
  emits('confirm')
  dialog.value = false
}

const cancel = () => {
  dialog.value = false
}
</script>
