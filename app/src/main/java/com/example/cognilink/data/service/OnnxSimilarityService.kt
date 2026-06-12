package com.example.cognilink.data.service

import android.content.Context
import android.util.Log
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.extensions.OrtxPackage
import com.example.cognilink.domain.repository.SimilarityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

class OnnxSimilarityService(context: Context) : SimilarityService {

    private val modelPath = "model.onnx"
    private val env: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var session: OrtSession? = null

    init {
        try {
            val modelBytes = context.assets.open(modelPath).use { it.readBytes() }
            val sessionOptions = OrtSession.SessionOptions()
            sessionOptions.registerCustomOpLibrary(OrtxPackage.getLibraryPath())
            session = env.createSession(modelBytes, sessionOptions)
            Log.d("OnnxSimilarity", "ONNX Session initialized successfully with extensions")
        } catch (e: Exception) {
            Log.e("OnnxSimilarity", "Failed to initialize ONNX session", e)
        }
    }

    override suspend fun calculateSimilarity(text1: String, text2: String): Float = withContext(Dispatchers.Default) {
        if (text1.trim().equals(text2.trim(), ignoreCase = true)) return@withContext 1.0f

        val currentSession = session ?: return@withContext 0.5f

        try {
            val v1 = getEmbedding(text1, currentSession)
            val v2 = getEmbedding(text2, currentSession)

            if ((v1 != null) && (v2 != null)) {
                cosineSimilarity(v1, v2)
            } else {
                0.5f
            }
        } catch (e: Exception) {
            Log.e("OnnxSimilarity", "Error calculating similarity", e)
            0.5f
        }
    }

    private fun getEmbedding(text: String, ortSession: OrtSession): FloatArray? {
        val inputName = ortSession.inputNames.iterator().next()
        val inputTensor = OnnxTensor.createTensor(env, arrayOf(text))
        
        return inputTensor.use {
            val output = ortSession.run(mapOf(inputName to inputTensor))
            output.use {
                when (val result = output[0].value) {
                    is Array<*> -> {
                        val firstElem = result[0]
                        when (firstElem) {
                            is FloatArray -> firstElem
                            is Array<*> -> (firstElem[0] as? FloatArray)
                            else -> null
                        }
                    }
                    is FloatArray -> result
                    else -> {
                        Log.e("OnnxSimilarity", "Unexpected output type: ${result?.javaClass?.name}")
                        null
                    }
                }
            }
        }
    }

    private fun cosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        val denominator = sqrt(normA.toDouble()) * sqrt(normB.toDouble())
        return if (denominator > 0) (dotProduct / denominator).toFloat() else 0.0f
    }

    fun close() {
        session?.close()
        env.close()
    }
}
