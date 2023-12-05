<template>
  <q-card>
    <q-card-section class="row">
      <div class="col" v-if="props.id_list.length!==0">
        <q-input v-model="filter" placeholder="Search"/>
        <q-list dense style="max-height: 200px; overflow-y: auto;">
          <q-item
            v-for="person in filteredPeople"
            :key="person"
            clickable
            @click="moveToRight(person)"
          >
            <q-item-section class="row">
                            <span>
                              <q-chip square dense class="col-3" color="blue-4"> {{person}}</q-chip>
                              <span class="col">
                                No Name
                              </span>
                            </span>
            </q-item-section>
          </q-item>
        </q-list>
      </div>
      <div v-else>
        No one can be selected
      </div>
      <div class="col" style="height: 250px; overflow-y: auto;">
        <q-list dense>
          <q-item
            v-for="person in selectedPeople"
            :key="person"
            clickable
            @click="moveToLeft(person)"
          >
            <q-item-section class="row">
                            <span>
                              <q-chip square dense class="col-3 text-white" color="primary"> {{person}}</q-chip>
                              <span class="col" v-if="props.map_list===undefined">
                                No Name
                              </span>
                              <span v-else>
                                You dong xi
                              </span>
                            </span>
            </q-item-section>
          </q-item>
        </q-list>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup lang="ts">
// 选择可见的人
import {ref} from "vue";
import {computed} from "vue-demi";
const props = defineProps<{
  id_list: number[];
  map_list?: Map<number, string>;
}>();
const emit = defineEmits(['update:selected']);
const filter = ref<string>(""),
  selectedPeople = ref<number[]>([]);

const filteredPeople = computed(() => {
  return props.id_list.filter(
    (person) =>
      (person.toString().includes(filter.value)  &&
        !selectedPeople.value.some((selected) => selected === person)
      ));
});

const moveToRight = (userId: number) => {
  const person = props.id_list.find((p) => p === userId);
  if (person && !selectedPeople.value.some((p) => p === userId)) {
    selectedPeople.value.push(person);
  }
  emit('update:selected', selectedPeople.value);
};

const moveToLeft = (userId: number) => {
  const index = selectedPeople.value.findIndex((p) => p === userId);
  if (index !== -1) {
    selectedPeople.value.splice(index, 1);
  }
  emit('update:selected', selectedPeople.value);
};
</script>

<style scoped>

</style>
