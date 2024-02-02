package com.cc221043.ccl3_mobileapplications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.cc221043.ccl3_mobileapplications.data.BookDatabase
import com.cc221043.ccl3_mobileapplications.ui.theme.CCL3MobileApplicationsTheme
import com.cc221043.ccl3_mobileapplications.ui.view.MainView
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.cc221043.ccl3_mobileapplications.ui.view_model.OnboardingViewModel

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
                    return MainViewModel(db.dao, this@MainActivity) as T
                }
            }
        }
    )

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                mainViewModel.updateImageURI(it)
                println(mainViewModel.mainViewState.value.selectedImageURI)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            CCL3MobileApplicationsTheme {
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