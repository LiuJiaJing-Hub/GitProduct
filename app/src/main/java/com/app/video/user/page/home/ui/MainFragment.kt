package com.app.video.user.page.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.video.user.databinding.FragmentMainBinding
import com.app.video.user.page.home.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * 首页的核心 Fragment。
 * 负责展示首页的 UI 并响应用户的交互操作。
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    /**
     * 访问视图绑定的属性。仅在 onCreateView 和 onDestroyView 之间有效。
     */
    private val binding: FragmentMainBinding
        get() = _binding!!

    /** 懒加载获取对应的 ViewModel */
    private val viewModel: MainViewModel by viewModels()

    /**
     * 创建 Fragment 的视图层级。
     *
     * @param inflater 布局加载器
     * @param container 视图容器
     * @param savedInstanceState 恢复的状态包
     * @return Fragment 的根视图
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 视图创建完成后的回调。
     * 在此方法中进行视图初始化、事件监听绑定和数据观察。
     *
     * @param view 根视图
     * @param savedInstanceState 恢复的状态包
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etTopic.setText(viewModel.uiState.value.topic)
        observeUiState()
    }

    /**
     * Fragment 的视图销毁时调用。
     * 在此处释放 ViewBinding 避免内存泄漏。
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /**
     * 观察 ViewModel 中暴露的 UI 状态流。
     * 使用 repeatOnLifecycle 保证仅在 STARTED 生命周期内收集数据。
     */
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvConnectionState.text = state.connectionText
                    binding.tvMessageLog.text = state.messageLog
                }
            }
        }
    }
}