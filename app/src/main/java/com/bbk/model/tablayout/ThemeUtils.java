
package com.bbk.model.tablayout;

import android.content.Context;
import android.content.res.TypedArray;

class ThemeUtils {

    private static final int[] APPCOMPAT_CHECK_ATTRS = { android.support.design.R.attr.colorPrimary };

    static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        if (a != null) {
            a.recycle();
        }
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }
}
