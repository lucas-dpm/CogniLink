package com.lucasdpm.cognilink.di

import androidx.room.Room
import com.google.android.gms.location.LocationServices
import com.lucasdpm.cognilink.data.datebase.CogniLinkDatabase
import com.lucasdpm.cognilink.data.repository.AuthRepository
import com.lucasdpm.cognilink.data.repository.AuthRepositoryImpl
import com.lucasdpm.cognilink.data.repository.DeckRepository
import com.lucasdpm.cognilink.data.repository.DeckRepositoryImpl
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.data.repository.FlashcardRepositoryImpl
import com.lucasdpm.cognilink.data.repository.StudyContextRepository
import com.lucasdpm.cognilink.data.repository.StudyContextRepositoryImpl
import com.lucasdpm.cognilink.data.repository.TermsRepository
import com.lucasdpm.cognilink.data.repository.TermsRepositoryImpl
import com.lucasdpm.cognilink.data.repository.UserRepository
import com.lucasdpm.cognilink.data.repository.UserRepositoryImpl
import com.lucasdpm.cognilink.data.service.AndroidGeofenceManager
import com.lucasdpm.cognilink.data.service.AndroidNetworkMonitor
import com.lucasdpm.cognilink.data.service.KtorAIService
import com.lucasdpm.cognilink.data.service.SystemNotificationService
import com.lucasdpm.cognilink.domain.repository.AIService
import com.lucasdpm.cognilink.domain.repository.NetworkMonitor
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.domain.service.GeofenceManager
import com.lucasdpm.cognilink.domain.usecase.CalculateDeckReviewCountUseCase
import com.lucasdpm.cognilink.domain.usecase.CalculateDifficultyLevelUseCase
import com.lucasdpm.cognilink.domain.usecase.CalculateSM2UseCase
import com.lucasdpm.cognilink.domain.usecase.CalculateUserRankingUseCase
import com.lucasdpm.cognilink.domain.usecase.UpdateUserStatsUseCase
import com.lucasdpm.cognilink.domain.usecase.ValidateBasicAnswerUseCase
import com.lucasdpm.cognilink.ui.viewmodels.AuthViewModel
import com.lucasdpm.cognilink.ui.viewmodels.ContextFormViewModel
import com.lucasdpm.cognilink.ui.viewmodels.DeckFormViewModel
import com.lucasdpm.cognilink.ui.viewmodels.DeckViewModel
import com.lucasdpm.cognilink.ui.viewmodels.FlashcardFormViewModel
import com.lucasdpm.cognilink.ui.viewmodels.StudySessionViewModel
import com.lucasdpm.cognilink.ui.viewmodels.HomeViewModel
import com.lucasdpm.cognilink.ui.viewmodels.IAGeneratorViewModel
import com.lucasdpm.cognilink.ui.viewmodels.ProfileViewModel
import com.lucasdpm.cognilink.ui.viewmodels.SettingsViewModel
import com.lucasdpm.cognilink.ui.viewmodels.ChangePasswordViewModel
import com.lucasdpm.cognilink.ui.viewmodels.TermsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val networkModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<DeckRepository> { DeckRepositoryImpl(get()) }
    singleOf(::FlashcardRepositoryImpl) { bind<FlashcardRepository>() }
    singleOf(::StudyContextRepositoryImpl) { bind<StudyContextRepository>() }
    singleOf(::TermsRepositoryImpl) { bind<TermsRepository>() }
    singleOf(::KtorAIService) { bind<AIService>() }
    single<GeofenceManager> { AndroidGeofenceManager(get()) }
    single { SystemNotificationService(get()) }
}

val domainModule = module {
    singleOf(::AppNotificationService)
    singleOf(::AndroidNetworkMonitor) { bind<NetworkMonitor>() }
    factoryOf(::CalculateDifficultyLevelUseCase)
    factoryOf(::CalculateDeckReviewCountUseCase)
    factoryOf(::CalculateUserRankingUseCase)
    factoryOf(::UpdateUserStatsUseCase)
    factoryOf(::ValidateBasicAnswerUseCase)
    factoryOf(::CalculateSM2UseCase)
}

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::ContextFormViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::DeckViewModel)
    viewModelOf(::DeckFormViewModel)
    viewModelOf(::FlashcardFormViewModel)
    viewModelOf(::StudySessionViewModel)
    viewModelOf(::TermsViewModel)
    viewModelOf(::IAGeneratorViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ChangePasswordViewModel)
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CogniLinkDatabase::class.java,
            "cognilink_db"
        ).fallbackToDestructiveMigration(true)
            .build()
    }
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single { get<CogniLinkDatabase>().userDao() }
    single { get<CogniLinkDatabase>().deckDao() }
    single { get<CogniLinkDatabase>().flashcardDao() }
    single { get<CogniLinkDatabase>().userStatsDao() }
    single { get<CogniLinkDatabase>().flashcardStatsDao() }
    single { get<CogniLinkDatabase>().studyContextDao() }
}

val appModule = listOf(
    networkModule,
    repositoryModule,
    domainModule,
    viewModelModule,
    databaseModule
)
