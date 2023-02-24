package ir.torob.sample.ui.product

import androidx.core.net.toUri
import androidx.navigation.NavController

fun NavController.navigateToProduct(productKey: String) {
    navigate("app.torob://product/$productKey".toUri())
}