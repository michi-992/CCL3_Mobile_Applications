package com.cc221043.ccl3_mobileapplications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cc221043.ccl3_mobileapplications.data.BookDatabase
import com.cc221043.ccl3_mobileapplications.ui.theme.CCL3MobileApplicationsTheme
import com.cc221043.ccl3_mobileapplications.ui.view.MainView
import com.cc221043.ccl3_mobileapplications.ui.view.Screen
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel
import com.cc221043.ccl3_mobileapplications.ui.view_model.OnboardingViewModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {

    private val onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

    private val onboardingViewModel by viewModels<OnboardingViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OnboardingViewModel(onboardingDataStore) as T
            }
        }
    }

    private val db by lazy {
    Room.databaseBuilder(this, BookDatabase::class.java, "BookDatabase.db").build()
}

    private val mainViewModel by viewModels<MainViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao, this@MainActivity, onboardingViewModel) as T
                }
            }
        }
    )

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Handle the returned image URI here
            uri?.let {
                mainViewModel.updateImageURI(it)
                println(mainViewModel.mainViewState.value.selectedImageURI)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            CCL3MobileApplicationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(mainViewModel, onboardingViewModel, pickImageLauncher)
                }
            }
        }
    }
}