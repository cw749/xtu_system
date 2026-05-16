<template>
    <el-menu-item v-if="isMenuItem" :index="item.routePath">
        <el-icon><component :is="iconComponent" /></el-icon>
        <span>{{ item.menuName }}</span>
    </el-menu-item>

    <el-sub-menu v-else-if="visibleChildren.length > 0" :index="submenuIndex">
        <template #title>
            <el-icon><component :is="iconComponent" /></el-icon>
            <span>{{ item.menuName }}</span>
        </template>

        <SidebarMenuItem
            v-for="child in visibleChildren"
            :key="child.id"
            :item="child"
        />
    </el-sub-menu>
</template>

<script setup>
import { computed } from 'vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

defineOptions({
    name: 'SidebarMenuItem'
})

const props = defineProps({
    item: {
        type: Object,
        required: true
    }
})

const isMenuItem = computed(() => props.item.menuType === 'C')
const submenuIndex = computed(() => props.item.routePath || `menu-${props.item.id}`)
const visibleChildren = computed(() => (props.item.children || []).filter((child) => child.menuType !== 'B'))
const iconComponent = computed(() => ElementPlusIconsVue[props.item.icon] || ElementPlusIconsVue.Menu)
</script>
