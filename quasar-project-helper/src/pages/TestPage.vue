<template>
  <div class="q-pa-md">
    <q-scroll-area style="height: 200px">
      <q-infinite-scroll @load="onLoad" :offset="10" >
        <div v-for="(item, index) in items" :key="index" class="caption">
          <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Rerum repellendus sit voluptate voluptas eveniet porro. Rerum blanditiis perferendis totam, ea at omnis vel numquam exercitationem aut, natus minima, porro labore.</p>
        </div>
        <template v-slot:loading>
          <div class="row justify-center q-my-md">
            <q-spinner-dots color="primary" size="40px" />
          </div>
        </template>

      </q-infinite-scroll>
    </q-scroll-area>

  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  setup () {
    const items = ref([ {}, {}, {}, {}, {}, {}, {} ])

    return {
      items,
      onLoad (index, done) {
        console.log('nxt')
        setTimeout(() => {
          items.value.push({}, {}, {}, {}, {}, {}, {})
          done()
        }, 1000)
      }
    }
  }
}
</script>
