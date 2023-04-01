package tech.ixor

import org.junit.Test
import tech.ixor.utils.CompatibilityStatus
import tech.ixor.utils.CompatibilityUtil

class CompatibilityUtilTest {
    private fun checkUnknownModule() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "unknown_module"
        val version = "1.0.0"
        val stage = "stable"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.UNKNOWN_MODULE)
    }

    private fun checkImcompatibleVersion() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_test"
        val version = "0.1.0"
        val stage = "alpha"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.INCOMPATIBLE_VERSION)
    }

    private fun checkIncompatibleStage() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_test"
        val version = "1.0.0"
        val stage = "alpha"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.INCOMPATIBLE_STAGE)
    }

    private fun checkCompatible1() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_test"
        val version = "1.0.0"
        val stage = "rc"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.COMPATIBLE)
    }

    private fun checkCompatible2() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_test"
        val version = "1.0.0"
        val stage = "stable"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.COMPATIBLE)
    }

    private fun checkCompatible3() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_test"
        val version = "1.1.0"
        val stage = "alpha"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.COMPATIBLE)
    }

    private fun checkCompatible4() {
        val compatibilityUtil = CompatibilityUtil()
        val module = "remotemc_test"
        val version = "1.1.0"
        val stage = "stable"
        println("Checking compatibility for $module $version $stage")
        val result = compatibilityUtil.checkComaptibility(module, version, stage)
        println("Result: $result")
        assert(result == CompatibilityStatus.COMPATIBLE)
    }

    @Test
    fun testCheckCompatibility() {
        checkUnknownModule()
        checkImcompatibleVersion()
        checkIncompatibleStage()
        checkCompatible1()
        checkCompatible2()
        checkCompatible3()
        checkCompatible4()
    }
}
