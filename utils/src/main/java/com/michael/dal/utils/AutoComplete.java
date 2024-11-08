package com.michael.dal.utils;

import java.util.List;

public class AutoComplete {
  private AutoComplete() {}

  // TODO we should use an actual algorithm for this
  public static List<String> findMostSimilar(
      final List<String> wordsToSearchFrom, final String text, final int limit) {
    return wordsToSearchFrom.stream()
        .filter(word -> word.toLowerCase().contains(text.toLowerCase()))
        .limit(limit)
        .toList();
  }
}
