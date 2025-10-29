package intellij.settings.fixtures

import com.intellij.driver.sdk.ui.components.common.welcomeScreen
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.driver.sdk.ui.xQuery
import com.intellij.ide.starter.driver.engine.BackgroundRun
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.NoProject
import com.intellij.ide.starter.runner.Starter
import intellij.settings.pages.FileAndCodeTemplatesSettingsPage
import org.junit.jupiter.api.Assertions

class FileAndCodeTemplatesSettingsFixture {
    val settingsSectionPath = "Editor"
    val fileAndCodeTemplatesSettingsLink = "File and Code Templates"

    private lateinit var runContext: BackgroundRun

    fun setUpWithoutProject(testName: String, ideVersion: String = "2025.2.4"): FileAndCodeTemplatesSettingsPage {
        val context = Starter.newContext(
            testName = testName,
            testCase = TestCase(
                ideInfo = IdeProductProvider.IC,
                projectInfo = NoProject
            ).withVersion(ideVersion)
        )

        runContext = context.runIdeWithDriver()

        // Open the settings dialog from the welcome screen
        val welcomeScreenUI = runContext.driver.welcomeScreen()
        welcomeScreenUI.openSettingsDialog()

        // Navigate to the target settings section
        val settingsDialogUI = welcomeScreenUI.settingsDialog()
        settingsDialogUI.openTreeSettingsSection(settingsSectionPath, fullMatch = true)
        settingsDialogUI.content {
            val targetActionLink = x(xQuery { byAccessibleName(fileAndCodeTemplatesSettingsLink) })
            Assertions.assertNotNull(targetActionLink, "Link $fileAndCodeTemplatesSettingsLink should exist")
            targetActionLink.click()
        }

        // Initialize a page object instance and pass it to tests
        return FileAndCodeTemplatesSettingsPage(settingsDialogUI.content())
    }

    fun tearDown() {
        this.runContext.closeIdeAndWait()
    }
}