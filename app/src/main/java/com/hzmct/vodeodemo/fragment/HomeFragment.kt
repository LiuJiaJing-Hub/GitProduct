package com.hzmct.vodeodemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzmct.vodeodemo.MyAdapter
import com.hzmct.vodeodemo.R
import com.hzmct.vodeodemo.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

/**
 * 首页 Fragment —— 展示视频列表，支持分页加载
 * @author liuJiaJing
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        initRecyclerView()
        observeState()
    }

    private fun initRecyclerView() {
        adapter = MyAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 视频列表项点击 → 跳转播放页
        adapter.setOnItemClickListener { video ->
            val bundle = Bundle().apply {
                putString("videoUrl", video.videoUrl)
            }
            findNavController().navigate(R.id.toPlayer, bundle)
        }

        // 滚动监听，滑到底部时自动加载下一页
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                // 当剩余可见项不足 3 个时触发加载
                if (visibleItemCount + firstVisiblePosition >= totalItemCount - 3 && dy > 0) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 更新列表数据：首次加载替换，后续追加由 RecyclerView 自动处理
                    adapter.setVideos(state.videoList)

                    // 控制底部加载指示器
                    progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }
}
