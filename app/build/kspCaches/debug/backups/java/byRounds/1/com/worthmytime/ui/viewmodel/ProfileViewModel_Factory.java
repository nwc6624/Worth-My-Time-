package com.worthmytime.ui.viewmodel;

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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<TaxRepository> taxRepositoryProvider;

  public ProfileViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider,
      Provider<TaxRepository> taxRepositoryProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.taxRepositoryProvider = taxRepositoryProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(profileRepositoryProvider.get(), taxRepositoryProvider.get());
  }

  public static ProfileViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<TaxRepository> taxRepositoryProvider) {
    return new ProfileViewModel_Factory(profileRepositoryProvider, taxRepositoryProvider);
  }

  public static ProfileViewModel newInstance(ProfileRepository profileRepository,
      TaxRepository taxRepository) {
    return new ProfileViewModel(profileRepository, taxRepository);
  }
}
