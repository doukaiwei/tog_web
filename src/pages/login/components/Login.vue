<template>
  <t-form
    ref="form"
    :class="['item-container', `login-${type}`]"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <template v-if="type == 'password'">
      <t-form-item name="account">
        <t-input v-model="formData.account" size="large" :placeholder="`${$t('pages.login.input.account')}`">
          <template #prefix-icon>
            <t-icon name="user" />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input
          v-model="formData.passWord"
          size="large"
          :type="showPsw ? 'text' : 'password'"
          clearable
          :placeholder="`${$t('pages.login.input.password')}`"
        >
          <template #prefix-icon>
            <t-icon name="lock-on" />
          </template>
          <template #suffix-icon>
            <t-icon :name="showPsw ? 'browse' : 'browse-off'" @click="showPsw = !showPsw" />
          </template>
        </t-input>
      </t-form-item>
    </template>

    <t-form-item class="btn-container">
      <t-button block size="large" type="submit"> {{ $t('pages.login.signIn') }} </t-button>
    </t-form-item>

    <div class="switch-container">
      <span class="tip" @click="()=>$router.push({path:'/vlogsList', query: { type: 'visitor' }})">{{
        $t('pages.login.visitorLogin')
      }}</span>
    </div>
  </t-form>
</template>

<script setup lang="ts">
import type { FormInstanceFunctions, FormRule, SubmitContext } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {getUserInfoByToken, userLogin} from "./login.ts"
import { useUserStore } from '@/store';
import { t } from '@/locales';

const userStore = useUserStore();
const INITIAL_DATA = {
  account: '',
  passWord: '',
};

const FORM_RULES: Record<string, FormRule[]> = {
  account: [{ required: true, message: t('pages.login.required.account'), type: 'error' }],
  passWord: [{ required: true, message: t('pages.login.required.password'), type: 'error' }],
};

const type = ref('password');

const form = ref<FormInstanceFunctions>();
const formData = ref({ ...INITIAL_DATA });
const showPsw = ref(false);


const router = useRouter();
const route = useRoute();

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    try {
      userLogin(formData.value).then(res=>{
        if(res.status){
          localStorage.setItem("token", res.data)
          MessagePlugin.success('登录成功');
          userStore.setToken(res.data)
          getUserInfoByToken(res.data).then(res=>{
            localStorage.setItem("userInfo",JSON.stringify(res.data))
            const redirect = route.query.redirect as string;
            const redirectUrl = redirect ? decodeURIComponent(redirect) : '/myVlogs/index';
            router.push(redirectUrl);
          })
        }
      }).catch((e)=>{
        MessagePlugin.error(e.message);
      })
    } catch (e) {
      console.log(e);
      MessagePlugin.error(e.message);
    }
  }
};
</script>

<style lang="less" scoped>
@import '../index.less';
</style>
