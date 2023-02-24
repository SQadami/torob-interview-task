package ir.torob.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.torob.sample.R
import ir.torob.sample.databinding.ActivityHomeBinding
import ir.torob.ui.binding.BindingActivity

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    /**
     * enables click listener on the back button
     */
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ir.torob.ui.R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }
}