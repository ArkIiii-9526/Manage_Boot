new Vue({
    el: '#app',
    data: {
        searchName: '',
        currentPage: 1,
        pageSize: 10,
        totalPages: 1,
        totalRecords: 0,
        customers: [],
        loading: false,
        searchTimer: null,
        showEditDialog: false,
        editingCustomer: null,
        isEditMode: false
    },
    mounted() {
        this.fetchCustomers();
    },
    methods: {
        // 防抖搜索（300ms）
        handleInput() {
            clearTimeout(this.searchTimer);
            this.searchTimer = setTimeout(() => {
                this.handleSearch();
            }, 300);
        },

        // 执行搜索
        handleSearch() {
            this.currentPage = 1;
            this.fetchCustomers();
        },

        // 获取数据
        async fetchCustomers() {
            this.loading = true;
            try {
                const params = {
                    searchName: this.searchName.trim(),
                    currentPage: this.currentPage,
                    pageSize: this.pageSize
                };

                const response = await axios.get('/api/customers', {params});
                if (response.data.code === 200) {
                    const data = response.data.data;
                    this.customers = data.list;
                    this.currentPage = data.currentPage;
                    this.totalPages = data.totalPages;
                    this.totalRecords = data.totalRecords;
                } else {
                    console.error('请求失败:', response.data.msg);
                }
            } catch (error) {
                console.error('请求错误:', error);
                alert('数据加载失败，请检查网络');
            } finally {
                this.loading = false;
            }
        },

        // 添加顾客
        addCustomer() {
            this.isEditMode = false;
            this.editingCustomer = {
                name: '',
                sex: 1,
                age: null,
                idCard: '',
                phone: ''
            };
            this.showEditDialog = true;
        },

        // 编辑顾客
        editCustomer(customer) {
            this.isEditMode = true;
            this.editingCustomer = {...customer};
            this.showEditDialog = true;
        },

        //判断并提交表单
        async submitEdit() {
            try {
                let response;
                if (this.isEditMode) {
                    // 更新请求
                    response = await axios.put('/api/editCustomers/${this.editingCustomer.idCard}', this.editingCustomer);
                } else {
                    // 新增请求
                    response = await axios.post('/api/addCustomers', this.editingCustomer);
                }

                if (response.data.code === 200) {
                    this.showEditDialog = false;
                    await this.fetchCustomers();
                    Swal.fire({
                        title: this.isEditMode ? '修改成功' : '添加成功',
                        timer: 1000,
                        timerProgressBar: true,
                        showConfirmButton: false
                    });
                } else {
                    alert(response.data.msg);
                }
            } catch (error) {
                console.error('操作失败:', error);
                alert(`操作失败: ${error.response?.data?.message || '系统错误'}`);
            }
        },

        //删除顾客
        async deleteEdit(customer) {
            if (!confirm('确定要删除该客户吗？')) return;

            try {
                const response = await axios.delete(`/api/deleteCustomers/${customer.idCard}`);
                if (response.data.code === 200) {
                    await this.fetchCustomers();
                    alert('删除成功');
                }
            } catch (error) {
                console.error('删除失败:', error);
                alert('删除失败，请稍后重试');
            }
        },
        // 分页切换
        changePage(newPage) {
            if (newPage < 1 || newPage > this.totalPages) return;
            this.currentPage = newPage;
            this.fetchCustomers();
        },
    },
    filters: {
        genderFormat(value) {
            return {2: '女', 1: '男'}[value] || '未知';
        },
        idCardFilter(value) {
            return value?.replace(/(\d{4})\d{10}(\w{4})/, '$1******$2') || '';
        }
    }
})
