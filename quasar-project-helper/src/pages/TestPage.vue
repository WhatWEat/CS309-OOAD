<template>
  <q-page class="q-pa-sm">
    <div class="row q-col-gutter-sm justify-center">
      <q-btn @click="testSign"></q-btn>
      <q-btn label="Start Genshin"
        @click="startGenshin"></q-btn>
    </div>
  </q-page>
</template>

<script setup>
import {api} from 'boot/axios';
function startGenshin(){
  api.get('/login',{
    withCredentials: true
  }).then((response) => {
    let jwt = response.data.jwt_token;
    localStorage.setItem('Token', jwt);
    console.log(jwt);
    console.log(response);
  }).catch((error) => {
    console.log(error);
  });
}
function testSign(){
  api.post('/signup',{
    identity: 0,
    password: '123456',
    name: 'Andy',
    gender: 'm'
  },{
    withCredentials: true
  }).then((response) => {
    console.log(response);
  }).catch((error) => {
    console.log(error);
  });
}

</script>

<style>
.my-event {
  position: relative;
  font-size: 12px;
  width: 100%;
  margin: 1px 0 0 0;
  justify-content: center;
  text-overflow: ellipsis;
  overflow: hidden;
  cursor: pointer;
}

.title {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.text-white {
  color: white
}

.bg-blue {
  background: blue
}

.bg-green {
  background: green
}

.bg-orange {
  background: orange
}

.bg-red {
  background: red
}

.bg-teal {
  background: teal
}

.bg-grey {
  background: grey
}

.bg-purple {
  background: purple
}

.rounded-border {
  border-radius: 2px
}

abbr.tooltip {
  text-decoration: none
}
</style>
