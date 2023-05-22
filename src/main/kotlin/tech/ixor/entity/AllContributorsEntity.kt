package tech.ixor.entity

import com.sksamuel.hoplite.ConfigAlias
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.json.JsonParser

class AllContributorsEntity {
    data class Contributor(
        val login: String,
        val name: String,
        @ConfigAlias("avatar_url") val avatarUrl: String,
        val profile: String,
        val contributions: List<String>
    )

    data class AllContributors(
        val projectName: String,
        val projectOwner: String,
        val repoType: String,
        val repoHost: String,
        val files: List<String>,
        val imageSize: Int,
        val commit: Boolean,
        val commitConvention: String,
        val contributorsPerLine: Int,
        val contributorsSortAlphabetically: Boolean,
        val linkToUsage: Boolean,
        val skipCi: Boolean,
        val contributors: List<Contributor>
    )

    fun loadAllContributorsSrc(): AllContributors {
        val pwd = System.getProperty("user.dir")
        val contributorsrcFile = "$pwd/.all-contributorsrc"
        return ConfigLoaderBuilder.default()
            .addParser("all-contributorsrc", JsonParser())
            .build()
            .loadConfigOrThrow<AllContributors>(contributorsrcFile)
    }
}
