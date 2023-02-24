package ir.torob.sample.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import ir.torob.core.extension.observeWithLifecycle
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.sample.R
import ir.torob.sample.databinding.FragmentProductBinding
import ir.torob.sample.ui.product.adapter.ProductGridDecoration
import ir.torob.sample.ui.product.adapter.ProductLoadStateAdapter
import ir.torob.sample.ui.product.adapter.SimilarProductAdapter
import ir.torob.ui.binding.BindingFragment
import ir.torob.ui.extension.dimenRes
import ir.torob.ui.widget.snackbar.showSnackBar

@AndroidEntryPoint
class ProductFragment : BindingFragment<FragmentProductBinding>(R.layout.fragment_product) {

    private val viewModel: ProductViewModel by viewModels()

    private val itemClickListener = object : SimilarProductAdapter.OnItemClickListener {
        override fun onItemClicked(product: SimilarEntryWithProduct) {
            //TODO: show clicked product detail page
        }
    }
    private val adapter = SimilarProductAdapter(itemClickListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        initData()
    }

    private fun bindViews() {
        bindRecyclerView()
    }

    private fun bindRecyclerView() {
        val loadStateAdapter = ProductLoadStateAdapter { adapter.retry() }

        adapter.addLoadStateListener { loadState ->
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let { showSnackBar("${it.error}") }
        }

        binding.recyclerView.let {
            it.addItemDecoration(
                ProductGridDecoration(
                    spacing = R.dimen.spacing_grid.dimenRes(requireContext())
                )
            )
            it.adapter = adapter.withLoadStateFooter(loadStateAdapter)
        }
    }

    private fun initData() =
        viewModel.let { vm ->
            vm.isLoading.observeWithLifecycle(this) {
                //TODO: show loading
            }
            vm.pendingActions.observeWithLifecycle(this) {
                it?.runOnContent {
                    when (this) {
                        is ErrorMessageAction -> showSnackBar(message)
                    }
                }
            }
            vm.pagedList.observeWithLifecycle(this) {
                adapter.submitData(lifecycle, it)
            }
        }
}