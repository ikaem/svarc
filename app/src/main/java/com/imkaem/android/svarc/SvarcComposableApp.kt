import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imkaem.android.svarc.costs.presentation.screens.HomeScreen

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