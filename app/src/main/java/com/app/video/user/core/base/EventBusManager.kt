package com.app.video.user.core.base

import org.greenrobot.eventbus.EventBus

/**
 * EventBus 事件总线管理类。
 * 封装了 [EventBus] 的常用方法，提供安全的注册、注销和事件发布机制，避免重复注册等异常。
 */
object EventBusManager {

    /**
     * 注册事件订阅者。
     *
     * @param subscriber 订阅者对象（如 Activity 或 Fragment）
     */
    fun register(subscriber: Any) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }

    /**
     * 注销事件订阅者。
     *
     * @param subscriber 订阅者对象（如 Activity 或 Fragment）
     */
    fun unregister(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    /**
     * 发布普通事件。
     *
     * @param event 要发布的事件对象
     */
    fun post(event: Any) {
        EventBus.getDefault().post(event)
    }

    /**
     * 发布粘性事件。
     * 粘性事件可以被在其发布之后才注册的订阅者接收到。
     *
     * @param event 要发布的粘性事件对象
     */
    fun postSticky(event: Any) {
        EventBus.getDefault().postSticky(event)
    }

    /**
     * 移除指定的粘性事件。
     *
     * @param event 要移除的粘性事件对象
     */
    fun removeStickyEvent(event: Any) {
        EventBus.getDefault().removeStickyEvent(event)
    }

    /**
     * 获取指定类型的粘性事件。
     *
     * @param T 事件类型
     * @return 如果存在该类型的粘性事件则返回，否则返回 null
     */
    inline fun <reified T : Any> getStickyEvent(): T? {
        return EventBus.getDefault().getStickyEvent(T::class.java)
    }
}
