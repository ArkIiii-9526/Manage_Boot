new Vue({
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

        ShowAlert(title, message = '', icon = 'error') {
            const config = {
                title: title,
                text: message,
                icon: icon,
                timerProgressBar: true,
                showConfirmButton: icon === 'error'
            };
            if (icon === 'success') {
                config.timer = 2000;
                config.showConfirmButton = false;
            } else {
                config.showConfirmButton = true;
            }
            Swal.fire(config);
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
                if (response.data.success) {
                    const result = response.data.data;
                    this.rooms = result.rooms;
                    this.currentPage = result.currentPage;
                    this.totalPages = result.totalPages;
                    this.totalRecords = result.totalRecords;
                }
            } catch (error) {
                const message = error.response?.data?.message || error.message;
                this.showAlert('操作失败', message);
            } finally {
                this.loading = false;
            }
        },

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

        editRoom(Room) {
            this.isEditMode = true;
            this.editingRoom = {...Room};
            this.showEditDialog = true;
        },

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

                if (response.data.success) {
                    this.showAlert('操作成功', '房间信息已更新', 'success');
                    this.showEditDialog = false;
                    this.fetchRooms().catch(error => {
                        console.error('获取房间列表失败:', error);
                    })
                } else {
                    this.showAlert('操作失败', response.data.message);
                }
            } catch (error) {
                const message = error.response?.data?.message || error.message;
                this.showAlert('操作失败', message);
            }
        },

        //删除顾客
        async deleteEdit(room) {
            const result = await Swal.fire({
                title: '确定删除？',
                text: `确定要删除房间 ${room.roomId} 吗？此操作不可恢复`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '确定删除',
                cancelButtonText: '取消'
            });
            if (!result.isConfirmed) return;
            try {
                const response = await axios.delete(`/api/deleteRooms/${Room.roomId}`);
                if (response.data.success) {
                    await this.fetchRooms();
                    this.showAlert('操作成功', '房间已删除', 'success');
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
