package com.example.fundamentaltest1.detail.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentaltest1.UserAdapter
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.databinding.FragmentFollowsBinding
import com.example.fundamentaltest1.detail.DetailViewModel
import com.example.fundamentaltest1.utils.Result


class FollowsFragment : Fragment() {

    private var binding: FragmentFollowsBinding? = null
    private val adapter by lazy {
        UserAdapter {

        }
    }
    var type = 0
    private val viewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)

            }

            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)

            }
        }

    }

    private fun manageResultFollows(state: Result) {
        when (state) {
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<items>)
            }

            is Result.Error -> {
                Toast.makeText(requireActivity(), state.exception.message, Toast.LENGTH_SHORT)
                    .show()
            }

            is Result.Loading -> {
                binding?.progressBar2?.isVisible = state.isLoading

            }
        }

    }

    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        @JvmStatic
        fun newInstance(type: Int) = FollowsFragment().apply { this.type = type }
    }
}