import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imkaem.android.svarc.core.presentation.screens.HomeScreen

@Composable
fun SvarcComposableApp(
) {

    val navController: NavHostController = rememberNavController()

    NavHost(
        startDestination = "home",
        navController = navController,
    ) {
        composable(
            route = "home",
        ) {
            HomeScreen()
        }
    }


//    NavHost(
//        startDestination = "home",
//        navController = navController,
//    )
}