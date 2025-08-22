package com.worthmytime.data.repo;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TaxRepository_Factory implements Factory<TaxRepository> {
  private final Provider<Context> contextProvider;

  public TaxRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TaxRepository get() {
    return newInstance(contextProvider.get());
  }

  public static TaxRepository_Factory create(Provider<Context> contextProvider) {
    return new TaxRepository_Factory(contextProvider);
  }

  public static TaxRepository newInstance(Context context) {
    return new TaxRepository(context);
  }
}
