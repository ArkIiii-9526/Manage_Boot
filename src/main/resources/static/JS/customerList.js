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

        showAlert(title, message = "", icon = 'error') {
            const config = {
                title: title,
                text: message,
                icon: icon,
                timerProgressBar: true,
                showConfirmButton: icon === 'error'
            };

            // 成功提示自动关闭
            if (icon === 'success') {
                config.timer = 2000;
                config.showConfirmButton = false;
            }
            // 错误提示手动关闭
            else {
                config.showConfirmButton = true;
            }

            Swal.fire(config);
        },

        // 执行搜索
        handleSearch() {
            this.currentPage = 1;
            this.fetchCustomers().then(r => {
                console.log();
            });
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
                if (response.data.success) {
                    const result = response.data.data;
                    this.customers = result.customers;
                    this.currentPage = result.currentPage;
                    this.totalPages = result.totalPages;
                    this.totalRecords = result.totalRecords;
                }
            } catch (error) {
                const message = error.response?.data?.message || error.message;
                this.showAlert('操作失败', message);
            }finally {
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
                    response = await axios.put(`/api/editCustomers/${this.editingCustomer.idCard}`, this.editingCustomer);
                } else {
                    // 新增请求
                    response = await axios.post('/api/addCustomers', this.editingCustomer);
                }

                if (response.data.success) {
                    this.showAlert('操作成功', '客户信息已更新', 'success');
                    this.showEditDialog = false;
                    this.fetchCustomers().catch(e => {
                        console.error("后台刷新失败:", e);
                    });
                }else {
                    this.showAlert('操作失败', response.data.message);
                }
            } catch (error) {
                const message = error.response?.data?.message || error.message;
                this.showAlert('操作失败', message);
            }
        },

        //删除顾客
        async deleteEdit(customer) {
            const result = await Swal.fire({
                title: '确定删除？',
                text: `确定要删除客户 ${customer.name} 吗？此操作不可恢复`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '确定删除',
                cancelButtonText: '取消'
            });
            if (!result.isConfirmed) return;

            try {
                const response = await axios.delete(`/api/deleteCustomers/${customer.idCard}`);
                if (response.data.success) {
                    await this.fetchCustomers();
                    this.showAlert('成功', '客户已删除', 'success');
                }else{
                    this.showAlert('删除失败', response.data.message);
                }
            } catch (error) {
                const message = error.response?.data?.message || error.message;
                this.showAlert('操作失败', message);
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
