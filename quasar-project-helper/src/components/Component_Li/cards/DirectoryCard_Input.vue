<template>
  <q-card bordered class="no-shadow">
    <!--   上方部分 -->
    <q-item>
      <q-item-section avatar>
        <q-avatar class="shadow-10" size="60px">
          <img src="temp">
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
            Group size :
          </span>
          <span style="font-weight: normal;">
            {{ groupSize }}
          </span>
        </q-item-label>
      </q-item-section>

      <q-item-section side>
        <q-item-label>
          <q-btn class="bg-indigo-7 text-white" flat icon="fab fa-facebook" round size="sm"/>
        </q-item-label>
        <q-item-label>
          <q-btn class="bg-info text-white" flat icon="fab fa-twitter" round size="sm"/>
        </q-item-label>

      </q-item-section>
    </q-item>
    <!--    间隔线部分-->
    <q-separator size="3px"></q-separator>
    <!--    下方详细信息部分-->
    <q-card-section>
      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="groups"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Members</q-item-label>
          <q-item-label caption lines="2">
            <q-input :model-value="members" dense disable/>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Creation Time</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="creationTime_temp" filled mask="date" dense >
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="creationTime_temp">
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup color="primary" flat label="Close"/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">DeadLine</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="deadLine_temp" filled mask="date" dense >
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="deadLine_temp">
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup color="primary" flat label="Close"/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Presentation Time</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="presentationTime_temp" filled mask="date" dense >
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="presentationTime_temp">
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup color="primary" flat label="Close"/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="person"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Leader</q-item-label>
          <q-item-label caption lines="2">
            <q-input dense v-model="leader_temp"></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="123"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Max Size</q-item-label>
          <q-item-label caption lines="2">
            <q-input disable :model-value="maxSize_temp" dense></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="more"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">More Information</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="moreInformation_temp" dense/>
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-card-section>


    <q-separator></q-separator>
    <!--   最终的按钮部分 -->
    <q-card-actions align="around">
      <q-btn-group :style="{'width':'100%'}" spread>
        <!--   点击后使该组件刷新回最初的状态-->
        <q-btn color="green" icon="" label="Reset" rounded @click="reset"/>
        <q-btn color="red" label="Submit Change" rounded/>
      </q-btn-group>
    </q-card-actions>
  </q-card>
</template>

<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: "DirectoryCard",
  props: ['groupId', "groupSize", 'members', "creationTime", "deadLine", "presentationTime", "leader", "maxSize", "moreInformation"],
  emits: ['update:groupId', 'update:groupSize', 'update:members', 'update:creationTime', 'update:deadLine', 'update:presentationTime', 'update:leader', 'update:maxSize', 'update:moreInformation'],
  methods: {
    reset() {
      this.groupId_temp = this.groupId.value
      this.groupSize_temp = this.groupSize
      this.members_temp = this.members
      this.creationTime_temp = this.creationTime
      this.deadLine_temp = this.deadLine
      this.presentationTime_temp = this.presentationTime
      this.leader_temp = this.leader
      this.maxSize_temp = this.maxSize
      this.moreInformation_temp = this.moreInformation
    }
  },
  data() {
    return {
      groupId_temp: this.groupId.value,
      groupSize_temp: this.groupSize,
      members_temp: this.members,
      creationTime_temp: this.creationTime,
      deadLine_temp: this.deadLine,
      presentationTime_temp: this.presentationTime,
      leader_temp: this.leader,
      maxSize_temp: this.maxSize,
      moreInformation_temp: this.moreInformation
    }
  }
})
</script>

<style scoped>

</style>




