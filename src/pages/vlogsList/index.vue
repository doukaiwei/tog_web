<template>
  <div>
    <t-layout>
      <t-header style="background-color: #02a6f2">
        <div class="vlog_head">
          <div class="left_title" @click="requery">
            Tog's Blog
          </div>
          <div class="right_search">
            <t-input v-model="searchInput" placeholder="搜索"/>
            <t-button variant="text" style="color: white" @click="$router.push('/login')">去登录</t-button>
          </div>
        </div>
      </t-header>
      <div>
        <t-layout v-if="!showDetail" class="vlog_layout">
          <t-aside class="vlog_aside">
            <t-menu :key="menuKey">
              <t-menu-item v-for="item in tagsList" :value="item.id" @click="queryByTag(item)">
                <template #icon>
                  <t-icon name="root-list"/>
                </template>
                {{item.tag}}
              </t-menu-item>
            </t-menu>
          </t-aside>
          <t-content class="vlog_body">
            <t-list :split="true">
              <t-list-item class="blog_item" v-for="item in blogsList" @click="viewBlog(item)">
                <t-list-item-meta :title="item.title" :description="item.summary" />
                <template #action>
                <span>
                  <t-tag class="tags_sty" v-for="item in item.tagsList" shape="mark" theme="success" variant="light" >{{item}}</t-tag>
                </span>
                </template>
              </t-list-item>
            </t-list>
            <t-pagination
              style="margin-top: 10px"
              v-model="form.pageIndex"
              v-model:pageSize="form.pageSize"
              :total="form.total"
              @page-size-change="onPageSizeChange"
              @current-change="onCurrentChange"
            />
          </t-content>
        </t-layout>
        <div v-show="showDetail" >
          <previewBlog class="vlog_layout" @reBack="reBack" ref="previewBlogRef"/>
        </div>
      </div>
      <t-footer class="copyright">豫ICP备2024059362号-1</t-footer>
    </t-layout>
  </div>
</template>
<script lang="ts" setup>
import {ref, onMounted} from "vue"
import {queryVlogsList,getTags} from "./index.ts"
import previewBlog from "./components/previewBlog.vue"
let searchInput = ref("")
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
let menuKey=ref(0)
let showDetail = ref(false)
const previewBlogRef = ref(null)
let tagsList = ref([])
let blogsList = ref([])
onMounted(()=>{
  queryVlogsList(form.value).send(true).then(res=>{
    blogsList.value = res.data.records
    form.value.total = res.data.total
    getTags().send(true).then(res=>{
      tagsList.value = res.data
      let options = ref([])
      blogsList.value.forEach(item=>{
        let tagsOptions = ref([])
        item.tagsList=[]
        options.value =  item.tagTypes.split(',').map(String)
        res.data.forEach(item1=>{
          if(options.value.includes(item1.id)&&!tagsOptions.value.includes(item1.tag)){
            tagsOptions.value.push(item1.tag)
          }
        })
        item.tagsList =tagsOptions.value
      })
    })
  })
})
let viewBlog = (item)=>{
  showDetail.value = true
  previewBlogRef.value.init(item)
}
const reBack = ()=>{
  showDetail.value = false
}
const queryByTag= (item)=>{
  form.value.tagType = item.id
  queryData()
}
const requery=()=>{
  form.value.tagType =""
  queryData()
  menuKey.value++
}
const queryData=()=>{
  queryVlogsList(form.value).send(true).then(res=>{
    blogsList.value = res.data.records
    form.value.total = res.data.total
    let options = ref([])
    blogsList.value.forEach(item=>{
      let tagsOptions = ref([])
      item.tagsList=[]
      options.value =  item.tagTypes.split(',').map(String)
      tagsList.value.forEach(item1=>{
        if(options.value.includes(item1.id)&&!tagsOptions.value.includes(item1.tag)){
          tagsOptions.value.push(item1.tag)
        }
      })
      item.tagsList =tagsOptions.value
    })
  })
}
const onCurrentChange=(val)=>{
  queryData()
}
const onPageSizeChange=(val)=>{
  queryData()
}
</script>
<style lang="less" scoped>
@import './index.less';
</style>
