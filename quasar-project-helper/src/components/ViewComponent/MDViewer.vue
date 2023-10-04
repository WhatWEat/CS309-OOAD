<template>
  <div id="box"></div>
</template>

<script setup lang="ts">
import { onMounted,ref } from "vue";
import {Editor, EditorViewMode, withUndoRedo} from "web-editor-markdown";

let text = ref<string>('# CS309-OOAD-ProjectHelper\n');
let editor:Editor;
onMounted(() => {
  editor = new Editor(document.querySelector("#box") as HTMLElement);
  editor = withUndoRedo(editor);
  editor.insertTextAtCursor(text.value);
  editor.switchViewMode(EditorViewMode.PREVIEW)
});

</script>

<style scoped></style>

