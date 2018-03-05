package org.jetbrains.dokka

import com.google.inject.Inject
import com.google.inject.name.Named
import org.jetbrains.dokka.Utilities.impliedPlatformsName

open class GitbookOutputBuilder(to: StringBuilder,
                            location: Location,
                            locationService: LocationService,
                            languageService: LanguageService,
                            extension: String,
                            impliedPlatforms: List<String>)
    : MarkdownOutputBuilder(to, location, locationService, languageService, extension, impliedPlatforms)
{
    override fun appendTable(vararg columns: String, body: () -> Unit) {
        to.appendln(columns.joinToString(" | ", "| ", " |"))
        to.appendln("|" + "---|".repeat(columns.size))
        body()
    }

    override fun appendLink(href: String, body: () -> Unit) {
      if (inCodeBlock) {
          wrap("[", "]($href)", body)
      }
      else {
          wrap("[", "]($href)", body)
      }
    }
}

open class GitbookFormatService(locationService: LocationService,
                            signatureGenerator: LanguageService,
                            linkExtension: String,
                            impliedPlatforms: List<String>)
: MarkdownFormatService(locationService, signatureGenerator, linkExtension, impliedPlatforms) {

    @Inject constructor(locationService: LocationService,
                        signatureGenerator: LanguageService,
                        @Named(impliedPlatformsName) impliedPlatforms: List<String>) : this(locationService, signatureGenerator, "md", impliedPlatforms)

    override fun createOutputBuilder(to: StringBuilder, location: Location): FormattedOutputBuilder =
        GitbookOutputBuilder(to, location, locationService, languageService, extension, impliedPlatforms)
}
