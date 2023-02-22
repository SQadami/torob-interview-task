package ir.torob.sample.ui

import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.torob.sample.R
import ir.torob.sample.databinding.ActivityHomeBinding
import ir.torob.ui.binding.BindingActivity

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>() {

    private val navController by lazy { bindNavGraph() }

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    /**
     * enables click listener on the back button
     */
    override fun onSupportNavigateUp() = navController.navigateUp()

    // Configure the navigation
    private fun bindNavGraph(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph_home)

        graph.startDestination = R.id.product_fragment

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)

        return navController
    }
}