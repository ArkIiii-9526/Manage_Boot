var app = new Vue({
    el: '#app',
    data: {
        searchRoomId: '',
        currentPage: 1,
        pageSize: 10,
        totalPages: 1,
        totalRecords: 0,
        Rooms: [],
        loading: false,
        searchTimer: null,
        showEditDialog: false,
        editingRoom: null,
        isEditMode: false
    },
    mounted() {
        this.fetchRooms();
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
            this.fetchRooms();
        },

        // 获取数据
        async fetchRooms() {
            this.loading = true;
            try {
                const params = {
                    searchRoomId: this.searchRoomId.trim(),
                    currentPage: this.currentPage,
                    pageSize: this.pageSize
                };

                const response = await axios.get('/api/rooms', {params});
                if (response.data.code === 200) {
                    const data = response.data.data;
                    this.Rooms = data.list;
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
        addRoom() {
            this.isEditMode = false;
            this.editingRoom = {
                roomId: '',
                roomType: 1,
                roomPrice: '',
                roomStatus: 0,
            };
            this.showEditDialog = true;
        },

        // 编辑顾客
        editRoom(Room) {
            this.isEditMode = true;
            this.editingRoom = {...Room};
            this.showEditDialog = true;
        },

        //判断并提交表单
        async submitEdit() {
            try {
                let response;
                if (this.isEditMode) {
                    // 更新请求
                    response = await axios.put(`/api/editRooms/${this.editingRoom.roomId}`, this.editingRoom);
                } else {
                    // 新增请求
                    response = await axios.post('/api/addRooms', this.editingRoom);
                }

                if (response.data.code === 200) {
                    this.showEditDialog = false;
                    await this.fetchRooms();
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
        async deleteEdit(Room) {
            if (!confirm('确定要删除该房间吗？')) return;
            try {
                const response = await axios.delete(`/api/deleteRooms/${Room.roomId}`);
                if (response.data.code === 200) {
                    await this.fetchRooms();
                    Swal.fire('删除成功', '', 'success');
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
            this.fetchRooms();
        },
    },
    filters: {
        roomTypeFormat(value) {
            return {1: '标准间', 2: '大床房', 3: '套房'}[value] || '未知类型';
        },
        roomStatusFormat(value) {
            return {0: '空闲中', 1: '已入住', 2: '维护中'}[value] || '未知状态';
        }

    }
})
