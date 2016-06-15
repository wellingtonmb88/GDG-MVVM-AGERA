package com.wellingtonmb88.gitrepo.preferences;


import android.content.Context;
import android.content.SharedPreferences;

public class RepositoryPreferences {

    private static final String REPOSITORY_LIST = "REPOSITORY_LIST";
    private static final String REPOSITORY_QUERY = "REPOSITORY_QUERY";
    private static final String REPOSITORY = "REPOSITORY";

    private static final String REPOSITORY_PREFERENCES = "REPOSITORY_PREFERENCES";

    public static void saveRepositoryList(Context context, String repositoryList) {
        SharedPreferences appSharedPrefs = context
                .getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(REPOSITORY_LIST, repositoryList);
        prefsEditor.apply();
    }

    public static String getRepositoryList(Context context) {

        SharedPreferences appSharedPrefs = context
                .getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);

        return appSharedPrefs.getString(REPOSITORY_LIST, "");
    }

    public static void saveRepositoryQuery(Context context, String repositoryQuery) {

        SharedPreferences appSharedPrefs = context
                .getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(REPOSITORY_QUERY, repositoryQuery);
        prefsEditor.apply();
    }

    public static String getRepositoryQuery(Context context) {

        SharedPreferences appSharedPrefs = context
                .getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);

        return appSharedPrefs.getString(REPOSITORY_QUERY, "");
    }

    public static void saveRepository(Context context, String repository) {
        SharedPreferences appSharedPrefs = context
                .getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(REPOSITORY, repository);
        prefsEditor.apply();
    }

    public static String getRepository(Context context) {

        SharedPreferences appSharedPrefs = context
                .getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);

        return appSharedPrefs.getString(REPOSITORY, "");
    }
}
