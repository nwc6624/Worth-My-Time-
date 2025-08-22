package com.worthmytime.ui.viewmodel;

import com.worthmytime.data.repo.GoalsRepository;
import com.worthmytime.data.repo.HistoryRepository;
import com.worthmytime.data.repo.ProfileRepository;
import com.worthmytime.data.repo.TaxRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class CalculatorViewModel_Factory implements Factory<CalculatorViewModel> {
  private final Provider<HistoryRepository> historyRepositoryProvider;

  private final Provider<GoalsRepository> goalsRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<TaxRepository> taxRepositoryProvider;

  public CalculatorViewModel_Factory(Provider<HistoryRepository> historyRepositoryProvider,
      Provider<GoalsRepository> goalsRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<TaxRepository> taxRepositoryProvider) {
    this.historyRepositoryProvider = historyRepositoryProvider;
    this.goalsRepositoryProvider = goalsRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.taxRepositoryProvider = taxRepositoryProvider;
  }

  @Override
  public CalculatorViewModel get() {
    return newInstance(historyRepositoryProvider.get(), goalsRepositoryProvider.get(), profileRepositoryProvider.get(), taxRepositoryProvider.get());
  }

  public static CalculatorViewModel_Factory create(
      Provider<HistoryRepository> historyRepositoryProvider,
      Provider<GoalsRepository> goalsRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<TaxRepository> taxRepositoryProvider) {
    return new CalculatorViewModel_Factory(historyRepositoryProvider, goalsRepositoryProvider, profileRepositoryProvider, taxRepositoryProvider);
  }

  public static CalculatorViewModel newInstance(HistoryRepository historyRepository,
      GoalsRepository goalsRepository, ProfileRepository profileRepository,
      TaxRepository taxRepository) {
    return new CalculatorViewModel(historyRepository, goalsRepository, profileRepository, taxRepository);
  }
}
