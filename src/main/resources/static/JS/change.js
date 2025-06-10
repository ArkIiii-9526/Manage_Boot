document.addEventListener('DOMContentLoaded', function () {
    const loginSec=document.querySelector('.login-section')
    const loginLink=document.querySelector('.login-link')
    const registerLink=document.querySelector('.register-link')
    registerLink.addEventListener('click',()=>{
        loginSec.classList.add('active')
    })
    loginLink.addEventListener('click',()=>{
        loginSec.classList.remove('active')
    })
})

