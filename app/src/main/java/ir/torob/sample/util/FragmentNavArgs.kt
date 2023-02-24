package ir.torob.sample.util

import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy

/**
 * This class created from [androidx.navigation.fragment.navArgs]
 * to not throw an Exception for null fragment argument
 * to handle the initial productKey arg in a clean way
 */
@MainThread
inline fun <reified Args : NavArgs> Fragment.navArgs(): NavArgsLazy<Args> =
    NavArgsLazy(Args::class) {
        arguments ?: bundleOf(KEY_PRODUCT to INITIAL_PRODUCT_KEY)
    }

@MainThread
fun SavedStateHandle.productKey() = get<String>(KEY_PRODUCT) ?: INITIAL_PRODUCT_KEY

const val KEY_PRODUCT = "productKey"
const val INITIAL_PRODUCT_KEY = "c248e83f-2af8-4957-8a91-5e72a0f3acef"