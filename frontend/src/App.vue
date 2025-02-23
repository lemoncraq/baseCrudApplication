<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title>Fullstack Directory App</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/professions" tag="router-link">Профессии</v-btn>
      <v-btn text to="/departments" tag="router-link">Отделы</v-btn>
      <v-btn text to="/employees" tag="router-link">Сотрудники</v-btn>
    </v-app-bar>
    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script setup lang="ts"></script>

<style scoped></style>
