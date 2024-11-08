package com.michael.dal.utils;

import java.util.List;
import java.util.function.BooleanSupplier;

public class SequentialBooleanChecker {
  private final List<BooleanSupplier> booleanSuppliers;

  public SequentialBooleanChecker(final List<BooleanSupplier> booleanSuppliers) {
    this.booleanSuppliers = booleanSuppliers;
  }

  /**
   * Check if all the boolean suppliers return true. This is run in sequence, if one of the boolean
   * suppliers returns false, the method will return false immediately.
   */
  public boolean check() {
    return booleanSuppliers.stream().allMatch(BooleanSupplier::getAsBoolean);
  }
}
