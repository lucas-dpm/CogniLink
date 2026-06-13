package com.lucasdpm.cognilink.data.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

interface TermsRepository {
    suspend fun getTerms(): String
}


class TermsRepositoryImpl(private val context: Context) : TermsRepository {

    companion object {
        private const val TAG = "TermsRepository"
        private const val ASSET_PATH = "files/terms_fallback.json"
        private const val JSON_KEY_CONTENT = "conteudo"
    }

    override suspend fun getTerms(): String = withContext(Dispatchers.IO) {
        try {
            // 1. Read the JSON file from assets
            val jsonString = context.applicationContext.assets
                .open(ASSET_PATH)
                .bufferedReader()
                .use { it.readText() }

            // 2. Parse the JSON to get only the Markdown content
            val jsonObject = JSONObject(jsonString)
            jsonObject.optString(JSON_KEY_CONTENT, "### Erro: Conteúdo não encontrado.")

        } catch (e: Exception) {
            // 3. Log the error for debugging
            Log.e(TAG, "Falha ao carregar termos de uso de $ASSET_PATH", e)

            // 4. Return a user-friendly Markdown error message
            "### Erro ao carregar os termos.\nPor favor, verifique sua conexão ou tente mais tarde."
        }
    }
}