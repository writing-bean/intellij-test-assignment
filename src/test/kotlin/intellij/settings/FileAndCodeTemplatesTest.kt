package intellij.settings

import intellij.settings.fixtures.FileAndCodeTemplatesSettingsFixture
import intellij.settings.pages.FileAndCodeTemplatesSettingsPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class FileAndCodeTemplatesTest {

    private val fixture = FileAndCodeTemplatesSettingsFixture()
    private lateinit var page: FileAndCodeTemplatesSettingsPage

    @AfterEach
    fun tearDown() {
        fixture.tearDown()
    }

    @Test
    fun shouldShowOnlyDefaultSchemeWithoutProject() {
        page = fixture.setUpWithoutProject("shouldShowOnlyDefaultSchemeWithoutProject")

        val schemesList = page
            .clickOnSchemeComboSelector()
            .getSchemesListAsText()

        Assertions.assertEquals(
            schemesList,
            listOf("Default"),
            "When settings are opened from welcome screen without project, only default scheme should be visible"
        )
    }

    @Test
    fun shouldShowRevertChangesLinkOnlyWhenChangesAreMade() {
        page = fixture.setUpWithoutProject("shouldShowRevertChangesLinkOnlyWhenChangesAreMade")

        page.clickOnCheckboxByName(FileAndCodeTemplatesSettingsPage.Checkbox.ENABLE_LIVE_TEMPLATES)

        Assertions.assertTrue(
            page.isRevertChangesLinkPresent(expectedToBe = true),
            "'Revert Changes' link should be present when changes are made"
        )

        page.clickOnRevertChangesLink()
        Assertions.assertFalse(
            page.isRevertChangesLinkPresent(expectedToBe = false),
            "'Revert Changes' link should not be present when changes are reverted"
        )
    }

    @Disabled("A popup with settings backup suggestion blocks the action. Need a proper workaround")
    @Test
    fun shouldNotShowRevertChangesLinkWhenChangesAreApplied() {
        page = fixture.setUpWithoutProject("shouldShowRevertChangesLinkOnlyWhenChangesAreMade")

        page.clickOnCheckboxByName(FileAndCodeTemplatesSettingsPage.Checkbox.ENABLE_LIVE_TEMPLATES)

        Assertions.assertTrue(
            page.isRevertChangesLinkPresent(),
            "'Revert Changes' link should be present when changes are made"
        )

        page.clickOnControlButtonByName(FileAndCodeTemplatesSettingsPage.ControlButton.APPLY)
        Assertions.assertFalse(
            page.isRevertChangesLinkPresent(),
            "'Revert Changes' link should not be present when changes are reverted"
        )
    }

    @Test
    fun shouldSelectTabsOnClick() {
        page = fixture.setUpWithoutProject("shouldSelectTabsOnClick")

        for (tab in FileAndCodeTemplatesSettingsPage.Tab.entries) {
            page.clickOnTabByName(tab)

            Assertions.assertTrue(
                page.isTabActive(tab),
                "Tab $tab should be active upon clicking on it"
            )
        }
    }

    @Test
    fun shouldMakeChangesToEditorField() {
        page = fixture.setUpWithoutProject("shouldMakeChangesToEditorField")

        page.typeTextInEditorField("Test text")

        Assertions.assertTrue(
            page.isRevertChangesLinkPresent(),
            "'Revert changes' link should be present after text is added to editor field"
        )
    }

    @Test
    fun shouldDisplayDescriptionText() {
        page = fixture.setUpWithoutProject("shouldDisplayDescriptionText")

        val descriptionText = page
            .clickOnTabByName(FileAndCodeTemplatesSettingsPage.Tab.FILES)
            .getDescriptionText()

        Assertions.assertTrue(
            descriptionText.isNotEmpty(),
            "First template of a Files tab should have a description text displayed in corresponding UI element"
        )
    }
}