package mods.railcraft.api.core.items;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class TagList
{
  private static final SortedSet<String> tags = new TreeSet();
  private static final SortedSet<String> tagsImmutable = Collections.unmodifiableSortedSet(tags);

  public static void addTag(String tag) {
    tags.add(tag);
  }

  public static SortedSet<String> getTags() {
    return tagsImmutable;
  }
}