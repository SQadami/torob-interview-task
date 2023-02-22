package ir.torob.sample.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.torob.core.extension.observeWithLifecycle
import ir.torob.sample.R
import ir.torob.sample.databinding.FragmentProductBinding
import ir.torob.ui.binding.BindingFragment
import ir.torob.ui.widget.snackbar.showSnackBar

@AndroidEntryPoint
class ProductFragment : BindingFragment<FragmentProductBinding>(R.layout.fragment_product) {

    private val viewModel: ProductViewModel by viewModels()

    companion object {

        private const val ARG_PRODUCT_KEY = "typeProductKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        initData()
    }

    private fun bindViews() {
        TODO("bind views")
    }

    private fun initData() =
        viewModel.let { vm ->
            vm.isLoading.observeWithLifecycle(this) {
                TODO("show loading")
            }
            vm.pendingActions.observeWithLifecycle(this) {
                it?.runOnContent {
                    when (this) {
                        is ErrorMessageAction -> showSnackBar(message)
                    }
                }
            }
        }
}