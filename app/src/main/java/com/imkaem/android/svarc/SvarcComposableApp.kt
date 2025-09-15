import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imkaem.android.svarc.core.presentation.screens.HomeScreen
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

    NavHost(
        startDestination = "home",
        navController = navController,
    ) {
        composable(
            route = "home",
        ) {
            HomeScreen(
                onNavigateToReports = ::onNavigateToReports,
            )
        }

        composable(
            route = "reports",
        ) {

            ReportsScreen(
                onNavigateBack = ::onNavigateBack,
            )

        }


    }


//    NavHost(
//        startDestination = "home",
//        navController = navController,
//    )
}