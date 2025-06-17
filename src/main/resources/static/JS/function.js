new Vue({
    el: '#app',
    data: {
        todayGuests: 86,
        availableRooms: 42,
        upcomingReservations: 127,
        vipGuests: 15,
        unreadCount: 3,
        messages: [
            { sender: '张经理', time: '今天 10:25', content: '今天下午3点有VIP客户入住，请准备好总统套房和相关接待事宜。', read: false },
            { sender: '王主管', time: '昨天 15:45', content: '305房间的空调系统需要检修，请安排工程部明天上午处理。', read: true },
            { sender: '李会计', time: '今天 08:30', content: '请提交上个月的水电费用报表，财务部需要完成月度结算。', read: false },
            { sender: '系统通知', time: '今天 09:15', content: '系统将在今晚12点进行维护升级，预计维护时间30分钟。', read: false },
            { sender: '客服中心', time: '前天 11:20', content: '收到客户对312房间清洁度的表扬，已记录在案。', read: true }
        ],
        customerChart: null,
        roomChart: null
    },
    mounted() {
        // 初始化图表
        this.initCustomerChart();
        this.initRoomChart();

        // 模拟实时数据更新
        setInterval(() => {
            this.updateStats();
        }, 5000);
    },
    methods: {
        // 初始化顾客图表（折线图）
        initCustomerChart() {
            this.customerChart = echarts.init(document.getElementById('customer-chart'));

            const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
            const data = days.map(() => Math.floor(Math.random() * 51) + 50);

            const option = {
                tooltip: {
                    trigger: 'axis',
                    formatter: '{b}: {c} 位客人'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    top: '10%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    data: days,
                    axisLine: {
                        lineStyle: {
                            color: '#999'
                        }
                    },
                    axisLabel: {
                        color: '#666'
                    }
                },
                yAxis: {
                    type: 'value',
                    min: 40,
                    max: 110,
                    axisLine: {
                        show: false
                    },
                    axisLabel: {
                        color: '#666'
                    },
                    splitLine: {
                        lineStyle: {
                            color: '#eee'
                        }
                    }
                },
                series: [{
                    data: data,
                    type: 'line',
                    smooth: true,
                    symbol: 'circle',
                    symbolSize: 8,
                    itemStyle: {
                        color: '#0078d4'
                    },
                    lineStyle: {
                        width: 3
                    },
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: 'rgba(0, 120, 212, 0.3)' },
                            { offset: 1, color: 'rgba(0, 120, 212, 0.05)' }
                        ])
                    }
                }]
            };

            this.customerChart.setOption(option);

            // 响应窗口大小变化
            window.addEventListener('resize', () => {
                this.customerChart.resize();
            });
        },

        // 初始化房间图表（环形图）
        initRoomChart() {
            this.roomChart = echarts.init(document.getElementById('room-chart'));

            // 房间状态数据
            const roomStatus = [
                { value: Math.floor(Math.random() * 30) + 20, name: '空闲中' },
                { value: Math.floor(Math.random() * 40) + 40, name: '已入住' },
                { value: Math.floor(Math.random() * 10) + 5, name: '维护中' }
            ];

            const option = {
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                    orient: 'horizontal',
                    bottom: 0,
                    data: roomStatus.map(item => item.name),
                    textStyle: {
                        color: '#666'
                    }
                },
                series: [
                    {
                        name: '房间状态',
                        type: 'pie',
                        radius: ['45%', '70%'],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: '18',
                                fontWeight: 'bold',
                                color: '#333'
                            }
                        },
                        labelLine: {
                            show: false
                        },
                        data: roomStatus,
                        color: ['#36c6a9', '#0078d4', '#f4516c']
                    }
                ]
            };

            this.roomChart.setOption(option);

            // 响应窗口大小变化
            window.addEventListener('resize', () => {
                this.roomChart.resize();
            });
        },

        // 更新统计数据
        updateStats() {
            // 模拟数据变化
            this.todayGuests = Math.floor(Math.random() * 20) + 80;
            this.availableRooms = Math.floor(Math.random() * 20) + 30;
            this.upcomingReservations = Math.floor(Math.random() * 40) + 100;
            this.vipGuests = Math.floor(Math.random() * 5) + 10;

            // 更新顾客图表
            const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
            const newData = days.map(() => Math.floor(Math.random() * 51) + 50);

            this.customerChart.setOption({
                xAxis: {
                    data: days
                },
                series: [{
                    data: newData
                }]
            });

            // 更新房间图表
            const newRoomStatus = [
                { value: Math.floor(Math.random() * 30) + 20, name: '空闲中' },
                { value: Math.floor(Math.random() * 40) + 40, name: '已入住' },
                { value: Math.floor(Math.random() * 10) + 5, name: '维护中' }
            ];

            this.roomChart.setOption({
                legend: {
                    data: newRoomStatus.map(item => item.name)
                },
                series: [{
                    data: newRoomStatus
                }]
            });
        },

        // 标记消息为已读
        markAsRead(index) {
            if (!this.messages[index].read) {
                this.messages[index].read = true;
                this.unreadCount--;
            }
        },

        // 删除消息
        deleteMessage(index) {
            if (!this.messages[index].read) {
                this.unreadCount--;
            }
            this.messages.splice(index, 1);
        }
    }
});