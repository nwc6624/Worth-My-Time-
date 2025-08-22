package com.worthmytime.data.repo;

import com.worthmytime.data.db.dao.GoalDao;
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
public final class GoalsRepository_Factory implements Factory<GoalsRepository> {
  private final Provider<GoalDao> goalDaoProvider;

  public GoalsRepository_Factory(Provider<GoalDao> goalDaoProvider) {
    this.goalDaoProvider = goalDaoProvider;
  }

  @Override
  public GoalsRepository get() {
    return newInstance(goalDaoProvider.get());
  }

  public static GoalsRepository_Factory create(Provider<GoalDao> goalDaoProvider) {
    return new GoalsRepository_Factory(goalDaoProvider);
  }

  public static GoalsRepository newInstance(GoalDao goalDao) {
    return new GoalsRepository(goalDao);
  }
}
