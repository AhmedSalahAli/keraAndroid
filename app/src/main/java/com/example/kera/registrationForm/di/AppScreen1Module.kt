
import com.example.kera.registrationForm.screen2.AppScreen2ViewModel
import com.example.kera.registrationForm.screen1.AppScreen1ViewModel
import com.example.kera.registrationForm.screen3.AppScreen3ViewModel
import com.example.kera.registrationForm.screen4.AppScreen4ViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appScreensModule = module {
    viewModel {
        AppScreen1ViewModel(get())
    }
    viewModel {
        AppScreen2ViewModel(get())
    }
    viewModel {
        AppScreen3ViewModel(get())
    }
    viewModel {
        AppScreen4ViewModel(get())
    }
}