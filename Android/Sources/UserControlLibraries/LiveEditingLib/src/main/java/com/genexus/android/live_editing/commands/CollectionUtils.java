package com.genexus.android.live_editing.commands;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

class CollectionUtils {
    public static <T> List<T> filter(@NonNull Iterable<T> iterable, @NonNull Predicate<T> predicate) {
        List<T> result = new ArrayList<>();

        for (T element : iterable) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }

        return result;
    }

    public interface Predicate<T> {
        boolean apply(@NonNull T input);
    }
}
