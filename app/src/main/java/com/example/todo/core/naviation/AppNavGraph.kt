package com.example.todo.core.naviation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo.feature.logIn.GoogleSignInViewModel
import com.example.todo.feature.logIn.LoginScreen
import com.example.todo.feature.logIn.SignUpScreen
import com.example.todo.presentation.myDay.MyDayScreen
import com.example.todo.presentation.update.UpdateScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(googleSignInViewModel: GoogleSignInViewModel) {

    val navController = rememberNavController()
    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (isUserLoggedIn) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("home") {
            MyDayScreen(navController)
        }

        composable(
            route = "update/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            UpdateScreen(taskId = taskId, onBack = { navController.popBackStack() })
        }

        composable("signup") {
            SignUpScreen(navController ,googleSignInViewModel)
        }
        composable("login") {
            LoginScreen(navController ,googleSignInViewModel)
        }


    }
}