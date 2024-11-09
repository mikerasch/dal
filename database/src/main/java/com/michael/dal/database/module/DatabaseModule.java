package com.michael.dal.database.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.michael.dal.database.repositories.CurlActivityRepository;
import com.michael.dal.database.repositories.impl.CurlActivityRepositoryImpl;
import com.michael.dal.database.routines.PreviousCurlActivityPurge;
import com.michael.dal.database.routines.common.Routine;

public class DatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CurlActivityRepository.class).to(CurlActivityRepositoryImpl.class);

    Multibinder<Routine> routineMultibinder = Multibinder.newSetBinder(binder(), Routine.class);
    routineMultibinder.addBinding().to(PreviousCurlActivityPurge.class);
  }
}
