package com.example.cognilink.di

import androidx.room.Room
import com.example.cognilink.data.datebase.CogniLinkDatabase
import com.example.cognilink.data.repository.AuthRepository
import com.example.cognilink.data.repository.AuthRepositoryImpl
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.data.repository.TermsRepository
import com.example.cognilink.data.repository.TermsRepositoryImpl
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.domain.usecase.CalculateDeckReviewCountUseCase
import com.example.cognilink.domain.usecase.CalculateDifficultyLevelUseCase
import com.example.cognilink.domain.usecase.CalculateUserRankingUseCase
import com.example.cognilink.domain.usecase.ValidateBasicAnswerUseCase
import com.example.cognilink.ui.viewmodels.AuthViewModel
import com.example.cognilink.ui.viewmodels.DeckFormViewModel
import com.example.cognilink.ui.viewmodels.DeckViewModel
import com.example.cognilink.ui.viewmodels.FlashcardFormViewModel
import com.example.cognilink.ui.viewmodels.StudySessionViewModel
import com.example.cognilink.ui.viewmodels.HomeViewModel
import com.example.cognilink.ui.viewmodels.IAGeneratorViewModel
import com.example.cognilink.ui.viewmodels.ProfileViewModel
import com.example.cognilink.ui.viewmodels.TermsViewModel
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
