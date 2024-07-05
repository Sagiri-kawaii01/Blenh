import kotlin.collections.mutableMapOf

val secret = mutableMapOf<String, Any>()

// ===== sign key =====
val sign = mutableMapOf<String, String>()
sign["RELEASE_KEY_ALIAS"] = "key0"
sign["RELEASE_KEY_PASSWORD"] = "qaq010109"
sign["RELEASE_STORE_PASSWORD"] = "qaq010109"
secret["sign"] = sign
// ===== sign key end =====

// ===== in Manifest =====
val manifestPlaceholders = mutableMapOf<String, String>()
secret["manifestPlaceholders"] = manifestPlaceholders
// ===== in Manifest end =====

extra["secret"] = secret