<template>
  <div>
    <div>
      <t-card v-if="!showDetail&&!editDetail" :bordered="false" hover-shadow class="cardBody">
        <t-list :split="true" class="listBody">
          <t-list-item v-for="item in blogList">
            <template #action>
              <t-link theme="primary" hover="color" style="margin-left: 16px" @click="editHandler(item)"> 编辑 </t-link>
              <t-link theme="primary" hover="color" style="margin-left: 16px" @click="delHandler(item)"> 删除 </t-link>
            </template>
            <t-list-item-meta :title="item.title" @click="viewVlogContent(item)" :description="item.summary" />
          </t-list-item>
        </t-list>
        <t-pagination
          v-model="form.pageIndex"
          v-model:pageSize="form.pageSize"
          :total="form.total"
          show-jumper
          @page-size-change="onPageSizeChange"
          @current-change="onCurrentChange"
          class="pagination"
        />
      </t-card>
      <t-card v-show="showDetail&&!editDetail" :bordered="false" hover-shadow class="cardBody">
        <viewVlog @reBack="reBack" ref="viewVlogRef"/>
      </t-card>
      <div v-show="editDetail" :bordered="false" hover-shadow class="cardBody">
        <editVlogContent @reBack="editReBack" ref="editVlogContentRef"/>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import {queryVlogsList, deleteVlog} from "./vlogPage"
import { ref,onMounted } from "vue"
import viewVlog from "./viewVlog.vue"
import editVlogContent from "../../../user/components/editVlogContent.vue";

let current = ref(1)
let pageSize = ref(10)
let showDetail = ref(false)
let blogList = ref([])
let viewBlog = ref({})
let editDetail = ref(false)
const editVlogContentRef =ref(null)
const viewVlogRef = ref(null)
let form=ref({
  "createTime": "",
  "pageIndex": 1,
  "pageSize": 10,
  "summary": "",
  "tagType": "",
  "title": "",
  "total": 0,
  "userId": ""
})


onMounted(()=>{
  form.value.userId=JSON.parse(localStorage.getItem("userInfo")).id
  queryData()
})
const queryData=()=>{
  queryVlogsList(form.value).send(true).then(res=>{
    blogList.value = res.data.records
    form.value.total = res.data.total
  })
}
const onPageSizeChange=(val)=>{
  queryData()
}
const onCurrentChange=(val)=>{
  queryData()
}
const reBack=()=>{
  showDetail.value = false
}
const editReBack=()=>{
  editDetail.value = false
  queryData()
}
const viewVlogContent = (item)=>{
  viewBlog.value = item
  showDetail.value=true
  viewVlogRef.value.init(item)
}
const editHandler=(item)=>{
  editDetail.value = true
  editVlogContentRef.value.setData(item)
}
const delHandler=(item)=>{
  deleteVlog({id: item.id}).send(true).then(()=>{
    queryData()
  })
}
</script>
<style scoped>
 .pagination{
   margin-top: 20px;
 }
 .cardBody{
   height: calc(100vh - 150px);
   overflow-y: auto;
   cursor: pointer;
 }
 .listBody{
   height: calc(100vh - 250px);
   overflow-y: auto;
 }
</style>
