package ir.torob.sample.ui.product

sealed class ProductUiAction

data class ErrorMessageAction(val message: String?) : ProductUiAction()