<template>
  <q-card bordered class="no-shadow" style="width: 200px">
    <!--   上方部分 -->
    <q-item>
      <q-item-section avatar>
        <q-avatar class="shadow-10" size="60px">
          <q-img :src="avatar"
                 spinner-color="primary"
                 loading="lazy">
            <template v-slot:loading>
              <q-spinner-gears size="40px" />
            </template>
          </q-img>
        </q-avatar>
      </q-item-section>

      <q-item-section>

        <q-item-label class="text-grey-8 text-weight-bold">
          <span style="font-weight: bold;">
            Group ID :
          </span>
          <span style="font-weight: normal;">
            {{ groupId }}
          </span>
        </q-item-label>

        <q-item-label class="text-grey-8">
          <span style="font-weight: bold;">
            Members :
          </span>
          <span style="font-weight: normal;">
            {{ Object.keys(this.members).join(', ') }}
          </span>

        </q-item-label>

        <q-item-label class="text-grey-8">
          <span style="font-weight: bold;">
            Group size :
          </span>
          <span style="font-weight: normal;">
            {{ groupSize }}
          </span>
        </q-item-label>
      </q-item-section>

<!--      <q-item-section side>-->
<!--        <q-item-label>-->
<!--          <q-btn class="bg-indigo-7 text-white" flat icon="fab fa-facebook" round size="sm"/>-->
<!--        </q-item-label>-->
<!--        <q-item-label>-->
<!--          <q-btn class="bg-info text-white" flat icon="fab fa-twitter" round size="sm"/>-->
<!--        </q-item-label>-->

<!--      </q-item-section>-->
    </q-item>
    <!--    间隔线部分-->
    <q-separator></q-separator>
    <!--    下方详细信息部分-->
    <q-card-section class="row">
      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          Group Name :
        </span>
        <span style="font-weight: normal;">
          {{ groupName }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          Creation Time :
        </span>
        <span style="font-weight: normal;">
          {{ creationTime }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          Deadline :
        </span>
        <span style="font-weight: normal;">
          {{ deadline }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;" >
          Presentation Time :
        </span>
        <span style="font-weight: normal;">
          {{ presentationTime }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          Instructor :
        </span>
        <span style="font-weight: normal;">
          {{ Object.keys(instructor).toString() }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          Leader :
        </span>
        <span style="font-weight: normal;">
          {{ Object.keys(this.leader).toString() }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          Max Size :
        </span>
        <span style="font-weight: normal;">
          {{ maxSize }}
        </span>
      </div>

      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1 row col-12">
        <span style="font-weight: bold;">
          More Info :
        </span>
        <span style="font-weight: normal;">
          {{ detail }}
        </span>
      </div>
    </q-card-section>

<!--        <pre>-->

<!--        </pre>-->
<!--        <q-separator></q-separator>-->
<!--        &lt;!&ndash;   最终的按钮部分 &ndash;&gt;-->
<!--          <q-card-actions align="around">-->
<!--            <q-btn-group spread :style="{'width':'100%'}">-->
<!--              <q-btn color="purple" label="First" icon="" />-->
<!--              <q-btn color="purple" label="Second" icon="visibility" />-->
<!--            </q-btn-group>-->
<!--          </q-card-actions>-->
  </q-card>
</template>

<script>
import {defineComponent} from 'vue'
import {descriptionProps} from "element-plus";

export default defineComponent({
  name: "DirectoryCard",
  computed: {
    descriptionProps() {
      return descriptionProps
    }
  },
  props: ['avatar', 'groupId', 'members', 'groupSize', 'deadline', 'detail' , 'instructor','leader', "maxSize", "creationTime", "presentationTime",'groupName']
})
</script>

<style scoped>

</style>




