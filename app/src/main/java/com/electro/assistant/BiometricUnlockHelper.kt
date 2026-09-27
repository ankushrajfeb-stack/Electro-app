package com.electro.assistant

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * IMPORTANT: No app - including this one - is allowed by Android to read or
 * store your actual PIN, pattern, or password. That is an OS security rule,
 * not a limitation of this code.
 *
 * What Electro CAN do when you say "unlock": pop up the phone's own
 * fingerprint / face / PIN screen. Android handles the check itself and
 * only tells this app "success" or "failed" - your credential itself
 * never touches Electro's code, memory, or logs.
 */
class BiometricUnlockHelper(private val activity: FragmentActivity) {

    fun requestUnlock(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onFailure(errString.toString())
            }
            override fun onAuthenticationFailed() {
                onFailure("Fingerprint/face did not match")
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Electro")
            .setSubtitle("Confirm it's you to unlock")
            // Allows fingerprint/face OR the phone's own PIN/pattern as backup
            .setAllowedAuthenticators(
                androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK or
                androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(promptInfo)
    }
}
