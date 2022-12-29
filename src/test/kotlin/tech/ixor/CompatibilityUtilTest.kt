package tech.ixor

import org.junit.Test
import tech.ixor.utils.CompatibilityUtil

class CompatibilityUtilTest {
    private fun checkIncompatibleModule() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "test"
        val version = "0.2.0"
        val stage = "dev"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(!result)
    }

    private fun checkIncompatibleVersion() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_mcdr"
        val version = "0.0.0"
        val stage = "dev"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(!result)
    }

    private fun checkIncompatibleStage() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_mcdr"
        val version = "0.2.0"
        val stage = "unknown"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(!result)
    }

    private fun checkCompatible() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_mcdr"
        val version = "0.2.0"
        val stage = "dev"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result)
    }

    @Test
    fun testCheckCompatibility() {
        checkIncompatibleModule()
        checkIncompatibleVersion()
        checkIncompatibleStage()
        checkCompatible()
    }
}
