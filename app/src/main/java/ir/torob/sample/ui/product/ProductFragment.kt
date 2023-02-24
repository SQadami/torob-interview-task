package ir.torob.sample.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.torob.core.extension.observeWithLifecycle
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.sample.R
import ir.torob.sample.databinding.FragmentProductBinding
import ir.torob.sample.ui.product.similar.SimilarProductDecoration
import ir.torob.sample.ui.product.similar.SimilarProductLoadAdapter
import ir.torob.sample.ui.product.similar.SimilarProductAdapter
import ir.torob.sample.ui.product.detail.ProductDetailAdapter
import ir.torob.sample.util.navArgs
import ir.torob.ui.binding.BindingFragment
import ir.torob.ui.extension.dimenRes
import ir.torob.ui.extension.onClick
import ir.torob.ui.extension.visibleIf
import ir.torob.ui.widget.recyclerview.layout_manager.GridManager
import ir.torob.ui.widget.snackbar.showSnackBar

@AndroidEntryPoint
class ProductFragment : BindingFragment<FragmentProductBinding>(R.layout.fragment_product) {

    private val viewModel: ProductViewModel by viewModels()
    private val args: ProductFragmentArgs by navArgs()

    private val itemClickListener = object : SimilarProductAdapter.OnItemClickListener {
        override fun onItemClicked(product: SimilarEntryWithProduct) {
            findNavController().navigateToProduct(product.similar.randomKey!!)
        }
    }
    private val similarProductAdapter = SimilarProductAdapter(itemClickListener)
    private val productDetailAdapter = ProductDetailAdapter(args.productKey)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        initData()
    }

    private fun bindViews() {
        bindAppbar()
        bindRecyclerView()
    }

    private fun bindAppbar() =
        with(binding.layoutAppbar) {
            btnBack.visibleIf(findNavController().backQueue.size > 2, gone = false)
            btnBack.onClick { findNavController().navigateUp() }
        }

    private fun bindRecyclerView() {
        val loadStateAdapter = SimilarProductLoadAdapter { similarProductAdapter.retry() }

        similarProductAdapter.addLoadStateListener { loadState ->
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let { showSnackBar("${it.error}") }
        }

        binding.recyclerView.let {
            it.addItemDecoration(
                SimilarProductDecoration(
                    spacing = R.dimen.spacing_grid.dimenRes(requireContext())
                )
            )
            it.layoutManager = createLayoutManager()
            it.adapter = ConcatAdapter(
                productDetailAdapter,
                similarProductAdapter.withLoadStateFooter(loadStateAdapter),
            )
        }
    }

    private fun initData() =
        viewModel.let { vm ->
            vm.similarProductPagination.observeWithLifecycle(this) {
                similarProductAdapter.submitData(lifecycle, it)
            }
            vm.productDetail.observeWithLifecycle(this) {
                binding.layoutAppbar.title.text = it.name1
                productDetailAdapter.setProduct(it)
            }
        }

    private fun createLayoutManager() =
        GridManager(context, SPAN_COUNT).apply {
            orientation = LinearLayoutManager.VERTICAL
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // set full span size for product detail
                    return if (position == 0) SPAN_COUNT else 1
                }

            }
        }
}

private const val SPAN_COUNT = 2