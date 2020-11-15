package com.rutgers.androidchess46;


import android.app.DialogFragment;


/**
 * Interface for Dialog to callback to activity with game title
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public interface IOnDialogClick {
    void onDialogPositiveClick(DialogFragment dialog);
    void onDialogNegativeClick(DialogFragment dialog);
}
