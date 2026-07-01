package com.hzmct.vodeodemo

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.WindowCompat

class LoginActivity : AppCompatActivity() {
    private var username: String = ""
    private var password: String = ""
    private lateinit var et_username: AppCompatEditText
    private lateinit var et_password: AppCompatEditText
    private lateinit var iv_eye: AppCompatImageView
    private var eyeOpen : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 关键代码：状态栏图标白色
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        // false = 白色图标；true = 黑色图标
        controller.isAppearanceLightStatusBars = false
        setContentView(R.layout.activity_login)
        et_username= findViewById(R.id.et_username)
        et_password= findViewById(R.id.et_password)
        iv_eye= findViewById(R.id.iv_eye)
        val button=findViewById<AppCompatButton>(R.id.btn_login)
        iv_eye.setOnClickListener {
            if(eyeOpen){
                eyeOpen = false
                iv_eye.setImageResource(R.drawable.icon_eye_show)
                password = et_password.text.toString()
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                eyeOpen = true
                iv_eye.setImageResource(R.drawable.icon_eye_closs)
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }
        button.setOnClickListener { login() }
        et_username.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                username = et_username.text.toString().trim()
                if (username.isEmpty()) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show()
                }
            }
        }
        et_password.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                password = et_password.text.toString().trim()
                if (password.isEmpty()) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    private fun login() {
        password = et_password.text.toString().trim()
        username = et_username.text.toString().trim()
        if (checkoutUsername() && checkoutPassword()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "用户名或密码输入不正确", Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkoutPassword () : Boolean{
        password = et_password.text.toString().trim()
        if(password.isNotEmpty()){
            return password == "123"
        }else{
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    private fun checkoutUsername () : Boolean {
        username = et_username.text.toString().trim()
        if (username.isEmpty()) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return username == "123"
        }
    }
}


