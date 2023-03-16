package io.outblock.lilico.widgets.easyfloat.data

import android.view.View
import io.outblock.lilico.widgets.easyfloat.anim.DefaultAnimator
import io.outblock.lilico.widgets.easyfloat.enums.ShowPattern
import io.outblock.lilico.widgets.easyfloat.enums.SidePattern
import io.outblock.lilico.widgets.easyfloat.interfaces.*
import io.outblock.lilico.widgets.easyfloat.utils.DefaultDisplayHeight

/**
 * @author: liuzhenfeng
 * @function: 浮窗的数据类，方便管理各属性
 * @date: 2019-07-29  10:14
 */
data class FloatConfig(

    // 浮窗的xml布局文件
    var layoutId: Int? = null,
    var layoutView: View? = null,

    // 当前浮窗的tag
    var floatTag: String? = null,

    // 是否可拖拽
    var dragEnable: Boolean = true,
    var isTouchEnable: Boolean = true,
    // 是否正在被拖拽
    var isDragging: Boolean = false,
    // 是否正在执行动画
    var isAnim: Boolean = false,
    // 是否显示
    var isShow: Boolean = false,
    // 是否包含EditText
    var hasEditText: Boolean = false,
    // 状态栏沉浸
    var immersionStatusBar: Boolean = false,

    // 浮窗的吸附方式（默认不吸附，拖到哪里是哪里）
    var sidePattern: SidePattern = SidePattern.DEFAULT,

    // 浮窗显示类型（默认只在当前页显示）
    var showPattern: ShowPattern = ShowPattern.CURRENT_ACTIVITY,

    // 宽高是否充满父布局
    var widthMatch: Boolean = false,
    var heightMatch: Boolean = false,

    // 浮窗的摆放方式，使用系统的Gravity属性
    var gravity: Int = 0,
    // 坐标的偏移量
    var offsetPair: Pair<Int, Int> = Pair(0, 0),
    // 固定的初始坐标，左上角坐标
    var locationPair: Pair<Int, Int> = Pair(0, 0),
    // ps：优先使用固定坐标，若固定坐标不为原点坐标，gravity属性和offset属性无效

    // Callbacks
    var invokeView: OnInvokeView? = null,
    var callbacks: OnFloatCallbacks? = null,
    // 通过Kotlin DSL设置回调，无需复写全部方法，按需复写
    var floatCallbacks: FloatCallbacks? = null,

    // 出入动画
    var floatAnimator: OnFloatAnimator? = DefaultAnimator(),

    // 设置屏幕的有效显示高度（不包含虚拟导航栏的高度），仅针对系统浮窗，一般不用复写
    var displayHeight: OnDisplayHeight = DefaultDisplayHeight(),

    // 不需要显示系统浮窗的页面集合，参数为类名
    val filterSet: MutableSet<String> = mutableSetOf(),

    var ignoreDragViewList: List<Int> = listOf(),

    var forceDragViewList: List<Int> = listOf(),

    var windowHeight: Int = 0,
    // 是否设置，当前创建的页面也被过滤
    internal var filterSelf: Boolean = false,
    // 是否需要显示，当过滤信息匹配上时，该值为false（用户手动调用隐藏，该值也为false，相当于手动过滤）
    internal var needShow: Boolean = true,

    // 是否可获取返回键等事件
    var hardKeyEventEnable: Boolean = false,
)