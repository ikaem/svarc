import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imkaem.android.svarc.core.presentation.screens.HomeScreen
import com.imkaem.android.svarc.dev.presentation.screens.DevScreen
import com.imkaem.android.svarc.reports.presentation.screens.ReportsScreen

@Composable
fun SvarcComposableApp(
) {

    val navController: NavHostController = rememberNavController()


    fun onNavigateBack() {
        navController.popBackStack()
    }

    fun onNavigateToReports() {
        navController.navigate("reports")
    }

    fun onNavigateToDev() {
        navController.navigate("dev")
    }

    NavHost(
        startDestination = "home",
        navController = navController,
    ) {
        composable(
            route = "home",
        ) {
            HomeScreen(
                onNavigateToReports = ::onNavigateToReports,
                onNavigateToDev = ::onNavigateToDev
            )
        }

        composable(
            route = "reports",
        ) {
            ReportsScreen(
                onNavigateBack = ::onNavigateBack,
            )
        }

        composable(
            route = "dev",
        ) {
            DevScreen(
                onNavigateBack = ::onNavigateBack
            )
        }


    }


//    NavHost(
//        startDestination = "home",
//        navController = navController,
//    )
}