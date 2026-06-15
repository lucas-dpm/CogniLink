package com.lucasdpm.cognilink.di

import androidx.room.Room
import com.lucasdpm.cognilink.data.datebase.CogniLinkDatabase
import com.lucasdpm.cognilink.data.repository.AuthRepository
import com.lucasdpm.cognilink.data.repository.AuthRepositoryImpl
import com.lucasdpm.cognilink.data.repository.DeckRepository
import com.lucasdpm.cognilink.data.repository.DeckRepositoryImpl
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.data.repository.FlashcardRepositoryImpl
import com.lucasdpm.cognilink.data.repository.TermsRepository
import com.lucasdpm.cognilink.data.repository.TermsRepositoryImpl
import com.lucasdpm.cognilink.data.repository.UserRepository
import com.lucasdpm.cognilink.data.repository.UserRepositoryImpl
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.domain.usecase.CalculateDeckReviewCountUseCase
import com.lucasdpm.cognilink.domain.usecase.CalculateDifficultyLevelUseCase
import com.lucasdpm.cognilink.domain.usecase.CalculateUserRankingUseCase
import com.lucasdpm.cognilink.domain.usecase.ValidateBasicAnswerUseCase
import com.lucasdpm.cognilink.ui.viewmodels.AuthViewModel
import com.lucasdpm.cognilink.ui.viewmodels.DeckFormViewModel
import com.lucasdpm.cognilink.ui.viewmodels.DeckViewModel
import com.lucasdpm.cognilink.ui.viewmodels.FlashcardFormViewModel
import com.lucasdpm.cognilink.ui.viewmodels.StudySessionViewModel
import com.lucasdpm.cognilink.ui.viewmodels.HomeViewModel
import com.lucasdpm.cognilink.ui.viewmodels.IAGeneratorViewModel
import com.lucasdpm.cognilink.ui.viewmodels.ProfileViewModel
import com.lucasdpm.cognilink.ui.viewmodels.TermsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<DeckRepository> { DeckRepositoryImpl(get(), get()) }
    singleOf(::FlashcardRepositoryImpl) { bind<FlashcardRepository>() }
    singleOf(::TermsRepositoryImpl) { bind<TermsRepository>() }
}

val domainModule = module {
    singleOf(::AppNotificationService)
    factoryOf(::CalculateDifficultyLevelUseCase)
    factoryOf(::CalculateDeckReviewCountUseCase)
    factoryOf(::CalculateUserRankingUseCase)
    factoryOf(::ValidateBasicAnswerUseCase)
}

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::DeckViewModel)
    viewModelOf(::DeckFormViewModel)
    viewModelOf(::FlashcardFormViewModel)
    viewModelOf(::StudySessionViewModel)
    viewModelOf(::TermsViewModel)
    viewModelOf(::IAGeneratorViewModel)
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
    single { get<CogniLinkDatabase>().userDao() }
    single { get<CogniLinkDatabase>().deckDao() }
    single { get<CogniLinkDatabase>().flashcardDao() }
    single { get<CogniLinkDatabase>().userStatsDao() }
    single { get<CogniLinkDatabase>().flashcardStatsDao() }
}

val appModule = listOf(
    repositoryModule,
    domainModule,
    viewModelModule,
    databaseModule
)
