<template>
    <div class="student-page">
        <section class="page-card toolbar-card">
            <div>
                <h1 class="page-title">学生管理</h1>
                <p class="page-subtitle">维护学生档案，同时支持批量删除、批量导入和 CSV 导出。</p>
            </div>

            <el-form :inline="true" :model="query" class="query-form">
                <el-form-item>
                    <el-input v-model="query.keyword" clearable placeholder="学号或姓名" />
                </el-form-item>
                <el-form-item>
                    <el-select v-model="query.deptId" clearable placeholder="所属部门" style="width: 180px;">
                        <el-option
                            v-for="option in flatDepartmentOptions"
                            :key="option.value"
                            :label="option.label"
                            :value="option.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="query.gradeYear" clearable placeholder="入学年级" style="width: 140px;">
                        <el-option
                            v-for="year in gradeYearOptions"
                            :key="year"
                            :label="`${year}级`"
                            :value="year"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="query.status" clearable placeholder="状态" style="width: 120px;">
                        <el-option :value="1" label="在读" />
                        <el-option :value="0" label="离校" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="loadStudents">查询</el-button>
                    <el-button @click="resetQuery">重置</el-button>
                    <el-button v-if="hasPermission('personnel:student:create')" type="success" @click="openCreateDialog">新增学生</el-button>
                    <el-button v-if="hasPermission('personnel:student:create')" type="warning" @click="openImportDialog">批量导入</el-button>
                    <el-button v-if="hasPermission('personnel:student:view')" type="info" @click="handleExport">导出数据</el-button>
                    <el-button
                        v-if="hasPermission('personnel:student:delete')"
                        type="danger"
                        plain
                        :disabled="!selectedStudentIds.length"
                        @click="handleBatchDelete"
                    >
                        批量删除
                    </el-button>
                </el-form-item>
            </el-form>
        </section>

        <section class="page-card table-card">
            <el-table ref="tableRef" :data="studentList" stripe @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="52" />
                <el-table-column prop="studentNo" label="学号" min-width="140" />
                <el-table-column prop="studentName" label="姓名" min-width="120" />
                <el-table-column label="性别" width="90">
                    <template #default="{ row }">
                        {{ formatGender(row.gender) }}
                    </template>
                </el-table-column>
                <el-table-column prop="deptName" label="所属部门" min-width="160" />
                <el-table-column prop="majorName" label="专业" min-width="160" show-overflow-tooltip />
                <el-table-column prop="gradeYear" label="年级" width="100" />
                <el-table-column prop="className" label="班级" min-width="160" show-overflow-tooltip />
                <el-table-column label="账号" min-width="180">
                    <template #default="{ row }">
                        <template v-if="row.userId">
                            <div>{{ row.accountUsername }}</div>
                            <el-tag size="small" :type="row.accountStatus === 1 ? 'success' : 'info'">
                                {{ row.accountStatus === 1 ? '启用' : '停用' }}
                            </el-tag>
                        </template>
                        <el-tag v-else size="small" type="info">未开通</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.status === 1 ? 'success' : 'info'">
                            {{ row.status === 1 ? '在读' : '离校' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="260" fixed="right">
                    <template #default="{ row }">
                        <el-button v-if="hasPermission('personnel:student:update')" link type="primary" @click="openEditDialog(row.id)">编辑</el-button>
                        <el-button v-if="hasPermission('personnel:student:delete')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
                        <el-button
                            v-if="!row.userId && hasPermission('personnel:student:account')"
                            link
                            type="success"
                            @click="openAccountDialog(row)"
                        >
                            创建账号
                        </el-button>
                        <el-button
                            v-if="row.userId && hasPermission('personnel:student:account')"
                            link
                            type="warning"
                            @click="handleRemoveAccount(row)"
                        >
                            注销账号
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="pager">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="query.pageNum"
                    :page-size="query.pageSize"
                    :total="total"
                    @current-change="handlePageChange"
                />
            </div>
        </section>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="680px" destroy-on-close>
            <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
                <el-form-item label="学号" prop="studentNo">
                    <el-input v-model="form.studentNo" />
                </el-form-item>
                <el-form-item label="姓名" prop="studentName">
                    <el-input v-model="form.studentName" />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="form.gender">
                        <el-radio :value="1">男</el-radio>
                        <el-radio :value="2">女</el-radio>
                        <el-radio :value="0">未知</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="所属部门" prop="deptId">
                    <el-select v-model="form.deptId" placeholder="请选择所属部门" style="width: 100%;">
                        <el-option
                            v-for="option in flatDepartmentOptions"
                            :key="option.value"
                            :label="option.label"
                            :value="option.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="专业" prop="majorName">
                    <el-input v-model="form.majorName" />
                </el-form-item>
                <el-form-item label="入学年级" prop="gradeYear">
                    <el-select v-model="form.gradeYear" clearable placeholder="请选择入学年级" style="width: 100%;">
                        <el-option
                            v-for="year in gradeYearOptions"
                            :key="year"
                            :label="`${year}级`"
                            :value="year"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="班级" prop="className">
                    <el-input v-model="form.className" />
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="form.phone" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-radio-group v-model="form.status">
                        <el-radio :value="1">在读</el-radio>
                        <el-radio :value="0">离校</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" :rows="3" />
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button
                    v-if="hasPermission(form.id ? 'personnel:student:update' : 'personnel:student:create')"
                    type="primary"
                    :loading="submitting"
                    @click="handleSubmit"
                >
                    保存
                </el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="accountDialogVisible" :title="accountDialogTitle" width="520px" destroy-on-close>
            <el-form ref="accountFormRef" :model="accountForm" :rules="accountRules" label-width="90px">
                <el-form-item label="登录账号" prop="username">
                    <el-input v-model="accountForm.username" />
                </el-form-item>
                <el-form-item label="初始密码" prop="password">
                    <el-input v-model="accountForm.password" show-password type="password" />
                </el-form-item>
                <el-form-item label="账号状态" prop="status">
                    <el-radio-group v-model="accountForm.status">
                        <el-radio :value="1">启用</el-radio>
                        <el-radio :value="0">停用</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="accountDialogVisible = false">取消</el-button>
                <el-button
                    v-if="hasPermission('personnel:student:account')"
                    type="primary"
                    :loading="accountSubmitting"
                    @click="handleCreateAccount"
                >
                    创建账号
                </el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="importDialogVisible" title="批量导入学生" width="760px" destroy-on-close>
            <el-alert
                type="info"
                show-icon
                :closable="false"
                title="每行一条记录，支持英文逗号、中文逗号或 Tab 分隔。字段顺序：学号, 姓名, 性别, 部门ID/部门名称, 专业, 年级, 班级, 手机号, 邮箱, 状态, 备注。"
            />
            <pre class="import-example">{{ importExample }}</pre>
            <el-input
                v-model="importText"
                type="textarea"
                :rows="10"
                resize="none"
                placeholder="例如：20260001,张三,男,信息工程学院,软件工程,2026,1班,13800138000,zhangsan@example.com,在读,新生"
            />

            <template #footer>
                <el-button @click="importDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="importing" @click="handleImport">开始导入</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
    batchDeleteStudents,
    createStudent,
    createStudentAccount,
    deleteStudent,
    exportStudents,
    getStudentDetail,
    getStudentPage,
    importStudents,
    removeStudentAccount,
    updateStudent
} from '@/api/personnel/student'
import { getDepartmentOptions } from '@/api/organization/department'
import { usePermission } from '@/composables/use_permission'

const tableRef = ref()
const formRef = ref()
const accountFormRef = ref()
const studentList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const accountDialogVisible = ref(false)
const importDialogVisible = ref(false)
const submitting = ref(false)
const accountSubmitting = ref(false)
const importing = ref(false)
const departmentOptions = ref([])
const selectedStudentIds = ref([])
const accountTargetId = ref(null)
const accountTargetName = ref('')
const importText = ref('')
const { hasPermission } = usePermission()

const query = reactive({
    pageNum: 1,
    pageSize: 10,
    keyword: '',
    deptId: null,
    gradeYear: null,
    status: null
})

const form = reactive({
    id: null,
    studentNo: '',
    studentName: '',
    gender: 1,
    deptId: null,
    majorName: '',
    gradeYear: null,
    className: '',
    phone: '',
    email: '',
    status: 1,
    remark: ''
})

const accountForm = reactive({
    username: '',
    password: '',
    status: 1
})

const rules = {
    studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
    studentName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
    deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }]
}

const accountRules = {
    username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入初始密码', trigger: 'blur' }]
}

const dialogTitle = computed(() => (form.id ? '编辑学生' : '新增学生'))
const accountDialogTitle = computed(() => `为 ${accountTargetName.value} 创建账号`)
const flatDepartmentOptions = computed(() => flattenOptions(departmentOptions.value))
const departmentLookup = computed(() => buildDepartmentLookup(departmentOptions.value))
const gradeYearOptions = computed(() => {
    const currentYear = new Date().getFullYear()
    return Array.from({ length: 8 }, (_, index) => currentYear - index)
})
const importExample = computed(() => [
    '学号,姓名,性别,部门,专业,年级,班级,手机号,邮箱,状态,备注',
    '20260001,张三,男,信息工程学院,软件工程,2026,1班,13800138000,zhangsan@example.com,在读,新生',
    '20260002,李四,女,2002,计算机科学,2026,2班,13800138001,lisi@example.com,在读,转专业'
].join('\n'))

function resetForm() {
    Object.assign(form, {
        id: null,
        studentNo: '',
        studentName: '',
        gender: 1,
        deptId: null,
        majorName: '',
        gradeYear: null,
        className: '',
        phone: '',
        email: '',
        status: 1,
        remark: ''
    })
}

function resetAccountForm() {
    Object.assign(accountForm, {
        username: '',
        password: '',
        status: 1
    })
}

async function loadStudents() {
    const response = await getStudentPage(query)
    studentList.value = response.data.list
    total.value = response.data.total
}

function resetQuery() {
    query.keyword = ''
    query.deptId = null
    query.gradeYear = null
    query.status = null
    query.pageNum = 1
    loadStudents()
}

function openCreateDialog() {
    resetForm()
    dialogVisible.value = true
}

function openImportDialog() {
    importText.value = ''
    importDialogVisible.value = true
}

async function openEditDialog(id) {
    const response = await getStudentDetail(id)
    Object.assign(form, response.data)
    dialogVisible.value = true
}

function openAccountDialog(row) {
    accountTargetId.value = row.id
    accountTargetName.value = row.studentName
    resetAccountForm()
    accountForm.username = row.studentNo
    accountForm.status = row.status ?? 1
    accountDialogVisible.value = true
}

async function handleSubmit() {
    await formRef.value.validate()
    submitting.value = true

    try {
        if (form.id) {
            await updateStudent(form.id, form)
            ElMessage.success('学生信息更新成功')
        } else {
            await createStudent(form)
            ElMessage.success('学生信息创建成功')
        }
        dialogVisible.value = false
        loadStudents()
    } finally {
        submitting.value = false
    }
}

async function handleCreateAccount() {
    await accountFormRef.value.validate()
    accountSubmitting.value = true

    try {
        await createStudentAccount(accountTargetId.value, accountForm)
        ElMessage.success('学生账号创建成功')
        accountDialogVisible.value = false
        loadStudents()
    } finally {
        accountSubmitting.value = false
    }
}

async function handleRemoveAccount(row) {
    await ElMessageBox.confirm(`注销账号 ${row.accountUsername} 后将无法登录，是否继续？`, '确认注销账号', {
        type: 'warning'
    })
    await removeStudentAccount(row.id)
    ElMessage.success('学生账号已注销')
    loadStudents()
}

async function handleDelete(id) {
    await ElMessageBox.confirm('删除学生后若已绑定账号，会同步注销该账号，是否继续？', '确认删除', {
        type: 'warning'
    })
    await deleteStudent(id)
    ElMessage.success('学生信息删除成功')
    loadStudents()
}

async function handleBatchDelete() {
    await ElMessageBox.confirm(`将批量删除 ${selectedStudentIds.value.length} 条学生记录，是否继续？`, '确认批量删除', {
        type: 'warning'
    })
    await batchDeleteStudents(selectedStudentIds.value)
    ElMessage.success('批量删除成功')
    selectedStudentIds.value = []
    tableRef.value?.clearSelection?.()
    loadStudents()
}

async function handleImport() {
    let payload
    try {
        payload = parseStudentImportText()
    } catch (error) {
        ElMessage.error(error.message || '学生导入失败')
        return
    }

    importing.value = true
    try {
        const response = await importStudents(payload)
        ElMessage.success(`成功导入 ${response.data} 条学生记录`)
        importDialogVisible.value = false
        importText.value = ''
        loadStudents()
    } finally {
        importing.value = false
    }
}

async function handleExport() {
    const blob = await exportStudents(buildExportQuery())
    downloadBlob(blob, 'students.csv')
    ElMessage.success('学生数据导出成功')
}

function handleSelectionChange(selection) {
    selectedStudentIds.value = selection.map((item) => item.id)
}

function handlePageChange(page) {
    query.pageNum = page
    loadStudents()
}

async function loadDepartmentOptions() {
    const response = await getDepartmentOptions()
    departmentOptions.value = response.data
}

function flattenOptions(tree, level = 0) {
    return tree.flatMap((item) => {
        const prefix = level === 0 ? '' : `${'　'.repeat(level)}└ `
        const current = {
            label: `${prefix}${item.label}`,
            value: item.value
        }
        return [current, ...flattenOptions(item.children || [], level + 1)]
    })
}

function buildDepartmentLookup(tree) {
    const map = new Map()
    const walk = (items) => {
        items.forEach((item) => {
            map.set(String(item.value), item.value)
            map.set(item.label, item.value)
            walk(item.children || [])
        })
    }
    walk(tree)
    return map
}

function parseStudentImportText() {
    const lines = importText.value
        .split(/\r?\n/)
        .map((line) => line.trim())
        .filter(Boolean)

    if (!lines.length) {
        throw new Error('请输入导入数据')
    }

    return lines
        .filter((line, index) => {
            const firstColumn = splitImportLine(line)[0]
            return !(index === 0 && firstColumn === '学号')
        })
        .map((line, index) => {
            const columns = splitImportLine(line)
            if (columns.length < 4) {
                throw new Error(`第 ${index + 1} 行至少需要填写学号、姓名、性别和部门`)
            }

            const [
                studentNo,
                studentName,
                genderText = '',
                deptText = '',
                majorName = '',
                gradeYearText = '',
                className = '',
                phone = '',
                email = '',
                statusText = '',
                remark = ''
            ] = columns

            return {
                studentNo,
                studentName,
                gender: parseGenderValue(genderText),
                deptId: parseDepartmentValue(deptText, index + 1),
                majorName,
                gradeYear: parseOptionalInteger(gradeYearText, '年级', index + 1),
                className,
                phone,
                email,
                status: parseStudentStatus(statusText),
                remark
            }
        })
}

function splitImportLine(line) {
    return line.split(/[\t,，]/).map((item) => item.trim())
}

function parseDepartmentValue(value, lineNumber) {
    const deptId = departmentLookup.value.get(value)
    if (deptId === undefined) {
        throw new Error(`第 ${lineNumber} 行的部门不存在：${value}`)
    }
    return deptId
}

function parseOptionalInteger(value, fieldName, lineNumber) {
    if (!value) {
        return null
    }
    if (!/^\d+$/.test(value)) {
        throw new Error(`第 ${lineNumber} 行的${fieldName}必须为数字`)
    }
    return Number(value)
}

function parseGenderValue(value) {
    if (!value || value === '未知' || value === '0') {
        return 0
    }
    if (value === '男' || value === '1') {
        return 1
    }
    if (value === '女' || value === '2') {
        return 2
    }
    return 0
}

function parseStudentStatus(value) {
    if (!value || value === '在读' || value === '1') {
        return 1
    }
    if (value === '离校' || value === '0') {
        return 0
    }
    return 1
}

function buildExportQuery() {
    return {
        keyword: query.keyword || undefined,
        deptId: query.deptId || undefined,
        gradeYear: query.gradeYear || undefined,
        status: query.status === null ? undefined : query.status
    }
}

function downloadBlob(blob, fileName) {
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
}

function formatGender(gender) {
    const genderMap = {
        0: '未知',
        1: '男',
        2: '女'
    }
    return genderMap[gender] || '未知'
}

onMounted(() => {
    loadDepartmentOptions()
    loadStudents()
})
</script>

<style scoped>
.student-page {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.toolbar-card,
.table-card {
    padding: 20px;
}

.query-form {
    margin-top: 18px;
}

.import-example {
    margin: 16px 0;
    padding: 14px 16px;
    overflow-x: auto;
    color: #475569;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    font-size: 13px;
    line-height: 1.7;
}

.pager {
    display: flex;
    justify-content: flex-end;
    margin-top: 18px;
}

.page-title {
    margin: 0;
    font-size: 24px;
}

.page-subtitle {
    margin: 6px 0 0;
    color: #64748b;
}
</style>
