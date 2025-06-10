new Vue({
    el: '#app',
    data: {
        activeForm: 'login',
        loginForm: {
            email: '',
            password: '',
            remember: false
        },
        registerForm: {
            username: '',
            email: '',
            password: '',
            agreed: false
        },
        loginErrors: {
            email: '',
            password: ''
        },
        registerErrors: {
            username: '',
            email: '',
            password: ''
        },
        globalError: ''
    },
    watch: {
        'loginForm.email': function(newVal) {
            if (!this.validateEmail(newVal)) {
                this.loginErrors.email = '请输入有效的电子邮件地址';
            } else {
                this.loginErrors.email = '';
            }
        },
        'loginForm.password': function(newVal) {
            if (!newVal) {
                this.loginErrors.password = '密码不能为空';
            } else {
                this.loginErrors.password = '';
            }
        },
        'registerForm.username': function(newVal) {
            if (newVal.length < 3) {
                this.registerErrors.username = '用户名需至少3位';
            } else if (newVal.length > 16) {
                this.registerErrors.username = '用户名最多16位';
            } else {
                this.registerErrors.username = '';
            }
        },
        'registerForm.email': function(newVal) {
            if (!this.validateEmail(newVal)) {
                this.registerErrors.email = '请输入有效的电子邮件地址';
            } else {
                this.registerErrors.email = '';
            }
        },
        'registerForm.password': function(newVal) {
            if (!this.validatePassword(newVal)) {
                this.registerErrors.password = '密码需6-18位，包含大小写字母、数字和特殊符号';
            } else {
                this.registerErrors.password = '';
            }
        }
    },
    methods: {

        validateEmail(email) {
            const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            return regex.test(email);
        },

        validatePassword(password) {
            const regex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*])[A-Za-z\d@#$%^&*]{6,18}$/;
            return regex.test(password);
        },

        submitLogin() {
            this.loginErrors = { email: '', password: '' };

            let isValid = true;

            if (!this.validateEmail(this.loginForm.email)) {
                this.loginErrors.email = '请输入有效的电子邮件地址';
                isValid = false;
            }

            if (!this.loginForm.password) {
                this.loginErrors.password = '密码不能为空';
                isValid = false;
            }

            if (!isValid) return;

            axios.post('/login', {
                email: this.loginForm.email,
                password: this.loginForm.password
            })
                .then(response => {
                    if (response.data.success) {
                        window.location.href = '/View/function.html';
                    } else {
                        this.showGlobalError(response.data.message || '登录失败');
                    }
                })
                .catch(error => {
                    this.showGlobalError('网络连接异常，请稍后重试');
                    console.error('登录错误:', error);
                });
        },

        submitRegister() {
            this.registerErrors = { username: '', email: '', password: '' };

            // 验证表单
            let isValid = true;

            if (this.registerForm.username.length < 3) {
                this.registerErrors.username = '用户名需至少3位';
                isValid = false;
            } else if (this.registerForm.username.length > 16) {
                this.registerErrors.username = '用户名最多16位';
                isValid = false;
            }

            if (!this.validateEmail(this.registerForm.email)) {
                this.registerErrors.email = '请输入有效的电子邮件地址';
                isValid = false;
            }

            if (!this.validatePassword(this.registerForm.password)) {
                this.registerErrors.password = '密码需6-18位，包含大小写字母、数字和特殊符号';
                isValid = false;
            }

            if (!this.registerForm.agreed) {
                this.showGlobalError('请同意用户协议');
                isValid = false;
            }

            if (!isValid) return;

            axios.post('/register', {
                username: this.registerForm.username,
                email: this.registerForm.email,
                password: this.registerForm.password
            })
                .then(response => {
                    if (response.data.success) {
                        window.location.href = '/View/lr.html';
                    } else {
                        this.showGlobalError(response.data.message || '注册失败');
                    }
                })
                .catch(error => {
                    this.showGlobalError('网络连接异常，请稍后重试');
                    console.error('注册错误:', error);
                });
        },

        showGlobalError(message) {
            this.globalError = message;
            Swal.fire({
                title: message,
                timer: 1000,
                timerProgressBar: true,
                showConfirmButton: false
            });
        }
    }
});