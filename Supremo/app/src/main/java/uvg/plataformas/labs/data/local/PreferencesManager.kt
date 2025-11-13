package uvg.plataformas.labs.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("crypto_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_UPDATE = "last_update"
    }

    fun saveLastUpdate(value: String) {
        prefs.edit().putString(KEY_LAST_UPDATE, value).apply()
    }

    fun getLastUpdate(): String? {
        return prefs.getString(KEY_LAST_UPDATE, null)
    }
}